/**
 * Argentino Trombin - Almaviva

**/




import it.finsiel.misc.DateUtil;
import it.finsiel.misc.Misc;
import it.finsiel.misc.MiscString;
import it.finsiel.misc.MiscStringTokenizer;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;


public class RimuoviRecordConDipendenze {
	public static final int MAX_BYTES_PER_UTF8_CHARACTER = 4;
	String msg; 

	final int TB_SOGGETTO = 1;
	
	
	Map parameters = new HashMap();    // hash table

	
	
	java.util.Vector vecInputFiles; // Buffer Reader Files
	String downloadDir = "";
	int fileCounter = 0;
	String logFileOut = "";
	String fileOut = "";
	boolean setCurCfg=false;
	boolean rimuoviRecordVivi=false;
	//FileOutputStream streamOutLog;
	BufferedWriter OutLog;
	
	BufferedOutputStream bufferedStreamOutTable;	// 15/09/2010
	BufferedOutputStream bufferedStreamOutTableBytes;

	
	Connection con = null;
	int commitOgniTotRighe = 0xFF;
	 
	
	String jdbcDriver="";
	String connectionUrl = ""; // "jdbc:postgresql://localhost:5432/sbnwebArge"
	String userName="";
	String userPassword="";
	
	String fileIdDaRimuovere = null;
	String query;
	String preprocess;


	int progress=0x3ff; 
	
