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

import org.xml.sax.SAXException;


class ConfigTableOld {
	String tableName;
	String sql;
	String typeList;
	String nextVal;
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTypeList() {
		return typeList;
	}
	public void setTypeList(String typeList) {
		this.typeList = typeList;
	}
	public String getNextVal() {
		return nextVal;
	}
	public void setNextVal(String nextVal) {
		this.nextVal = nextVal;
	}
	}




public class DbDelete {
	public final static int IN_TABLE_DECLARATION = 1;
	public final static int IN_SQL_DECLARATION = 2;
	public final static int IN_TYPE_DECLARATION = 3;
	public final static int IN_NEXT_VAL_DECLARATION = 4;

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
	
	// String dbName = "";
	// String url = "jdbc:odbc:bob"; // "jdbc:myDriver:wombat"
	String jdbcDriver = "";
	String connectionUrl = ""; // "jdbc:postgresql://localhost:5432/sbnwebArge"
	String userName = "";
	String userPassword = "";
	String fieldSeparator = "|";
	String removeAllRecordsBeforeUpload = "";
	long startLoadingFrom = 1;
	String searchPath = ""; // "sbnweb, pg_catalog, public;"
	String schema = "";
	//boolean checkUriCopia = false;
	String cdPoloBiblioteca="";
	String cdPoloOperante="";
	String cdBibliotecaOperante="";
	//int livelloAutoritaSoggettoPolo=0;
	
	// java.util.Vector vecConfigTable; // Buffer Reader Files
	HashMap hashConfigTable;
	char charSepArrayComma[] = { ',' };
	// char charSepArrayPipe[] = { '|'};
	static String stringSepDiCampoInArray[] = { "&$%" }; // &$%
	static char escapeCharacter = '\\'; // il backslash di default
	//
	int tabella;
	final static int TABELLA_UNDEFINED = 0;
	final static int TABELLA_TB_STAT_CRECATT = 1;
	final static int TABELLA_TB_CODICI = 2;
	final static int TABELLA_TR_TIT_SOG = 3;
	final static int TABELLA_TB_SOGGETTO = 4;

	// final static int TABELLA_TB_AUTORE = 2;

