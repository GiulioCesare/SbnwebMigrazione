import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
// import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import it.finsiel.misc.DateUtil;
import it.finsiel.misc.Misc;
import it.finsiel.misc.MiscString;
import it.finsiel.misc.MiscStringTokenizer;
public class DbUpload {
//	public final static int IN_TABLE_DECLARATION  = 1;
	public final static int IN_PROCEDURE_DECLARATION = 1;
	
	public final static int IN_SQL_DECLARATION  = 2;
	public final static int IN_TYPE_DECLARATION  = 3;
	public final static int IN_NEXT_VAL_DECLARATION  = 4;

	final static int TABELLA_UNDEFINED = 0;
	final static int TABELLA_TR_TIT_TIT = 1;
//	final static int TABELLA_TB_TITOLO = 2;
//	final static int TABELLA_TB_AUTORE = 3;
	
	
	String msg;

	java.util.Vector vecInputFiles; // Buffer Reader Files
	String UploadDir = "";
	int fileCounter = 0;
	boolean setCurCfg=false;
	String logFileOut = "";
	//FileOutputStream streamOutLog;
	BufferedWriter OutLog;
	Connection con = null;
	int commitOgniTotRighe = 255;
	//String dbName = "";
//	String url = "jdbc:odbc:bob"; // "jdbc:myDriver:wombat"
	String jdbcDriver="";
	String connectionUrl = ""; // "jdbc:postgresql://localhost:5432/sbnwebArge"
	String userName="";
	String userPassword="";
	boolean useSingleSeparator = false;
	String	fieldSeparator="|";
	byte fieldSeparatorByte = (byte)0x7C; // 0xC0
	String removeAllRecordsBeforeUpload="";
	long startLoadingFrom = 1;
	long elaboraNRighe=0;	// 11/02/2022
	
