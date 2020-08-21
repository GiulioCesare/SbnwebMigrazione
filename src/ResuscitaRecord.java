/**
 * Argentino Trombin - Almaviva

08/06/2017
Resuscita record logicamente cancellato rescuscitando anche tutti i legami logicamente cancellati

08/06/2017 TB_SOGGETTO

**/




import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import it.finsiel.misc.DateUtil;
import it.finsiel.misc.Misc;
import it.finsiel.misc.MiscString;


public class ResuscitaRecord {
	public static final int MAX_BYTES_PER_UTF8_CHARACTER = 4;

	final int TB_SOGGETTO = 1;
	final int TB_TITOLO = 2;
	final int TB_AUTORE = 3;
	
	boolean rescuscita_record_fuso=false;	// Potrebbe trattarsi di recuperare un record semplicemente cancellato
											// oppure du un record cancellato per fusione
	
	int recordResuscitati=0;
	boolean toCommit=false;
	
	Map parameters = new HashMap();    // hash table

	
	
	java.util.Vector vecInputFiles; // Buffer Reader Files
	String downloadDir = "";
	int fileCounter = 0;
	String logFileOut = "";
	String fileOut = "";
	boolean setCurCfg=false;
	//FileOutputStream streamOutLog;
	BufferedWriter OutLog;
	
	BufferedOutputStream bufferedStreamOutTable;	// 15/09/2010
	BufferedOutputStream bufferedStreamOutTableBytes;

	
	Connection con = null;
	int commitEveryNRows = 0xFF;
	String jdbcDriver="";
	String connectionUrl = ""; // "jdbc:postgresql://localhost:5432/sbnwebArge"
	String userName="";
	String userPassword="";
	
	String fileIdDaRimuovere = null;
	String query;
	String preprocess;
    Statement stmt1 = null;
	Statement stmt = null;


	int progress=0x3ff; 
	
	public ResuscitaRecord()
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
				
