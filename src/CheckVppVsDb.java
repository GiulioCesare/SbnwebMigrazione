/**
 * @author Arge
 * date: 13/11/08
 * Descrizione:
 * 		Tool per verificare 
 * 			1. 	incongruenze tra il DDL geberato da DB Visual Architect (.ddl)
 * 				e il DDL generato da PGMANAGER sul DB di riferiemnto (.sql).
 *
 * 			2. 	TODO incongruenze tra il DDL geberato PGMANAGER DB TARGET (.sql)
 *				e il DDL generato da PGMANAGER sul DB di riferiemnto(.sql).
 * 
 * 		Verranno segnalati le seguenti incongruenze:
 * 			- Numero diverso di tabelle
 * 			- Numero diverso di campi nelle tabelle
 * 			- Tipi diversi di campi nelle tabelle
 * 			- Diversita' per campi NULLabili
 * 			- Numero diverso di foreign key
 * 			- Diversita' nella foreign key
 * 			- ...
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.Comparator;
import it.finsiel.misc.*;



public class CheckVppVsDb {
	public static final int TYPE_CHARACTER  		= 1;
	public static final int TYPE_VARIABLE_CHARACTER	= 2;
	public static final int TYPE_TEXT  				= 3;
	public static final int TYPE_INT2  				= 4;
	public static final int TYPE_INT4  				= 5;
	public static final int TYPE_INTEGER 			= 6;
	public static final int TYPE_NUMERIC  			= 7;
	public static final int TYPE_SEQUENCE  			= 8;
	public static final int TYPE_TIMESTAMP  		= 9;
	public static final int TYPE_DATE  				= 10;
	public static final int TYPE_TSVECTOR  			= 11;
	public static final int TYPE_UNKNOWN  			= 12;
	
	public static final String SCHEMA  			= "SBNWEB";

	char charSepArraySpace[] = { ' '};
	char charSepArrayComma[] = { ','};
	char charSepArrayParentyhesis[]  = { '(', ')' };	
//	char charSepArrayNumeric[]  = { '(', '|', ')' };	
	char charSepArrayNumericComma[]  = { '(', ',', ')', ' '};	
	
	DataBase dataBase = new DataBase();

	
    private class ForeignKey
    {
    	String 	name; // constraintName
    	String 	sourceTableName;
    	ArrayList 	sourceFieldAL; 
    	String 	referencedTableName;
    	ArrayList 	referencedFieldAL; 
    	ForeignKey()
    	{
    		sourceFieldAL = new ArrayList();
    		referencedFieldAL = new ArrayList();
    	}
    	
    } // End ForeignKey
	
    private class PrimaryKey
    {
    	String name;
    	ArrayList fieldsAL;
    	PrimaryKey()
    	{
    		fieldsAL = new ArrayList();
    	}
    }

    private class Index
    {
    	String name;
    	String type; // UNIQUE, ...
    	String using; // btree, ...
    	Vector fieldVect; 
    	String tableName;
    	Index()
    	{
    		fieldVect = new Vector();
    	}
    }
    private class Field
    {
		String name;
		int type;
		int size;
		int sizeDecimal;
		boolean timezone;
		boolean notNull;
		boolean unique;
		String defaultInit;
		boolean isSequence;
	
		Field()
		{
		size = -1;
		sizeDecimal = -1;
		timezone = false;
		notNull = false;
		unique = false;
		defaultInit = "";
		isSequence = false;
		}
    }
    private class Table
    {
    	String name;
    	Vector fieldsVect;
    	//Vector foreignKeyVect;
    	Vector functionVect;
    	Vector indexVect;
    	PrimaryKey primaryKey;
    	Boolean withOIDS;
    	Table(String aName)
    	{
    		name = aName; 
    		fieldsVect = new Vector();
    		//foreignKeyVect = new Vector();
    		functionVect = new Vector();
    		indexVect = new Vector();
    		primaryKey = new PrimaryKey();
    	}
    	void addField(Field aField)
    	{
    		fieldsVect.add(aField);
    	}
    };

    public class DataBase
    {
    	public HashMap vppTableMap; // Tabelle della create 
    	public HashMap dbTableMap; 
    	
    	public HashMap dbFkTableMap; // Tabella delle foreign keys
    	public HashMap vppFkTableMap; 

    	
    	DataBase()
    	{
    		vppTableMap = new HashMap();
    		dbTableMap = new HashMap();
    		
    		dbFkTableMap = new HashMap();
    		vppFkTableMap = new HashMap();
    	}
    	
    };

    






void addCreateIndexVpp(String sql)
{
	
}
void addCreateViewVpp(String sql)
{
	
}
void addCreateFunctionVpp(String sql)
{
	
}

void addCommentOnTableVpp(String sql)
{

}

void addCommentOnColumnVpp(String sql)
{

}




private boolean loadVpp(String filename)
{
	BufferedReader inFile = null;
	String s;
	int elm;
	char charSepArraySpace[] = { ' '};
	char charSepArrayComma[] = { ','};
	
	try {
		inFile = new BufferedReader(new FileReader(filename));
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println("==========");
	System.out.println(" LOAD VPP ");	
	System.out.println("==========");
	
	try {
		while ((s = inFile.readLine())!=null)
		{
		if (s.length() == 0 || MiscString.isEmpty(s))    
			continue;
		if (s.startsWith("create table"))
			addCreateTableVpp(s);
		else if (s.startsWith("alter table"))
			addAlterTableVpp(s, inFile);
		else if (s.startsWith("create index "))
			addCreateIndexVpp(s);
		else if (s.startsWith("create view"))
			addCreateViewVpp(s);
		else if (s.startsWith("create function"))
			addCreateFunctionVpp(s);
		else if (s.startsWith("comment on table"))
			addCommentOnTableVpp(s);
		else if (s.startsWith("comment on column"))
			addCommentOnColumnVpp(s);
		
//		else
//			System.out.println("Comando SQL sconosciuto: \"" + s + "\"");
		} // End while
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return true;
} // End loadVpp








void addAlterTableVpp(String sql, BufferedReader inFile)
{
//	alter table sbnweb.trl_relazioni_servizi add constraint FKtrl_relazi297111 foreign key (cd_bib, cd_polo) references sbnweb.tbf_biblioteca_in_polo;
	sql = sql.substring(0, sql.length()-1);
	String arFk[];
	String arFieldNames[];
	
	arFk = MiscString.estraiCampi(sql, charSepArrayParentyhesis, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);

	String arTable[] = MiscString.estraiCampi(arFk[0], charSepArraySpace, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
	
	if (!arTable[6].equals("foreign") || !arTable[7].equals("key"))
	{
			System.out.println("ALTER TABLE non per foreign key: " + arTable[3]);
			return;
	}

	ForeignKey fk = new ForeignKey();
	
	fk.name = arTable[5].toUpperCase();
	fk.sourceTableName =  arTable[2].toUpperCase();

	arFieldNames = MiscString.estraiCampi(arFk[1], charSepArrayNumericComma, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);

	for (int i=0; i < arFieldNames.length; i++)
		fk.sourceFieldAL.add(arFieldNames[i]);
	
	// Get referenced table info
	arTable = MiscString.estraiCampi(arFk[2], charSepArraySpace, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
	fk.referencedTableName = arTable[1].toUpperCase();

	if (arFk.length > 3) // Sono stati dichiarati i campi della tabella di riferimento?
	{
		arFieldNames = MiscString.estraiCampi(arFk[3], charSepArrayNumericComma, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);

		for (int i=0; i < arFieldNames.length; i++)
			fk.referencedFieldAL.add(arFieldNames[i]);
	}
	else
	{
		// The referenced fields are not declard and have the same names as the source fields
		for (int i=0; i < fk.sourceFieldAL.size(); i++)
			fk.referencedFieldAL.add(fk.sourceFieldAL.get(i));
	}
	
	dataBase.vppFkTableMap.put(fk.sourceTableName+"."+fk.name, fk);
} // end addAlterTableVpp






void compare()
{
	System.out.println("================");
	System.out.println("Inizio controllo");
	System.out.println("================");

	System.out.println("DB Visual Architect  tables: "+ dataBase.vppTableMap.size());
	System.out.println("PADMIN  tables: "+ dataBase.dbTableMap.size());

	System.out.println("===================");
	System.out.println(" Controllo tabelle ");
	System.out.println("===================");
	
	if (dataBase.vppTableMap.size() != dataBase.dbTableMap.size())
	{
		HashMap bigMap, smallMap;
		if (dataBase.vppFkTableMap.size() > dataBase.dbFkTableMap.size())
		{
//			bigMap = dataBase.vppFkTableMap;
//			smallMap = dataBase.dbFkTableMap;
			bigMap = dataBase.vppTableMap;
			smallMap = dataBase.dbTableMap;
		}
		else
		{
//			bigMap = dataBase.dbFkTableMap;
//			smallMap = dataBase.vppFkTableMap;
			bigMap = dataBase.vppTableMap;
			smallMap = dataBase.dbTableMap;
		}
			Iterator it = bigMap.keySet().iterator();
			while(it.hasNext())
			{
				// Prendi la tabella del DB
				String tbName = (String)it.next();
				Table table = (Table)smallMap.get(tbName);
				if (table == null)
					System.out.println("Tabella: '" + tbName + "' mancante!!!"); // presente nel DB di riferimento manca nel VPP
			} // end while
	}
	System.out.println("");

	Table dbTable, vppTable;
	boolean validTable = true;
	
	// Cicliamo sul sulle tabelle del DB di riferimento  
	Iterator it = dataBase.dbTableMap.keySet().iterator();
	while(it.hasNext())
	{
		// Prendi la tabella del DB
		String tbName = (String)it.next();
		dbTable = (Table)dataBase.dbTableMap.get(tbName);
		
		// Prendi la tabella della VPP
		if (dataBase.vppTableMap.containsKey(tbName) == true)
		{
			vppTable = (Table)dataBase.vppTableMap.get(tbName);
			validTable = true;

			System.out.println("Tabella VPP: " + dbTable.name);
			
			// Controlliamo corrispondenza del numero dei campi
			if (vppTable.fieldsVect.size() != dbTable.fieldsVect.size())
				System.out.println("Tabella " + tbName + " ha un numero diverso di campi; " + vppTable.fieldsVect.size() + " in VPP e "  + dbTable.fieldsVect.size() + " in DB");
			// Controlliamo i singoli campi
			for (int i=0; i<vppTable.fieldsVect.size(); i++)
			{
				  
				// Controlliamo il nome del campo
				if (!((Field)vppTable.fieldsVect.get(i)).name.equals(((Field)dbTable.fieldsVect.get(i)).name))
					if (((Field)vppTable.fieldsVect.get(i)).name.equals("PASSWORD") && ((Field)dbTable.fieldsVect.get(i)).name.equals("\"PASSWORD\""))
						continue;
					else if (((Field)vppTable.fieldsVect.get(i)).name.equals("PATH") && ((Field)dbTable.fieldsVect.get(i)).name.equals("\"PATH\""))
						continue;
					else
					{
						System.out.println("Nome campo non combacia: VPP->'" + ((Field)vppTable.fieldsVect.get(i)).name + "' DB->'" + ((Field)dbTable.fieldsVect.get(i)).name);
						validTable = false;
					}

				// Controlliamo il tipo del campo
				if (((Field)vppTable.fieldsVect.get(i)).type != ((Field)dbTable.fieldsVect.get(i)).type)
				{
					String tipoVpp, tipoDb;
					
					switch (((Field)vppTable.fieldsVect.get(i)).type)
					{
					case TYPE_CHARACTER: tipoVpp = "character"; break;
					case TYPE_VARIABLE_CHARACTER: tipoVpp = "varchar"; break;
					case TYPE_TEXT: tipoVpp = "text"; break;
					case TYPE_INT2: tipoVpp = "int2"; break;
					case TYPE_INT4: tipoVpp = "int4"; break;
					case TYPE_INTEGER: tipoVpp = "integer"; break;
					case TYPE_NUMERIC: tipoVpp = "numeric"; break;
					case TYPE_SEQUENCE: tipoVpp = "sequence"; break;
					case TYPE_TIMESTAMP: tipoVpp = "timestamp"; break;
					case TYPE_DATE: tipoVpp = "date"; break;
					default: tipoVpp = "????"; break;
					}
					switch (((Field)dbTable.fieldsVect.get(i)).type)
					{
					case TYPE_CHARACTER: tipoDb = "character"; break;
					case TYPE_VARIABLE_CHARACTER: tipoDb = "varchar"; break;
					case TYPE_TEXT: tipoDb = "text"; break;
					case TYPE_INT2: tipoDb = "int2"; break;
					case TYPE_INT4: tipoDb = "int4"; break;
					case TYPE_INTEGER: tipoDb = "integer"; break;
					case TYPE_NUMERIC: tipoDb = "numeric"; break;
					case TYPE_SEQUENCE: tipoDb = "sequence"; break;
					case TYPE_TIMESTAMP: tipoDb = "timestamp"; break;
					case TYPE_DATE: tipoDb = "date"; break;
					default: tipoDb = "????"; break;
					}
					System.out.println("Tipo campo non combacia per campo '" +
							((Field)vppTable.fieldsVect.get(i)).name +  
							"': VPP->'" + tipoVpp + "' DB->'" + tipoDb);
					
					validTable = false;
				} // End controllo del tipo del campo

				// Controlliamo la dimensione del campo
				if (	((Field)vppTable.fieldsVect.get(i)).type == TYPE_CHARACTER || 
						((Field)vppTable.fieldsVect.get(i)).type == TYPE_VARIABLE_CHARACTER)
				{
					int sizeVpp = ((Field)vppTable.fieldsVect.get(i)).size;
					int sizeDb = ((Field)dbTable.fieldsVect.get(i)).size;
					if (sizeVpp != sizeDb)
					System.out.println("Dimensione del campo non combacia per campo '" +
							((Field)vppTable.fieldsVect.get(i)).name +  
							"': VPP->'" + sizeVpp + "' DB->'" + sizeDb);
					
				}
				else if (	((Field)vppTable.fieldsVect.get(i)).type == TYPE_NUMERIC)
				{
					int sizeVpp = ((Field)vppTable.fieldsVect.get(i)).size;
					int sizeDecimalVpp = ((Field)vppTable.fieldsVect.get(i)).sizeDecimal;
					int sizeDb = ((Field)dbTable.fieldsVect.get(i)).size;
					int sizeDecimalDb = ((Field)dbTable.fieldsVect.get(i)).sizeDecimal;
					if (sizeVpp != sizeDb || 
						sizeDecimalVpp != sizeDecimalDb)
					System.out.println("Dimensione del campo non combacia per campo '" +
							((Field)vppTable.fieldsVect.get(i)).name +  
							"': VPP->'" + sizeVpp + "' DB->'" + sizeDb + " decimal VPP-> " + sizeDecimalVpp + " decimal DB-> " + sizeDecimalDb);
				}

				// Controllo obbligaorieta' del campo 
				if (	((Field)vppTable.fieldsVect.get(i)).notNull !=  
						((Field)dbTable.fieldsVect.get(i)).notNull)
				{
					System.out.println("Obbligatorieta del campo non combacia per campo '" +
							((Field)vppTable.fieldsVect.get(i)).name +  
							"': VPP->'" + ((Field)vppTable.fieldsVect.get(i)).notNull + "' DB->'" + ((Field)dbTable.fieldsVect.get(i)).notNull);
				}
			} // End ciclo sui campi campo

			// Controlliamo il nome della primary key
			if (vppTable.primaryKey.name == null && dbTable.primaryKey.name == null)
				System.out.println(" Primary key senza nme");
			else if (vppTable.primaryKey.name == null && dbTable.primaryKey.name != null)
				System.out.println(" Primary key NULL in VPP e " + dbTable.primaryKey.name + " in DB");
			else if (vppTable.primaryKey.name != null && dbTable.primaryKey.name == null)
				System.out.println(" Primary key NULL in DB e " + vppTable.primaryKey.name + " in VPP");
			else if (vppTable.primaryKey.name != null && dbTable.primaryKey.name != null && !vppTable.primaryKey.name.equals(dbTable.primaryKey.name))
				System.out.println(" Primary key in VPP = " + vppTable.primaryKey.name + " in DB = " + dbTable.primaryKey.name);
			
			// Controlliamo il numero di campi della primary key
			if (vppTable.primaryKey.fieldsAL.size() != dbTable.primaryKey.fieldsAL.size())
				System.out.println(" Primary key fields non coincidono: " + vppTable.primaryKey.fieldsAL.size() + " in VPP e " + dbTable.primaryKey.fieldsAL.size() + " in DB");
			else
			{
				// Controlliamo il nome di campi della primary key
				for (int i=0; i < vppTable.primaryKey.fieldsAL.size(); i++)
				{
					if (!vppTable.primaryKey.fieldsAL.get(i).equals(dbTable.primaryKey.fieldsAL.get(i)))
						System.out.println(" Primary key fields hanno nomi diversi: " + vppTable.primaryKey.fieldsAL.get(i) + " in VPP e " + dbTable.primaryKey.fieldsAL.get(i) + " in DB");
						
				}
				
			}
				
//++++				
				
				
			if (validTable == true)
				// Dopo tutti i controlli 
				System.out.println(" - Tabella OK");
			else
				System.out.println(" - Tabella NOT OK");

		}
		else
			System.out.println("Tabella MANCANTE in VPP: " + dbTable.name);


	}
	
	System.out.println("========================");
	System.out.println(" Controllo Foreign Keys ");
	System.out.println("========================");

	System.out.println("DB Visual Architect  FOREIGN KEYS: "+ dataBase.vppFkTableMap.size());
	System.out.println("PgAdmin  FOREIGN KEYS: "+ dataBase.dbFkTableMap.size());
	HashMap bigMap = dataBase.vppFkTableMap, smallMap = dataBase.dbFkTableMap;
	String mancanteIn = "";
	String bigMapName = "VPP", smallMapName = "DB";
	

	// Controlliamo le Foreign Keys mancancati sul DB o VPP
	if (dataBase.vppFkTableMap.size() != dataBase.dbFkTableMap.size())
	{
		if (dataBase.vppFkTableMap.size() > dataBase.dbFkTableMap.size())
		{
//			bigMap = dataBase.vppFkTableMap;
//			smallMap = dataBase.dbFkTableMap;
			mancanteIn = "in DB!!";
		}
		else
		{
			bigMap = dataBase.dbFkTableMap;
			smallMap = dataBase.vppFkTableMap;
			bigMapName = "DB";
			smallMapName = "VPP";
			mancanteIn = "in VPP!!";
		}

		// Verifichiamo le chiavi mancanti in smallMap (causa assenza o nomi diversi)
		it = bigMap.keySet().iterator();
		while(it.hasNext())
		{
			// Prendi la tabella del DB
			String fkName = (String)it.next();
			ForeignKey fk = (ForeignKey)smallMap.get(fkName);
			if (fk == null)
				System.out.println("Foreign key: '" + fkName + "' mancante " + mancanteIn); 
		} // end while

		// Verifichiamo le chiavi mancanti in bigmapMap (causa nomi diversi) 
		System.out.println("----"); 
		it = smallMap.keySet().iterator();
		String tmpMancanteIn = mancanteIn;
		if (mancanteIn.equals("in DB!!"))
			tmpMancanteIn = "in VPP!!";
		else
			tmpMancanteIn = "in DB!!";
		while(it.hasNext())
		{
			// Prendi la tabella del DB
			String fkName = (String)it.next();
			ForeignKey fk = (ForeignKey)bigMap.get(fkName);
			if (fk == null)
				System.out.println("Foreign key: '" + fkName + "' mancante " + tmpMancanteIn); 
		} // end while
		
		
	} // fine controllo fk mancanti

	System.out.println("----"); 
	// Controlliamo congruenza delle foreign keys
	it = bigMap.keySet().iterator();
	while(it.hasNext())
	{
		// Prendi la tabella del DB
		String fkName = (String)it.next();
	
		ForeignKey bigMapFk = (ForeignKey)bigMap.get(fkName);
		ForeignKey smallMapFk = (ForeignKey)smallMap.get(fkName);
		if (smallMapFk == null)
		{
			// System.out.println("Foreign key: '" + fkName + "' mancante " + mancanteIn); Già detto sopra
			continue;
		}
	
		// Controlliamo il numeri di campi nella foreign key
		if (bigMapFk.sourceFieldAL.size() != smallMapFk.sourceFieldAL.size())
		{
			System.out.println("SOURCE FIELDS in Foreign key: '" + fkName + " ha " + bigMapFk.sourceFieldAL.size() + " campi in " + bigMapName + 
					" e " + smallMapFk.sourceFieldAL.size() + " in " + smallMapName); 
		}
		else
		{
			/* CAUSA INCOMPLETEZZA DATI DI DB VISUAL ARCHITECT nei campi referenziati
			 * questo test riporta una marea di inconsistenze
			 * Per cui al momento lo disabilito
			 * 
			// Controlliamo che i nomi dei campi che referenziano siano uguali
			for (int i=0; i < bigMapFk.sourceFieldAL.size(); i++)
			{
				if (!bigMapFk.sourceFieldAL.get(i).equals(smallMapFk.sourceFieldAL.get(i)))
					System.out.println("SOURCE FIELD in Foreign key: '" + fkName + " " + bigMapFk.sourceFieldAL.get(i) + 
							" diverso da " + smallMapFk.sourceFieldAL.get(i)); 
			}
			// Controlliamo che i nomi dei campi referenziati siano uguali
			for (int i=0; i < bigMapFk.sourceFieldAL.size(); i++)
			{
				if (!bigMapFk.referencedFieldAL.get(i).equals(smallMapFk.referencedFieldAL.get(i)))
					System.out.println("SOURCE FIELD in Foreign key: '" + fkName + " " + bigMapFk.referencedFieldAL.get(i) + 
							" diverso da " + smallMapFk.referencedFieldAL.get(i)); 
			}
			*/			
			
		}
		if (bigMapFk.referencedFieldAL.size() != smallMapFk.referencedFieldAL.size())
		{
			System.out.println("REFERENCED FIELDS Foreign key: '" + fkName + " ha " + bigMapFk.referencedFieldAL.size() + " campi in " + bigMapName + 
					" e " + smallMapFk.referencedFieldAL.size() + " in " + smallMapName); 
		}
	} // end while
	
} // End compare 


