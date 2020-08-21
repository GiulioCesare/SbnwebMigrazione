import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import org.xml.sax.SAXException;

import java.util.StringTokenizer; //Aggiunto da Bruno
import java.sql.PreparedStatement;//Aggiunto da Bruno
import java.sql.ResultSet;        //Aggiunto da Bruno   
import java.io.PrintStream;       //Aggiunto da Bruno
import it.finsiel.misc.*;

/*
class ConfigTable {
	String tableName;
	String sql;
	String typeList;
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
	}
*/
public class Trasf_Data {
	java.util.Vector vecInputFiles; // Buffer Reader Files
	String UploadDir = "";
	int fileCounter = 0;
	//FileOutputStream streamOutLog;
	BufferedWriter OutLog;
	Connection con = null;
	int commitEveryNRows = 10;
	//String dbName = "";
//	String url = "jdbc:odbc:bob"; // "jdbc:myDriver:wombat"
	String jdbcDriver="";
	String connectionUrl = ""; // "jdbc:postgresql://localhost:5432/sbnwebArge"
	String userName="";
	String userPassword="";
	String	fieldSeparator="|";
	String removeAllRecordsBeforeUpload="";
//	java.util.Vector vecConfigTable; // Buffer Reader Files
	HashMap hashConfigTable;
	
	
	