	public DbDelete() {
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

			Statement stmt = null;
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

	public void doDelete(String tabl_parameters) {
		String cfgId;
		int pos;
		Statement stmt = null;
		String data = ""; // eg. "'Colombian', 00101, 7.99, 0, 0"
		String dataIn = ""; // eg. "'Colombian', 00101, 7.99, 0, 0"
		int rowCtr = 0;

		int recordInseriti = 0;
		int updateEseguite = 0;
		int record_cancellati = 0;

		int commitRowCtr = 0;
		int rollbackRecordsFrom = 1;

		int recordsErrati = 0;
		String arTipi[] = null;
		String arData[] = null;
		StringBuffer sb = new StringBuffer();
		ArrayList sqlExecutedOk = new ArrayList();


//		pos = filename.indexOf(".");
//		if (pos == -1) {
//			msg = "Manca estensione a nome file: " + filename;			
//			System.out.println();
//			log("\n"+msg+"\n");
//			return;
//		}
		String fileIdDaCancellare="";
//		if (pos > -1) {
//			cfgId = filename.substring(0,pos);
//			fileIdDaCancellare = UploadDir+"/"+filename;
//		}
//		else
//		{
//			cfgId = filename;	//.substring(0, pos);
//		}
		
		String arTable[] = MiscString.estraiCampiDelimitatiENon(tabl_parameters, " ", '"', '"', MiscString.KEEP_DELIMITERS_FALSE, MiscString.KEEP_GROUP_DELIMITERS_TRUE, MiscString.TRIM_FALSE, MiscString.HAS_ESCAPED_CHARACTERS_TRUE, MiscString.KEEP_ESCAPE_TRUE);

		cfgId = arTable[0];	//.substring(0, pos);

		for (int i=1; i < arTable.length; i++)
		{
			if (arTable[i].startsWith("fileIdDaCancellare="))
				fileIdDaCancellare = fileIdDaCancellare = UploadDir+"/"+arTable[i].substring(19);
		} // end for

		log("\n\nInizio cancellazione " + cfgId + " "	+ DateUtil.getDate() + " " + DateUtil.getTime());
		log("\n\n-------------------------");
		
		
		try {
			stmt = con.createStatement();
			// if (removeAllRecordsBeforeUpload.equals("true"))
			// {
			// if (startLoadingFrom > 1)
			// {
			// System.out.println("Non posso troncare tabella se inizio ad inserire dal record N."
			// + startLoadingFrom);
			// }
			// else
			// {
			// boolean ret;
			// if (searchPath.length() > 0)
			// {
			// String setPath = "SET search_path = "+searchPath; //sbnweb,
			// pg_catalog, public;
			// ret = stmt.execute(setPath);
			// }
			// // There is no TRUNCATE command in the SQL standard.
			// // Pulire la tabella a MANO PRIMA DI ESEGUIRE !!!!
			// String st = "TRUNCATE TABLE " + schema + "." + tableName ; //
			// "sbnweb."
			// System.out.println(st);
			// stmt.execute(st); // Esegui questa select per attivare gli indici
			// testuali
			//
			// }
			// }
		} catch (SQLException e) {
			e.printStackTrace();
//			try {
			log(e.getMessage()+"\n");
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
			if (e.getMessage().contains("cannot truncate"))
				return;
		}

		
		
		
		// Prendiamo i tipi dei dati per questa tabella
		ConfigTableOld cfgTableOld = (ConfigTableOld) this.hashConfigTable.get(cfgId);
		if (cfgTableOld == null) {
			String l = "\n\tConfigurazione non trovata per tabella: " + cfgId;
//			try {
			log(l);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			System.out.println(l);
			return;
		}

		// Scomponiamo la riga dei tipi in un array
		String tipi = cfgTableOld.getTypeList();
		if (tipi != null)
			arTipi = MiscString.estraiCampi(tipi, charSepArrayComma,MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);

		// Open file and read through

		BufferedReader fileIn = null;
		try {
			if (!fileIdDaCancellare.isEmpty())
				fileIn = new BufferedReader(new FileReader(fileIdDaCancellare)); // UploadDir + "/" + 
			String sql = "";

			// Leggiamo le configurazioni di base
			// ----------------------------------
			while (true) {
//			while (rowCtr < 100) {
				
				String s;
				try {
					if (fileIn != null)
						dataIn = fileIn.readLine();
					
					if (fileIn != null && dataIn == null)
						break;
					else if (fileIn != null && Misc.emptyString(dataIn))
						continue;

					rowCtr++;
					if (rowCtr < startLoadingFrom)
						continue;

					// Prendiamo la SQL e sostituiamo i ?# con i dati 
					sql = cfgTableOld.sql;							
					
					// System.out.println("\nRiga " + rowCtr + ": " + data);
					if (fileIn != null)
					{
						data = dataIn.replace("'", "''"); // gestione di eventuali
													// singoli apici
						// Scomponiamo la riga dei dati in un array
						arData = MiscString.estraiCampi(data,
								stringSepDiCampoInArray,
								MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
	//System.out.println("DATA: " + data);
						if (arData.length != arTipi.length) {
							String l = "\n\tAlla riga " + rowCtr
									+ " il numero dei TIPI " + arTipi.length
									+ " e' diverso dal numero dei CAMPI "
									+ arData.length + " per tabella " + cfgId;
							recordsErrati++;
//							try {
								log(l);
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
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
								sb.append(cfgTableOld.getNextVal());

							else if (arTipi[i].charAt(0) == 'd'
									|| arTipi[i].charAt(0) == 'D')
								sb.append("CURRENT_TIMESTAMP");

							else
								sb.append(arData[i]);
						}

						for (int i=0; i < arData.length; i++)
						{
							if (arTipi[i].equals("s"))
								sql = sql.replace("?"+i, "'"+arData[i]+"'");
							else
								sql = sql.replace("?"+i, arData[i]);
						}

					}


					// Cancella record
					try {

						// Commit every tot records
//						if ((rowCtr & 0x3FF) == 0) // Mettiamo ogni 1024 records
						// if ((rowCtr & 0xF) == 0) // Mettiamo ogni 15
							
						if ((rowCtr & commitOgniTotRighe) == 0)	
						{
							//System.out.print("\nCommitting at row " + rowCtr);
							log("\nCommitting at row " + rowCtr);
							stmt.execute("COMMIT"); // ;
							sqlExecutedOk.clear();
							rollbackRecordsFrom = rowCtr;
						}

						// Esiste un record da aggiornare in tabella?
						String sqlUtf8;

							sqlUtf8 = new String(sql.getBytes(), "UTF-8");
//System.out.println("Da eseguire: " + sqlUtf8);
							stmt.execute(sqlUtf8);

							int rowsAffected = stmt.getUpdateCount();
							if (rowsAffected != 0)
							{
								updateEseguite++;
								record_cancellati += rowsAffected; 
							}
							else
							{
//								System.out.println("Record non aggiornato per '"+arData[0]+"'");
								if (fileIn != null)
									log("Record non cancellato per '"+dataIn+"'\n");
								else
									log("Record non cancellato per '"+sql+"'\n");
								
								recordsErrati++;
							}
						sqlExecutedOk.add(sql);

						commitRowCtr++;
						
						if (fileIn == null)
							break;
						
					} catch (SQLException e) {
//						OutLog.write("\tRecord errato alla riga " + rowCtr + ": " + e.getMessage() + " ---> " + sql + "\n");
						log("Record errato alla riga " + rowCtr + ": " + e.getMessage()); //  + "\n"
						log("Record data: '" + dataIn +"'\n");

						System.out.println("\nRecord errato alla riga " + rowCtr + ": " + sql);
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
								log("COMMITting rolled back records;"); 
								stmt.execute("COMMIT"); // ; Commit what had been rolled back before tje exception
								sqlExecutedOk.clear(); // Get ready for next round
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

					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
//			try {
				log("\nCommitting at row " + rowCtr);
				try {
					stmt.execute("COMMIT"); // ;
				} catch (SQLException e) {
					e.printStackTrace();
				}


				msg = 	  "\n\n\tRecord letti per " + cfgId +" " + rowCtr 
						+ "\n\tRecord errati per " + cfgId +" " + recordsErrati
						+ "\n\tRecord inseriti per " + cfgId +" " + recordInseriti
						+ "\n\tUpdate eseguite per " + cfgId +" " + updateEseguite
						+ "\n\tRecord cancellati per " + cfgId +" " + record_cancellati;
				 

				System.out.println(msg);
				log(msg);

//			} catch (IOException e) {
//				e.printStackTrace();
//			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// Close file
		if (fileIn != null)
			try {
				fileIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		try {
			msg = "\nFine aggiornamento " + cfgId + " " +DateUtil.getDate()	+ " " + DateUtil.getTime(); 
			System.out.println(msg);
			log(msg);

			OutLog.close(); // Chiudi log dopo aver caricato un file
//			OutLog = new BufferedWriter(new FileWriter(logFileOut, true)); // Riapri
			OutLog = new FileWriter(logFileOut, true); // Riapri
																			// in
																			// APPEND
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // update

	public static void main(String args[]) {
		char charSepArrayEquals[] = { '=' };
		char charSepArraySpace[] = { ' ' };

		if (args.length < 2) {
			System.out
					.println("Uso: DbDelete [DbDelete].txt [DbDelete].cfg"); //
			System.exit(1);
		}
		String configFile = args[0];
		String inputFile = args[1];
		// logFileOut = args[2];

		String startMsg = "DbDelete tool - � Almaviva S.p.A 2011-2017"
				+    "\n=========================================="
				+ "\nTool di cancellazione della base dati"
				+ "\nInizio esecuzione " + DateUtil.getDate() + " "
				+ DateUtil.getTime() + "\nFile di configurazione: "
				+ configFile + "\nElenco tabelle da trattare: " + inputFile;

		System.out.println(startMsg);

		// System.out.println("inp: " + inputFile);

		DbDelete dbDelete = new DbDelete();

		BufferedReader in;
		String s;
		String ar[];
		ConfigTableOld configTable = null;

		int state = 0;

		try {
			in = new BufferedReader(new FileReader(configFile));

			// Leggiamo le configurazioni di base
			// ----------------------------------
			while (true) {
				try {
					s = in.readLine();
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
								dbDelete.hashConfigTable.put(configTable.tableName, configTable);
							configTable = new ConfigTableOld();
							configTable.tableName = new String(s.substring(1,s.length() - 1));
							state = IN_TABLE_DECLARATION;
						} else if (state == IN_TABLE_DECLARATION) {
							configTable.sql = new String(s);
							state = IN_SQL_DECLARATION;
						} else if (state == IN_SQL_DECLARATION) {
							configTable.typeList = new String(s);
							state = IN_TYPE_DECLARATION;
						} else if (state == IN_TYPE_DECLARATION) {
							configTable.nextVal = new String(s);
							state = IN_NEXT_VAL_DECLARATION;
						} else {
							// Ignore anything else
						}

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// Salviamo l'ultima dichiarazione
			if (configTable != null)
				dbDelete.hashConfigTable.put(configTable.tableName, configTable);

			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			// in = new BufferedReader(new FileReader(inputFile));
			in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF8"));

			// Leggiamo le configurazioni di base
			// ----------------------------------
			while (true) {
				try {
					s = in.readLine();
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
							dbDelete.UploadDir = ar[1];
						else if (ar[0].startsWith("jdbcDriver"))
							dbDelete.jdbcDriver = ar[1];
						else if (ar[0].startsWith("connectionUrl"))
							dbDelete.connectionUrl = ar[1];
						else if (ar[0].startsWith("userName"))
							dbDelete.userName = ar[1];
						else if (ar[0].startsWith("userPassword"))
							dbDelete.userPassword = ar[1];
						else if (ar[0].startsWith("fieldSeparator")) {dbDelete.fieldSeparator = ar[1];
							stringSepDiCampoInArray[0] = ar[1];} 
						else if (ar[0].startsWith("removeAllRecordsBeforeUpload"))
							dbDelete.removeAllRecordsBeforeUpload = ar[1];
						else if (ar[0].startsWith("logFileOut"))
							dbDelete.logFileOut = ar[1];
						else if (ar[0].startsWith("startLoadingFrom"))
							dbDelete.startLoadingFrom = Long.parseLong(ar[1]);
						else if (ar[0].startsWith("searchPath"))
							dbDelete.searchPath = ar[1];
						else if (ar[0].startsWith("schema"))
							dbDelete.schema = ar[1];
						else if (ar[0].startsWith("cdPoloBiblioteca"))
						{
							dbDelete.cdPoloBiblioteca = ar[1];
							dbDelete.cdPoloOperante=ar[1].substring(0, 3);
							dbDelete.cdBibliotecaOperante=ar[1].substring(4, 6);;

						}
//						else if (ar[0].startsWith("livelloAutoritaSoggettoPolo"))
//							dbDelete.livelloAutoritaSoggettoPolo = Integer.parseInt(ar[1]);
						else if (ar[0].startsWith("setCurCfg")) {
							if (ar[1].equals("true"))
								dbDelete.setCurCfg = true;
							else
								dbDelete.setCurCfg = false;
						}
						
						else if (ar[0].startsWith("commitOgniTotRighe"))
							dbDelete.commitOgniTotRighe = Integer.parseInt(ar[1], 16);
						
						
						else if (ar[0].startsWith("endConfig"))
							break;
						else
							System.out.println("ERRORE: parametro sconosciuto: "+ ar[1]);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			// Apriamo il file di log
			try {

				// logFileOut = DbDelete.UploadDir + "/DbDelete.log";

				// DbDelete.streamOutLog = new FileOutputStream(logFileOut);
				System.out.println("File di log: " + dbDelete.logFileOut);

//				DbDelete.OutLog = new BufferedWriter(new FileWriter(DbDelete.logFileOut));
				dbDelete.OutLog = new FileWriter(dbDelete.logFileOut);
				dbDelete.log(startMsg);

			} catch (Exception fnfEx) {
				fnfEx.printStackTrace();
				return;
			}

			// Apriamo il DB
			if (!dbDelete.openConnection()) {
				try {
					dbDelete.log("Failed to open DB of URL: " + dbDelete.connectionUrl);
					dbDelete.OutLog.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}

			// Esegui trattamento delle singole tabelle
			// ----------------------------------------
			dbDelete.fileCounter = 0;
			while (true) {
				try {
					s = in.readLine();
					if (s == null)
						break;
					else {
						if ((s.length() == 0) || (s.charAt(0) == '#')
								|| (Misc.emptyString(s) == true))
							continue;

						// System.out.println("\n\nCaricando: " + s);
						// DbDelete.OutLog.write("\n\nCaricando: " + s);

//						ar = MiscString.estraiCampi(s, charSepArraySpace, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE); // " "

						dbDelete.fileCounter++;
//						dbDelete.doDelete(ar[0]); // Inserisci tabella
						dbDelete.doDelete(s); // Inserisci tabella

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				System.out.println("\n\nFine cancellazione!");
				dbDelete.log("\n\nFine cancellazione!");
				dbDelete.log("\nFine esecuzione " + DateUtil.getDate()
						+ " " + DateUtil.getTime());
				System.out.println("Vedi " + dbDelete.logFileOut
						+ " per i dettagli dell'elaborazione");
				// Close log file
				if (dbDelete.OutLog != null)
					dbDelete.OutLog.close();
				// Close filelist input file
				in.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

			// Chiudiamo il DB
			dbDelete.closeConnection();

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


}
