import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import it.finsiel.misc.*;

//import org.xml.sax.SAXException;

public class DbUpdateInsert {
	public final static int IN_PROCEDURE_DECLARATION = 1;
	public final static int IN_SQL_DECLARATION = 2;
	public final static int IN_TYPE_DECLARATION = 3;
	public final static int IN_NEXT_VAL_DECLARATION = 4;

	public final static int UPDATE_WITH_DATA = 1;
	public final static int UPDATE_WITHOUT_DATA = 2;

	final static int TABELLA_UNDEFINED = 0;
	
	int tabella;
	ConfigTable configTable = null;
	
	java.util.Vector vecInputFiles; // Buffer Reader Files
	String UploadDir = "";
	int fileCounter = 0;
	String logFileOut = "";
	boolean setCurCfg = false;
	// FileOutputStream streamOutLog;
//	BufferedWriter OutLog;
	FileWriter  OutLog;  

	Connection con = null;
	int commitOgniTotRighe = 10;
	String msg;
	
	String jdbcDriver = "";
	String connectionUrl = ""; // "jdbc:postgresql://localhost:5432/sbnwebArge"
	String userName = "";
	String userPassword = "";
	String fieldSeparator = "|";
	String removeAllRecordsBeforeUpload = "";
	boolean showQuery=false;
	long startLoadingFrom = 1;
	long elaboraNRighe=0;
	int progress=0x7f;

	
	String searchPath = ""; // "sbnweb, pg_catalog, public;"
	String schema = "";
	
	// java.util.Vector vecConfigTable; // Buffer Reader Files
	HashMap hashConfigTable;
	char charSepArrayComma[] = { ',' };
	// char charSepArrayPipe[] = { '|'};
	static String stringSepDiCampoInArray[] = { "&$%" }; // &$%
	static char escapeCharacter = '\\'; // il backslash di default
	//

	// final static int TABELLA_TB_AUTORE = 2;
	ConfigTable cfgTable=null;
	String tableName=null;
	String data = ""; // eg. "'Colombian', 00101, 7.99, 0, 0"
	String dataIn = ""; // eg. "'Colombian', 00101, 7.99, 0, 0"
	Statement stmt = null;
	int rowCtr = 0;
	
	int updateEseguite = 0;
	int recordInseriti = 0;
	int recordAggiornati = 0;
	int recordsErrati = 0;
	
	int commitRowCtr = 0;
	int rollbackRecordsFrom = 1;
	String arTipi[];
	String arData[];
	StringBuffer sb;
	ArrayList sqlExecutedOk = new ArrayList();
	
	
	
	public DbUpdateInsert() {
		hashConfigTable = new java.util.HashMap();
	}