	public RimuoviRecordConDipendenze()
	    {
	    }

	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
	public boolean openConnection()
	{
		System.out.println("Conncecting to " + connectionUrl + " userName=" + userName);
		
		try {
			 Class.forName ( jdbcDriver);
		} catch(java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}

		try {

				con = DriverManager.getConnection(connectionUrl,userName, userPassword);
				boolean autoCommit = con.getAutoCommit();
				con.setAutoCommit(false);
				
				Statement stmt = null;
				try {
					stmt = con.createStatement();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// if (jdbcDriver.contains("postgres"))
				int pos = jdbcDriver.indexOf("postgres"); // 09/12/2010
				if (pos  != -1)
				{
					stmt.execute("SET search_path = sbnweb, pg_catalog, public");
					if (setCurCfg == true)
						stmt.execute("select set_curcfg('default')"); // Esegui questa select per attivare gli indici testuali 
				}
				
				return true;
		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
		return false;
	} // End openConnection
	

	public void closeConnection()
	{
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} // End closeConnection
	
String[] getPropertyKeyValue(String s)
{
	int pos = s.indexOf("=");
	if (pos == -1)
	{
		String[] arrCampi = new String[1];
		arrCampi[0] = s;
		return arrCampi;
	}
		
	
	String key = new String (s.substring(0,pos));
	String value = new String (s.substring(pos+1));
	String[] arrCampi = new String[2];

	arrCampi[0] = key;
	arrCampi[1] = value;
	return arrCampi;

} // End getPropertyKeyValue




private byte[] myGetBytesUtf8(String s) {
	int len = s.length();
	int en = MAX_BYTES_PER_UTF8_CHARACTER * len;
	byte[] ba = new byte[en];
	if (len == 0)
		return ba;

	int ctr = 0;

	for (int i = 0; i < len; i++) {
		char c = s.charAt(i);
		if (c < 0x80) {
			ba[ctr++] = (byte) c;
		} else if (c < 0x800) {
			ba[ctr++] = (byte) (0xC0 | c >> 6);
			ba[ctr++] = (byte) (0x80 | c & 0x3F);
		} else if (c < 0x10000) {
			ba[ctr++] = (byte) (0xE0 | c >> 12);
			ba[ctr++] = (byte) (0x80 | c >> 6 & 0x3F);
			ba[ctr++] = (byte) (0x80 | c & 0x3F);
		} else if (c < 0x200000) {
			ba[ctr++] = (byte) (0xE0 | c >> 18);
			ba[ctr++] = (byte) (0x80 | c >> 12 & 0x3F);
			ba[ctr++] = (byte) (0x80 | c >> 6 & 0x3F);
			ba[ctr++] = (byte) (0x80 | c & 0x3F);
		} else if (c < 0x800) {

		}
	} // end for

	return trim(ba, ctr);
} // End myGetBytesUtf8

private static byte[] trim(byte[] ba, int len) {
	if (len == ba.length)
		return ba;
	byte[] tba = new byte[len];
	System.arraycopy(ba, 0, tba, 0, len);
	return tba;
}





public static void main(String args[])
{
	char charSepArrayEquals[] = { '='};
	char charSepArraySpace[] = { ' '};
	String ar[];

	if(args.length < 1)
    {
        System.out.println("Uso: RimuoviRecordConDipendenze  <file di configurazione>"); 
        System.exit(1);
    }
//    String configFile = args[0];
    String inputFile = args[0];
//	logFileOut = args[2];
    
    String start=
       "Rimuovi record son dipendenze tool - � Almaviva S.p.A 15/11/2016"+
	 "\n================================================================"+
	 "\nTool per la rimozione record da DB rimuovendo prima tutte le DIPENDENZE"+
	 "\nInizio esecuzione " + DateUtil.getDate() + " " + DateUtil.getTime()+ 
	 "\nElenco tabelle da trattare: " + inputFile ;

    System.out.println(start);

//	System.out.println("inp: " + inputFile);

	RimuoviRecordConDipendenze rimuoviRecord = new RimuoviRecordConDipendenze();

    // abbiamo parametri in ingresso?
    for (int i=1; i < args.length; i++)
    {
    	ar = rimuoviRecord.getPropertyKeyValue(args[i]);  
    	// Add key/value pairs to the map
    	rimuoviRecord.parameters.put(ar[0], ar[1]);
    }
	
	
	BufferedReader in;
	String s;
	
	try {
		in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),"UTF8"));

		// Leggiamo le configurazioni di base
		// ----------------------------------
		while (true) {
			try {
				s = in.readLine();
				if (s == null)
					break;
				else if (Misc.emptyString(s))
					continue;
				else if ((	s.length() == 0) 
							||  (s.charAt(0) == '#') 
							|| (Misc.emptyString(s) == true))
						continue;
				else {
					ar = rimuoviRecord.getPropertyKeyValue(s); //MiscString.estraiCampi(s,  charSepArrayEquals, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE); // "="
					if (ar[0].startsWith("jdbcDriver"))
						rimuoviRecord.jdbcDriver = ar[1];
					else if (ar[0].startsWith("connectionUrl"))
						rimuoviRecord.connectionUrl = ar[1];
					else if (ar[0].startsWith("userName"))
						rimuoviRecord.userName = ar[1];
					else if (ar[0].startsWith("userPassword"))
						rimuoviRecord.userPassword = ar[1];
					else if (ar[0].startsWith("logFileOut"))
						rimuoviRecord.logFileOut = ar[1];

					else if (ar[0].startsWith("rimuoviRecordVivi"))
					{
						if (ar[1].equals("true"))
							rimuoviRecord.rimuoviRecordVivi = true;
					}
					else if (ar[0].startsWith("endConfig"))
						break;

					else if (ar[0].startsWith("progress"))
						rimuoviRecord.progress = Integer.parseInt(ar[1], 16);
					else if (ar[0].startsWith("commitOgniTotRighe"))
						rimuoviRecord.commitOgniTotRighe = Integer.parseInt(ar[1],16);
					else
						System.out.println("ERRORE: parametro sconosciuto: "	+ ar[0]);
					
						
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Apriamo il file di log
		try {

			System.out.println("File di log: "	+ rimuoviRecord.logFileOut);
			rimuoviRecord.OutLog = new BufferedWriter(new FileWriter(rimuoviRecord.logFileOut));				
			rimuoviRecord.OutLog.write("\n"+start);
			
		} catch (Exception fnfEx) {
			fnfEx.printStackTrace();
			return;
		}
		
		// Apriamo il DB
		if (!rimuoviRecord.openConnection())
			{
			try {
				rimuoviRecord.OutLog.write("\nFailed to open DB of URL: " + rimuoviRecord.connectionUrl);
				rimuoviRecord.OutLog.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
			}
		
		// Esegui trattamento delle singole tabelle
			// ----------------------------------------
			rimuoviRecord.fileCounter = 0;
			long startTime = System.currentTimeMillis();

			while (true) {
				try {
					s = in.readLine();
					if (s == null)
						break;
					else {
						if ((	s.length() == 0) 
								||  (s.charAt(0) == '#') 
								|| (Misc.emptyString(s) == true))
							continue;

						
						//ar = MiscString.estraiCampi(s, charSepArraySpace, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE); //  " "
						ar = MiscString.estraiCampiDelimitatiENon(s, " ", '"', '"', MiscString.KEEP_DELIMITERS_FALSE, MiscString.KEEP_GROUP_DELIMITERS_TRUE, MiscString.TRIM_FALSE, MiscString.HAS_ESCAPED_CHARACTERS_TRUE, MiscString.KEEP_ESCAPE_TRUE);
						
						rimuoviRecord.fileCounter++;
//						dbDownload.download(ar[0]); // Esporta tabella
						rimuoviRecord.doRimuovi(s); // Esporta tabella
						
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
			    long elapsedTimeMillis = System.currentTimeMillis()-startTime;
			    s = "\n----------------------------------\nExport eseguito in " + elapsedTimeMillis/(60*1000F) + " minuti";
//				System.out.println(s);
				rimuoviRecord.OutLog.write(s);
				
				System.out.println("Vedi " + rimuoviRecord.logFileOut + " per i dettagli dell'elaborazione");
				// Close log file 
				if (rimuoviRecord.OutLog != null)
					rimuoviRecord.OutLog.close();
				// Close filelist input file	
				in.close();

				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Chiudiamo il DB
			rimuoviRecord.closeConnection();
		
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();

	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
//    System.out.println("Righe elaborate: " + Integer.toString(rowCounter));

	System.exit(0);
} // End main

public void doRimuovi(String tabl_parameters) 
{
	String arTable[];
	
	arTable = MiscString.estraiCampiDelimitatiENon(tabl_parameters, " ", '"', '"', MiscString.KEEP_DELIMITERS_FALSE, MiscString.KEEP_GROUP_DELIMITERS_TRUE, MiscString.TRIM_FALSE, MiscString.HAS_ESCAPED_CHARACTERS_TRUE, MiscString.KEEP_ESCAPE_TRUE);
	
	String tableName = arTable[0];
	Statement stmt = null;
//	StringBuffer sb = new StringBuffer();
	int rows=0;
	String idRecord;
	String fileIdDaRimuovere="";
//	String lunghezzeCampi = "";
	
	int idTabella = -1;
	// Leggiamo i parametri specifici della tabella
	String nome_tabella = arTable[0];
	if (nome_tabella.equals("tb_soggetto"))
		idTabella = TB_SOGGETTO;
	
	for (int i=1; i < arTable.length; i++)
	{
		if (arTable[i].startsWith("fileIdDaRimuovere="))
			fileIdDaRimuovere = arTable[i].substring(18);
	} // end for
	
	
 	System.out.println("\nInizio rimozione record " + tableName + " su " + fileIdDaRimuovere); //  + " " + DateUtil.getDate() + " " + DateUtil.getTime() 
	try {
		OutLog.write("\n\n-------------------------");
		OutLog.write("\nInizio rimozione record " + fileIdDaRimuovere); //  + " " + DateUtil.getDate() + " " + DateUtil.getTime()
	} catch (IOException e2) {
		e2.printStackTrace();
	} 
	
		
	// Apriamo il file di export
	try {

		// Apri file degli identificativi in lettura
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileIdDaRimuovere),"UTF8"));
		stmt = con.createStatement();
		
		// Elabora
		int recordCancellati=0;
		int recordDaCancellareMaVivi=0;
		while (true) {
			try {
				idRecord = in.readLine();
				if (idRecord == null)
					break;
				else {
					if ((	idRecord.length() == 0) 
							||  (idRecord.charAt(0) == '#') 
							|| (Misc.emptyString(idRecord) == true))
						continue;
					
				if (progress == 0)
					OutLog.write("\nRimuovi id: "+idRecord);
					
				if ((rows & progress) == progress)
				{
					msg = "\nFatti: " + rows + " record.";
System.out.print(msg);
					OutLog.write(msg);
					
				}
					
					// Gestiamo le dipendenze della tabella prima di cancellare il record
				if (idTabella == TB_SOGGETTO)
					
				{ // Gestiamo le dipendenze
					// Abbiamo il soggetto in tabella o e' gia stato rimosso?
//					ResultSet rs = stmt.executeQuery("select cid from tb_soggetto where cid='"+idRecord+"'" );
//					ResultSet rs = stmt.executeQuery("select cid from tb_soggetto where cid='"+idRecord+"' and fl_canc = 'S'" ); // 12/01/2017
//					ResultSet rs = stmt.executeQuery("select cid from tb_soggetto where cid='"+idRecord+"'" ); // 11/04/2017
					
					ResultSet rs = stmt.executeQuery("select cid, fl_canc from tb_soggetto where cid='"+idRecord+"'" ); // 11/04/2017

					if(rs.next())
					{
						String fl_canc = rs.getString(2);
						if (!fl_canc.equals("S"))
							{
							if  (rimuoviRecordVivi == false)
								{ // 17/04/2018 Non cancelliamo i record vivi!!
									OutLog.write("Vorresti rimuovere un soggetto vivo: " + rs.getString(1) + "\n");
									recordDaCancellareMaVivi++;
									continue;
								}
							else
								{
								OutLog.write("RIMOZIONE di un soggetto vivo! : " + rs.getString(1) + " NON IMPLEMENTATO\n");
								}
							}
						else
						{ // Record cancellato logicamente
							if (rimuoviDipendenzeTbSoggetto(idRecord, stmt) == true)
							{
								// Rimuoviamo il soggetto
							    stmt.executeUpdate("DELETE FROM tb_soggetto WHERE cid = '"+idRecord+"'");
								System.out.println("Cancellato record: " + idRecord + " incluse sue dipendenze"); // 28/07/2021
								recordCancellati++;
							}
							else
							{
								msg = "\nNon e' stato possibile rimuovere le dipendenze per il record "+idRecord;
								//System.out.println();
								OutLog.write(msg);
							}							
						}
						
					}
					else
					{
						 System.out.println("Record "+idRecord+" non presente in indice" );
					}
				}
				
//				ResultSet rs = stmt.executeQuery(sql);
				rows++;
				// Commit every tot records
				if ((rows & commitOgniTotRighe) == 0) 
				{
					msg = "\nCommitting at row " + rows;
					System.out.print(msg);
					OutLog.write(msg);

					msg = "\nCancellati: " + recordCancellati + " record fino al committ";
					OutLog.write(msg);

					try {
						stmt.execute("COMMIT");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // ;
				}
				
				} // End riga con testo
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} // End while
		
		msg="\nCommitting at row " + rows;
System.out.print(msg);
		OutLog.write(msg);
		try {
			stmt.execute("COMMIT");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // ;

		
		msg = "\nLetti: " + rows + " record";
		OutLog.write(msg);
		msg = "\nCancellati: " + recordCancellati + " record";
		OutLog.write(msg);
		msg = "\nNON cancellati: " + recordDaCancellareMaVivi + " record";
		OutLog.write(msg);

		
		
		try {
			in.close();
			stmt.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	} catch (Exception fnfEx) {
		fnfEx.printStackTrace();
		return;
	}
} // End doRimuovi

boolean  rimuoviDipendenzeTbSoggetto(String idRecord, Statement stmt)
{
	ResultSet rs;
	// Controlliamo i legami soggetto descrittore
	try {
		// Controlliamo che non ci siano legami a titoli.
		// Non possiamo rimuovere soggetti legati a titoli
		rs = stmt.executeQuery("select count (*) as RECORDCOUNT from tr_tit_sog where cid='"+idRecord+"'"); //  and fl_canc != 'S' 
		if(rs.next() && rs.getInt("RECORDCOUNT") > 0)
		{
//		    System.out.println("Soggetto con cid "+idRecord+" non puo' essere rimosso perche ci sono "+rs.getInt("RECORDCOUNT")+" legami a titolo/i");
//			return false;
			
			// 12/01/2017 Rimuoviamo i legami soggetto titolo in quanto il soggetto che stiamo trattando e' stato logicamente cancellato!! 
		    stmt.executeUpdate("DELETE FROM tr_tit_sog  WHERE cid = '"+idRecord+"'");

		}		
		
		// Se ci sono legami a descrittori rimuovere i legami
		rs = stmt.executeQuery("select count (*) as RECORDCOUNT from tr_sog_des where cid='"+idRecord+"'" );
		if(rs.next() && rs.getInt("RECORDCOUNT") > 0)
		{
//		    System.out.println("Recordcount: "+rs.getInt("RECORDCOUNT"));
		    
			// Rimuoviamo i legami soggetto descrittore
		    stmt.executeUpdate("DELETE FROM tr_sog_des  WHERE cid = '"+idRecord+"'");

//		    System.out.println("Rimossi legami");
		    
//		    return true;
	      
		}
		else
		{
//		    System.out.println("Nessun legame da rimuovere");
//			return true; // Soggetto senza legame. Puo' essere rimosso
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
//	throw new RuntimeException();
	return true;
}

} // End RimuoviRecordConDipendenze