void addCreateTableVpp(String sql)
{
//	String stringSepArrayPrimary[] = { " primary"};
//	String stringSepArrayConstraint[] = { " constraint"};
	String arConstraints[];
	String arStringFields[];
	String arFields[];
	String arFields1[];
	String arField[];
	String arSize[];
//	boolean hasPrimaryKey = false;

	
	
	// se abbiamo campi di tipo numeric dobiamo sostituire i lseparatore da ',' in '|'
	// altrimenti toppo il parsing
//	if (sql.contains("numeric")) 
//	    sql = MiscString.regexpReplace(sql, "\\((\\d+), *(\\d+)\\)", "\\($1|$2\\)");

	int idx = sql.indexOf("(");
	String s1 = sql.substring(0, idx);
	String s2 = sql.substring(idx+1, sql.length()-2);
	
	
	// Prendiamo il nome della tabella
	String arTable[] = MiscString.estraiCampi(s1, charSepArraySpace, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
	Table tb;
	String tbName;
	if (arTable[2].startsWith("sbnweb."))
		tbName = arTable[2].substring(7).toUpperCase();
	else
		tbName = arTable[2].toUpperCase();
	tb = new Table(tbName);

	dataBase.vppTableMap.put(new String (tbName), tb);
		
	
	arFields = MiscString.estraiCampiDelimitatiENon(s2, ",", '(', ')', MiscString.KEEP_DELIMITERS_FALSE, MiscString.KEEP_GROUP_DELIMITERS_TRUE, MiscString.TRIM_TRUE, MiscString.HAS_ESCAPED_CHARACTERS_FALSE, MiscString.KEEP_ESCAPE_FALSE);
	
	// Splittiamo il campo
	for (int i=0; i < arFields.length; i++)
	{
//		arField = MiscString.estraiCampi(arFields[i], charSepArraySpace, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
		arField = MiscString.estraiCampiDelimitatiENon(arFields[i], " ", '(', ')', MiscString.KEEP_DELIMITERS_FALSE, MiscString.KEEP_GROUP_DELIMITERS_TRUE, MiscString.TRIM_FALSE, MiscString.HAS_ESCAPED_CHARACTERS_FALSE, MiscString.KEEP_ESCAPE_FALSE);

		Field field = new Field();

		if (arField[0].startsWith("primary"))
		{
			tb.primaryKey.name = tb.name+"_PK" ;
			arFields1 = MiscString.estraiCampi(arField[2].substring(1, arField[2].length()-1), charSepArrayComma, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
			for (int j=0; j < arFields1.length; j++)
			{
				tb.primaryKey.fieldsAL.add(arFields1[j].trim().toUpperCase());
			}
			continue; 
		}
		if (arField[0].startsWith("constraint"))
		{
			if (arField[2].startsWith("primary"))
			{
				tb.primaryKey.name = arField[1].toUpperCase();
				String fields = arField[4].substring(1, arField[4].length()-1);
				arFields1 = MiscString.estraiCampi(fields, charSepArrayComma, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
				for (int j=0; j < arFields1.length; j++)
					tb.primaryKey.fieldsAL.add(arFields1[j].trim().toUpperCase());
			}
			// Costraint di altro tipo TO DO
			continue; 
		}

		
		// Nome campo
//		if (i == 0)
//			// se primo campo rimuoviamo la parentesi
//			field.name = arField[0].substring(1).toUpperCase();
//		else
			field.name = arField[0].toUpperCase();
		// Tipo del campo
		if (arField[1].startsWith("char"))
			field.type = TYPE_CHARACTER;
		else if (arField[1].startsWith("varchar"))
			field.type = TYPE_VARIABLE_CHARACTER;
		else if (arField[1].startsWith("text"))
			field.type = TYPE_TEXT;
		else if (arField[1].startsWith("int4"))
			field.type = TYPE_INT4;
		else if (arField[1].startsWith("int2"))
			field.type = TYPE_INT2;
		else if (arField[1].startsWith("numeric"))
			field.type = TYPE_NUMERIC;
		else if (arField[1].startsWith("serial"))
			{
			field.type = TYPE_INT4;
			field.isSequence = true;
			}
		else if (arField[1].startsWith("timestamp"))
			field.type = TYPE_TIMESTAMP;
		else if (arField[1].startsWith("date"))
			field.type = TYPE_DATE;
		else if (arField[1].startsWith("tsvector"))
			field.type = TYPE_TSVECTOR;
		else
			field.type = TYPE_UNKNOWN;

		
		
		if (arField.length > 2)
		{
			for (int j = 2; ; j++)
			{
				if (j >= arField.length)
					break;
				// NOT NULL
				if (arField[j].equals("not") && arField[j+1].equals("null"))
				{
					field.notNull = true;
					j++;
				}
				// DEFAULT
				else if (arField[j].equals("default"))
				{
					field.defaultInit = arField[j+1];
					j++;
				}
				// UNIQUE
				else if (arField[j].equals("unique"))
				{
					field.unique = true;
				}
//				else 
//					System.out.println("Field property unknown: " + arField[j]);
			}

			// controlla unique
		}
		// Dobbiamo prendere la lunghezza?
		if (field.type == TYPE_CHARACTER ||
			field.type == TYPE_VARIABLE_CHARACTER ||
			field.type == TYPE_TIMESTAMP)
		{
			arSize = MiscString.estraiCampi(arFields[i], charSepArrayParentyhesis, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
			field.size = Integer.valueOf(arSize[1]).intValue();
		}
		else if (field.type == TYPE_NUMERIC)
		{
			arSize = MiscString.estraiCampi(arFields[i], charSepArrayNumericComma, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
			field.size = Integer.valueOf(arSize[2]).intValue();
			field.sizeDecimal = Integer.valueOf(arSize[3]).intValue();
		}
		// Salva campo in tabella
		tb.addField(field);
	}
	System.out.println("Done table: " + tb.name + ", " + tb.fieldsVect.size() + " campi");
} // End addCreateTableVpp



private boolean loadDb(String filename)
{
	BufferedReader inFile = null;
	String s;
	int elm;
	char charSepArraySpace[] = { ' '};
	char charSepArrayComma[] = { ','};
	
	try {
		inFile = new BufferedReader(new FileReader(filename));
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println("==========");
	System.out.println(" LOAD DB  ");	
	System.out.println("==========");
	
	try {
		while ((s = inFile.readLine())!=null)
		{
		if (s.length() == 0 || s.charAt(0) == '#' || MiscString.isEmpty(s))    
			continue;
		if (s.startsWith("CREATE TABLE"))
			addCreateTableDb(s, inFile);
		else if (s.startsWith("ALTER TABLE"))
			addAlterTableDb(s, inFile);
/*
 		else if (s.startsWith("create index "))
			addCreateIndexVpp(s);
		else if (s.startsWith("create view"))
			addCreateViewDb(s);
		else if (s.startsWith("create function"))
			addCreateFunctionDb(s);
		else if (s.startsWith("comment on table"))
			addCommentOnTableDb(s);
		else if (s.startsWith("comment on column"))
			addCommentOnColumnDb(s);
		else
			System.out.println("Comando SQL sconosciuto: \"" + s + "\"");
*/		

		
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
} // End loadDb

void addCreateTableDb(String sql, BufferedReader inFile)
{
	String arField[];
	String arSize[];

	// se abbiamo campi di tipo numeric dobiamo sostituire i lseparatore da ',' in '|'
	// altrimenti toppo il parsing
	
	
	// Prendiamo il nome della tabella
	String arTable[] = MiscString.estraiCampi(sql, charSepArraySpace, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
	Table tb = new Table(arTable[2].toUpperCase());
	dataBase.dbTableMap.put(new String (arTable[2].toUpperCase()), tb);

	String s;
	// Leggiamo tutti i campo della tabella
	try {
		while ((s = inFile.readLine())!=null)
		{
		if (s.length() == 0 || s.charAt(0) == '#' || MiscString.isEmpty(s))    
			continue;
		if (s.endsWith(";"))
			break;
		s = s.trim();

		//arField = MiscString.estraiCampi(s, charSepArraySpace, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);

		arField = MiscString.estraiCampiDelimitatiENon(s, " ", '(', ')', MiscString.KEEP_DELIMITERS_FALSE, MiscString.KEEP_GROUP_DELIMITERS_TRUE, MiscString.TRIM_FALSE, MiscString.HAS_ESCAPED_CHARACTERS_FALSE, MiscString.KEEP_ESCAPE_FALSE);

		
		
		Field field = new Field();

		field.name = arField[0].toUpperCase();
		
		
		int i=1;
		// Tipo del campo
		if (arField[i].startsWith("character"))
		{
			field.type = TYPE_CHARACTER;
			if ((i+1) < arField.length && arField[i+1].startsWith("varying"))
			{
				field.type = TYPE_VARIABLE_CHARACTER;
				i++;
			}
		}
//		else if (arField[i].startsWith("varchar"))
//			field.type = TYPE_VARIABLE_CHARACTER;
		else if (arField[i].startsWith("text"))
			field.type = TYPE_TEXT;
		else if (arField[i].startsWith("integer"))
//			field.type = TYPE_INTEGER;
			field.type = TYPE_INT4;
		else if (arField[i].startsWith("public.tsvector")) 
			field.type = TYPE_TSVECTOR;
		else if (arField[i].startsWith("smallint"))
			field.type = TYPE_INT2;
		else if (arField[i].startsWith("numeric"))
			field.type = TYPE_NUMERIC;
		else if (arField[i].startsWith("serial"))
			field.type = TYPE_SEQUENCE;
		else if (arField[i].startsWith("timestamp"))
			field.type = TYPE_TIMESTAMP;
		else if (arField[i].startsWith("date"))
			field.type = TYPE_DATE;
		else
			field.type = TYPE_UNKNOWN;

		
		
		if (arField.length > (i+1))
		{
			for (int j = (i+1); ; j++)
			{
				if (j >= arField.length)
					break;
				// NOT NULL
				if (arField[j].equals("NOT") && arField[j+1].startsWith("NULL"))
				{
					field.notNull = true;
					j++;
				}
				// DEFAULT
				else if (arField[j].equals("DEFAULT"))
				{
					field.defaultInit = arField[j+1];
					j++;
				}
				// UNIQUE
				else if (arField[j].startsWith("UNIQUE"))
				{
					field.unique = true;
				}
//				else 
//					System.out.println("Field property unknown: " + arField[j]);
			}

			// controlla unique
		}

		
		// Dobbiamo prendere la lunghezza?
		if (field.type == TYPE_CHARACTER ||
			field.type == TYPE_VARIABLE_CHARACTER ||
			field.type == TYPE_TIMESTAMP )
		{
			arSize = MiscString.estraiCampi(s, charSepArrayParentyhesis, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
			field.size = Integer.valueOf(arSize[1]).intValue();
		}
		else if (field.type == TYPE_NUMERIC)
		{
			arSize = MiscString.estraiCampi(s, charSepArrayNumericComma, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
			field.size = Integer.valueOf(arSize[2]).intValue();
			field.sizeDecimal = Integer.valueOf(arSize[3]).intValue();
		
		}
		
		// Salva campo in tabella
		tb.addField(field);
		
		}
	} catch (NumberFormatException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} // end while	
	
	System.out.println("Done table: " + tb.name + ", " + tb.fieldsVect.size() + " campi");
} // End addCreateTableDb


public static void main(String args[])
{
	
	if(args.length < 1)
    {
        System.out.println("Parametri mancanti. Uso: CheckVppVsDb <DBVA.ddl> <db.sql>");
        System.exit(1);
    }
    String inputVppFile = args[0];
    String inputDbFile = args[1];

    
	System.out.println("DB Visual Architect  ddl: "+inputVppFile);
	System.out.println("PGMANAGER ddl: "+inputDbFile);

    CheckVppVsDb checkVppVsDb = new CheckVppVsDb();

    checkVppVsDb.loadVpp(inputVppFile);
	checkVppVsDb.loadDb(inputDbFile);
	
	checkVppVsDb.compare();

    System.exit(0);

/*
	String arString[] = MiscString.estraiCampiDelimitatiENon("id_parametro  serial not null", " ", '(', ')', MiscString.KEEP_DELIMITERS_FALSE, MiscString.KEEP_GROUP_DELIMITERS_FALSE, MiscString.TRIM_TRUE);
	for (int i=0; i < arString.length; i++)
	{
		System.out.println("String: " + arString[i].toString());
	}
*/
} // End main


void addAlterTableDb(String sql, BufferedReader inFile)
{
//	ALTER TABLE ONLY tbl_controllo_attivita
//    ADD CONSTRAINT "FKtbl_contro76603" FOREIGN KEY (cd_bib, cd_polo) REFERENCES tbf_biblioteca_in_polo(cd_biblioteca, cd_polo);
	String s=null;
	try {
		s = inFile.readLine();
		if (Misc.emptyString(s))
		{
//			System.out.println("ALTER TABLE non per foreign key: " + sql);
			return;
	}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return;
	}

	sql = sql + s.substring(0,s.length()-1);	

	String arFk[];
	String arFieldNames[];
	arFk = MiscString.estraiCampi(sql, charSepArrayParentyhesis, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
	String arTable[] = MiscString.estraiCampi(arFk[0], charSepArraySpace, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);

	// E' una foreign key?
	if (arTable[7].equals("FOREIGN") && arTable[8].equals("KEY"))
	{
		ForeignKey fk = new ForeignKey();
		fk.name = arTable[6].toUpperCase();
		fk.sourceTableName =  arTable[3].toUpperCase();
		arFieldNames = MiscString.estraiCampi(arFk[1], charSepArrayNumericComma, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
		for (int i=0; i < arFieldNames.length; i++)
			fk.sourceFieldAL.add(arFieldNames[i]);
		// Get referenced table info
		arTable = MiscString.estraiCampi(arFk[2], charSepArraySpace, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
		fk.referencedTableName = arTable[1].toUpperCase();
		if (arFk.length > 3) // Sono stati dichiarati i campi della tabella di riferimento?
		{
			arFieldNames = MiscString.estraiCampi(arFk[3], charSepArrayNumericComma, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);

			for (int i=0; i < arFieldNames.length; i++)
				fk.referencedFieldAL.add(arFieldNames[i]);
		}
		else
		{
			// The referenced fields are not declard and have the same names as the source fields
			for (int i=0; i < fk.sourceFieldAL.size(); i++)
				fk.referencedFieldAL.add(fk.sourceFieldAL.get(i));
			
		}
		dataBase.dbFkTableMap.put(SCHEMA+"."+fk.sourceTableName+"."+fk.name, fk);
	}
	// E' una Primary Key?
	else if (arTable[7].equals("PRIMARY") && arTable[8].equals("KEY"))
	{
		// Troviamo la tabella di riferimento
		String tbName = arTable[3].toUpperCase();
		Table table = (Table)dataBase.dbTableMap.get(tbName);
		table.primaryKey.name = arTable[6].toUpperCase();
		
		//s = arTable[9].substring(1, arTable[9].length()-1);
		String arFields1[] = MiscString.estraiCampi(arFk[1], charSepArrayComma, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
		for (int j=0; j < arFields1.length; j++)
			table.primaryKey.fieldsAL.add(arFields1[j].trim().toUpperCase());
		
	}

	return;
} // end addAlterTableDb


} // End CheckVppVsDb