				try {
					stmt = con.createStatement();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					stmt1 = con.createStatement();
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
//	int rowCtr =0;
	
	if(args.length < 1)
    {
        System.out.println("Uso: ResuscitaRecord  <file di configurazione>\n"
        		 +"Resuscita: SOGGETTI, TITOLI"); 
        System.exit(1);
    }
//    String configFile = args[0];
    String inputFile = args[0];
//	logFileOut = args[2];
    
    String start=
       "Rimuovi record tool - � Almaviva S.p.A 15/11/2016-2018"+
	 "\n======================================================"+
	 "\nTool per la resuscitazione dei record da DB rescuscitando prima tutte le DIPENDENZE"+
	 "\nResuscita: SOGGETTI, TITOLI"+
	 "\nInizio esecuzione " + DateUtil.getDate() + " " + DateUtil.getTime()+ 
	 "\nElenco tabelle da trattare: " + inputFile ;

    System.out.println(start);

//	System.out.println("inp: " + inputFile);

	ResuscitaRecord resuscitaRecord = new ResuscitaRecord();

    // abbiamo parametri in ingresso?
    for (int i=1; i < args.length; i++)
    {
    	ar = resuscitaRecord.getPropertyKeyValue(args[i]);  
    	// Add key/value pairs to the map
    	resuscitaRecord.parameters.put(ar[0], ar[1]);
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
					ar = resuscitaRecord.getPropertyKeyValue(s); //MiscString.estraiCampi(s,  charSepArrayEquals, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE); // "="
					if (ar[0].startsWith("jdbcDriver"))
						resuscitaRecord.jdbcDriver = ar[1];
					else if (ar[0].startsWith("connectionUrl"))
						resuscitaRecord.connectionUrl = ar[1];
					else if (ar[0].startsWith("userName"))
						resuscitaRecord.userName = ar[1];
					else if (ar[0].startsWith("userPassword"))
						resuscitaRecord.userPassword = ar[1];
					
					else if (ar[0].startsWith("logFileOut"))
						resuscitaRecord.logFileOut = ar[1];
					

					else if (ar[0].startsWith("progress"))
						resuscitaRecord.progress = Integer.parseInt(ar[1], 16);

					else if (ar[0].startsWith("rescuscita_record_fuso"))
						resuscitaRecord.rescuscita_record_fuso = true;
					
					
					else if (ar[0].startsWith("endConfig"))
						break;

										
					
					else
						System.out.println("ERRORE: parametro sconosciuto"	+ ar[0]);
					
						
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Apriamo il file di log
		try {

			System.out.println("File di log: "	+ resuscitaRecord.logFileOut);
			resuscitaRecord.OutLog = new BufferedWriter(new FileWriter(resuscitaRecord.logFileOut));				
			resuscitaRecord.OutLog.write(start);
			
		} catch (Exception fnfEx) {
			fnfEx.printStackTrace();
			return;
		}
		
		// Apriamo il DB
		if (!resuscitaRecord.openConnection())
			{
			try {
				resuscitaRecord.OutLog.write("Failed to open DB of URL: " + resuscitaRecord.connectionUrl);
				resuscitaRecord.OutLog.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
			}
		
		// Esegui trattamento delle singole tabelle
			// ----------------------------------------
			resuscitaRecord.fileCounter = 0;
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
						
						resuscitaRecord.fileCounter++;
						resuscitaRecord.doResuscita(s);
						
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
			    s = "\n----------------------------------\nResuscita record eseguito in " + elapsedTimeMillis/(60*1000F) + " minuti";
	
				
				System.out.println(s);
				resuscitaRecord.OutLog.write(s);
				
				System.out.println("Vedi " + resuscitaRecord.logFileOut + " per i dettagli dell'elaborazione");
				// Close log file 
				if (resuscitaRecord.OutLog != null)
					resuscitaRecord.OutLog.close();
				// Close filelist input file	
				in.close();

				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Chiudiamo il DB
			resuscitaRecord.closeConnection();
		
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


void resuscitaSoggetto(String idRecord)
{
	// Gestiamo le dipendenze
	
	// Abbiamo il soggetto in tabella o e' gia stato rimosso?
	// Gestiamo anche quei soggetti non cancellati ma che hanno i legami cancellati
	ResultSet rs;
	try {
		rs = stmt.executeQuery("select cid from tb_soggetto where cid='"+idRecord+"'" );
		if(rs.next())
		{
			if (resuscitaDipendenzeTbSoggetto(idRecord, stmt) == true)
			{
				// Rescuscitiamo il soggetto
			    stmt.executeUpdate("update tb_soggetto set fl_canc = 'N' WHERE cid = '"+idRecord+"'");

				recordResuscitati++;
				toCommit=true;
			}
			else
			{
				System.out.println("Non e' stato possibile rescuscitare le dipendenze per il record "+idRecord );
			}
		}
		else
		{
			System.out.println("cid "+idRecord+" non presente in indice" );
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} //  and fl_canc = 'S' 
}



boolean  resuscitaDipendenzeTbSoggetto(String idRecord, Statement stmt)
{
	ResultSet rs;
	// Controlliamo i legami del soggetto 
	try {
		// Controlliamo che non ci siano legami a titoli.
		// Riattiviamo soggetti legati a titoli
		rs = stmt.executeQuery("select count (*) as RECORDCOUNT from tr_tit_sog where cid='"+idRecord+"' and fl_canc = 'S'");
		if(rs.next() && rs.getInt("RECORDCOUNT") > 0)
		{
			// 08/06/2017 Riattiviamo i legami soggetto titolo in quanto il soggetto che stiamo trattando e' stato logicamente cancellato!! 
		    stmt.executeUpdate("update TR_TIT_SOG set fl_canc = 'N'  WHERE cid = '"+idRecord+"'");
		}		
		
		// Se ci sono legami a descrittori riattiviamo i legami ed i descrittori
		rs = stmt.executeQuery("select CID, DID from TR_SOG_DES where cid='"+idRecord+"'" ); //  and fl_canc = 'S'
	    String did;


	    while(rs.next())
		{
//		    System.out.println("Recordcount: "+rs.getInt("RECORDCOUNT"));
			// Riattiviamo i legami soggetto descrittore
		    did = rs.getString("DID");
//System.out.println("Resuscita did "+did );
		    stmt1.executeUpdate("update TR_SOG_DES set fl_canc = 'N'  WHERE cid='"+idRecord+"' and did = '"+did+"'");
		    stmt1.executeUpdate("update TB_DESCRITTORE set fl_canc = 'N'  WHERE did = '"+did+"'");
		    	
		}
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
//	throw new RuntimeException();
	return true;
}


public void doResuscita(String tabl_parameters) 
{
	String arTable[];
	
	arTable = MiscString.estraiCampiDelimitatiENon(tabl_parameters, " ", '"', '"', MiscString.KEEP_DELIMITERS_FALSE, MiscString.KEEP_GROUP_DELIMITERS_TRUE, MiscString.TRIM_FALSE, MiscString.HAS_ESCAPED_CHARACTERS_TRUE, MiscString.KEEP_ESCAPE_TRUE);
	
	String tableName = arTable[0];
	Statement stmt = null;
//	StringBuffer sb = new StringBuffer();
	int rows=0;
	String idRecord;
	String fileIdDaResuscitare="";
//	String lunghezzeCampi = "";
	
	int idTabella = -1;
	// Leggiamo i parametri specifici della tabella
	String nome_tabella = arTable[0];
	
	if (nome_tabella.equals("tb_soggetto"))
		idTabella = TB_SOGGETTO;
	
	if (nome_tabella.equals("tb_titolo"))
		idTabella = TB_TITOLO;
	
	
	
	
	for (int i=1; i < arTable.length; i++)
	{
		if (arTable[i].startsWith("fileIdDaResuscitare=")) 
			fileIdDaResuscitare = arTable[i].substring(20);
		else if (arTable[i].startsWith("fileIdDaRimuovere=" )) // per compatibilita' col passato
			fileIdDaResuscitare = arTable[i].substring(18);
	} // end for
	
	
 	System.out.println("\nInizio resuscita record " + tableName + " su " + fileIdDaResuscitare); //  + " " + DateUtil.getDate() + " " + DateUtil.getTime() 
	try {
		OutLog.write("\n\n-------------------------");
		OutLog.write("\nInizio rescuscita record " + fileIdDaResuscitare); //  + " " + DateUtil.getDate() + " " + DateUtil.getTime()
	} catch (IOException e2) {
		e2.printStackTrace();
	} 
	
		
	// Apriamo il file degli ID da resuscitare
	try {

		// Apri file degli identificativi in lettura
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileIdDaResuscitare),"UTF8"));
		stmt = con.createStatement();
		
		// Elabora
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
					System.out.println("Rimuovi id: "+idRecord);
					
				if ((rows & progress) == progress)
					System.out.println("Fatti: " + rows + " record.");

//System.out.println("Resuscita record "+idRecord );
					
					// Gestiamo le dipendenze della tabella prima di cancellare il record
				if (idTabella == TB_SOGGETTO)
				{ 
					resuscitaSoggetto(idRecord);
				}

				else if (idTabella == TB_TITOLO)
				{ 
					resuscitaTitolo(idRecord);
				}
				
				rows++;
				
				// Commit every tot records
				if ((rows & commitEveryNRows) == 0) 
				{
					try {
						if (toCommit == true)
						{
							System.out.println("Committing at row " + rows);
							stmt.execute("COMMIT");
							toCommit=false;
						}
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
		
		try {
			if (toCommit == true)
			{
				System.out.println("Committing at row " + rows);
				stmt.execute("COMMIT");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // ;
		
		
		String s = "Letti: " + rows + " record";
		System.out.println(s);
		s = "Resuscitati: " + recordResuscitati + " record";
		System.out.println(s);
		OutLog.write("\n"+s);
		
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
} // End doResuscita

/*
La riattivazione sarà possibile solo se non sono stati richiamati 
gli allineamenti dai poli diversi dall'operante

Query prese dal rilascio di Paolucci
*/
void resuscitaTitolo(String idRecord)
{
	System.out.println("Resuscita titolo: " + idRecord);
	
	String[] array = idRecord.split(","); 
	
	String	bidCancellato = array[0], 
			bidSopravissuto = array[1],
			cdPolo = array[2],
			cdBiblioteca = array[3];
	

	try {
		ResultSet rs;
		String ts_var;
		String fl_allinea;
		java.sql.Timestamp ts_tb_titolo, ts_tr_tit_bib;
		Date dateTbTitolo;
		Date dateTrTitBib;
		Calendar calendarTbTitolo = Calendar.getInstance();
		Calendar calendarTrTitBib = Calendar.getInstance();
		String cdPoloTrTitBib;
		rs = stmt.executeQuery("select to_char(ts_var, 'yyyy-mm-dd hh:mm:ss') from tb_titolo where bid='"+bidCancellato+"' and bid_link='"+bidSopravissuto+"' and fl_canc='S'");
		
		if(rs.next())
		{
			// Prendiamo il timestamp dalla tb_titolo
			ts_var=rs.getString(1);
			//ts_tb_titolo = java.sql.Timestamp.valueOf( ts_var ) ;
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
			dateTbTitolo = sdf.parse(ts_var);
			calendarTbTitolo.setTime(dateTbTitolo);
		}
		else
			System.out.println("bidCancellato "+bidCancellato+" non presente in tb_titolo" );

		
		// Controlliamo se possiamo procedere con il recupero 
		// Controlliamo che i flag allinea siano tutti accesi tranne che per il polo operante
		// Se polo operante = XXX (interfaccia diretta) allora tutti gli fl_allinea vengono accesi 

		
		// Troviamo tutti i legami per il bid cancellato sulla tr_tit_bib
		
		rs = stmt.executeQuery("select bid,cd_polo,cd_biblioteca,fl_allinea,ute_var,to_char(ts_var, 'yyyy-mm-dd hh:mm:ss'), fl_canc from tr_tit_bib where bid='"+bidCancellato+"' order by fl_allinea" ); 
		while(rs.next())
		{
			fl_allinea=rs.getString(4);
			ts_var=rs.getString(6);
			cdPoloTrTitBib=rs.getString(2);
//			ts_tr_tit_bib = java.sql.Timestamp.valueOf( ts_var ) ;

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
			dateTrTitBib = sdf.parse(ts_var);
			calendarTrTitBib.setTime(dateTrTitBib);
			
			
			long diffMilliseconds = calendarTbTitolo.getTimeInMillis() - calendarTrTitBib.getTimeInMillis();
			
//			System.out.println("calendarTbTitolo="+calendarTbTitolo.getTimeInMillis());
//			System.out.println("calendarTrTitBib="+calendarTrTitBib.getTimeInMillis());


			//long diffSeconds =  diffMilliseconds/1000;
//			System.out.println("diff in seconds" + diffSeconds);

			System.out.println("diff in milliseconds " + diffMilliseconds);
			
			if (fl_allinea.charAt(0) == ' ' && diffMilliseconds < -2000)
			{	
				if (cdPolo.equals("XXX") || !cdPolo.equals(cdPoloTrTitBib))
				{	
					System.out.println("Non e' possibile resuscitare il titolo perche' qualcuno si e' gia' allineato");
					return;
				}
				
			}
		}
		 

		System.out.println("Possiamo resuscitare il titolo");

		// Inizio riattivazione
		rs = stmt.executeQuery("update tr_tit_bib set fl_canc=' ', fl_allinea=' ', fl_allinea_sbnmarc=' ',ute_var=substr(ute_var,1,6)||'proced' where bid='"+bidCancellato
				+"' and to_char(ts_var,'yyyymmddhh24mi') >= (select to_char(ts_var,'yyyymmddhh24mi') from tb_titolo where bid='"+bidCancellato+"')");
				

		// AGGIUNTO rispetto a queries di Paolucci
		rs = stmt.executeQuery("update tr_tit_bib set fl_canc=' ', fl_allinea=' ', fl_allinea_sbnmarc=' ',ute_var=substr(ute_var,1,6)||'proced' where bid='"+bidSopravissuto
			+"' and to_char(ts_var,'yyyymmddhh24mi') >= (select to_char(ts_var,'yyyymmddhh24mi') from tb_titolo where bid='"+bidSopravissuto+"')");
		
		
		rs = stmt.executeQuery("update tb_titolo set fl_canc=' ', bid_link='', tp_link='', ute_var=substr(ute_var,1,6)||'proced' where bid='"+bidCancellato+"'");

		
		rs = stmt.executeQuery("update tb_titset_1 set fl_canc=' ', ute_var=substr(ute_var,1,6)||'proced' where bid='"+bidCancellato+"'");


		rs = stmt.executeQuery("update tb_impronta "+ // -- vanno riattivate solo quelle con lo stesso timbro in tb_titolo
				"set fl_canc=' ', ute_var=substr(ute_var,1,6)||'proced'"+
				" where bid='"+bidCancellato+"'" +
				" and to_char(ts_var,'yyyymmddhh24mi')>=(select to_char(ts_var,'yyyymmddhh24mi')"+
				" from tb_titolo where bid='"+bidCancellato+"')");
		
		rs = stmt.executeQuery("update tb_musica set fl_canc=' ', ute_var=substr(ute_var,1,6)||'proced' where bid='"+bidCancellato
				+"' and to_char(ts_var,'yyyymmddhh24mi') >= (select to_char(ts_var,'yyyymmddhh24mi') from tb_titolo where bid='"+bidCancellato+"')");
		
		rs = stmt.executeQuery("update tb_nota set fl_canc=' ', ute_var=substr(ute_var,1,6)||'proced' where bid='"+bidCancellato
				+"' and to_char(ts_var,'yyyymmddhh24mi') >= (select to_char(ts_var,'yyyymmddhh24mi') from tb_titolo where bid='"+bidCancellato+"')");
		rs = stmt.executeQuery("update tb_rappresent set fl_canc=' ', ute_var=substr(ute_var,1,6)||'proced' where bid='"+bidCancellato
				+"' and to_char(ts_var,'yyyymmddhh24mi') >= (select to_char(ts_var,'yyyymmddhh24mi') from tb_titolo where bid='"+bidCancellato+"')");
		rs = stmt.executeQuery("update tb_audiovideo set fl_canc=' ', ute_var=substr(ute_var,1,6)||'proced' where bid='"+bidCancellato
				+"' and to_char(ts_var,'yyyymmddhh24mi') >= (select to_char(ts_var,'yyyymmddhh24mi') from tb_titolo where bid='"+bidCancellato+"')");
		rs = stmt.executeQuery("update tb_disco_sonoro set fl_canc=' ', ute_var=substr(ute_var,1,6)||'proced' where bid='"+bidCancellato
				+"' and to_char(ts_var,'yyyymmddhh24mi') >= (select to_char(ts_var,'yyyymmddhh24mi') from tb_titolo where bid='"+bidCancellato+"')");

		// legami da rimettere in vita
		// (con stesso timbro di bidCancellato in tb_titolo)
		
		stmt.executeQuery("update tr_tit_tit set fl_canc=' ', ute_var=substr(ute_var,1,6)||'proced' where bid_base='"+bidCancellato
			+"'	and to_char(ts_var,'yyyymmddhh24mi')>=(select to_char(ts_var,'yyyymmddhh24mi') from tb_titolo where bid='"+bidCancellato+"')");

		rs = stmt.executeQuery("update tr_tit_aut set fl_canc=' ',ute_var=substr(ute_var,1,6)||'proced' where bid='"+bidCancellato
			+"' and to_char(ts_var,'yyyymmddhh24mi')>=(select to_char(ts_var,'yyyymmddhh24mi') from tb_titolo where bid='"+bidCancellato+"')");
		rs = stmt.executeQuery("update tr_tit_aut set fl_canc='S',ute_var=substr(ute_var,1,6)||'proced' where bid='"+bidSopravissuto // imposta bid link
				+"'	and to_char(ts_ins,'yyyymmddhh24mi')>=(select to_char(ts_var,'yyyymmddhh24mi') from tb_titolo where bid='"+bidCancellato+"')");

		rs = stmt.executeQuery("update tr_tit_sog set fl_canc=' ',ute_var=substr(ute_var,1,6)||'proced' where bid='"+bidCancellato
				+"' and to_char(ts_var,'yyyymmddhh24mi')>=(select to_char(ts_var,'yyyymmddhh24mi') from tb_titolo where bid='"+bidCancellato+"')");
		rs = stmt.executeQuery("update tr_tit_sog set fl_canc='S',ute_var=substr(ute_var,1,6)||'proced' where bid='"+bidSopravissuto // imposta bid link
				+"'	and to_char(ts_ins,'yyyymmddhh24mi')>=(select to_char(ts_var,'yyyymmddhh24mi') from tb_titolo where bid='"+bidCancellato+"')");
		
		rs = stmt.executeQuery("update tr_tit_cla set fl_canc=' ',ute_var=substr(ute_var,1,6)||'proced' where bid='"+bidCancellato
				+"' and to_char(ts_var,'yyyymmddhh24mi')>=(select to_char(ts_var,'yyyymmddhh24mi') from tb_titolo where bid='"+bidCancellato+"')"); 
		rs = stmt.executeQuery("update tr_tit_cla set fl_canc='S',ute_var=substr(ute_var,1,6)||'proced' where bid='"+bidSopravissuto // imposta bid link
				+"'	and to_char(ts_ins,'yyyymmddhh24mi')>=(select to_char(ts_var,'yyyymmddhh24mi') from tb_titolo where bid='"+bidCancellato+"')");
		
		rs = stmt.executeQuery("update tr_tit_mar set fl_canc=' ',ute_var=substr(ute_var,1,6)||'proced' where bid='"+bidCancellato
				+"'	and to_char(ts_var,'yyyymmddhh24mi')>=(select to_char(ts_var,'yyyymmddhh24mi') from tb_titolo where bid='"+bidCancellato+"')");
		rs = stmt.executeQuery("update tr_tit_mar set fl_canc='S',ute_var=substr(ute_var,1,6)||'proced' where bid='"+bidSopravissuto // imposta bid link
				+"'	and to_char(ts_ins,'yyyymmddhh24mi')>=(select to_char(ts_var,'yyyymmddhh24mi') from tb_titolo where bid='"+bidCancellato+"')");
		
		rs = stmt.executeQuery("update tr_tit_luo set fl_canc=' ',ute_var=substr(ute_var,1,6)||'proced'	where bid='"+bidCancellato
				+"' and to_char(ts_var,'yyyymmddhh24mi')>=(select to_char(ts_var,'yyyymmddhh24mi')from tb_titolo where bid='"+bidCancellato+"')");
		rs = stmt.executeQuery("update tr_tit_luo set fl_canc='S',ute_var=substr(ute_var,1,6)||'proced' where bid='"+bidSopravissuto // imposta bid link
				+"'	and to_char(ts_ins,'yyyymmddhh24mi')>=(select to_char(ts_var,'yyyymmddhh24mi') from tb_titolo where bid='"+bidCancellato+"')");
		
		
		//---------------------------------------------------------------
		//--- gestione trascinamenti (titoli inferiori e numeri standard)
		//---------------------------------------------------------------

		// ripristina legami verso il bid fuso trascinati sull'arrivo (per data variazione = data fusione)
		// cancella dall'arrivo i legami trascinati (per data inserimento = data fusione)

//		rs = stmt.executeQuery("select * from tr_tit_tit where bid_coll='"+bidCancellato+"'");
		
		rs = stmt.executeQuery("update tr_tit_tit set fl_canc=' ',ute_var=substr(ute_var,1,6)||'proced'	where bid_coll='"+bidCancellato
			+"' and to_char(ts_var,'yyyymmddhh24mi')>=(select to_char(ts_var,'yyyymmddhh24mi')	from tb_titolo where bid='"+bidCancellato+"')"); 

		rs = stmt.executeQuery("update tr_tit_tit set fl_canc='S',ute_var=substr(ute_var,1,6)||'proced'	where bid_coll='"+bidSopravissuto // imposta bid link
			+"' and to_char(ts_ins,'yyyymmddhh24mi')>=(select to_char(ts_var,'yyyymmddhh24mi') from tb_titolo where bid='"+bidCancellato+"')"); // imposta il bid fuso

		// analogamente per i numeri standard trascinati : ripristina in partenza, cancella dall'arrivo

//		rs = stmt.executeQuery("select * from tb_numero_std where bid='"+bidCancellato+"'"); // imposta bid fuso

		rs = stmt.executeQuery("update tb_numero_std set fl_canc=' ',ute_var=substr(ute_var,1,6)||'proced' where bid='"+bidCancellato
			+"' and to_char(ts_var,'yyyymmddhh24mi')>=(select to_char(ts_var,'yyyymmddhh24mi') from tb_titolo where bid='"+bidCancellato+"')");

		rs = stmt.executeQuery("update tb_numero_std set fl_canc='S',ute_var=substr(ute_var,1,6)||'proced' where bid='"+bidSopravissuto // imposta bid link
			+"'	and to_char(ts_ins,'yyyymmddhh24mi')>=(select to_char(ts_var,'yyyymmddhh24mi') from tb_titolo where bid='"+bidCancellato+"')");

		

		
		
		
		
//		rs = stmt.executeQuery("commit");
		recordResuscitati++;
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		
		try {
			stmt.executeQuery("rollback");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	} //  and fl_canc = 'S' 
 catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
}


} // End RescuscitaRecord
