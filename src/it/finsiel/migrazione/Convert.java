package it.finsiel.migrazione;

import it.finsiel.misc.Misc;
import it.finsiel.misc.MiscString;
import it.finsiel.misc.MiscStringTokenizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.NoSuchElementException;

import oracle.xml.parser.v2.DOMParser;
import oracle.xml.parser.v2.XMLDocument;
import oracle.xml.parser.v2.XMLElement;
import oracle.xml.parser.v2.XMLNodeList;

import org.xml.sax.SAXException;

/*
	- Un  record logico in Informix può essere composto da + records fisici fino ad un Massimo di 5 identificati dalla sequenza (dichiarata nekl secondo campo).
	- Le sequenze di escape vanno convertite in UTF8 dopo aver concatenate + records fisici.
	- La sequenza di escape in lettura da Informix è: \|
	- Un record può essere composto da più records logici provenienti da + file legati da un identificativo univoco (eg. BID per tpiiis e tpiiob in posizione 1(primo campo))
	- La sequenza di escape del separatore in scrittura è: \| (ridefinibile da utente)
	- Il separatore per oracle e': ### ridefinibile da utente


	ok - Togliere BID del secondo file
	ok - Togliere # record fisico al record logico
	ok - Togliere | dell'ultimo campo
	
	ok - Rimappare i campi in uscita
	ok - Concatenazione campi in uscita
	ok - Generazione di contenuti per i campi (obbligatori e non), eg. ISBD
	ok - Gestione delle date (anche dove mancanti)
	ok - Gestione dei campi vuoti indicati da '$'
	
	- Gestiamo disallineamenti per records di file multipli ma non sul primo file!!
	- Padding per campi ordinati (eg. tbc_collocazione)
	- Drop di campi in ingresso
	-- POPOLAMENTO DB PostgreSql
	copy sbnweb.tb_titolo FROM 'C:/eclipse3.1/SbnwebMigrazione/data/tb_titolo_test.upl' DELIMITER AS '|'

Esempio di ARGS per BULL:	CFX data/filelist.new.cfi.txt \ &$% &$%
Esempio di ARGS per INFORMIX:	MIL data/filelist.new.mil.txt \ | &$%

*/


public class Convert
{
	String BidLeft=null, BidRight = null;

	ConvertDb convertDb;
    ConvertBull convertBull;
    ConvertInformix convertInformix;
    
	public int sequence = 1; // for serial fields (only the first field)
//	static String SEQUENCE_DIR_BULL = "C:/SbnWeb/migrazione/CFI/db_out_postgres/"; 
//	static String SEQUENCE_DIR_INFORMIX = "C:/SbnWeb/migrazione/MIL/db_out_postgres/"; 
    String sequenceDir = "";

	Method metodoConvert = null; 
   	Class paramTypes[] = new Class[1];
    Object arglist[] = new Object[1];

	Hashtable charTable;
    Hashtable fieldValue2IdMapping;
    
    String applicationPath;
    static int recordCounter = 0;
    static int recordErrariCounter = 0;
    boolean stripN = false;
    boolean stripPipe = false;
    boolean multiline = false;
    boolean tpiiis = false;
    boolean fsiiis = false;
    boolean fsiiau = false;
    boolean fsiinn = false;
    boolean fscdoc = false;
    boolean fscinv = false;
    boolean fscloc = false;
    
    
    boolean tlrbit = false;
    char escapeCharacter = '\\'; // il backslash di default
//    static String separatoreDiCampoIn = "|"; // Separatore di campo in input fi default 
	static char charSepDiCampoInArray[] = { '?'}; // |
	String stringSepDiCampoInArray[] = { "???"}; // &$%
	static int separatorType = ConvertConstants.CHARACTER_SEPARATOR;
	int separatorLength = 0;
	static int db_type = 0;
    String separatoreDiCampoOut = "###"; // Separatore di campo in output di default
    String escapeSeparatorSequence = ""+ escapeCharacter + charSepDiCampoInArray[0]; //separatoreDiCampoIn;    

    String sortCommand="";
    char fileSystemSeparator=' ';
    
    java.util.Vector vecBfrFiles; // Buffer Reader Files 
	java.util.Vector vecBfrFilesNames; // Nome files
	java.util.Vector vecBfrFilesMultiline; // Flag if file is multiline
	String  vecBfrFilesReadAhead[] = null; // Read ahead buffer 
	
	
    FileOutputStream streamOut; // declare a file output object
    String fieldsMapFilename; //  = "data/fieldsMap.txt"
    String caratteriSpecialiFilename;
    String codPolo = "";
    private HashMap tableFieldsHashMap = new HashMap();
    private HashMap tableMethodsHashMap = new HashMap();
    private int Serial = 1;
	
    
    