	public Trasf_Data()
	    {
		hashConfigTable = new java.util.HashMap();
	    }

	
	public boolean openConnection() // String Filedati
	{
		
		try {
			//Class.forName("org.postgresql.Driver");
			 Class.forName ( jdbcDriver);
			
		} catch(java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}

		try {


				con = DriverManager.getConnection(connectionUrl,userName, userPassword);
//				con.setAutoCommit(false);

	

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
	
	

public static void main(String args[])
{
    if(args.length < 1)
    {
        System.out.println("Uso: Trasf_Data <config filename> <List filename>");
        System.exit(1);
    }
    String configFile = args[0];
    String inputFile = args[1];
	char charSepArrayEquals[] = { '='};
    
    String start="Trascodifica dati tool - © Almaviva S.p.A 2008"+
	 "\n====================================="+
	 "\nTool di conversione dati Cites da Oracle a SQL Client/Server"+
	 "\nEseguito il " + DateUtil.getDate() + " " + DateUtil.getTime()+ 
	 "\nFile da trattare: " + configFile +      //T_Dati_AG.txt
	 "\nFile di configurazione: " + inputFile + "\n"; // Trasf_cfg.txt

    System.out.println(start);

	
//	System.out.println("inp: " + inputFile);

	Trasf_Data dbUpload = new Trasf_Data();
	BufferedReader in;
	String s;
	String ar[];
	String logFileOut = "";
	
	int state = 0;
	


	try {
		in = new BufferedReader(new FileReader(inputFile));

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
					ar = MiscString.estraiCampi(s, "=");
//ar = MiscString.estraiCampi(s, charSepArrayEquals, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
					
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
					else if (ar[0].startsWith("fieldSeparator"))
						dbUpload.fieldSeparator = ar[1];
					else if (ar[0].startsWith("removeAllRecordsBeforeUpload"))
						dbUpload.removeAllRecordsBeforeUpload = ar[1];
					
					else if (ar[0].startsWith("endConfig"))
						break;
					else
						System.out.println("ERRORE: parametro sconosciuto"	+ ar[1]);
						
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Apriamo il file di log
		try {

			logFileOut = dbUpload.UploadDir + "/Trasf_Data.log";
//			dbUpload.streamOutLog = new FileOutputStream(logFileOut); 
			System.out.println("File di log: "	+ logFileOut);

			dbUpload.OutLog = new BufferedWriter(new FileWriter(logFileOut));				
			dbUpload.OutLog.write(start);
			
		} catch (Exception fnfEx) {
			fnfEx.printStackTrace();
			return;
		}
		
		// Apriamo il DB
		if (!dbUpload.openConnection()) // configFile
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
		
			dbUpload.elabora (configFile);
		
			// Chiudiamo il DB
			dbUpload.closeConnection();
		
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();

		// Convert from binary format to readable format

	}

//    System.out.println("Righe elaborate: " + Integer.toString(rowCounter));
    System.exit(0);
} // End main


void elabora(String Filedati) //, Connection con
{
    BufferedReader in;
    String s; 
    ResultSet rs = null;
	PreparedStatement prepStatement = null;

	try {
        FileOutputStream file = new FileOutputStream("T_Dati_AG.txt",true);
        PrintStream Output = new PrintStream(file);

in = new BufferedReader(new FileReader(Filedati));
int contarec=0;
String Rec_MinAmb="";
String Genere="";
String Merce="";
String Misura="";
String PaeseOrig="";
String PaeseProv="";
String Fonte="";
String Finalita="";

// Leggiamo le configurazioni di base
// ----------------------------------
while (true) {
	try {
		s = in.readLine();
						
		if (s == null)
			break;
		else 
		 {
         //StringTokenizer stringTokenizer =new StringTokenizer(stringaDaInterpretare,separatore);
		      StringTokenizer st = new StringTokenizer(s,",");
         String[] stringheRestituite = new String[st.countTokens()];
             for (int i=0 ; i < stringheRestituite.length ; i++ )
               {
                stringheRestituite[i] = st.nextToken();

                 }
                contarec++;
            StringBuffer select = new StringBuffer();

            //Decodifica Paese SPECIE
           select.append ("SELECT GENERE ");
           select.append (" FROM RC00UR ");               
           select.append (" WHERE COD_GENERE= ? and ");
           select.append (" COD_SPECIE= ? and TAX_SPECIE= ? ");


				prepStatement = con.prepareStatement(select.toString());

           prepStatement.setString(1, stringheRestituite[3]);
           prepStatement.setString(2, stringheRestituite[4]);
           prepStatement.setString(3, stringheRestituite[5]);

		    rs = prepStatement.executeQuery();
           //System.out.println(" GENERE = " + rs.getString("GENERE"));
        	if (rs.next())
        	   	{
                 System.out.println(" GENERE = " + rs.getString("GENERE"));
                 Genere=rs.getString("GENERE");

               } //fine if
          
           else {
              System.out.println("RECORD  "+ contarec + "  GENERE NON TROVATO");
OutLog.write("RECORD  "+ contarec + "  GENERE NON TROVATO");
									}

            //Decodifica MERCE
	            select = new StringBuffer();
           select.append ("SELECT DESC_ITAL ");
           select.append (" FROM RC00U9 ");               
           select.append (" WHERE COD_DESC= ? and FLAG_REG='N' ");

           prepStatement = con.prepareStatement(select.toString());
           prepStatement.setString(1, stringheRestituite[6]);

		    rs = prepStatement.executeQuery();
        	if (rs.next())
        	   	{
                 System.out.println(" DESC_ITAL = " + rs.getString("DESC_ITAL"));
                 Merce=rs.getString("DESC_ITAL");

               } //fine if
          
           else {
              System.out.println("RECORD  "+ contarec + "  MERCE NON TROVATA");
             }
             
            //Decodifica Unta di Misura
           select = new StringBuffer();
           select.append ("SELECT UN_MIS ");
           select.append (" FROM RC00U8 ");               
           select.append (" WHERE COD_UN_MIS= ? ");

           prepStatement = con.prepareStatement(select.toString());
           prepStatement.setString(1, stringheRestituite[7]);

		    rs = prepStatement.executeQuery();
        	if (rs.next())
        	   	{
                 System.out.println(" UNITA' DI MISURA = " + rs.getString("UN_MIS"));
                 Misura=rs.getString("UN_MIS");

               } //fine if
          
           else {
              System.out.println("RECORD  "+ contarec + "  MISURA NON TROVATA");
             }   
             
           //Decodifica Paese d'Origine
           select = new StringBuffer();
           select.append ("SELECT NOME_PAESE ");
           select.append (" FROM RC00U4 ");               
           select.append (" WHERE COD_ISO= ? ");

           prepStatement = con.prepareStatement(select.toString());
           prepStatement.setString(1, stringheRestituite[8]);

		    rs = prepStatement.executeQuery();
        	if (rs.next())
        	   	{
                 System.out.println(" PAESE D'ORIGINE = " + rs.getString("NOME_PAESE"));
                 PaeseOrig=rs.getString("NOME_PAESE");

               } //fine if
          
           else {
              System.out.println("RECORD  "+ contarec + "  PAESE ORIGINE NON TROVATO");
             }  
             
              
          //Decodifica Paese di Provenienza
           select = new StringBuffer();
           select.append ("SELECT NOME_PAESE ");
           select.append (" FROM RC00U4 ");               
           select.append (" WHERE COD_ISO= ? ");

           prepStatement = con.prepareStatement(select.toString());
           prepStatement.setString(1, stringheRestituite[9]);

		    rs = prepStatement.executeQuery();
        	if (rs.next())
        	   	{
                 System.out.println(" PAESE D'ORIGINE = " + rs.getString("NOME_PAESE"));
                 PaeseProv=rs.getString("NOME_PAESE");

               } //fine if
          
           else {
              System.out.println("RECORD  "+ contarec + "  PAESE PROVENIENZA NON TROVATO");
             }   
           
           
           //Decodifica FONTE
           select = new StringBuffer();
           select.append ("SELECT FONTE_ITAL ");
           select.append (" FROM RC00UI ");               
           select.append (" WHERE COD_FONTE= ? ");

           prepStatement = con.prepareStatement(select.toString());
           prepStatement.setString(1, stringheRestituite[10]);

		    rs = prepStatement.executeQuery();
        	if (rs.next())
        	   	{
                 System.out.println(" FONTE = " + rs.getString("FONTE_ITAL"));
                 Fonte=rs.getString("FONTE_ITAL");

               } //fine if
          
           else {
              System.out.println("RECORD  "+ contarec + " FONTE NON TROVATA");
             }                      
           
            //Decodifica FINALITA'
           select = new StringBuffer();
           select.append ("SELECT FIN_ITAL  ");
           select.append (" FROM RC00UL ");               
           select.append (" WHERE COD_FIN= ? ");

           prepStatement = con.prepareStatement(select.toString());
           prepStatement.setString(1, stringheRestituite[11]);

		    rs = prepStatement.executeQuery();
        	if (rs.next())
        	   	{
                 System.out.println(" FINALITA' = " + rs.getString("FIN_ITAL"));
                 Fonte=rs.getString("FIN_ITAL");

               } //fine if
          
           else {
              System.out.println("RECORD  "+ contarec + " FINALITA' NON TROVATA");
             }                                
             
            } //fine else
            
            

         Output.println(s + ','+ Genere +',' + Merce +',' + Misura+',' + Fonte+',' + Finalita);

        }//fine try

	 catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	 }//fine catch
	catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	 

	} //fine While

		in.close();
} //fine try;

catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} 
	
}// End elabora

}