	public boolean openConnection() {
		System.out.println("Conncecting to " + connectionUrl + " userName="	+ userName);

		try {
			// Class.forName("org.postgresql.Driver");
			Class.forName(jdbcDriver);

		} catch (java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}

		try {
			con = DriverManager.getConnection(connectionUrl, userName,
					userPassword);
			boolean autoCommit = con.getAutoCommit();
			con.setAutoCommit(false);

			//Statement stmt = null;
			try {
				stmt = con.createStatement();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (jdbcDriver.contains("postgres")) {
				stmt.execute("SET search_path = " + searchPath); // sbnweb,
																	// pg_catalog,
																	// public
				if (setCurCfg == true)
					stmt.execute("select set_curcfg('default')"); // Esegui
																	// questa
																	// select
																	// per
																	// attivare
																	// gli
																	// indici
																	// testuali
			}

			if (jdbcDriver.contains("mysql")) {
				con.setAutoCommit(false);
			}
			
			return true;
		} catch (SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
		return false;
	} // End openConnection

	public void closeConnection() {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} // End closeConnection


	public static void main(String args[]) {
		char charSepArrayEquals[] = { '=' };
		char charSepArraySpace[] = { ' ' };

		if (args.length < 2) {
			System.out
					.println("Uso: DbUpdateInsert <config filename> <List filename>"); // <logFilename>
			System.exit(1);
		}
		String configFile = args[0];
		String inputFile = args[1];
		// logFileOut = args[2];

		String startMsg = "DbUpdateInsert tool - � Almaviva S.p.A 2020"
				+    "\n=============================================="
				+ "\nTool di aggiornamento o inserimento record della base dati"
				+ "\nInizio esecuzione " + DateUtil.getDate() + " "
				+ DateUtil.getTime() + "\nFile di configurazione: "
				+ configFile + "\nElenco tabelle da trattare: " + inputFile;

		System.out.println(startMsg);

		// System.out.println("inp: " + inputFile);

		DbUpdateInsert dbUpdateInsert = new DbUpdateInsert();

		BufferedReader inTxt;
		String s;
		String ar[];

		int state = 0;

		dbUpdateInsert.leggiFileDiConfigurazioneCFG(configFile);
			

		try {
			// in = new BufferedReader(new FileReader(inputFile));
			inTxt = new BufferedReader(new InputStreamReader(new FileInputStream( inputFile), "UTF8"));

			// Leggiamo le configurazioni di accesso al db
			// ----------------------------------------
			while (true) {
				try {
					s = inTxt.readLine();
					if (s == null)
						break;
					else if (Misc.emptyString(s))
						continue;
					else if ((s.length() == 0) || (s.charAt(0) == '#')
							|| (Misc.emptyString(s) == true))
						continue;
					else {
						ar = MiscString.estraiCampi(s, charSepArrayEquals,
								MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE); // "="
						if (ar[0].startsWith("uploadDir"))
							dbUpdateInsert.UploadDir = ar[1];
						else if (ar[0].startsWith("jdbcDriver"))
							dbUpdateInsert.jdbcDriver = ar[1];
						else if (ar[0].startsWith("connectionUrl"))
							dbUpdateInsert.connectionUrl = ar[1];
						else if (ar[0].startsWith("userName"))
							dbUpdateInsert.userName = ar[1];
						else if (ar[0].startsWith("userPassword"))
							dbUpdateInsert.userPassword = ar[1];
						else if (ar[0].startsWith("fieldSeparator")) {
							dbUpdateInsert.fieldSeparator = ar[1];
							stringSepDiCampoInArray[0] = ar[1];
						} else if (ar[0]
								.startsWith("removeAllRecordsBeforeUpload"))
							dbUpdateInsert.removeAllRecordsBeforeUpload = ar[1];
						else if (ar[0].startsWith("logFileOut"))
							dbUpdateInsert.logFileOut = ar[1];
						else if (ar[0].startsWith("startLoadingFrom"))
							dbUpdateInsert.startLoadingFrom = Long.parseLong(ar[1]);
						
						else if (ar[0].startsWith("elaboraNRighe"))
							dbUpdateInsert.elaboraNRighe = Long.parseLong(ar[1]);

						else if (ar[0].startsWith("progress"))
							dbUpdateInsert.progress = Integer.parseInt(ar[1], 16);
						
						else if (ar[0].startsWith("showQuery"))
						{
							if (ar[1].equals("true"))
								dbUpdateInsert.showQuery = true; // default is false
						}
						
						
						else if (ar[0].startsWith("searchPath"))
							dbUpdateInsert.searchPath = ar[1];
						else if (ar[0].startsWith("schema"))
							dbUpdateInsert.schema = ar[1];
						else if (ar[0].startsWith("setCurCfg")) {
							if (ar[1].equals("true"))
								dbUpdateInsert.setCurCfg = true;
							else
								dbUpdateInsert.setCurCfg = false;
						}
						else if (ar[0].startsWith("commitOgniTotRighe"))
							dbUpdateInsert.commitOgniTotRighe = Integer.parseInt(ar[1], 16);
						
						else if (ar[0].startsWith("endConfig"))
							break;
						else
							System.out.println("ERRORE: parametro sconosciuto: " + ar[1]);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} // fine leggiamo file di configurazione accesso al DB

			
			// Apriamo il file di log
			try {

				// logFileOut = dbUpdate.UploadDir + "/dbUpdate.log";

				// dbUpdate.streamOutLog = new FileOutputStream(logFileOut);
				System.out.println("File di log: " + dbUpdateInsert.logFileOut);

//				dbUpdate.OutLog = new BufferedWriter(new FileWriter(dbUpdate.logFileOut));
				dbUpdateInsert.OutLog = new FileWriter(dbUpdateInsert.logFileOut);
				dbUpdateInsert.log(startMsg);

			} catch (Exception fnfEx) {
				fnfEx.printStackTrace();
				return;
			}

			// Apriamo il DB
			if (!dbUpdateInsert.openConnection()) {
				try {
					dbUpdateInsert.log("Failed to open DB of URL: " + dbUpdateInsert.connectionUrl);
					dbUpdateInsert.OutLog.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}

			// Esegui trattamento delle singole procedure
			// ----------------------------------------
			dbUpdateInsert.fileCounter = 0;
			while (true) {
				try {
					s = inTxt.readLine();
					if (s == null)
						break;
					else {
						if ((s.length() == 0) || (s.charAt(0) == '#')
								|| (Misc.emptyString(s) == true))
							continue;

						// System.out.println("\n\nCaricando: " + s);
						// dbUpdate.OutLog.write("\n\nCaricando: " + s);

						ar = MiscString.estraiCampi(s, charSepArraySpace,
								MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE); // " "

						dbUpdateInsert.fileCounter++;
						dbUpdateInsert.updateInsert(ar[0]); // Update tabella

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try {
				System.out.println("\n\nFine caricamento!");
				dbUpdateInsert.log("\n\nFine caricamento!");
				dbUpdateInsert.log("\nFine esecuzione " + DateUtil.getDate()
						+ " " + DateUtil.getTime());
				System.out.println("Vedi " + dbUpdateInsert.logFileOut
						+ " per i dettagli dell'elaborazione");
				// Close log file
				if (dbUpdateInsert.OutLog != null)
					dbUpdateInsert.OutLog.close();
				// Close filelist input file
				inTxt.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

			// Chiudiamo il DB
			dbUpdateInsert.closeConnection();

		} catch (FileNotFoundException e) {
			e.printStackTrace();

			// Convert from binary format to readable format

			// } catch (UnsupportedEncodingException e) {
			// e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// System.out.println("Righe elaborate: " +
		// Integer.toString(rowCounter));
		System.exit(0);
	} // End main


void log(String s)
{
	try {
		OutLog.write(s);
		OutLog.flush();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}







private void updateInsertDefault(ConfigQuery configQuery, Statement stmt, int hasData) throws SQLException
{
	// Update gestita in file di configurazione .cfg
	// Prendiamo la SQL e sostituiamo i ?# con i dati 
	String multiple_sql = configQuery.sql;							
	
	String[] split_sql = multiple_sql.split("\\|");
	if (split_sql.length != 2)
	{
		log("Invalid update or insert multiple query: '"+configQuery.sql+"'\n");
		return;
	}
	String sql = split_sql[0];
	if (hasData == UPDATE_WITH_DATA)
	{
		for (int i=0; i < arData.length; i++)
		{
			if (arTipi[i].equals("s"))
				sql = sql.replace("?"+i, "'"+arData[i]+"'");
			else
				sql = sql.replace("?"+i, arData[i]);
		}
	}

	String sqlUtf8="";
	try {
		sqlUtf8 = new String(sql.getBytes(), "UTF-8");
		//System.out.println("Da eseguire: " + sqlUtf8);

		if (showQuery == true)	// 19/06/2019
		{
			log("query='"+sqlUtf8+"'");
			System.out.println("query='"+sqlUtf8+"'");
		}
		
		updateEseguite++;
		stmt.execute(sqlUtf8);

		int rowsAffected = stmt.getUpdateCount();
		if (rowsAffected != 0)
		{
			recordAggiornati += rowsAffected; 
		}
		else
		{
//			System.out.println("Record non aggiornato per '"+arData[0]+"'");
			sql = split_sql[1];
			if (hasData == UPDATE_WITH_DATA)
			{
				for (int i=0; i < arData.length; i++)
				{
					if (arTipi[i].equals("s"))
						sql = sql.replace("?"+i, "'"+arData[i]+"'");
					else
						sql = sql.replace("?"+i, arData[i]);
				}
			}

			sqlUtf8 = new String(sql.getBytes(), "UTF-8");
			// System.out.println("Da eseguire: " +
			// sqlUtf8);
			
			if (showQuery == true)
				log("query='"+sqlUtf8+"'");
			
			stmt.execute(sqlUtf8);
			rowsAffected = stmt.getUpdateCount();
			if (rowsAffected != 0)
				recordInseriti++;
			else
			{
				log("Record non aggiornato per '"+sqlUtf8+"'\n");
				recordsErrati++;
			}
		}
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
//		e.printStackTrace();
		System.out.println("\nRecord errato alla riga " + rowCtr + ": " + sqlUtf8);
		recordsErrati++;
		throw new SQLException(e);
	}
}// End updateDefault




private void manageSQLException(SQLException e)
{
//	OutLog.write("\tRecord errato alla riga " + rowCtr + ": " + e.getMessage() + " ---> " + sql + "\n");
	log("Record errato alla riga " + rowCtr + ": " + e.getMessage()); //  + "\n"
	log("Record data: '" + dataIn +"'\n");

//	System.out.println("\nRecord errato alla riga " + rowCtr + ": " + sql);
	recordsErrati++;
	e.printStackTrace();
	log("EXCEPTION: " + e.getMessage());

	// Commit, altrimenti tutti gli altri records fino al
	// prossimo commit verranno cestinati
	log("\nCommitting at row " + rowCtr);
	try {
		stmt.execute("COMMIT"); // ;
	} catch (SQLException e1) {
		e1.printStackTrace();
	}
	// Inserire tutti i record prima di questa eccezione che
	// la commit si � persa, di fatto ha fatto un fottuto
	// rollback
	// ... TO DO
	try {
		if (sqlExecutedOk.size() > 0)
			log("\nInserting rolled back records: from "
							+ rollbackRecordsFrom
							+ " to "
							+ (rowCtr - 1));
		for (int i = 0; i < sqlExecutedOk.size(); i++)
			stmt.execute((String) sqlExecutedOk.get(i));
		if (sqlExecutedOk.size() > 0) {
			log("COMMITting rolled back records;"); // Commit
																	// what
																	// had
																	// been
																	// rolled
																	// back
																	// before
																	// tje
																	// exception
			stmt.execute("COMMIT"); // ; Commit what had
									// been rolled back
									// before tje exception
			sqlExecutedOk.clear(); // Get ready for next
									// round
		}
		rollbackRecordsFrom = rowCtr + 1;
	} catch (SQLException ex) {

		try {
			stmt.execute("COMMIT"); // ;
			log("EXCEPTION: Errore in inserimento dei records ROLLED BACK"
							+ ex.getMessage());
			log("Records persi da "
					+ rollbackRecordsFrom + " a " + rowCtr);
		} catch (SQLException e1) {
			e1.printStackTrace();
		} // Commit what had been rolled back before tje
			// exception
	}
} // End manageSQLException



public void updateInsert(String filename) {
	cfgTable=null;
	tableName=null;
	data = ""; // eg. "'Colombian', 00101, 7.99, 0, 0"
	dataIn = ""; // eg. "'Colombian', 00101, 7.99, 0, 0"
	stmt = null;
	rowCtr = 0;
	recordInseriti = 0;
	updateEseguite = 0;
	recordAggiornati = 0;
	commitRowCtr = 0;
	rollbackRecordsFrom = 1;

	int recordsErrati = 0;

	int pos;
	boolean existsDataFile;
	
	
	
	log("\n\nInizio aggiornamento " + filename + " "	+ DateUtil.getDate() + " " + DateUtil.getTime());
	log("\n\n-------------------------");

	pos = filename.indexOf(".");
	if (pos == -1) {
		tableName = filename;	// 14/11/2017 gestione update senza dati
		existsDataFile = false;
	}
	else
	{
		tableName = filename.substring(0, pos);
		existsDataFile = true;
	}
	tabella = TABELLA_UNDEFINED;
	
	try {
		stmt = con.createStatement();
		// NO TRUNCATE SE IN AGGIORNAMENTO
//		truncateTable();
	} catch (SQLException e) {
		e.printStackTrace();
		log(e.getMessage()+"\n");
		if (e.getMessage().contains("cannot truncate"))
			return;
	}

	// Prendiamo i tipi dei dati per questa tabella
	cfgTable = (ConfigTable) this.hashConfigTable.get(tableName);
	if (cfgTable == null) {
		String l = "\n\tConfigurazione non trovata per tabella: " + tableName;
		log(l);
		System.out.println(l);
		return;
	}


	if (existsDataFile)
		updateWithData(filename);
	else
		updateWithoutData();
			
	msg = 	  "\n\n\tRecord letti per " + filename +" " + rowCtr 
			+ "\n\tUpdate eseguite per " + filename +" " + updateEseguite
			+ "\n\tRecord errati per " + filename +" " + recordsErrati
			+ "\n\tRecord inseriti per " + filename +" " + recordInseriti
			+ "\n\tRecord aggiornati per " + filename +" " + recordAggiornati;
	 

	System.out.println(msg);
	log(msg);
	
} // End update


private void leggiFileDiConfigurazioneCFG(String configFile)
{
	BufferedReader inCfg, inTxt;
	String s;
	int state = 0;
	ConfigQuery configQuery=null;
	
	try {
		inCfg = new BufferedReader(new FileReader(configFile));

		// Leggiamo le configurazioni delle queries
		// ----------------------------------
		while (true) {
			try {
				s = inCfg.readLine();
				if (s == null)
					break;
				else if (Misc.emptyString(s))
					continue;
				else if ((s.length() == 0) || (s.charAt(0) == '#')
						|| (Misc.emptyString(s) == true))
					continue;
				else {
					// E' una dichiarazione di tabella
					if (s.charAt(0) == '[') {
						// Abbiamo un'altra config di tabella da salvare?
						if (configTable != null)
						{
							if (configQuery != null)
								{
								configTable.addQuery(configQuery);
								configQuery = null;
								}
							
							hashConfigTable.put(configTable.procedureName, configTable);
							configTable.dump();
						}
						configTable = new ConfigTable();
						configTable.procedureName = new String(s.substring(1,s.length() - 1));
						
						state = IN_PROCEDURE_DECLARATION;
						
						
						
					} else if (state == IN_PROCEDURE_DECLARATION || 
								s.startsWith("update") || 
								s.startsWith("UPDATE")
								) 
						{
						if (configQuery != null)
							{
							configTable.addQuery(configQuery);
							configQuery = null;
							}
						
						configQuery=new ConfigQuery();
						configQuery.sql = new String(s);
						
						state = IN_SQL_DECLARATION;
					} else if (state == IN_SQL_DECLARATION) {
						configQuery.typeList = new String(s);
						state = IN_TYPE_DECLARATION;
					} else if (state == IN_TYPE_DECLARATION) {
						configQuery.nextVal = new String(s);
						state = IN_NEXT_VAL_DECLARATION;
					} else {
						// Ignore anything else
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} // end while
		// Salviamo l'ultima dichiarazione
		if (configTable != null)
		{
			if (configQuery != null)
				configTable.addQuery(configQuery);
			
			hashConfigTable.put(configTable.procedureName, configTable);
//			configTable.dump();
		}

		inCfg.close(); inCfg=null;
	} // Fine lettura file di configurazione delle queries
	
	catch (IOException e) {
		e.printStackTrace();
	}
} // End leggiFileDiConfigurazioneCFG





private void updateWithData2(ConfigQuery configQuery, BufferedReader fileIn, String filename)
{
	sb = new StringBuffer();

	// Scomponiamo la riga dei tipi in un array
	String tipi = configQuery.getTypeList();
	arTipi = MiscString.estraiCampi(tipi, charSepArrayComma, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
	String sql = "";

	// Leggiamo le configurazioni di base
	// ----------------------------------
	while (true) {
		String s;
		try {
			dataIn = fileIn.readLine();
			if (dataIn == null)
				break;
			else if (Misc.emptyString(dataIn)  || dataIn.charAt(0) == '#')
				continue;
			rowCtr++;
			if (rowCtr < startLoadingFrom)
				continue;
			if ((rowCtr % progress) == 0)
			{
				msg = 	  "\n\n\tRecord letti per " + filename +" " + (rowCtr-startLoadingFrom+1)  
						+ "\n\tUpdate eseguite per " + filename +" " + updateEseguite;
				System.out.print(msg);
			}
			data = dataIn.replace("'", "''"); // gestione di eventuali singoli apici
			arData = MiscString.estraiCampi(data, // // Scomponiamo la riga dei dati in un array
					stringSepDiCampoInArray,
					MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
			if (arData.length != arTipi.length) {
				String l = "\n\tAlla riga " + rowCtr
						+ " il numero dei TIPI " + arTipi.length
						+ " e' diverso dal numero dei CAMPI "
						+ arData.length + " per tabella " + tableName;
				recordsErrati++;
					log(l);
				System.out.println(l);
				continue;
			}

			// Componiamo la parte dei dati (values) in caso di insert
			sb.setLength(0);
			for (int i = 0; i < arTipi.length; i++) {
				if (i != 0)
					sb.append(',');
				if ((arData[i].compareTo("NULL") == 0)
						|| (arData[i].compareTo("null") == 0))
					sb.append(arData[i]);
				else if (arTipi[i].charAt(0) == 's'
						|| arTipi[i].charAt(0) == 'S'
						|| arTipi[i].charAt(0) == 't'
						|| arTipi[i].charAt(0) == 'T')
					sb.append("'" + arData[i] + "'"); // circonda con
														// singoli apici
				else if (arTipi[i].charAt(0) == 'c'
						|| arTipi[i].charAt(0) == 'C') // contatore
					sb.append(configQuery.getNextVal());

				else if (arTipi[i].charAt(0) == 'd'
						|| arTipi[i].charAt(0) == 'D')
					sb.append("CURRENT_TIMESTAMP");

				else
					sb.append(arData[i]);
			}

			// UPDATE OR INSERT RECORD
			try {

				// Commit every tot records
//				if ((rowCtr & 0x3FF) == 0) // Mettiamo ogni 1024 records
				// if ((rowCtr & 0xF) == 0) // Mettiamo ogni 15
					
//				if ((rowCtr & commitOgniTotRighe) == 0)	
//				if ((rowCtr & commitOgniTotRighe) == commitOgniTotRighe)	
				if ((rowCtr % commitOgniTotRighe) == 0) 
				{
					//System.out.print("\nCommitting at row " + rowCtr);
					log("\nCommitting at row " + rowCtr);
					stmt.execute("COMMIT"); // ;
					sqlExecutedOk.clear();
					rollbackRecordsFrom = rowCtr;
				}

				// Esiste un record da aggiornare in tabella?
				String sqlUtf8;
				
				updateInsertDefault(configQuery, stmt, UPDATE_WITH_DATA);

				sqlExecutedOk.add(sql);

				commitRowCtr++;
			} catch (SQLException e) {
				manageSQLException(e);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		if ( elaboraNRighe !=0 && (rowCtr-startLoadingFrom+1) >= elaboraNRighe)
			break;
		
		
		
	} // End while


	log("\nCommitting at row " + rowCtr);
	try {
		stmt.execute("COMMIT"); // ;
	} catch (SQLException e) {
		e.printStackTrace();
	}

	
	
	
}



private void updateWithData(String filename)
{
	ArrayList<ConfigQuery> configQueries =  cfgTable.getConfigQueries(); 
	
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
			updateWithData2(configQuery, bufferedReader, filename);
		}	
	
		
	} catch (IOException e) {
		e.printStackTrace();
	}

	// Close file
	if (bufferedReader != null)
		try {
			bufferedReader.close();
			fIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	try {
		msg = "\nFine aggiornamento " + filename + " " +DateUtil.getDate()	+ " " + DateUtil.getTime(); 
		System.out.println(msg);
		log(msg);

		OutLog.close(); // Chiudi log dopo aver caricato un file
//		OutLog = new BufferedWriter(new FileWriter(logFileOut, true)); // Riapri
		OutLog = new FileWriter(logFileOut, true); // Riapri
																		// in
																		// APPEND
	} catch (IOException e) {
		e.printStackTrace();
	}

} // End updateWithData


private void updateWithoutData()
{


	ArrayList<ConfigQuery> configQueries =  cfgTable.getConfigQueries(); 
	
	for (ConfigQuery configQuery:configQueries)
	{
		// "reset" to beginning of file (discard old buffered reader)
		try {
			updateInsertDefault(configQuery,stmt, UPDATE_WITHOUT_DATA);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

} // End updateWithData()








}