    public class FieldsMap
    {
    	int [][]mappaCampi; // a,da 
        int campiInUscita = 0;
        int campiInEntrata = 0;
        FieldsMap(int elements)
        {
        	mappaCampi = new int[elements][2]; // [ampo in entrata, campo in uscita]
        }
    };
    
    
    private boolean loadDescriptionToIdMap(String filename)
    {
//        DOMParser parser = new DOMParser();
    	String fileName = applicationPath + File.separator + "relations"+ File.separator + filename;
    	
    	fieldValue2IdMapping.clear();
    	char nonTokenDelimiters = ';';
    	
    		BufferedReader in;
    		try {
    			in = new BufferedReader(new FileReader(fileName));
    			String s;
    			while(true) {
    			try {
    				s = in.readLine();
    				if(s==null)
    					break;
    				else
    				{
    				if (s.length() == 0 || s.charAt(0) == '#' || MiscString.isEmpty(s) || s.charAt(0) > 127)
    					continue;
//    				System.out.println("\n\nReading: "+s);
    				String ar[] = MiscString.estraiCampiAncheDelimitati(s, nonTokenDelimiters, '"', '"');

    				//for (int i=0; i < ar.length; i++)
    				fieldValue2IdMapping.put(ar[0],ar[1]);
    				
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
    		} catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        return false;
    } // End loadDescriptionToIdMap
    
    
    public Convert()
    {
        applicationPath = System.getProperty("user.dir");
        vecBfrFiles = new java.util.Vector();
        vecBfrFilesNames = new java.util.Vector();
        vecBfrFilesMultiline = new java.util.Vector();
        charTable = new Hashtable();
        fieldValue2IdMapping = new Hashtable();
    }

void setupDbReflectionMapping()
{
	if (db_type == ConvertConstants.DB_BULL)
	{
        convertBull = new ConvertBull(db_type, codPolo, fileSystemSeparator);
        convertInformix = null;
    	convertDb = convertBull;

	}
	else
	{
        convertBull = null;
        convertInformix = new ConvertInformix(codPolo, fileSystemSeparator);
    	convertDb = convertInformix;
	}
	
}
    
    
    
    
    public static void main(String args[])
    {
        if(args.length < 1)
        {
            System.out.println("Parametri mancanti. Uso: Convert <filelist> ");
            System.exit(1);
        }
//        codPolo = args[0];
        String inputFile = args[0];
    	char charSepArrayEquals[] = { '='};
    	String ar[];
    	
    	
/*
        if(args.length >= 2)
        	escapeCharacter = args[2].charAt(0);
        if(args.length >= 3)
        {
        	if (args[3].length()  == 1)
        	{
        		//separatoreDiCampoIn = args[3];
        		charSepDiCampoInArray[0] = args[3].charAt(0);
        		separatorType = ConvertConstants.CHARACTER_SEPARATOR;
        		separatorLength = 1;
        		db_type = ConvertConstants.DB_INFORMIX;
        		sequenceDir = SEQUENCE_DIR_INFORMIX;
        	}
        	else
        	{
        		stringSepDiCampoInArray[0] = args[3];
        		separatorType = ConvertConstants.STRING_SEPARATOR;
        		separatorLength = args[3].length();
        		db_type = ConvertConstants.DB_BULL;
        	    sequenceDir = SEQUENCE_DIR_BULL;

        	}
        }
        if(args.length >= 4)
        	separatoreDiCampoOut = args[4];
*/        	
        
		System.out.println("inp: "+inputFile);
//		System.out.println("out: "+outputFile);

        Convert convert = new Convert();
        
        
 		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(inputFile));
			String s;
			
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
						if (ar[0].startsWith("codPolo"))
							convert.codPolo = ar[1];
						else if (ar[0].startsWith("stringSepDiCampoInArray"))
						{
							convert.stringSepDiCampoInArray[0] = ar[1];
							convert.separatorLength = ar[1].length();
							separatorType = ConvertConstants.STRING_SEPARATOR;
						}
						else if (ar[0].startsWith("charSepDiCampoInArray"))
						{
							convert.charSepDiCampoInArray[0] = ar[1].charAt(0);
							convert.separatorLength = 1;
							separatorType = ConvertConstants.CHARACTER_SEPARATOR;
						}
						
						else if (ar[0].startsWith("separatoreDiCampoOut"))
							convert.separatoreDiCampoOut = ar[1];
						
						else if (ar[0].startsWith("escapeCharacter"))
							convert.escapeCharacter = ar[1].charAt(0);
						else if (ar[0].startsWith("db_type"))
						{
							if (ar[1].equals("BULL"))
							{
								db_type = ConvertConstants.DB_BULL;
//								sequenceDir = SEQUENCE_DIR_BULL;
							}
							else
							{
				                db_type = ConvertConstants.DB_INFORMIX;
//			                	sequenceDir = SEQUENCE_DIR_INFORMIX;
							}
							convert.setupDbReflectionMapping();						
							
						}
						else if (ar[0].startsWith("sequenceDir"))
						{
							convert.sequenceDir = ar[1];
						}
						else if (ar[0].startsWith("fieldsMapFilename"))
							convert.fieldsMapFilename = ar[1];
						else if (ar[0].startsWith("caratteriSpecialiFilename"))
							convert.caratteriSpecialiFilename = ar[1];
			
						else if (ar[0].startsWith("sortCommand"))
							convert.sortCommand = ar[1];

						else if (ar[0].startsWith("fileSystemSeparator"))
							convert.fileSystemSeparator = ar[1].charAt(0);
						
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
			
	        try
	        {
	            convert.loadCodesTable();
	            convert.loadTableMethods();
	        }
	        catch(IOException ioe)
	        {
	            System.out.println("File Xml dei caratteri speciali non trovato");
	            System.exit(1);
	        }
	        catch(SAXException se)
	        {
	            se.printStackTrace();
	            System.exit(1);
	        }

	        convert.caricaCampiDaMappare();

			
			
			
			
			
			while(true) {
			try {
				s = in.readLine();
				if(s==null)
					break;
				else
				{
				if (s.length() == 0 || s.charAt(0) == '#' || MiscString.isEmpty(s) || s.charAt(0) > 127)
					continue;
				System.out.println("\n\nReading: "+s);
//				String nonTokenDelimiters = " ";
		    	char charSepArraySpace[] = { ' '};
				ar = MiscString.estraiCampi(s, charSepArraySpace, MiscStringTokenizer.RETURN_EMPTY_TOKENS_TRUE);

				recordCounter = 0;
				recordErrariCounter = 0;
				convert.transform(ar);
		        String ap = System.getProperty("user.dir");
		        int k=ap.lastIndexOf("\\");
		        if (k==-1)
		        	k=ap.lastIndexOf("/");
		        ap=ap.substring(0, k);
				System.out.println("-------------------------------------------");
				System.out.println("Tabella: " + ar[0]);
				for(int i=2;i<ar.length;i++){
					System.out.println("File input: "+ap+ ar[i]);
				}
				System.out.println("File output: " +ap+ ar[1]);
				System.out.println("Record letti: " + Integer.toString(recordCounter));
				System.out.println("Record errati: " + Integer.toString(recordErrariCounter));
				
				System.out.println("-------------------------------------------");
				
//        convert.transform(inputFile, outputFile);

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
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


//        System.out.println("Righe elaborate: " + Integer.toString(rowCounter));
        System.exit(0);
    } // End main


    private String makeRecord(String []ar, String tableName)
	{
	int i, pos;
	StringBuffer sb = new StringBuffer();
	
	FieldsMap fm = getFieldsMap(tableName);
	
	// Reorder fields
	String []ar1 = new String[fm.campiInUscita];
	int from, to;
	for (i=0; i < fm.mappaCampi.length; i++)
	{
		from = fm.mappaCampi[i][0]-1;
		to = fm.mappaCampi[i][1]-1;
		if (to < 0)
			continue;
		if (ar1[to] == null)
			ar1[to] = ar[from];
		else
			ar1[to] = ar1[to]+ar[from]; //gv il campo in output è presente due volte, la seconda appendo
	}	
	if (tableName.compareTo("tb_autore") == 0)
		ar1[4-1] = MiscString.paddingString(ar1[4-1], 6, ' ', ConvertConstants.RIGHT_PADDING); // tpiiau_cautun_1
//	else if (tableName.compareTo("tb_impronta") == 0)
//	{
//		ar1[2-1] = ar1[2-1].substring(1, 10);
//		ar1[3-1] = ar1[3-1].substring(10, 24);
//		ar1[4-1] = ar1[4-1].substring(24);
//	}


	
	//gv ora creo la stringa in output. 
	//importante che ora il numero dei campi indicati nel foglio excel sia ESATTO altrimenti andiamo in null pointer
 	for (i=0; i < ar1.length; i++)
	{
//	if (i > 0)
//		sb.append(separatoreDiCampoOut);
	sb.append(escapeSeparator(ar1[i]));
	sb.append(separatoreDiCampoOut);
	}
/*	
	pos =  sb.length()-1; // Problema se ultimo campo empty
	if (sb.charAt(pos) == '|')
		return sb.substring(0,sb.length()-1); // Remove last field separator (default |)
	else
*/
		return sb.toString(); 
	} // End makeRecord


    private void loadCodesTable()
        throws IOException, SAXException
    {
        DOMParser parser = new DOMParser();
        //String fileName = applicationPath  +File.separator + "src" +File.separator + "CaratteriSpeciali.xml";
//		String fileName = applicationPath + File.separator + "conf"+ File.separator + "CaratteriSpecialiUnimarc.xml";
		
		System.out.println("File caratteri speciali: " + caratteriSpecialiFilename);
		
        FileReader fr = new FileReader(caratteriSpecialiFilename);
        parser.parse(fr);
        XMLDocument doc = parser.getDocument();
        XMLNodeList nodeList = (XMLNodeList)doc.getElementsByTagName("CARATTERE");
        for(int i = 0; i < nodeList.getLength(); i++)
        {
            XMLElement currentElement = (XMLElement)nodeList.item(i);
//           String esabyteCode = currentElement.getAttribute("CODICE");
  	         String esabyteCode = currentElement.getAttribute("ESABYTE");
//           String utf16Code = currentElement.getText();
            String utf16Code = currentElement.getAttribute("UNICODE");
            charTable.put(esabyteCode, utf16Code);
        }

        fr.close();
        
    }

    private String fromEsabyte2Unicode(String textLine)
    {
        String messaggioTrasformato = "";
        String messaggioTrasformato1 = "";
        String codificaSBN = "";
        String utf16Code = null;
        //String delimiter = String.valueOf('\033');
    	char charSepArray[] = {'\033'};
        
        
//        StringTokenizer stringTokenizer = new StringTokenizer(textLine, delimiter);
        MiscStringTokenizer stringTokenizer = new MiscStringTokenizer(textLine, charSepArray, MiscStringTokenizer.RETURN_DELIMITERS_AS_TOKEN_FALSE, MiscStringTokenizer.RETURN_EMPTY_TOKENS_TRUE);
  
        char Caratteri[] = new char[stringTokenizer.countTokens()];
        String nonConvertiti[] = new String[stringTokenizer.countTokens()];
        try
        {
            messaggioTrasformato1 = stringTokenizer.nextToken();
        }
        catch(NoSuchElementException noSuchElementException)
        {
            String s1 = textLine.replace("\\", "\\\\"); 
            return s1;
        }
        messaggioTrasformato = messaggioTrasformato1;
        String codificaTemp = "";
        int ii = stringTokenizer.countTokens();
        if(ii != 0)
        {
               for(int i = 1; i < ii; i++)
            {
                codificaTemp = stringTokenizer.nextToken();
                Integer prova;
                try
                {
                    codificaSBN = codificaTemp.substring(1, 5);
                    utf16Code = (String)charTable.get(codificaSBN);
                    new Integer(0);
                    prova = Integer.decode("0x" + utf16Code);
                }
                catch(StringIndexOutOfBoundsException e)
                {
                    Caratteri[i] = ' ';
                    int riga = recordCounter+1;
                    nonConvertiti[i] = charSepArray[0] + codificaSBN;// delimiter;
                    System.out.println("*** Codice ESABYTE errato *** Codice ESABYTE: " + codificaSBN + ", ID=" + BidLeft + ", Riga " +  + riga);
                    continue;
                }
                catch(NumberFormatException nfe)
                {
                    Caratteri[i] = ' ';
                    int riga = recordCounter+1;
                    nonConvertiti[i] = charSepArray[0] + codificaTemp; // delimiter;
                    System.out.println("*** Codice UTF-8 corrispondente non trovato *** Codice ESABYTE: " + codificaSBN + ", ID=" + BidLeft + ", Riga " + riga);
                    continue;
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                    String s = textLine;
                    String s2 = s;
                    return s2;
                }
                char conv = (char)prova.intValue();
                Caratteri[i] = conv;
            }

        }
        stringTokenizer = null;
        messaggioTrasformato1 = "";
        messaggioTrasformato = "";
//        stringTokenizer = new StringTokenizer(textLine, delimiter);
        stringTokenizer = new MiscStringTokenizer(textLine, charSepArray, MiscStringTokenizer.RETURN_DELIMITERS_AS_TOKEN_FALSE, MiscStringTokenizer.RETURN_EMPTY_TOKENS_TRUE);
        
        messaggioTrasformato1 = stringTokenizer.nextToken().toString();
//        messaggioTrasformato1 = stringTokenizer.peek().toString();
        messaggioTrasformato = messaggioTrasformato1;
        if(ii != 0)
        {
            String appo = "";
//            for(int i = 0; i < ii; i++)
                for(int i = 1; i < ii; i++)
            {
                appo = stringTokenizer.nextToken().toString();
                if(appo.length() > 5)
                {
                    if(Caratteri[i] != ' ')
                    {
                        messaggioTrasformato = messaggioTrasformato + Caratteri[i] + appo.substring(5);
                        //System.out.println(messaggioTrasformato);
                    } else
                    {
                        messaggioTrasformato = messaggioTrasformato + nonConvertiti[i];
                        //System.out.println(messaggioTrasformato);
                    }
                } else
                if(Caratteri[i] != ' ')
                {
                    messaggioTrasformato = messaggioTrasformato + Caratteri[i];
                    //System.out.println(messaggioTrasformato);
                } else
                {
                    messaggioTrasformato = messaggioTrasformato + nonConvertiti[i];
                    //System.out.println(messaggioTrasformato);
                }
            }
        }
	else
        {
        String s1 = textLine.replace("\\", "\\\\"); 
        return s1; // 
        }
        String s1 = messaggioTrasformato.replace("\\", "\\\\");
        return s1;
    }

    private String unescape(String textLine)
    {
	return "";
    } // End fromEsabyte2Utf8


	private String[] fromEsabyte2Unicode(String []ar)
	{
	java.util.Vector vecCampi = new java.util.Vector();
	int i;

	for (i=0; i < ar.length; i++)
	{
	String s = fromEsabyte2Unicode(ar[i]);
	vecCampi.addElement(s);
	}


	vecCampi.trimToSize();
	String[] arrCampi = new String[vecCampi.size()];
	for (i = 0; i < vecCampi.size(); i++) {
		arrCampi[i] = (String)vecCampi.elementAt(i);
	}	
	return arrCampi;
	} // End fromEsabyte2Utf8

    private String escapeSeparator(String in)
    {
    String out = in.replace(""+charSepDiCampoInArray, escapeSeparatorSequence); //separatoreDiCampoIn
    return out;
    } // End escapeSeparator


//    public void transform(String inputFile, String outputFile)
    public void transform(String fileOutIn[])
    {
    String inputFile,outputFile;
//    outputFile = applicationPath+"\\.."+fileOutIn[1];
    outputFile = fileOutIn[1];
    FileReader frIn;
	BufferedReader brIn;
	
	vecBfrFiles.clear();
	vecBfrFilesNames.clear();
	vecBfrFilesMultiline.clear();
	resetVariables();
	
		// Se file in scrittura gia' esiste cancellalo e ricrealo
        File file = new File(outputFile);
        if(file.exists())
        {
            file.delete();
        }
        try
        {
            file.createNewFile();
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
            return;
        }
		// Apri file in scrittura
        try
        {
            streamOut = new FileOutputStream(outputFile); // "data/stream.out" 
        }
        catch(Exception fnfEx)
        {
            fnfEx.printStackTrace();
            return;
        }


        	
        try
        {
		for (int i = 2; i < fileOutIn.length; i++ )
			{
//			String inFile=applicationPath+"\\.."+fileOutIn[i];
			String inFile=fileOutIn[i];
			brIn = new BufferedReader(new FileReader(inFile));
			vecBfrFiles.addElement(brIn);
			vecBfrFilesNames.addElement(inFile);
             if (fileOutIn[i].contains("tpiiis"))
             {
            	 tpiiis = true;
            	 vecBfrFilesMultiline.add(new Boolean(true));
             }
             else if (fileOutIn[i].contains("fsiiis") || fileOutIn[i].contains("FSIIIS"))
             {
            	 fsiiis = true;
            	 vecBfrFilesMultiline.add(new Boolean(false));
             }
             else if (fileOutIn[i].contains("fsiiau") || fileOutIn[i].contains("FSIIAU"))
             {
            	 fsiiau = true;
            	 vecBfrFilesMultiline.add(new Boolean(false));
             }
             else if (fileOutIn[i].contains("fsiinn") || fileOutIn[i].contains("FSIINN"))
             {
            	 fsiinn = true;
            	 vecBfrFilesMultiline.add(new Boolean(false));
             }
             else if (fileOutIn[i].contains("fscdoc") || fileOutIn[i].contains("FSCDOC"))
             {
            	 fscdoc = true;
            	 vecBfrFilesMultiline.add(new Boolean(false));
             }
             else if (fileOutIn[i].contains("fscinv") || fileOutIn[i].contains("FSCINV"))
             {
            	 fscinv = true;
            	 vecBfrFilesMultiline.add(new Boolean(false));
             }
             else if (fileOutIn[i].contains("fscloc") || fileOutIn[i].contains("FSCLOC"))
             {
            	 fscloc = true;
            	 vecBfrFilesMultiline.add(new Boolean(false));
             }
             
                          
             
             else if (fileOutIn[i].contains("tlrbit"))
             {
            	 tlrbit = true;
            	 vecBfrFilesMultiline.add(new Boolean(false));
             }
             else 
            	 vecBfrFilesMultiline.add(new Boolean(false));
			}
			
        }
        catch(IOException e)
        {
            try {
				streamOut.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            e.printStackTrace();
            return;
        }
//		if (!doTransform(fileOutIn))
		if (!doTransformNew(fileOutIn))
			{
			return;
			}
        try
        {
    		for (int i = 1; i < vecBfrFiles.size(); i++ )
			{
			brIn = (BufferedReader)vecBfrFiles.get(i);
			brIn.close();
			}
        }
        catch(IOException ioEx)
        {
            ioEx.printStackTrace();
        }
        try
        {
            streamOut.close();
        }
        catch(IOException ioEx)
        {
            ioEx.printStackTrace();
        }
    } // End transform


    
    private boolean caricaCampiDaMappare() //String filename
    {
//    	int [][]from = new int [][] 
    	BufferedReader inFile = null;
    	String s;
		int elm;
    	char charSepArraySpace[] = { ' '};
    	char charSepArrayComma[] = { ','};
    	
    	try {
			inFile = new BufferedReader(new FileReader(fieldsMapFilename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			while ((s = inFile.readLine())!=null)
			{
			if (s.length() == 0 || s.charAt(0) == '#' || MiscString.isEmpty(s)) //   
				continue;
			// Scomponiamo riga
			String ar[] = MiscString.estraiCampi(s, charSepArraySpace, MiscStringTokenizer.RETURN_EMPTY_TOKENS_TRUE); // " "
			String tableName = ar[0];
			int tableOutFields = 0;
	    	try {
				tableOutFields = Integer.valueOf(ar[1]).intValue();
			} catch (Exception e) {
				System.out.println("errore nel filedsmap, numero campi non numerico: " + tableName);
				System.exit(1);
			}
			if(ar[tableOutFields] == null){
				//errore nel filedsmap
				System.out.println("errore nel filedsmap, non ci sono abbastanza coppie: " + tableName);
				System.exit(1);
			}
	
			FieldsMap fm = new FieldsMap(ar.length-2); // tableOutFields
			// Carica la mappa
			for (elm = 0; (elm+2)< ar.length; elm++ )
			{
				String ar1[] = MiscString.estraiCampi(ar[elm+2], charSepArrayComma, MiscStringTokenizer.RETURN_EMPTY_TOKENS_TRUE); // ","
/*
  				if (ar1[0].charAt(0) < '0' || ar1[0].charAt(0) > '9')
				{
					elm--;
					break; // found an uninitialized value 
				}
*/
				fm.mappaCampi[elm][0] = Integer.valueOf(ar1[0]).intValue();
				fm.mappaCampi[elm][1] = Integer.valueOf(ar1[1]).intValue();
			}
			fm.campiInUscita = tableOutFields;
			fm.campiInEntrata = elm; 
			tableFieldsHashMap.put(tableName, fm);
			
			} // End while
			
			try {
				inFile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
    } // End caricaCampiDaMappare
    
    
    private FieldsMap getFieldsMap(String tableName)
    {
    	return (FieldsMap)tableFieldsHashMap.get(tableName);
    }
    


    

   

    


    
    private static byte[] trim(byte[] ba, int len) {
       	if (len == ba.length)
       	    return ba;
       	byte[] tba = new byte[len];
       	System.arraycopy(ba, 0, tba, 0, len);
       	return tba;
           }
    
    

       private byte [] myGetBytesUtf8(String s)
       {
    	int len = s.length();    
	    int en = ConvertConstants.MAX_BYTES_PER_UTF8_CHARACTER * len;
	    byte[] ba = new byte[en];
	    if (len == 0)
		return ba;

	    int ctr = 0;

	    for (int i=0; i < len; i++)
	    {
	    	char c = s.charAt(i);
	    	if (c < 0x80)
	    	{
	    		ba[ctr++] = (byte)c;
	    	}
	    	else if (c < 0x800)
	    	{
	    		ba[ctr++] = (byte)(0xC0 | c >> 6);
	    		ba[ctr++] = (byte)(0x80 | c & 0x3F);
	    	}
	    	else if (c < 0x10000)
	    	{
	    		ba[ctr++] = (byte)(0xE0 | c >> 12);
	    		ba[ctr++] = (byte)(0x80 | c >> 6 & 0x3F);
	    		ba[ctr++] = (byte)(0x80 | c & 0x3F);
	    	}
	    	else if (c < 0x200000)
	    	{
	    		ba[ctr++] = (byte)(0xE0 | c >> 18);
	    		ba[ctr++] = (byte)(0x80 | c >> 12 & 0x3F);
	    		ba[ctr++] = (byte)(0x80 | c >> 6 & 0x3F);
	    		ba[ctr++] = (byte)(0x80 | c & 0x3F);
	    	}
	    	else if (c < 0x800)
	    	{
	    		
	    	}
	    } // end for
	    
	    return trim(ba, ctr);
       } // End myGetBytesUtf8 
    
       // Trim the given byte array to the given length
       //
    






 		/**
 	     * Read a record, single or multiline
 	     *
 	     * @return     A String containing the contents of the record  or null if the end of the
 	     *             stream has been reached
 		 */
 	 	    
 	 	    private String readRecord(int i)  
 	 	    {
 	 	    	BufferedReader brIn = 	(BufferedReader)vecBfrFiles.get(i);
 	            boolean ml = ((Boolean)vecBfrFilesMultiline.get(i)).booleanValue();
 	 	    	if (ml == true) // Read multiline record
 	        	{
 	 	 	    	StringBuffer sb = new StringBuffer();  
// 		        	stripN = true;
 		        	
 	 	 	    	String s = vecBfrFilesReadAhead[i];
 		        	if (s == null)
 		        		{
 		        		try {
 							s = brIn.readLine();
 						} catch (IOException e) {
 							// TODO Auto-generated catch block
 							e.printStackTrace();
 							return null;
 						}
 		        		vecBfrFilesReadAhead[i] = s;
 		        		}
 					if (s == null)
 						return s; // EOF	        		
 		        	// We are on the first of a possible multiline record	
 		        	sb.append(s.substring(0,10)+s.substring(12, s.length()-3)); // rimuovi il numero di record (del record fisico) e |N|

 		        	// Read the othe lines
 		        	while (true)
 		        	{
 		        		try {
 							s = brIn.readLine();
 						} catch (IOException e) {
 							// TODO Auto-generated catch block
 							e.printStackTrace();
 							return null;
 						}
 		        		if (s == null || s.charAt(11) == '1') // EOF or start of another record
 		        		{
 		        			vecBfrFilesReadAhead[i] = s;
 		        			break;
 		        		}

 						
 	        		sb.append(s.substring(13,s.length()-3)); // Rimuovi |N| prima della concatenazione
 		        	} // End while 
 		        	
 		        	if (sb.length() == 0 && s == null)
 		        		return null;
 					else 
 						return sb.toString(); // return the record				        	
 	        	}
 	 	    	else // Read single line record
 	 	    	{
 					try {
 						String s = brIn.readLine();
 						if (s == null || s.length() < separatorLength || tlrbit)
 							return s;
//						return s.substring(0,s.length()-1); // Rimuovi | prima della concatenazione
 						
						return s.substring(0,s.length()-separatorLength); // Rimuovi separatore prima della concatenazione
							
 						
 					} catch (IOException e) {
 						// TODO Auto-generated catch block
 						e.printStackTrace();
 						return null;
 					}
 	 	    	}


 	 	    } // End readRecord
 	    
 	    private boolean doTransformNew(String fileOutInAr[])
 	    {
 	    	String arInputRecord[] = new String[vecBfrFiles.size()]; // one input record per file   
	        int files = vecBfrFiles.size(); // MAX 2
	        StringBuffer sb = new StringBuffer();

	        vecBfrFilesReadAhead = new String[files]; 
			Serial = 1; // Reinizializza

	        // Do we have to load any description 2 Id mapping?
	        for (int i=0; i < files; i++)
        	{
             if (((String)vecBfrFilesNames.get(i)).contains("tpeilp"))
             {
            	 loadDescriptionToIdMap("luogo2lid.rel");
            	 convertDb.setFieldValue2IdMapping(fieldValue2IdMapping);
             }
        	}


// Prepariamo le chiamate dinamiche tramite reflection (tipo function call in C)
// Provo ad usare la reflection per chiamare dinamicamente i metodi

Class c;
if (db_type == ConvertConstants.DB_BULL)
	c = convertBull.getClass();	// Prendi la classe
else
	c = convertInformix.getClass();	// Prendi la classe
	
try {
	paramTypes[0] = Class.forName("[Ljava.lang.String;");
    try {
		String nomeMetodo = (String)tableMethodsHashMap.get(fileOutInAr[0]); 
    	metodoConvert = c.getMethod(nomeMetodo,paramTypes);
	} catch (SecurityException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NoSuchMethodException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

} catch (ClassNotFoundException e1) {
	// TODO Auto-generated catch block
	e1.printStackTrace();
}	// Definisci i tipi degli argomenti
		        
	        
	        // Dobbiamo generare e/o caricare degli id automatici?
 	   		if (fileOutInAr[0].compareTo("tbf_anagrafe_utenti_professionali") == 0)
 	   		{
 	   			// Scriviamo l-utente root di default 
 	   			String root = "1&$%Argentino&$%Trombin&$%20081101&$%20081101&$%" +codPolo +"MIG000000&$%" +codPolo +"MIG000000&$%N&$%";
 	   			byte arUtf8[] = myGetBytesUtf8(root);
                try {
					streamOut.write(arUtf8, 0, arUtf8.length);
 	                streamOut.write('\n');
 	                recordCounter++;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				convertDb.apriSequenceFile( sequenceDir +"SequenceTbfAnagrafeUtentiProfessionali.txt", 3+9);
 	   		}
 	   		else if (fileOutInAr[0].compareTo("tbf_biblioteca") == 0)
 	   		{
 	   			// Scriviamo la biblioteca root di default
 	   			String rootBib = "1&$%NULL&$%" +codPolo +"&$% __&$%Gestione sistema&$% &$%Via Casal Boccone, 188&$%&$%40125&$%06 3993 1&$%06 3993 1&$%&$% &$%  &$%a.trombin@finsiel.it&$%B&$%IT&$%BO&$% __&$% __&$%0&$%C&$%na &$% &$% &$%" +codPolo +"MIG000000&$%20081101&$%" +codPolo +"MIG000000&$%20081101&$%N&$%NULL&$%NULL&$%";
 	   			byte arUtf8[] = myGetBytesUtf8(rootBib);
                try {
					streamOut.write(arUtf8, 0, arUtf8.length);
 	                streamOut.write('\n');
 	                recordCounter++;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
 	   			
 	   			convertDb.apriSequenceFile(sequenceDir +"SequenceTbfBiblioteca.txt", 3+3);
 	   		}
 	   		else if (fileOutInAr[0].compareTo("tbl_utenti") == 0)
 	   			convertDb.apriSequenceFile(sequenceDir +"SequenceTblUtenti.txt", 3+10); 
 	   		
 	   		else if (fileOutInAr[0].compareTo("tba_sez_acquis_bibliografiche") == 0)
 	   			convertDb.apriSequenceFile(sequenceDir +"SequenceTbaSezAcquisBibliografiche.txt", 3+7);
 	   		
 	   		
 	   		else if (fileOutInAr[0].compareTo("tbb_capitoli_bilanci") == 0)
 	   			convertDb.apriSequenceFile(sequenceDir +"SequenceTbbCapitoliBilanci.txt", 3+4+4); 
 	   		else if (fileOutInAr[0].compareTo("tba_ordini") == 0)
 	   		{
 	   			convertDb.apriSequenceFile(sequenceDir +"SequenceTbaOrdini.txt", 3+4+9); 
 	   			convertDb.caricaSequenceFiles(sequenceDir +"SequenceTbaSezAcquisBibliografiche.srt", 7+3+7, 
 	   										  sequenceDir +"SequenceTbbCapitoliBilanci.srt", 7+3+8);
 	   		}
 	   		else if (fileOutInAr[0].compareTo("tba_cambi_ufficiali") == 0)
 	   			convertDb.apriSequenceFile(sequenceDir +"SequenceTbaCambiUfficiali.txt", 3+3);
 	   		
 	   		else if (fileOutInAr[0].compareTo("tb_parola") == 0)
 	   			convertDb.apriSequenceFile(sequenceDir +"SequenceTbParola.txt", 3+3); 

 	   		// Dobbiamo caricare degli id automatici (sequenze)?
 	   		else if (fileOutInAr[0].compareTo("trf_utente_professionale_biblioteca") == 0)
 	   		{
 	   			// Scriviamo l-utente root di default 
 	   			String root = "0000001&$%" + codPolo + "&$% __&$%Super&$%note di competenza&$%Uff. appartenenza";
 	   			byte arUtf8[] = myGetBytesUtf8(root);
                try {
					streamOut.write(arUtf8, 0, arUtf8.length);
 	                streamOut.write('\n');
 	                recordCounter++;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
 	   			convertDb.caricaSequenceFiles(sequenceDir +"SequenceTbfAnagrafeUtentiProfessionali.srt", 7+3+9); 
 	   		}  	
 	   		else if (fileOutInAr[0].compareTo("tbf_utenti_professionali_web") == 0)
 	   		{
 	   			// Scriviamo l-utente root di default
 	   			
 	   			// Crea la password di root
 	   			String rootPwdInChiaro;
 	   			PasswordEncrypter passwordEncrypter;
 	   			String rootPwdCriptata;

// 	   			rootPwdInChiaro = "istituto"; // dk83YUVwF/27wxuFm6L2IA==
// 	   			passwordEncrypter = new PasswordEncrypter(rootPwdInChiaro);   
// 	   			rootPwdCriptata = passwordEncrypter.encrypt(rootPwdInChiaro);
// 	   			
// 	   			rootPwdInChiaro = "password"; // N9uD5elZXi0LxVwXm1U2ZQ==
// 	   			passwordEncrypter = new PasswordEncrypter(rootPwdInChiaro);   
// 	   			rootPwdCriptata = passwordEncrypter.encrypt(rootPwdInChiaro);
 	   			
 	   			rootPwdInChiaro = "firenze"; // vpRrmAI0h2o=
 	   			passwordEncrypter = new PasswordEncrypter(rootPwdInChiaro);   
 	   			rootPwdCriptata = passwordEncrypter.encrypt(rootPwdInChiaro); 
 	   			
 	   			
 	           DateFormat df = new SimpleDateFormat ( "yyyyMMdd" ) ; 
 	           String date = df.format (new Date())  ; 

 	   			String root = "0000001&$%root&$%" + rootPwdCriptata + "&$%Argentino&$%" +date +"&$%Argenntino&$%" + date +"&$%N&$%S&$%" + date +"&$%" ;
 	   			byte arUtf8[] = myGetBytesUtf8(root);
                try {
					streamOut.write(arUtf8, 0, arUtf8.length);
 	                streamOut.write('\n');
 	                recordCounter++;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
 	   			convertDb.caricaSequenceFiles(sequenceDir +"SequenceTbfAnagrafeUtentiProfessionali.srt", 7+3+9); 
 	   		}  	
 	   		
 	   		
 	   	 	   		
 	   		
 	   		else if (fileOutInAr[0].compareTo("tba_suggerimenti_bibliografici") == 0 || fileOutInAr[0].compareTo("tba_profili_acquisto") == 0)
 	   			convertDb.caricaSequenceFiles(sequenceDir +"SequenceTbaSezAcquisBibliografiche.srt", 7+3+7); 
 	   		else if (fileOutInAr[0].compareTo("tbb_bilanci") == 0)
 	   			convertDb.caricaSequenceFiles(sequenceDir +"SequenceTbbCapitoliBilanci.srt", 7+3+8); 
 	   		else if (fileOutInAr[0].compareTo("trl_utenti_biblioteca") == 0)
 	   			convertDb.caricaSequenceFiles(sequenceDir +"SequenceTblUtenti.srt", 7+3+10); 

 	   		
 	   		
 	   	 	   	
 	   	 	   	 	   	 	   	 	   	 	   		
	        
	        // Read first record for all
	        for (int i=0; i < files; i++)
	        	{
		        	arInputRecord[i] = readRecord(i);
		        	if (arInputRecord[i] == null)
			        {
			        	if (i==0)
			        		BidLeft = null;
			        	else
			        		BidRight = null;
			        }
		        	else
			        {
			        	if (i==0)
			        	{
//			        		if (tpiiis == true)
			        		if (tpiiis == true || fsiiis == true)
			        			BidLeft = arInputRecord[i].substring(0,10);
			        		else if (tlrbit == true)
			        		{
			        			BidLeft = arInputRecord[i].substring(40,40+7);
			        			if (BidLeft.charAt(BidLeft.length()-1) != '|')
			        				BidLeft = BidLeft.substring(0, BidLeft.length()-1);
			        		}
			        		else
			        			BidLeft = "DummyBid";
			        		
			        	}
			        	else 
			        	{
//			        		if (tpiiis == true)
			        		if (tpiiis == true || fsiiis == true)
			        		{
			        			BidRight = arInputRecord[i].substring(0,10);
			        			// Record di Firenze troncato con CR/LF in campo dati
			        			// Leggiamo la riga successiva
			        			if (arInputRecord[i].length() < 1200)
			        				{
			        				String s = readRecord(i);
			        				arInputRecord[i] = arInputRecord[i] + s;
			        				System.out.println("Record spezzato: " + recordCounter);
			        				}
			        		}
			        		else if (fsiiau == true)
			        		{
			        			if (arInputRecord[i].length() < 318) 
		        				{
		        				String s = readRecord(i);
		        				arInputRecord[i] = arInputRecord[i] + s;
		        				System.out.println("Record spezzato: " + recordCounter);
		        				}
			        		}
			        		else if (fsiinn == true)
			        		{
			        			if (arInputRecord[i].length() < 126) 
		        				{
		        				String s = readRecord(i);
		        				arInputRecord[i] = arInputRecord[i] + s;
		        				System.out.println("Record spezzato: " + recordCounter);
		        				}
			        		}
			        		else if (fscdoc == true)
			        		{
			        			if (arInputRecord[i].length() < 810) 
		        				{
		        				String s = readRecord(i);
		        				arInputRecord[i] = arInputRecord[i] + s;
		        				System.out.println("Record spezzato: " + recordCounter);
		        				}
			        		}
			        		else if (fscinv == true)
			        		{
			        			if (arInputRecord[i].length() < 857) 
		        				{
		        				String s = readRecord(i);
		        				arInputRecord[i] = arInputRecord[i] + s;
		        				System.out.println("Record spezzato: " + recordCounter);
		        				}
			        		}
			        		else if (fscloc == true)
			        		{
			        			if (arInputRecord[i].length() < 346) 
		        				{
		        				String s = readRecord(i);
		        				arInputRecord[i] = arInputRecord[i] + s;
		        				System.out.println("Record spezzato: " + recordCounter);
		        				}
			        		}
			        		
			        		else if (tlrbit == true)
			        		{
			        			BidRight = arInputRecord[i].substring(0,0+7);
			        			if (BidRight.charAt(6) != '|')
			        				BidRight = BidRight.substring(0, BidRight.length()-1);
			        		}
			        		else
			        			BidRight = "DummyBid";
			        	}
			        }
	        	}
	        
	        while (true)
	    	{
	        	// if BIDleft = BIDright then unify record and process 	
//				if (BidLeft != null && BidRight != null && BidLeft.equals(BidRight))

				if ((files == 1 && BidLeft != null ) || (BidLeft != null && BidRight != null && BidLeft.equals(BidRight)))
//				if ((files == 1 ) || (BidLeft != null && BidRight != null && BidLeft.equals(BidRight)))
				{
	 	            // Concatenate the records from each file in one record
	 	            // ====================================================
	 	            sb.setLength(0);
	 	            for (int i=0; i <vecBfrFiles.size(); i++)
	 	            {
	 	            	if (i == 0)
	 	            	{
	 	            		sb.append(arInputRecord[i]);
			        		if (fsiiau == true)
			        		{
			        			if (arInputRecord[i].length() < 318) 
		        				{
		        				String s = readRecord(i);
		        				System.out.println("Record spezzato: " + recordCounter);
		 	            		sb.append(s);
		        				}
			        		}
			        		else if (fsiinn == true)
			        		{
			        			if (arInputRecord[i].length() < 126) 
		        				{
		        				String s = readRecord(i);
		        				System.out.println("Record spezzato: " + recordCounter);
		 	            		sb.append(s);
		        				}
			        		}
			        		else if (fscdoc == true)
			        		{
			        			if (arInputRecord[i].length() < 810) 
		        				{
		        				String s = readRecord(i);
		        				System.out.println("Record spezzato: " + recordCounter);
		 	            		sb.append(s);
		 	            		if (s.length() < 10)
		 	            		{
			        				s = readRecord(i);
			        				System.out.println("Record spezzato: " + recordCounter);
			 	            		sb.append(s);
		 	            		}
		 	            		
		        				}
			        		}
			        		else if (fscinv == true)
			        		{
			        			if (arInputRecord[i].length() < 857) 
		        				{
					        		String s;
		        				s = readRecord(i);
		        				System.out.println("Record spezzato: " + recordCounter);
		 	            		sb.append(s);
		 	            		while (s.length() < 10)
		 	            		{
			        				s = readRecord(i);
			        				System.out.println("Record spezzato: " + recordCounter);
			 	            		sb.append(s);
		 	            		}
		        				}
			        		}
			        		else if (fscloc == true)
			        		{
			        			if (arInputRecord[i].length() < 346) 
		        				{
					        		String s;
		        				s = readRecord(i);
		        				System.out.println("Record spezzato: " + recordCounter);
		 	            		sb.append(s);
		 	            		while (s.length() < 10)
		 	            		{
			        				s = readRecord(i);
			        				System.out.println("Record spezzato: " + recordCounter);
			 	            		sb.append(s);
		 	            		}
		        				}
			        		}

	 	            	}
	 	            	else
	 	            	{
	 	            		//gv aggiunta: l'ultimo campo del primo file ed il primo del secondo file erano appiccicati
	 	            		sb.append(stringSepDiCampoInArray[0]); //  separatoreDiCampoIn 
	 	            		//gv fine
//	 	            		if (tpiiis == true )
	 	            		if (tpiiis == true || fsiiis)
	 	            		{
	 	            			sb.append(arInputRecord[i].substring(10));
			        			// Record di Firenze troncato con CR/LF in campo dati
			        			// Leggiamo la riga successiva
			        			if (arInputRecord[i].length() < 1200)
			        				{
			        				String s = readRecord(i);
			        				arInputRecord[i] = arInputRecord[i]  + s;
			        				System.out.println("Record spezzato: " + recordCounter);
			        				}
	 	            		}
	 	            		else
		 	            		sb.append(arInputRecord[i]);
	 	            	}
	 	            }
	 				if (sb.length() == 0)
	 					break; // No more records to read in
// 	System.out.println("SB: "+sb.toString());
	 				
	 	        	// Remove escaped sequences
	 	            // ========================
//	 				String[] ar = MiscString.estraiCampiConEscapePerSeparatore(sb.toString(), separatoreDiCampoIn, escapeCharacter);
	 				String[] ar;
	 				if (separatorType == ConvertConstants.CHARACTER_SEPARATOR)
	 					ar = MiscString.estraiCampiConEscapePerSeparatore(sb.toString(), charSepDiCampoInArray, escapeCharacter);
	 				else
	 					ar = MiscString.estraiCampiConEscapePerSeparatore(sb.toString(), stringSepDiCampoInArray, escapeCharacter);
	 				
	        		if (fsiiau == true && ar.length != 19)
	        		{
        				System.out.println("*** NUMERO CAMPI non corrisponde: " + ar.length + " invece che 19. BID " + ar[2]);
        				recordErrariCounter++;
	        		}
	        		else
	        		{
		 				// Convert esabytes to UTF8
		 				// ========================
		 	            String[] ar1 = fromEsabyte2Unicode(ar);
		 	            // Create missing fields
		 	            // ========================
		 	            //gv ------------------------------------------------------------------- 
		 	            //ar2 ha tanti campi quanti item sono in mappa
		 	            //sembra un errore, perché la mappa è più ampia della tabella di output 
		 	            //per tb_autore (e non so se per altre tabelle)
		 	            //in realtà il numero che precede le coppie del mapping NON è il numero
		 	            //di coppie presenti (sarebbe banale e ridondante), ma il numero di campi 
		 	            //in uscita da creare: Il foglio excel quindi va corretto.
		 	            //gv ------------------------------------------------------------------- 
		 		        
		 	            String ar2[] = createFields(ar1,fileOutInAr[0]);
		 	            // Recompose record
		 	            // ========================
		 	            String s = makeRecord(ar2,fileOutInAr[0]);
		 				
		 				// Write record
		 	            // ========================
		 	            try {
		 	                byte arUtf8[] = myGetBytesUtf8(s);
		 	                
		 	                streamOut.write(arUtf8, 0, arUtf8.length);
		 	                streamOut.write('\n');
		 	                recordCounter++;
	
						//	 	String s1 =  new String(arUtf8, 0, arUtf8.length, "UTF8");
						//	 	System.out.print(s1+"\n");
						if ((recordCounter & 0x7FF) == 0)
							System.out.println("Records trattati: " + recordCounter);
		
		 	            } catch (IOException e) {
		 					// TODO Auto-generated catch block
		 					e.printStackTrace();
		 	                return false;
		 				}
	        		}
	 	            
	 	            
	 	            // Read next record for all
		        for (int i=0; i < files; i++)
		        	{
		        	arInputRecord[i] = readRecord(i);
		        	if (arInputRecord[i] == null)
			        	{
			        	if (i==0)
			        		BidLeft = null;
			        	else
			        		BidRight = null;
			        	}
		        	else
			        	{
//			        	if (i==0)
//			        		BidLeft = arInputRecord[i].substring(0,10);
//			        	else 
//			        		BidRight = arInputRecord[i].substring(0,10);
			        	if (i==0)
			        	{
			        		if (tpiiis == true || fsiiis == true)
			        			BidLeft = arInputRecord[i].substring(0,10);
			        		else if (tlrbit == true)
			        		{
			        			BidLeft = arInputRecord[i].substring(40,40+7);
			        			if (BidLeft.charAt(BidLeft.length()-1) != '|')
			        				BidLeft = BidLeft.substring(0, BidLeft.length()-1);
			        		}
			        		else
			        			BidLeft = "DummyBid";
			        	}
			        	else 
			        	{
			        		if (tpiiis == true || fsiiis == true)
			        			BidRight = arInputRecord[i].substring(0,10);
			        		else if (tlrbit == true)
			        		{
			        			BidRight = arInputRecord[i].substring(0,0+7);
			        			if (BidRight.charAt(6) != '|')
			        				BidRight = BidRight.substring(0, BidRight.length()-1);
			        		}
			        		else
			        			BidRight = "DummyBid";
			        	}
			        	}
		        	}
				}// End process record 

        		// else if BIDleft < BIDright read record from BidLeft file (REALIGN)
				else if (BidLeft != null  && (BidRight == null || BidLeft.compareTo(BidRight) < 0))
				{
				System.out.println("*** DISALLINEAMENTO: BidLeft " + BidLeft + " in " + vecBfrFilesNames.get(0) + " non trovato prima di BidRight " + BidRight +" in " + vecBfrFilesNames.get(1));
				// Riallineiamoci su BidLeft
	        	arInputRecord[0] = readRecord(0);
        		if (arInputRecord[0] == null)
        			BidLeft = null;
        		else
        			{
        			//BidLeft = arInputRecord[0].substring(0,10);
	        		if (tpiiis == true || fsiiis == true)
	        			BidLeft = arInputRecord[0].substring(0,10);
	        		else if (tlrbit == true)
	        		{
	        			BidLeft = arInputRecord[0].substring(40,40+7);
	        			if (BidLeft.charAt(BidLeft.length()-1) != '|')
	        				BidLeft = BidLeft.substring(0, BidLeft.length()-1);
	        		}
	        		else
	        			BidLeft = "DummyBid";

        			}
				} 
	        	
        		// else if BIDleft > BIDright read record from BidRight file (REALIGN)
				else if (BidRight != null && (BidLeft != null || BidLeft.compareTo(BidRight) > 0))
				{
				System.out.println("*** DISALLINEAMENTO: BidRight " + BidRight + " in " + vecBfrFilesNames.get(1) + " non trovato prima di BidLeft " + BidLeft +" in " + vecBfrFilesNames.get(0));
	        	arInputRecord[1] = readRecord(1);
        		if (arInputRecord[1] == null)
        			BidRight = null;
        		else
        			{
        			//BidRight = arInputRecord[1].substring(0,10);
	        		if (tpiiis == true || fsiiis == true)
	        			BidRight = arInputRecord[1].substring(0,10);
	        		else if (tlrbit == true)
	        		{
	        			BidRight = arInputRecord[1].substring(0,0+7);
	        			if (BidRight.charAt(6) != '|')
	        				BidRight = BidRight.substring(0, BidRight.length()-1);
	        		}
	        		else
	        			BidRight = "DummyBid";

        			}
				} 
	        	
	        	// else if !BIDleft AND !BIDright fine dei due file  
				else if (BidLeft == null && BidRight == null)
				{
				System.out.println("Fine");
				break;
				}
	    		
	    	} // End while
	 	    
// 	   		if (fileOutInAr[0].compareTo("tbf_anagrafe_utenti_professionali") == 0)
// 	   			convertDb.chiudiSequenceFile(sortCommand + " -k 2 -o ");
// 	   		else if (fileOutInAr[0].compareTo("tbf_biblioteca") == 0)
// 	   			convertDb.chiudiSequenceFile(sortCommand + " -k 1,8 -o ");
// 	   		else
 	   			convertDb.chiudiSequenceFile(sortCommand + " -t# -k 1.8 -o "); // , come separatore inesistente per avere solo un campo di sort
	        
	        
	 	    return true;
 	    } // doTransformNew

 	    private String [] createFields(String ar[],String tableMapName)
 	    {
 		String []ar1;
 		FieldsMap fm;
 		int i;
 		
 		fm = getFieldsMap(tableMapName);
 	  		
 		if (fm != null)
 			{
 			ar1 = new String[fm.mappaCampi.length];
 			}
 		else
 		{
 			ar1 = new String[ar.length];
 		}


	   	// Get original fields
 		if (tableMapName.compareTo("tbc_esemplare_titolo") == 0 || 
 			tableMapName.compareTo("tbc_collocazione") == 0)
 		{
 	 	   	for (i=0; i < ar.length; i++)
 	 	   	{
 	 	   		if (ar[i].charAt(0) == '$')
 	 	   			ar1[i] = "";
 	 	   		else	
 	 	   			ar1[i] = ar[i];
 	 	   	}
 		}
 		else
 		{
 	 	   	for (i=0; i < ar.length; i++)
 	 	   	{
 	   			ar1[i] = ar[i];
 	 	   	}
 		}
 	   	
 	   	if (fm != null) 
 	   	{
 	   		// Add generated fields
 	   		//gv =-------------------------------------------------------
 	   		//gv = i campi sono puntati con ar1[N-1] e si riferiscono
 	   		//gv = ai campi del foglio excel:
 	   		//gv =    il nome(commento) viene dalla colonna "C"
 	   		//gv =    il numero(N) viene dalla colonna "G"
 	   		//gv =-------------------------------------------------------

            arglist[0] = ar1;
            try {
				metodoConvert.invoke(convertDb,arglist);
//		 	   	return ar1;	
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 	   	} // end if table to be mapped
 	   	return ar1;	
 	    } // end createFields

private void resetVariables()
{
    recordCounter = 0;
    stripN = false;
    stripPipe = false;
    multiline = false;
    tpiiis = false;
    fsiiis = false;
    tlrbit = false;
    //fieldValue2IdMapping.clear(); done at load time
    sequence = 1;

}
@SuppressWarnings("unchecked")
private void loadTableMethods()
{
	// AMMINISTRZIONE
	tableMethodsHashMap.put("tbf_biblioteca","completaTbfBiblioteca");
	tableMethodsHashMap.put("tbf_anagrafe_utenti_professionali","completaTbfAnagrafeUtentiProfessionali");
	tableMethodsHashMap.put("trf_utente_professionale_biblioteca","completaTrfUtenteProfessionaleBiblioteca");
	tableMethodsHashMap.put("tbf_utenti_professionali_web", "completaTbfUtentiProfessionaliWeb");
	// BIBLIOGRAFICA
	tableMethodsHashMap.put("tb_autore","completaTbAutore");
	tableMethodsHashMap.put("tb_impronta","completaTbImpronta");
	tableMethodsHashMap.put("tb_marca","completaTbMarca");
	tableMethodsHashMap.put("tr_tit_mar","completaTrTitMar");
	tableMethodsHashMap.put("tr_tit_tit","completaTrTitTit");
	tableMethodsHashMap.put("tb_numero_std","completaTbNumeroStd");
	tableMethodsHashMap.put("tb_parola","completaTbParola");
	tableMethodsHashMap.put("tb_titolo","completaTbTitolo");
	tableMethodsHashMap.put("tr_aut_aut","completaTrAutAut");
	tableMethodsHashMap.put("tr_tit_aut","completaTrTitAut");
	// SEMANTICA
	tableMethodsHashMap.put("tb_classe","completaTbClasse");
	tableMethodsHashMap.put("tb_descrittore","completaTbDescrittore");
	tableMethodsHashMap.put("tb_soggetto","completaTbSoggetto");
	tableMethodsHashMap.put("tb_termine_thesauro","completaTbTermineThesauro");
	tableMethodsHashMap.put("tr_tit_cla","completaTrTitCla");
	tableMethodsHashMap.put("tr_des_des","completaTrDesDes");
	tableMethodsHashMap.put("tr_sog_des","completaTrSogDes");
	tableMethodsHashMap.put("tr_tit_sog_bib","completaTrTitSogBib");
	tableMethodsHashMap.put("tb_abstract","completaTbAbstract");
	tableMethodsHashMap.put("trs_termini_titoli_biblioteche","completaTrsTerminiTitoliBiblioteche");
	// ACQUISIZIONI
	tableMethodsHashMap.put("tba_suggerimenti_bibliografici","completaTbaSuggeriomentiBibliografici");
	tableMethodsHashMap.put("tbb_capitoli_bilanci","completaTbbCapitoliBilanci");
	tableMethodsHashMap.put("tbb_bilanci","completaTbbBilanci");
	tableMethodsHashMap.put("tba_ordini","completaTbaOrdini");
	tableMethodsHashMap.put("tbr_fornitori","completaTbrFornitori");
	tableMethodsHashMap.put("tbr_fornitori_biblioteche","completaTbrFornitoriBiblioteche");
	tableMethodsHashMap.put("tba_sez_acquis_bibliografiche","completaTbaSezAcquisBibliografiche");
	tableMethodsHashMap.put("tba_profili_acquisto","completaTbaProfiliAcquisto");
	tableMethodsHashMap.put("tba_richieste_offerta","completaTbaRichiesteOfferta");
	tableMethodsHashMap.put("tra_ordini_biblioteche","completaTraOrdiniBiblioteche");
	tableMethodsHashMap.put("tra_sez_acquisizione_fornitori","completaTraSezAcquisizioneFornitori");
	tableMethodsHashMap.put("tra_messaggi","completaTraMessaggi");
	tableMethodsHashMap.put("tba_cambi_ufficiali","completaTbaCambiUfficiali");
	tableMethodsHashMap.put("tba_fatture","completaTbaFatture");
	tableMethodsHashMap.put("tba_righe_fatture","completaTbaRigheFatture");
	tableMethodsHashMap.put("tba_offerte_fornitore","completaTbaOfferteFornitore");
	tableMethodsHashMap.put("tra_fornitori_offerte","completaTraFornitoriOfferte");
	// DOCUMENTO FISICO	
	tableMethodsHashMap.put("tbc_collocazione","completaTbcCollocazione");
	tableMethodsHashMap.put("tbc_esemplare_titolo","completaTbcEsemplareTitolo");
	tableMethodsHashMap.put("tbc_possessore_provenienza","completaTbcPossessoreProvenienza");
	tableMethodsHashMap.put("tbc_provenienza_inventario","completaTbcProvenienzaInventario");
	tableMethodsHashMap.put("tbc_serie_inventariale","completaTbcSerieInventariale");
	tableMethodsHashMap.put("tbc_sezione_collocazione","completaTbcSezioneCollocazione");
	tableMethodsHashMap.put("tbc_inventario","completaTbcInventario");
	tableMethodsHashMap.put("trc_formati_sezioni","completaTrcFormatiSezioni");
	tableMethodsHashMap.put("trc_poss_prov_inventari","completaTrcPossProvInventari");
	tableMethodsHashMap.put("trc_possessori_possessori","completaTrcPossessoriPossessori");
	// SERVIZI	
	tableMethodsHashMap.put("tbl_utenti","completaTblUtenti");
	tableMethodsHashMap.put("trl_utenti_biblioteca","completaTrlUtentiBiblioteca");
	tableMethodsHashMap.put("tbl_materie","completaTblMaterie");
	tableMethodsHashMap.put("tbl_occupazioni","completaTblOccupazioni");
	tableMethodsHashMap.put("tbl_specificita_titoli_studio","completaTblSpecificitaTitoliStudio");
	// ALTRE
	tableMethodsHashMap.put("tb_luogo","completaTbLuogo");
	tableMethodsHashMap.put("tb_repertorio","completaTbRepertorio");
	tableMethodsHashMap.put("tr_rep_mar","completaTrRepMar");
	tableMethodsHashMap.put("tbc_esemplare","completaTbcEsemplare");
	tableMethodsHashMap.put("trf_biblioteche_funzioni","completaTrfBibliotecheFunzioni");
	tableMethodsHashMap.put("trf_bibliotecari_funzioni","completaTrfBibliotecariFunzioni");
	tableMethodsHashMap.put("tbf_bibliotecario","completaTbfBibliotecario");
	tableMethodsHashMap.put("tbf_biblioteca_in_polo","completaTbfBibliotecaInPolo");
	tableMethodsHashMap.put("trf_profiliabil_funzioni","completaTrfProfiliAbilFunzioni");
	tableMethodsHashMap.put("trs_classi_titoli","completaTrsClassiTitoli");
	tableMethodsHashMap.put("trs_classi_parole","completaTrsClassiParole");
	tableMethodsHashMap.put("trs_descrittori_descrittori","completaTrsDescrittoriDescrittori");
	tableMethodsHashMap.put("trs_descrittori_parole","completaTrsDescrittoriParole");
	tableMethodsHashMap.put("trs_descrittori_soggetti","completaTrsDescrittoriSoggetti");
	tableMethodsHashMap.put("tr_sistemi_classi_biblioteche","completaTrsSistemiClassiBiblioteche");
	tableMethodsHashMap.put("tr_tit_luo","completaTrTitLuo");
	tableMethodsHashMap.put("tr_soggettari_biblioteche","completaTrSoggettariBiblioteche");
	tableMethodsHashMap.put("tr_tit_bib","completaTrTitBib");
	tableMethodsHashMap.put("tr_aut_bib","completaTrAutBib");
	tableMethodsHashMap.put("tr_mar_bib","completaTrMarBib");
	tableMethodsHashMap.put("tr_termini_termini","completaTrTerminiTermini");
	tableMethodsHashMap.put("tr_thesauri_biblioteche","completaTrThesauriBiblioteche");
	tableMethodsHashMap.put("tbc_default_inven_schede","completaTbcDefaultInvenSchede");
	tableMethodsHashMap.put("tba_buono_ordine","completaTbaBuonoOrdine");
	tableMethodsHashMap.put("tra_elementi_buono_ordine","completaTraElementiBuonoOrdine");
	tableMethodsHashMap.put("tbf_profilo_abilitazione","completaTbfProfiloAbilitazione");
} // End loadTableMethods









} // End Convert