	String searchPath=""; // "sbnweb, pg_catalog, public;"
	String schema="";
	Statement stmt = null;
	
	
//	java.util.Vector vecConfigTable; // Buffer Reader Files
	HashMap hashConfigTable;
	char charSepArrayComma[] = { ','};
//	char charSepArrayPipe[] = { '|'};
	static String stringSepDiCampoInArray[] = { "&$%"}; // &$%
	static char escapeCharacter = '\\'; // il backslash di default
//  	
	int tabella;
	ConfigTable configTable = null;
	
	
	public DbUpload()
	    {
		hashConfigTable = new java.util.HashMap();
	    }

	
	public boolean openConnection()
	{
		
		try {
//			if (connectionUrl.contains("postgresql"))
//				Class.forName("org.postgresql.Driver");
//			else
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

//				if (jdbcDriver.contains("postgres"))
//				{
//					stmt.execute("SET search_path = "+searchPath); // sbnweb, pg_catalog, public
//					if (setCurCfg == true)
//						stmt.execute("select set_curcfg('default')"); // Esegui questa select per attivare gli indici testuali 
//				}
				
				int pos = jdbcDriver.indexOf("postgres"); // 10/02/2022
				if (pos  != -1)
				{
					stmt.execute("SET search_path = sbnweb, pg_catalog, public");
					if (setCurCfg == true)
						stmt.execute("select set_curcfg('default')"); // Esegui questa select per attivare gli indici testuali 
				}
				
				OutLog.write("\nOpened connection: "+connectionUrl);
				System.out.println("\nOpened connection: "+connectionUrl);
				
				return true;
		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
		
	
public long getFileSize(String filename) {

    File file = new File(filename);
		    
    if (!file.exists() || !file.isFile()) {
      System.out.println("File doesn\'t exist");
      return -1;
    }
    
    //Here we get the actual size
    return file.length();
  }	



private void upload2(ConfigQuery configQuery, BufferedReader fileIn, String filename)
{
	String data = ""; // eg. "'Colombian', 00101, 7.99, 0, 0"
	int rowCtr = 0;
	int commitRowCtr = 0;
	int rollbackRecordsFrom = 1;
	
	int recordsErrati = 0; 
	String arTipi[];
	String arData[];
	StringBuffer sb = new StringBuffer();
	ArrayList sqlExecutedOk = new ArrayList();
	String tableName;
	int pos;
	

	
	pos = filename.indexOf(".");
	tableName = filename.substring(0, pos);
	if (tableName.equals("tr_tit_tit"))
		tabella = TABELLA_TR_TIT_TIT;
	else
		tabella = TABELLA_UNDEFINED;
	
	// Prendiamo i tipi dei dati per questa tabella
//	ConfigTable cfgTable = (ConfigTable)this.hashConfigTable.get(tableName);
//	if (cfgTable == null)
//	{
//		String l = "\n\tConfigurazione non trovata per tabella: " + tableName;
//		try {
//			OutLog.write(l);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(l);
//		return;
//	}
	
	// Scomponiamo la riga dei tipi in un array 
	String tipi =  configQuery.getTypeList();
	
	arTipi = MiscString.estraiCampi(tipi, charSepArrayComma, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
	

	// Open file and read through

//	BufferedReader fileIn = null;
//	try {
//		fileIn = new BufferedReader(new FileReader(UploadDir+"/"+filename));
		String sql = "";

		// Leggiamo le configurazioni di base
		// ----------------------------------
		while (true) {
			String s;
			try {
				data = fileIn.readLine();
				if (data == null)
					break;
				else if (data.charAt(0) == '#' || Misc.emptyString(data)) // se commento o riga vuota
					continue;

				rowCtr++;
				if (rowCtr < startLoadingFrom )
					continue;

//				System.out.println("\nRiga " + rowCtr + ": " + data);
				
				data = data.replace("'", "''"); // gestione di eventuali singoli apici 
				// Scomponiamo la riga dei dati in un array 
				// arData = MiscString.estraiCampi(data, stringSepDiCampoInArray, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
				arData = MiscString.estraiCampi(data, stringSepDiCampoInArray, MiscStringTokenizer.RETURN_DELIMITERS_AS_TOKEN_FALSE);

				if (   configQuery.sql.startsWith("insert into") 
					|| configQuery.sql.startsWith("use default insert"))
				{
					if (arData.length != arTipi.length)
					{
						String l = "\n\tAlla riga " +rowCtr +" il numero dei TIPI " + arTipi.length + " e' diverso dal numero dei CAMPI " + arData.length + " per tabella "+ tableName;
						recordsErrati++;
						try {
							OutLog.write(l);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println(l);
						continue;
						
					}
					// Componiamo la parte dei dati (values) della insert
					sb.setLength(0);
					for (int i=0; i < arTipi.length; i++)
					{
						if (i != 0)
							sb.append(',');
						if ( (arData[i].compareTo("NULL") == 0) || 
								(arData[i].compareTo("null") == 0)) 
							sb.append(arData[i]);
						else if (	arTipi[i].charAt(0) == 's' || arTipi[i].charAt(0) == 'S' ||
									arTipi[i].charAt(0) == 't' || arTipi[i].charAt(0) == 'T'
									)
							sb.append("'" + arData[i] + "'"); // circonda con singoli apici
						else if (	arTipi[i].charAt(0) == 'c' || arTipi[i].charAt(0) == 'C') // contatore
							sb.append(configQuery.getNextVal());

						else if (	arTipi[i].charAt(0) == 'd' || arTipi[i].charAt(0) == 'D')
							sb.append("CURRENT_TIMESTAMP");

						else if (	(arTipi[i].charAt(0) == 'n' || arTipi[i].charAt(0) == 'N') && arData[i].length() == 0)
							sb.append("null"); // 20/09/2015

						else 
							sb.append(arData[i]);
					}

				} // End use default insert
				else if (   configQuery.sql.startsWith("use parametric insert"))
				{
					sql = configQuery.sql;							
					while (true)
					{
						// Troviamo se ci sono parametri da sostituire
						int idx = sql.indexOf('?');
						if (idx == -1)
							break;
						
						// troviamo il primo carattere non numerico
						int i;
						for (i=idx+1; i< sql.length(); i++)
						{
							if (Character.isDigit(sql.charAt(i)) == false)
								break;
						}
						
						String sub = sql.substring(idx+1, i);
						int paramId=Integer.parseInt(sub);
						
						if (	arTipi[paramId].charAt(0) == 's' || arTipi[paramId].charAt(0) == 'S')
							sql = sql.replace("?"+paramId, "'"+arData[paramId]+"'");
						else
							sql = sql.replace("?"+paramId, arData[paramId]);
							
					}
				} // End use parametric insert
				else
				{
					String l = "\n\tInvalid insert type  per tabella "+ tableName;
					recordsErrati++;
					try {
						OutLog.write(l);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(l);
					continue;
					
				}

//				
//ResultSet rs;
//try {
//	rs = con.getMetaData().getTables(null, null, null, null);
//	
//	sql = "SELECT `name`, `sql` FROM `sqlite_master` WHERE type='table'";
//	 Statement stmt  = con.createStatement();
//     rs    = stmt.executeQuery(sql);
//	while (rs.next()) {
//	    System.out.println(rs.getString("TABLE_NAME"));
//	}
//} catch (SQLException e1) {
//	// TODO Auto-generated catch block
//	e1.printStackTrace();
//}
//				
				
				// Insert record
				try {
					
					// Commit every tot records
//					if ((rowCtr & commitOgniTotRighe) == 0) 
					if ((rowCtr % commitOgniTotRighe) == 0) 
					{
						if (!connectionUrl.contains("sqlite"))
						{
							System.out.print("\rCommitting at row " + rowCtr);
							stmt.execute("COMMIT"); // ;
							sqlExecutedOk.clear();
							rollbackRecordsFrom = rowCtr;
						}
					}

					// TODO Manage user insert
					if (configQuery.sql.startsWith("insert into")) // 30/09/15
						{
						int idx = configQuery.sql.indexOf(')');
						sql = configQuery.sql.substring(0, idx+1) + " values(" + sb.toString() + ")";
						}
					else if ( configQuery.sql.startsWith("use default insert"))
						if (schema.length() > 0)
							sql = "insert into " + schema + "." + tableName + " values(" + sb.toString() + ")";
						else
							sql = "insert into " + tableName + " values(" + sb.toString() + ")";
					else
						sql = sql.replace("use parametric insert", "insert into");
						

					// Else sql reaady
					String sqlUtf8 = new String (sql.getBytes(), "UTF-8");

//System.out.println("Insert " + sqlUtf8 );
					stmt.execute(sqlUtf8);
					
					if (tabella == TABELLA_TR_TIT_TIT) // 17/11/2017
						{
							// Aggiorniao il flag allinea per il titolo di sinistra legato al titolo di destra
							
							
						//	#  Se fl_allinea NON impostato
						//	#     Si imposta l'attributo tipoModifica secondo la regola seguente:
						//	#     se fl_allinea o fl_allinea_sbnmarc= C si imposta 'Legami' (VALUE_0)
						//	#*     se fl_allinea o fl_allinea_sbnmarc= S si imposta 'Dati' (VALUE_1)
						//	#     se fl_allinea o fl_allinea_sbnmarc= Z si imposta 'Dati e Legami' (VALUE_5)
						//	#     se fl_allinea o fl_allinea_sbnmarc= X si imposta 'Natura' (VALUE_6)
						//	#     se S o Z e tb_titolo.fl_canc='S' si si imposta 'Cancellato' (VALUE_2)
						//	#     se S o Z e tb_titolo.tp_link='F' si si imposta 'Fusione' (VALUE_4)
						//
						//	# Aggiornamento di fl_allinea se e' GIA' valorizzato (tr_tit_bin)
						//	#     se fl_allinea = C e si tratta di 'legami' fl_allinea rimane C
						//	#*     se fl_allinea = C e si tratta di 'dati'   fl_allinea diventa Z
						//	#*     se fl_allinea = S e si tratta di 'dati'   fl_allinea rimane S
						//	#     se fl_allinea = S e si tratta di 'legami' fl_allinea diventa Z
						//	#     se fl_allinea = Z                         fl_allinea rimane Z
						//	#     se fl_allinea = X e si tratta di 'dati'   fl_allinea rimane X
						//	#     se fl_allinea = X e si tratta di 'legami' fl_allinea rimane X
						//	#     se fl_allinea = S o = Z e tb_titolo.fl_canc='S' non si cambia fl_allinea
						//	#     se fl_allinea = S o = Z e tb_titolo.tp_link='F' non si cambia fl_allinea
						
							String bid_base = arData[0]; // natura M
							
							sql = "update tr_tit_bib set fl_allinea = (CASE fl_allinea WHEN ' ' THEN 'S' WHEN 'C' THEN 'Z' ELSE fl_allinea END), ute_var='dbuploadVar1', ts_var=CURRENT_TIMESTAMP where BID='"+bid_base+"'";
							sqlUtf8 = new String (sql.getBytes(), "UTF-8");
							stmt.execute(sqlUtf8);
							
							sql = "update tb_titolo set ute_var='dbuploadVar1', ts_var=CURRENT_TIMESTAMP where BID='"+bid_base+"'";
							sqlUtf8 = new String (sql.getBytes(), "UTF-8");
							stmt.execute(sqlUtf8);
							
							
						}
					
					sqlExecutedOk.add(sql);
					commitRowCtr++;
					
//break;					
				} catch (SQLException e) {
					OutLog.write("\n\tRecord errato alla riga " + rowCtr + ": " + e.getMessage() + " ---> " + sql);
					System.out.println("\nRecord errato alla riga " + rowCtr + ": " + sql);
					recordsErrati++;
					System.out.println ("EXCEPTION: " +e.getMessage());
				} // end catch
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		if ( elaboraNRighe !=0 && (rowCtr-startLoadingFrom+1) >= elaboraNRighe)
			break;
			
			
		} // end while
		try {
			msg = "\nCommitting at row " + rowCtr;
			System.out.print(msg);
			OutLog.write(msg);

			if (!connectionUrl.contains("sqlite"))
			{
				try {
					stmt.execute("COMMIT"); // ;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			

			System.out.println("\n\tStarLoadingFrom " + (startLoadingFrom));
			System.out.println("\n\n\tRecord letti " + (rowCtr - startLoadingFrom +1));
			System.out.println("\n\tRecord errati " + recordsErrati);
			System.out.println("\n\tRecord inseriti " + (rowCtr - recordsErrati - startLoadingFrom +1));

			OutLog.write("\n\tStarLoadingFrom " + (startLoadingFrom));
			OutLog.write("\n\n\tRecord letti " + (rowCtr - startLoadingFrom));
			OutLog.write("\n\tRecord errati " + recordsErrati);
			OutLog.write("\n\tRecord inseriti " + (rowCtr - recordsErrati - startLoadingFrom));


			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//	}
//	 catch (FileNotFoundException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
	
} // End upload2
	
public void upload(String filename) {
	
	int pos;
	
//	if (useSingleSeparator == true)
//		filename = filename+".bytes"; 19/08/2020 (per DOPPIONI)
	
	System.out.println("\n\nInizio caricamento " + filename + " " + DateUtil.getDate() + " " + DateUtil.getTime()); 
	try {
		OutLog.write("\n\n-------------------------");
		OutLog.write("\nInizio caricamento " + filename + " " + DateUtil.getDate() + " " + DateUtil.getTime());
	} catch (IOException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	} 
	
	pos = filename.indexOf(".");
	String tableName = filename.substring(0, pos);	
	if (pos == -1)
	{
		System.out.println("Manca estensione a nome file: " + filename);
		return;
	}
		
	
	
	try {
		stmt = con.createStatement();
//		if (removeAllRecordsBeforeUpload.compareTo("true") == 0)
		if (removeAllRecordsBeforeUpload.equals("true"))
		{
			long filesize = getFileSize(UploadDir+"/"+filename);
			if (filesize < 1)
			{
				System.out.println("Non posso troncare tabella se file da caricare e' vuoto o non esistente: " + UploadDir+"/"+filename);
				return;
			}
			
			if (startLoadingFrom > 1)
			{
				System.out.println("Non posso troncare tabella se inizio ad inserire dal record N." + startLoadingFrom);
			}
			else
			{
				boolean ret;
				if (searchPath.length() > 0)
				{
					String setPath = "SET search_path = "+searchPath; //sbnweb, pg_catalog, public;
					ret = stmt.execute(setPath);
				}

				String st;
				// There is no TRUNCATE command in the SQL standard.
//				String st = "TRUNCATE TABLE " + schema + "." + tableName ; // "sbnweb."

				if (connectionUrl.contains("sqlite"))
					st = "DELETE FROM " + tableName +";" ; // SQLITE3
				else if (connectionUrl.contains("postgresql"))
					st = "TRUNCATE TABLE " + tableName ; // POSTGRES
				else
					st = "TRUNCATE TABLE " + tableName ; // Generic 
				
				System.out.println(st);
				stmt.execute(st); // Esegui questa select per attivare gli indici testuali
			}
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		try {
			OutLog.write ("\n" + e.getMessage());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (e.getMessage().contains("cannot truncate"))
			return;
	}


	
	String procedureName = filename.substring(0, filename.lastIndexOf('.'));
	ConfigTable cfgTable = (ConfigTable)this.hashConfigTable.get(procedureName);
	if (cfgTable == null)
	{
		String l = "\n\tConfigurazione non trovata per tabella: " + tableName;
		try {
			OutLog.write(l);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(l);
		return;
	}
	ArrayList<ConfigQuery> configQueries = cfgTable.getConfigQueries(); 
	
	System.out.println("Inizio caricamento da riga "+startLoadingFrom);
	
	FileInputStream fIn=null;
	BufferedReader bufferedReader = null;
	
	try {
		// Open data file and read through
		fIn = new FileInputStream(UploadDir + "/" + filename);
//		bufferedReader = new BufferedReader(new FileReader(UploadDir + "/" + filename));

		for (ConfigQuery configQuery:configQueries)
		{
			// "reset" to beginning of file (discard old buffered reader)
			fIn.getChannel().position(0);
			bufferedReader = new BufferedReader(new InputStreamReader(fIn));
			upload2(configQuery, bufferedReader, filename);
		}	
	
		
	} catch (IOException e) {
		e.printStackTrace();
	}

	// Close file
	 if (fIn != null)
		try {
			fIn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("\nFine caricamento "+ filename  + DateUtil.getDate() + " " + DateUtil.getTime()); 
		try {
			OutLog.write("\nFine caricamento " + filename + DateUtil.getDate() + " " + DateUtil.getTime() );
			
			OutLog.close();	// Chiudi log dopo aver caricato un file
			OutLog = new BufferedWriter(new FileWriter(logFileOut, true)); // Riapri in APPEND				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
} // End upload
	


public static void run_main(String args[])
{
	char charSepArrayEquals[] = { '='};
	char charSepArraySpace[] = { ' '};

	if(args.length < 2)
    {
        System.out.println("Uso: DbUpload <config filename> <List filename>"); // <logFilename>
        System.exit(1);
    }
	String configFile = args[0];
    String inputFile = args[1];
	// String configFile = "/home/argentino/indice_3/migrazione_oracle_postgres/DbUpload.cfg";
    // String inputFile = "/home/argentino/indice_3/migrazione_oracle_postgres/DbUpload.con";



//	logFileOut = args[2];
    
    String start=      
	 "\n========================================================="+
	 "\nTool di caricamento della base dati"+
	 "\nInizio esecuzione " + DateUtil.getDate() + " " + DateUtil.getTime()+ 
	 "\nFile di configurazione: " + configFile +
	 "\nElenco tabelle da trattare: " + inputFile ;

    System.out.println(start);

	
//	System.out.println("inp: " + inputFile);

//	ImportaLegamiCidBid dbUpload = new ImportaLegamiCidBid();
//    RimuoviSoggetti dbUpload = new RimuoviSoggetti();
    DbUpload dbUpload = new DbUpload();
    
	BufferedReader in;
	String s;
	String ar[];
	
	int state = 0;
	
	try {
		
		
		in = new BufferedReader(new FileReader(configFile));

		ConfigQuery configQuery=null;
		
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
					// E' una dichiarazione di tabella
					if (s.charAt(0) == '[')
					{
						// Abbiamo un'altra config di tabella da salvare?
//						if (configTable != null)
//							dbUpload.hashConfigTable.put(configTable.tableName, configTable);
//						configTable = new ConfigTable();
//						configTable.tableName = new String (s.substring(1, s.length()-1));
//						state = IN_TABLE_DECLARATION;	
						
						if (dbUpload.configTable != null)
						{
							if (configQuery != null)
								{
								dbUpload.configTable.addQuery(configQuery);
								configQuery = null;
								}
							
							dbUpload.hashConfigTable.put(dbUpload.configTable.procedureName, dbUpload.configTable);
//							dbUpload.configTable.dump();
						}
						dbUpload.configTable = new ConfigTable();
						dbUpload.configTable.procedureName = new String(s.substring(1,s.length() - 1));
						
						state = IN_PROCEDURE_DECLARATION;
						
					}
					else if (state == IN_PROCEDURE_DECLARATION
							
							|| s.startsWith("insert into") 
							|| s.startsWith("use ") // default insert / parametric insert
							)
							
					{
						if (configQuery != null)
						{
						dbUpload.configTable.addQuery(configQuery);
						configQuery = null;
						}

						configQuery=new ConfigQuery();
						configQuery.sql = new String(s);
						
//						configTable.sql = new String (s);
						state = IN_SQL_DECLARATION;	
					}
					else if (state == IN_SQL_DECLARATION)
					{
//						configTable.typeList = new String (s);
						configQuery.typeList = new String(s);
						state = IN_TYPE_DECLARATION;	
					}
					else if (state == IN_TYPE_DECLARATION)
					{
//						configTable.nextVal = new String (s);
						configQuery.nextVal = new String(s);
					state = IN_NEXT_VAL_DECLARATION;	
					}
					else
					{
						// Ignore anything else
					}
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Salviamo l'ultima dichiarazione
//		if (configTable != null)
//			dbUpload.hashConfigTable.put(configTable.tableName, configTable);
		if (dbUpload.configTable != null)
		{
			if (configQuery != null)
				dbUpload.configTable.addQuery(configQuery);
			
			dbUpload.hashConfigTable.put(dbUpload.configTable.procedureName, dbUpload.configTable);
//			dbUpload.configTable.dump();
		}

		in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	try {
//		in = new BufferedReader(new FileReader(inputFile));
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
					ar = MiscString.estraiCampi(s,  charSepArrayEquals, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE); // "="
					if (ar[0].startsWith("uploadDir"))
						dbUpload.UploadDir = ar[1];
					else if (ar[0].startsWith("jdbcDriver"))
						dbUpload.jdbcDriver = ar[1];
					else if (ar[0].startsWith("connectionUrl"))
						dbUpload.connectionUrl = ar[1];
					else if (ar[0].startsWith("userName"))
						dbUpload.userName = ar[1];
					else if (ar[0].startsWith("userPassword"))
						dbUpload.userPassword = ar[1];
					
					else if (ar[0].startsWith("useSingleSeparator"))
					{
						if (ar[1].charAt(0) == '1')
							dbUpload.useSingleSeparator = true;
						else
							dbUpload.useSingleSeparator = false;
					}
					
					else if (ar[0].startsWith("fieldSeparator"))
					{
						if (dbUpload.useSingleSeparator)
						{
							byte [] bSep = hexStringToByteArray(ar[1]);
							//dbUpload.fieldSeparatorByte = bSep[0];
							stringSepDiCampoInArray[0]=new String(bSep);
							dbUpload.fieldSeparator = stringSepDiCampoInArray[0];
							
						}
						else
						{
							dbUpload.fieldSeparator = ar[1];
							stringSepDiCampoInArray[0] = ar[1];
						}

						System.out.println("Field separator: '"	+ dbUpload.fieldSeparator + "'");
						
					}
					else if (ar[0].startsWith("removeAllRecordsBeforeUpload"))
						dbUpload.removeAllRecordsBeforeUpload = ar[1];
					else if (ar[0].startsWith("logFileOut"))
						dbUpload.logFileOut = ar[1];
					else if (ar[0].startsWith("startLoadingFrom"))
						dbUpload.startLoadingFrom = Long.parseLong(ar[1]);

					else if (ar[0].startsWith("elaboraNRighe"))
						dbUpload.elaboraNRighe = Long.parseLong(ar[1]);
					
					
					else if (ar[0].startsWith("searchPath"))
						dbUpload.searchPath = ar[1];
					else if (ar[0].startsWith("schema"))
						dbUpload.schema = ar[1];
					else if (ar[0].startsWith("commitOgniTotRighe"))
//						dbUpload.commitOgniTotRighe = Integer.parseInt(ar[1], 16);
						dbUpload.commitOgniTotRighe = Integer.parseInt(ar[1]);
					
					else if (ar[0].startsWith("setCurCfg"))
					{
						if (ar[1].equals("true"))
							dbUpload.setCurCfg = true;
						else
							dbUpload.setCurCfg = false;
					}
								
					else if (ar[0].startsWith("endConfig"))
						break;
					else
						System.out.println("ERRORE: parametro sconosciuto: "	+ ar[1]);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Apriamo il file di log
		try {

//			logFileOut = dbUpload.UploadDir + "/DbUpload.log";
			
//			dbUpload.streamOutLog = new FileOutputStream(logFileOut); 
			System.out.println("File di log: "	+ dbUpload.logFileOut);

			dbUpload.OutLog = new BufferedWriter(new FileWriter(dbUpload.logFileOut));				
			dbUpload.OutLog.write(start);
			
		} catch (Exception fnfEx) {
			fnfEx.printStackTrace();
			return;
		}
		
		// Apriamo il DB
		if (!dbUpload.openConnection())
			{
			try {
				dbUpload.OutLog.write("Failed to open DB of URL: " + dbUpload.connectionUrl);
				dbUpload.OutLog.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
			}
		
		
		
		// Esegui trattamento delle singole tabelle
			// ----------------------------------------
			dbUpload.fileCounter = 0;
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

//						System.out.println("\n\nCaricando: " + s);
//						dbUpload.OutLog.write("\n\nCaricando: " + s);
						
						ar = MiscString.estraiCampi(s, charSepArraySpace, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE); //  " "
						
						dbUpload.fileCounter++;
						dbUpload.upload(ar[0]); // Inserisci tabella
						
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} // End while
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				System.out.println("\n\nFine caricamento!");
				dbUpload.OutLog.write("\n\nFine caricamento!");
				dbUpload.OutLog.write("\nFine esecuzione " + DateUtil.getDate() + " " + DateUtil.getTime()); 
				System.out.println("Vedi " + dbUpload.logFileOut + " per i dettagli dell'elaborazione");
				// Close log file 
				if (dbUpload.OutLog != null)
					dbUpload.OutLog.close();
				// Close filelist input file	
				in.close();

				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Chiudiamo il DB
			dbUpload.closeConnection();
		
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();

		// Convert from binary format to readable format

//	} catch (UnsupportedEncodingException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	
} // End run_main 



public static void test()
{
/*	
    String url = "jdbc:sqlite:/home/argentino/sqlite3/lo1.db";
    Connection conn = null;
    try {
        conn = DriverManager.getConnection(url);
        String sql = "SELECT * from mig_coll";
        Statement stmt  = conn.createStatement();
        ResultSet rs    = stmt.executeQuery(sql);
       // loop through the result set
       while (rs.next()) {
           System.out.println(rs.getString("col_1") + "\t" + 
                              rs.getString("col_1") + "\t" +
                              rs.getString("col_2"));
       }

    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
*/

	
//	int ret;
//	ret = 1023 % 1024; 
//	System.out.println("ret="+ret);
//
//	ret = 1024 % 1024; 
//	System.out.println("ret="+ret);
//
//	ret = 1025 % 1024; 
//	System.out.println("ret="+ret);
//
//	ret = 2048 % 1024; 
//	System.out.println("ret="+ret);

//	int ret;
//	for (int i=0; i < 10000; i++)
//	{
//		ret= (i % 1024);
//		if (ret == 0)
//		{
//			System.out.println(i);
//		}
//	}
	
	
} // End test



public static void main(String args[])
{
//	test();

	System.out.println ("DbUpload tool - (c) Almaviva S.p.A 2008-2022 V.2022_02_16a"); // fixed section namwe selection
	run_main(args);
} // End main





}
