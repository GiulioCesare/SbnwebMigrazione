package it.finsiel.migrazione;
import it.finsiel.misc.MiscString;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

public class ConvertBull extends ConvertDb {

/*    
	private String sequenceFilename;
	int sequenceKeyLength; 
*/
	PasswordEncrypter passwordEncrypter; 
	String MIGRATION_DATE;	
	int db_type;
	String codPolo;
//	public int sequence = 1; // for serial fields (only the first field)
	public int sequenceIdUtenteProfessionale = 2;
	public int sequenceIdBiblioteca = 2;
	public int sequenceIdUtente = 1;
	public int sequenceIdSezAcquisBibliografiche = 1;
	public int sequenceIdCapitoliBilanci = 1;
	public int sequenceIdOrdine = 1;
	public int sequenceIdValuta = 1;
	public int sequenceIdParola = 1;
	public int sequenceIdUtenteBiblioteca = 1;
	public int sequenceIdBuonoOrdine = 1;
	public int sequenceIdInvenSchede = 1;
	
    public ConvertBull(int aDb_type, String aCodPolo, char fileSystemSeparator)
    {
    	db_type = aDb_type;
    	codPolo = aCodPolo;
    	this.fileSystemSeparator = fileSystemSeparator;
    	this.sequenceFilename = null;
    	sequenceOut = null;
        mappaCodiciRepertorio = new Hashtable();
        mappaCodiciRepertorio.put(" ", "0");
        mappaCodiciRepertorio.put("A", "1");
        mappaCodiciRepertorio.put("B", "15");
        mappaCodiciRepertorio.put("C", "12");
        mappaCodiciRepertorio.put("D", "17");
        mappaCodiciRepertorio.put("E", "20");
        mappaCodiciRepertorio.put("F", "13");
        mappaCodiciRepertorio.put("G", "11");
        mappaCodiciRepertorio.put("H", "14");
        mappaCodiciRepertorio.put("I", "8");
        mappaCodiciRepertorio.put("J", "21");
        mappaCodiciRepertorio.put("K", "4");
        mappaCodiciRepertorio.put("L", "7");
        mappaCodiciRepertorio.put("M", "18");
        mappaCodiciRepertorio.put("N", "16");
        mappaCodiciRepertorio.put("O", "26");
        mappaCodiciRepertorio.put("P", "19");
        mappaCodiciRepertorio.put("Q", "3");
        mappaCodiciRepertorio.put("R", "9");
        mappaCodiciRepertorio.put("S", "10");
        mappaCodiciRepertorio.put("T", "2");
        mappaCodiciRepertorio.put("U", "25");
        mappaCodiciRepertorio.put("V", "5");
        mappaCodiciRepertorio.put("W", "24");
        mappaCodiciRepertorio.put("X", "22");
        mappaCodiciRepertorio.put("Y", "23");
        mappaCodiciRepertorio.put("Z", "6");
        
        DateFormat df = new SimpleDateFormat ( "yyyyMMdd" ) ; 
        MIGRATION_DATE = df.format ( new Date (  )  )  ; 
        
        
        
    }	

/*
	String getSequenceIdBuonoOrdine(String capitolo)
	{
		return "1"; // Da fare
	}
*/	
/*
	String getSequenceIdUtenteBiblioteca(String utente)
	{
		return "1"; // Da fare
	}
*/
/**
    public String creaTipoRecord(String tipoMatreriale, String genere1)
    {
        String tipoRecord = "";
        if (tipoMatreriale.equals("M") && genere1.equals("0"))
        	tipoRecord = "k";
        else if (tipoMatreriale.equals("M") && genere1.equals("0"))
        	tipoRecord = "k";
        
+++        
        else
        	tipoRecord = "a";
    	
    	return tipoRecord;
    } // End creaTipoRecord
**/	
    
    
	public void completaTbAutore(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
			ar1[1-1] = ar1[1-1] + ar1[2-1]; 

      ar1[5-1] = MiscString.trimRight(ar1[5-1]); 
				
			if (ar1[6-1] == null)
				ar1[6-1] = ConvertConstants.MIGRATION_NULL;
			else
        ar1[6-1] = MiscString.trimRight(ar1[6-1]); 

			
			if (ar1[16-1] == null)
			{
   				ar1[16-1] = MIGRATION_DATE;
			}
			else if (ar1[16-1].length() < 8) // ts_ins missing
			{
	   			if ( ar1[33-1] == null ||  ar1[33-1].length() < 8) // ts_var missing ?
	   				ar1[33-1] = MIGRATION_DATE;
	   			else 
					ar1[33-1] = ar1[16-1];
			}
			
			if (ar1[33-1] == null || ar1[33-1].length() < 8) // ts_var missing ?
				ar1[33-1] = ar1[16-1];

			
			ar1[20-1] = ConvertConstants.MIGRATION_NULL; // ISADN
			
			String desrNomeInAscii = ConvertCaratteriSpeciali.convertAutore(ar1[5-1]);
			calcoloCles(desrNomeInAscii, ar1[5-1]); // Descrizione nome, tipo nome
			ar1[21-1] = ky_cles1_A; // ky_cles1_a
			ar1[22-1] = ky_cles2_A; // ky_cles2_a

			ar1[23-1] = ConvertConstants.MIGRATION_NULL; // cd_paese
			ar1[24-1] = ConvertConstants.MIGRATION_NULL; // cd_lingua
			ar1[25-1] = ConvertConstants.MIGRATION_NULL; // aa_nascita
			ar1[26-1] = ConvertConstants.MIGRATION_NULL; // aa_morte

			ar1[28-1] = ConvertConstants.MIGRATION_NULL; // cd_agenzia
			ar1[29-1] = ConvertConstants.MIGRATION_NULL; // cd_norme_cat  
			ar1[30-1] = ConvertConstants.MIGRATION_NULL; // nota_cat_aut
			ar1[31-1] = ConvertConstants.MIGRATION_NULL; // vid_linkc

			ar1[32-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[27-1] = codPolo + "MIG" + "000000"; //ute_var


			ar1[34-1] = ConvertConstants.MIGRATION_FL_CANC; // ute_forza_ins
			ar1[35-1] = ConvertConstants.MIGRATION_FL_CANC; // ute_forza_var

			ar1[36-1] = ConvertConstants.MIGRATION_FL_CANC; 
			
			ar1[37-1] = ConvertConstants.MIGRATION_FL_CONDIVISO_SI;
			
			ar1[38-1] = codPolo + "MIG" + "000000"; //ute_condiviso
			ar1[39-1] = MIGRATION_DATE; // ts_condiviso
//			ar1[40-1] = ConvertConstants.MIGRATION_NULL; // tidx_vector
		}
		else
		{ // Informix

			ar1[27-1] = ""; // ISADN
			ar1[28-1] = ""; // cd_paese
			ar1[29-1] = ""; // cd_lingua
			ar1[30-1] = ""; // aa_nascita
			ar1[31-1] = ""; // aa_morte
			ar1[32-1] = ""; // cd_agenzia
			ar1[33-1] = ""; // cd_norme_cat  
			ar1[34-1] = ""; // nota_cat_aut
			ar1[35-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[36-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[37-1] = ""; // ute_forza_ins
				ar1[38-1] = ""; // ute_forza_var
				ar1[39-1] = "S"; // fl_condiviso valore fisso
			ar1[40-1] = codPolo + "MIG" + "000000"; //ute_condiviso
				ar1[41-1] = ""; // tidx_vector
			
			if (ar1[18-1].length() < 8) // ts_ins missing
			{
	   			if (ar1[19-1].length() < 8) // ts_var missing ?
	   				ar1[18-1] = MIGRATION_DATE;
	   			else 
					ar1[18-1] = ar1[19-1];
			}

			if (ar1[19-1].length() < 8) // ts_var missing ?
					ar1[19-1] = ar1[18-1];
		}
			
	} // completaTbAutore

	 public void oneDExample(String[] strs){
	     System.out.println(strs[0]);
	 }
	 	
	 public void completaTbImpronta(String[]ar1)
		{
		if (db_type == ConvertConstants.DB_BULL)
		{
			// Spacchetta impronta
			ar1[11-1] = ar1[4-1].substring(0, 10);
			ar1[12-1] = ar1[4-1].substring(10, 24);
			ar1[13-1] = ar1[4-1].substring(24);
			
			if (ar1[5-1] == null) //
				ar1[5-1] = "";
			else
        ar1[5-1] = MiscString.trimRight(ar1[5-1]); 
			ar1[6-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[7-1] = MIGRATION_DATE; //ts_ins
			ar1[8-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[9-1] = MIGRATION_DATE; //ts_var
			ar1[10-1] = ConvertConstants.MIGRATION_FLAG_CANC;

		
		
		
		}
		else
		{ // Informix
			ar1[7-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[8-1] = MIGRATION_DATE; //ts_ins
			ar1[9-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[10-1] = MIGRATION_DATE; //ts_var
			ar1[11-1] = ar1[2-1].substring(25-1); //impronta_3
		}
		} // End completaTbImpronta


	public void completaTbMarca(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
      ar1[3-1] = MiscString.trimRight(ar1[3-1]); 
      ar1[6-1] = MiscString.trimRight(ar1[6-1]); 
			
			ar1[18-1] = ""; // Motto
			ar1[19-1] = codPolo + "MIG" + "000000"; //ute_ins
			//ar1[10-1] = MIGRATION_DATE;
			ar1[20-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[21-1] = MIGRATION_DATE;
			ar1[22-1] = ConvertConstants.MIGRATION_FLAG_CANC;
			ar1[23-1] = ConvertConstants.MIGRATION_FL_CONDIVISO_SI;
			ar1[24-1] = codPolo + "MIG" + "000000"; //ute_condiviso
			ar1[25-1] = MIGRATION_DATE;
			ar1[26-1] = ConvertConstants.MIGRATION_TIDX_VECTOR;
		}
		else
		{ // Informix
			ar1[10-1] = ""; // Motto
			ar1[11-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[12-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[13-1] = "S"; // fl_condiviso valore fisso
			ar1[14-1] = codPolo + "MIG" + "000000"; //ute_condiviso
			if (ar1[5-1].length() < 8) // ts_ins missing
			{
	   			if (ar1[6-1].length() < 8) // ts_var missing ?
	   				ar1[5-1] = MIGRATION_DATE;
	   			else 
					ar1[5-1] = ar1[6-1];
			}
			if (ar1[6-1].length() < 8) // ts_var missing ?
					ar1[6-1] = ar1[5-1];
			ar1[15-1] = ""; //tidx_vector
		}
	} // End completaTbMarca


	public void completaTrTitMar(String ar1[])
			{
		if (db_type == ConvertConstants.DB_BULL)
		{
      ar1[5-1] = MiscString.trimRight(ar1[5-1]); 
			ar1[6-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[7-1] = MIGRATION_DATE; //ts_ins
			ar1[8-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[9-1] = MIGRATION_DATE; //ts_var
			ar1[10-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
		else
		{ // Informix
				//ar1[4-1] = " "; // fl_canc 
				ar1[5-1] = codPolo + "MIG" + "000000"; //ute_ins
				ar1[6-1] = MIGRATION_DATE; //ts_ins
				ar1[7-1] = codPolo + "MIG" + "000000"; //ute_var
				ar1[8-1] = MIGRATION_DATE; //ts_var
			}
		} // End completaTrTitMar


	public void completaTrTitTit(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
			if (ar1[8-1] == null)
				ar1[8-1] = "";
			else
        ar1[8-1] = MiscString.trimRight(ar1[8-1]); 

			ar1[13-1] = "NULL"; // 
			ar1[14-1] = "NULL"; // 
			ar1[15-1] = "NULL"; // 

			ar1[16-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[17-1] = MIGRATION_DATE; //ts_ins
			ar1[18-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[19-1] = MIGRATION_DATE; //ts_var

			ar1[20-1] = ConvertConstants.MIGRATION_FLAG_CANC;
			ar1[21-1] = ConvertConstants.MIGRATION_FL_CONDIVISO_SI;
			
			ar1[22-1] = codPolo + "MIG" + "000000"; //ute_condiviso
			ar1[23-1] = MIGRATION_DATE; //ts_condiviso
		}
		else
		{ // Informix
			ar1[9-1] = "NULL"; // 
			ar1[10-1] = "NULL"; // 
			ar1[11-1] = "NULL"; // 
			ar1[12-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[13-1] = MIGRATION_DATE; //ts_ins
			ar1[14-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[15-1] = MIGRATION_DATE; //ts_var
			ar1[16-1] = "S"; // fl_condiviso valore fisso
			ar1[17-1] = codPolo + "MIG" + "000000"; //ute_condiviso
			ar1[18-1] = MIGRATION_DATE; //ts_condiviso
		}
	} // End completaTrTitTit

		
	public void completaTbNumeroStd(String ar1[])
		{
		if (db_type == ConvertConstants.DB_BULL)
		{
      ar1[6-1] = MiscString.trimRight(ar1[6-1]); 
			
			ar1[7-1] = "NULL"; 
			ar1[8-1] = "NULL"; 
			ar1[9-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[10-1] = MIGRATION_DATE; //ts_ins
			ar1[11-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[12-1] = MIGRATION_DATE; //ts_var
			ar1[13-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
		else
		{ // Informix
			ar1[6-1] = ConvertConstants.MIGRATION_NUMERO_LASRTA; //numero_lastra
			ar1[7-1] = "NULL"; //cd_paese
			ar1[8-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[9-1] = MIGRATION_DATE; //ts_ins
			ar1[10-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[11-1] = MIGRATION_DATE; //ts_var
		}
		} // End completaTbNumeroStd 	   	

		
	public void completaTbParola(String ar1[])
		{
		if (db_type == ConvertConstants.DB_BULL)
		{
			ar1[14-1] = Integer.toString(sequenceIdParola++);
			salvaSequence(ar1[14-1], ar1[1-1]);
			
			ar1[15-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[16-1] = MIGRATION_DATE; //ts_ins
			ar1[17-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[18-1] = MIGRATION_DATE; //ts_var
			ar1[19-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
		else
		{ // Informix
			ar1[4-1] = Integer.toString(sequenceIdParola++);
			ar1[5-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[6-1] = MIGRATION_DATE; //ts_ins
			ar1[7-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[8-1] = MIGRATION_DATE; //ts_var
		}
		} // End completaTbParola





	public void completaTbTitolo(String ar1[])
		{
		if (db_type == ConvertConstants.DB_BULL)
		{
			ar1[8-1] += ar1[9-1]; 

      ar1[22-1] = MiscString.trimRight(ar1[22-1]); // FSIIOB_CLES2
      ar1[26-1] = MiscString.trimRight(ar1[26-1]); // FSIIOB_CLES2CT
			

			ar1[34-1] = ""; // ISADN
			ar1[35-1] = creaTipoMateriale(ar1[1-1], ar1[2-1]); // BID, codice_natura
			if (ar1[35-1].equals(" "))
				ar1[36-1] = ConvertConstants.MIGRATION_NULL;
			else
				ar1[36-1] = creaTipoRecord(ar1[4-1], ar1[15-1], ar1[16-1], ar1[17-1]); // genere1, gener2, genere3, genere4
			

			ar1[31-1] = ""; // 
			if (ar1[32-1] != null)
        ar1[32-1] = MiscString.trimRight(ar1[32-1]); // FSIIIS_DESC 
			ar1[37-1] = creaIndiceISBD(ar1[32-1], ar1[2-1]); // ISBD, codice_natura
			ar1[38-1] = ""; // 
			ar1[39-1] = ""; // 

			ar1[38-1] = ConvertConstants.MIGRATION_NULL; // KY_EDITORE
			ar1[39-1] = ConvertConstants.MIGRATION_NULL; // CD_AGENZIA
			ar1[40-1] = ConvertConstants.MIGRATION_NULL; // CD_NORME_CAT
			ar1[41-1] = ConvertConstants.MIGRATION_NULL; // NOTA_INF_TIT
			ar1[42-1] = ConvertConstants.MIGRATION_NULL; // NOTA_CAT_TIT
			ar1[43-1] = ConvertConstants.MIGRATION_NULL; // bid_link
			ar1[44-1] = ConvertConstants.MIGRATION_NULL; // tp_link

			ar1[45-1] = codPolo + "MIG" + "000000"; // ute_ins
			if (MiscString.isEmpty(ar1[12-1]))
				ar1[12-1] = MIGRATION_DATE; // ts_ins

			ar1[46-1] = codPolo + "MIG" + "000000"; // ute_var 
			ar1[47-1] = MIGRATION_DATE; // ts_var
			ar1[48-1] = ConvertConstants.MIGRATION_NULL; // UTE_FORZA_INS
			ar1[49-1] = ConvertConstants.MIGRATION_NULL; // UTE_FORZA_VAR
			ar1[50-1] = ConvertConstants.MIGRATION_FLAG_CANC;
			
			ar1[51-1] = ConvertConstants.MIGRATION_FL_CONDIVISO_SI;
			ar1[52-1] = codPolo + "MIG" + "000000"; // ute_condiviso
			ar1[53-1] = MIGRATION_DATE; // ts_condiviso
		}
		else
		{ // Informix
			if (MiscString.isEmpty(ar1[18-1]))
				ar1[18-1] = MIGRATION_DATE;
			if (MiscString.isEmpty(ar1[19-1]))
				ar1[19-1] = MIGRATION_DATE;
				
			ar1[33-1] = ""; // ISADN
			
			ar1[34-1] = creaTipoMateriale(ar1[1-1], ar1[3-1]); // BID, codice_natura
			ar1[35-1] = creaTipoRecord(ar1[12-1], ar1[13-1], ar1[14-1], ar1[14-1]); // genere1, gener2, genere3, genere4

			ar1[36-1] = creaIndiceISBD(ar1[2-1], ar1[3-1]); // ISBD, codice_natura
			ar1[37-1] = ""; // KY_EDITORE
			ar1[38-1] = ""; // CD_AGENZIA
			ar1[39-1] = ""; // CD_NORME_CAT
			ar1[40-1] = ""; // NOTA_INF_TIT
			ar1[41-1] = ""; // NOTA_CAT_TIT
			ar1[42-1] = codPolo + "MIG" + "000000"; // ute_ins
			ar1[43-1] = codPolo + "MIG" + "000000"; // ute_var 
			ar1[44-1] = ""; // UTE_FORZA_INS
			ar1[45-1] = ""; // UTE_FORZA_VAR
			ar1[46-1] = ConvertConstants.MIGRATION_FL_CONDIVISO_SI;
			ar1[20-1] = codPolo + "MIG" + "000000"; //ute_condiviso 
		}
		} // End completaTbTitolo

	public void completaTrAutAut(String ar1[])
		{
		if (db_type == ConvertConstants.DB_BULL)
		{
      ar1[4-1] = MiscString.trimRight(ar1[4-1]);
			ar1[6-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[7-1] = MIGRATION_DATE; //ts_ins
			ar1[8-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[9-1] = MIGRATION_DATE; //ts_var
			ar1[10-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
		else
		{ // Informix
			
		}
		}

	public void completaTrTitAut(String ar1[])
		{
		if (db_type == ConvertConstants.DB_BULL)
		{
			if (ar1[4-1].equals("4"))
				ar1[18-1] = "650";
			else if (ar1[4-1].equals("1") || ar1[4-1].equals("2"))
				ar1[18-1] = "070";
			else if (ar1[4-1].equals("3")) 
				ar1[18-1] = ConvertConstants.MIGRATION_EMPTY;
					
				
      ar1[5-1] = MiscString.trimRight(ar1[5-1]);
			ar1[7-1] = ConvertConstants.MIGRATION_FL_INCERTO_NO; 
			ar1[8-1] = ConvertConstants.MIGRATION_FL_SUPERFLUO_NO; 
			ar1[9-1] = ConvertConstants.MIGRATION_CD_STRUMENTO_MUSICALE_NULL;  
			ar1[10-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[11-1] = MIGRATION_DATE; //ts_ins
			ar1[12-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[13-1] = MIGRATION_DATE; //ts_var
			ar1[14-1] = ConvertConstants.MIGRATION_FL_CANC; 
			ar1[15-1] = ConvertConstants.MIGRATION_FL_CONDIVISO_SI; 
			ar1[16-1] = codPolo + "MIG" + "000000"; //ute_condiviso
			ar1[17-1] = MIGRATION_DATE; //ts_condiviso
			ar1[18-1] = ConvertConstants.MIGRATION_CD_RELAZIONE; 
		}
		else
		{ // Informix
			
		}
		} // End completaTrTitAut


	public void completaTbClasse(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
      ar1[1-1] = MiscString.trimRight(ar1[1-1]);
      ar1[2-1] = MiscString.trimRight(ar1[2-1]);
      ar1[3-1] = MiscString.trimRight(ar1[3-1]);
      ar1[4-1] = MiscString.trimRight(ar1[4-1]);
			if (ar1[1-1].length() == 0)
				ar1[1-1] = codPolo + "MIG" + "000000"; //ute_ins
			if (ar1[2-1].length() == 0)
				ar1[2-1] = MIGRATION_DATE; //ts_ins
			if (ar1[3-1].length() == 0)
				ar1[3-1] = codPolo + "MIG" + "000000"; //ute_var
			if (ar1[4-1].length() == 0)
				ar1[4-1] = MIGRATION_DATE; //ts_var

			
			
      ar1[7-1] = MiscString.trimRight(ar1[7-1]);
      ar1[8-1] = MiscString.trimRight(ar1[8-1]);
			
//			ar1[10-1] = ConvertConstants.MIGRATION_CD_EDIZIONE_SISTEMA_CLASSIFICAZIONE; // da prendere dalla tb codici?
			ar1[10-1] = ar1[5-1].substring(1);
			
			ar1[11-1] = ConvertConstants.MIGRATION_FL_COSTRUITO;
			ar1[12-1] = ConvertConstants.MIGRATION_FL_SPECIALE; 
			ar1[13-1] = ConvertConstants.MIGRATION_KY_CLASSE_ORD;
			ar1[14-1] = ConvertConstants.MIGRATION_SUFFISSO;  
			ar1[15-1] = ConvertConstants.MIGRATION_FL_CONDIVISO;  
			ar1[16-1] = ConvertConstants.MIGRATION_UTE_CONDIVISO_NULL;
			ar1[17-1] = ConvertConstants.MIGRATION_TS_CONDIVISO_NULL;
			ar1[18-1] = ConvertConstants.MIGRATION_FL_CANC;
			ar1[19-1] = ConvertConstants.MIGRATION_TIDX_VECTOR;
			
		}
		else
		{ // Informix
				//ar1[11-1] = "SUFFISSO";
				ar1[11-1] = ConvertConstants.MIGRATION_CD_EDIZIONE_SISTEMA_CLASSIFICAZIONE; // da prendere dalla tb codici?
				ar1[12-1] = ConvertConstants.MIGRATION_FL_COSTRUITO;
				ar1[13-1] = ConvertConstants.MIGRATION_FL_SPECIALE; 
				ar1[14-1] = ConvertConstants.MIGRATION_SUFFISSO;  

				ar1[15-1] = ConvertConstants.MIGRATION_FL_CONDIVISO;  
				ar1[16-1] = ConvertConstants.MIGRATION_UTE_CONDIVISO_NULL;
				ar1[17-1] = ConvertConstants.MIGRATION_TS_CONDIVISO_NULL;
				ar1[18-1] = ConvertConstants.MIGRATION_FL_CANC;
			
			}
	} // End completaTb


	public void completaTbDescrittore(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
      ar1[2-1] = MiscString.trimRight(ar1[2-1]);
      ar1[3-1] = MiscString.trimRight(ar1[3-1]);
      ar1[4-1] = MiscString.trimRight(ar1[4-1]);

			if (ar1[6-1].charAt(0) == 0) // LOCALE = 1 FL_CONDIVISO = N altrimenti S
	   				ar1[6-1] = ConvertConstants.MIGRATION_FL_NON_CONDIVISO;
	   			else 
	   				ar1[6-1] = ConvertConstants.MIGRATION_FL_CONDIVISO;
 
        ar1[8-1] = MiscString.trimRight(ar1[8-1]);
        ar1[9-1] = MiscString.trimRight(ar1[9-1]);
				ar1[10-1] = normalizzazioneGenerica(ar1[8-1]); 

	   			ar1[11-1] = ConvertConstants.MIGRATION_TP_FORMA_DES;
	   			ar1[12-1] = ConvertConstants.MIGRATION_CD_LIVELLO;
	   			ar1[13-1] = ConvertConstants.MIGRATION_FLAG_CANC;
				ar1[14-1] = ConvertConstants.MIGRATION_TIDX_VECTOR;
	   			
		}
		else
		{ // Informix
		}
	} // End completaTb


	public void completaTbSoggetto(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{

      ar1[6-1] = MiscString.trimRight(ar1[6-1]);
      ar1[8-1] = MiscString.trimRight(ar1[8-1]);
			if (ar1[8-1].length() == 0)
				ar1[8-1] = ConvertConstants.MIGRATION_NULL;
			
      ar1[1-1] = MiscString.trimRight(ar1[1-1]);
      ar1[2-1] = MiscString.trimRight(ar1[2-1]);
      ar1[3-1] = MiscString.trimRight(ar1[3-1]);
      ar1[4-1] = MiscString.trimRight(ar1[4-1]);
			if (ar1[1-1].length() == 0)
				ar1[1-1] = codPolo + "MIG" + "000000"; //ute_ins
			if (ar1[2-1].length() == 0)
				ar1[2-1] = MIGRATION_DATE; //ts_ins
			if (ar1[3-1].length() == 0)
				ar1[3-1] = codPolo + "MIG" + "000000"; //ute_var
			if (ar1[4-1].length() == 0)
				ar1[4-1] = MIGRATION_DATE; //ts_var
			
			if (ar1[7-1].charAt(0) == 0) // Caratteri speciali = 1 Si = N altrimenti 
	   				ar1[7-1] = ConvertConstants.MIGRATION_FL_CARATTERI_SPECIALI_SI;
	   			else 
	   				ar1[7-1] = ConvertConstants.MIGRATION_FL_CARATTERI_SPECIALI_NO;
			
//	  			ar1[12-1] = ConvertConstants.MIGRATION_KY_CLES1_S;
	  			ar1[12-1] = normalizzazioneGenerica(ar1[6-1]);
	  			
	  			ar1[13-1] = ConvertConstants.MIGRATION_KY_PRIMO_DES;
	  			
	   			ar1[14-1] = ConvertConstants.MIGRATION_FL_CONDIVISO;
	   			ar1[15-1] = ConvertConstants.MIGRATION_UTE_CONDIVISO;
	   			ar1[16-1] = MIGRATION_DATE;
	   			ar1[17-1] = ConvertConstants.MIGRATION_FL_CANC;
	  			ar1[18-1] = ConvertConstants.MIGRATION_KY_CLES2_S;
	   			ar1[19-1] = ConvertConstants.MIGRATION_TIDX_VECTOR; //non viene mappato. Ignora 
			
		}
		else
		{ // Informix
		}
	} // End completaTbSoggetto


		public void completaTbTermineThesauro(String ar1[])
			{
				if (db_type == ConvertConstants.DB_BULL)
				{
					ar1[9-1] = ConvertConstants.MIGRATION_KY_TEMINE_THESAURO;
					ar1[10-1] = ConvertConstants.MIGRATION_FLAG_CANC;
					ar1[11-1] = ConvertConstants.MIGRATION_TP_FORMA_THE;
					ar1[12-1] = ConvertConstants.MIGRATION_CD_LIVELLO;
					ar1[13-1] = ConvertConstants.MIGRATION_FL_CONDIVISO;
					ar1[14-1] = ConvertConstants.MIGRATION_TIDX_VECTOR;
				}
				else
				{ // Informix
				}
			} // End completaTbTermineThesauro

		public void completaTrTitCla(String ar1[])
		{
			if (db_type == ConvertConstants.DB_BULL)
			{
        ar1[4-1] = MiscString.trimRight(ar1[4-1]);
	   			//ar1[5-1] = ConvertConstants.MIGRATION_EDIZIONE_CLASSE;
 				ar1[5-1] = ar1[2-1].substring(1); // Da COD_CLA 
	   			ar1[6-1] = codPolo + "MIG" + "000000"; //ute_ins
	   			ar1[7-1] = MIGRATION_DATE; //ts_ins
	   			ar1[8-1] = codPolo + "MIG" + "000000"; //ute_var
	   			ar1[9-1] = MIGRATION_DATE; //ts_var
	   			ar1[10-1] = ConvertConstants.MIGRATION_FL_CANC; 
			}
			else
			{ // Informix
			}
		} // End completaTb

		
		public void completaTrDesDes(String ar1[])
		{
			if (db_type == ConvertConstants.DB_BULL)
			{
        ar1[3-1] = MiscString.trimRight(ar1[3-1]);
				
				if (ar1[4-1].equals("1 ") || ar1[4-1].equals("99"))
					//ar1[4-1] = "US"; // use (  vedi  )
					ar1[4-1] = "USE"; // use (  vedi  ) // Sbnweb non capisce US
				else if (ar1[4-1].equals("2 "))
					ar1[4-1] = "BT"; // broader term ( ha come termine piu' esteso)
				else if (ar1[4-1].equals("3 "))
					ar1[4-1] = "RT"; // related  term ( vedi anche )
				else if (ar1[4-1].equals("4 "))
					ar1[4-1] = "NT"; // narrower term ( ha come termine piu' ristretto )
				else if (ar1[4-1].equals("5 ") || ar1[4-1].equals("98"))
					ar1[4-1] = "UF"; // used for ( usato al posto di )
				else if (ar1[4-1].equals("6 "))
					ar1[4-1] = "AT"; // Legame autore titolo
				else if (ar1[4-1].equals("7 "))
					ar1[4-1] = "TA"; // Legame titolo autore
				
				
	   			ar1[6-1] = codPolo + "MIG" + "000000"; //ute_ins
	   			ar1[7-1] = MIGRATION_DATE; //ts_ins
	   			ar1[8-1] = codPolo + "MIG" + "000000"; //ute_var
	   			ar1[9-1] = MIGRATION_DATE; //ts_var
	   			ar1[10-1] = ConvertConstants.MIGRATION_FL_CANC; 
				
			}
			else
			{ // Informix
			}
		} // End completaTb

		public void completaTrSogDes(String ar1[])
		{
			if (db_type == ConvertConstants.DB_BULL)
			{
				ar1[3-1] = ConvertConstants.MIGRATION_FL_POSIZIONE;
	   			ar1[4-1] = codPolo + "MIG" + "000000"; //ute_ins
	   			ar1[5-1] = MIGRATION_DATE; //ts_ins
	   			ar1[6-1] = codPolo + "MIG" + "000000"; //ute_var
	   			ar1[7-1] = MIGRATION_DATE; //ts_var
	   			ar1[8-1] = ConvertConstants.MIGRATION_FL_CANC; 
	   			ar1[9-1] = ConvertConstants.MIGRATION_FL_PRIMA_VOCE_SI; 
			}
			else
			{ // Informix
	   			ar1[5-1] = ar1[5-1].substring(0, ar1[5-1].indexOf(".")); 
			}
		} // End completaTb

		public void completaTrTitSogBib(String ar1[])
		{
			if (db_type == ConvertConstants.DB_BULL)
			{
        ar1[1-1] = MiscString.trimRight(ar1[1-1]);
        ar1[2-1] = MiscString.trimRight(ar1[2-1]);
				if (ar1[1-1].length() == 0)
					ar1[1-1] = MIGRATION_DATE; //ts_ins
				if (ar1[2-1].length() == 0)
					ar1[2-1] = MIGRATION_DATE; //ts_var

        ar1[5-1] = MiscString.trimRight(ar1[5-1]);
				if (ar1[5-1].length() == 0)
					ar1[5-1] = ConvertConstants.MIGRATION_BIB;

        ar1[6-1] = MiscString.trimRight(ar1[6-1]);
	   			ar1[7-1] = codPolo;
	   			ar1[8-1] = ConvertConstants.MIGRATION_CD_SOGGETTARIO;
	   			ar1[9-1] = codPolo + "MIG" + "000000"; //ute_ins
	   			ar1[10-1] = codPolo + "MIG" + "000000"; //ute_var
	   			ar1[11-1] = ConvertConstants.MIGRATION_FL_CANC;
			}
			else
			{ // Informix
			}
		} // End completaTb

	public void completaTbAbstract(String ar1[])
		{
			if (db_type == ConvertConstants.DB_BULL)
			{
        ar1[3-1] = MiscString.trimRight(ar1[3-1]);
	   			ar1[5-1] = codPolo + "MIG" + "000000"; //ute_ins
	   			ar1[6-1] = MIGRATION_DATE; //ts_ins
	   			ar1[7-1] = codPolo + "MIG" + "000000"; //ute_var
	   			ar1[8-1] = MIGRATION_DATE; //ts_var
	   			ar1[9-1] = ConvertConstants.MIGRATION_FL_CANC; 
			}
			else
			{ // Informix
			}
		} // End completaTb
		




	public void completaTbaSuggeriomentiBibliografici(String ar1[])
		{
			if (db_type == ConvertConstants.DB_BULL)
			{
        ar1[1-1] = MiscString.trimRight(ar1[1-1]);
        ar1[2-1] = MiscString.trimRight(ar1[2-1]);
				if (ar1[1-1].length() == 0)
					ar1[1-1] = MIGRATION_DATE; //ts_ins
				if (ar1[2-1].length() == 0)
					ar1[2-1] = MIGRATION_DATE; //ts_var

        ar1[6-1] = MiscString.trimRight(ar1[6-1]);
        ar1[7-1] = MiscString.trimRight(ar1[7-1]);
        ar1[8-1] = MiscString.trimRight(ar1[8-1]);

				ar1[10-1] = getSequenceIdSezAcquisBibliografiche(ar1[3-1]+ar1[10-1].trim()); // cd_bib + cod_sez
				
				ar1[14-1] = codPolo;

				
				ar1[15-1] = codPolo + "MIG" + "000000"; //ute_ins
				ar1[16-1] = codPolo + "MIG" + "000000"; //ute_var
				
				ar1[17-1] = ConvertConstants.MIGRATION_FLAG_CANC;
				
				
			}
			else
			{ // Informix
			}
		} // End completaTb


	public void completaTbbCapitoliBilanci(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
      ar1[1-1] = MiscString.trimRight(ar1[1-1]);
      ar1[2-1] = MiscString.trimRight(ar1[2-1]);
			if (ar1[1-1].length() == 0)
				ar1[1-1] = MIGRATION_DATE; //ts_ins
			if (ar1[2-1].length() == 0)
				ar1[2-1] = MIGRATION_DATE; //ts_var

			ar1[7-1] = ar1[6-1] + ar1[7-1].substring(1,12) + '.' + ar1[7-1].substring(12); // Componi segno+numro+decimali
			
			ar1[8-1] = Integer.toString(sequenceIdCapitoliBilanci++);
			salvaSequence(ar1[8-1], ar1[3-1]+ar1[4-1]+ar1[5-1]);
			
	    	ar1[9-1] = codPolo;
	    	
			ar1[10-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[11-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[12-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
		else
		{ // Informix
		}
	} // End completaTb

	public void completaTbbBilanci(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
      ar1[1-1] = MiscString.trimRight(ar1[1-1]);
      ar1[2-1] = MiscString.trimRight(ar1[2-1]);
			if (ar1[1-1].length() == 0)
				ar1[1-1] = MIGRATION_DATE; //ts_ins
			if (ar1[2-1].length() == 0)
				ar1[2-1] = MIGRATION_DATE; //ts_var

			ar1[5-1] = getSequenceIdCapitoloBilancio(ar1[3-1]+ar1[4-1]+ar1[5-1]); // cd_bib+annoEsercizio+capitolo

			ar1[8-1] = ar1[7-1] + ar1[8-1]; // metti il segno al budget  
			ar1[10-1] = ar1[9-1] + ar1[10-1]; // metti il segno all'ordinato  
			ar1[12-1] = ar1[11-1] + ar1[12-1]; // metti il segno al fatturato  
			ar1[14-1] = ar1[13-1] + ar1[14-1]; // metti il segno al pagato  
			
			ar1[15-1] = ConvertConstants.MIGRATION_NULL;
			
			ar1[16-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[17-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[18-1] = ConvertConstants.MIGRATION_FLAG_CANC;
			
			 
		}
		else
		{ // Informix
		}
	} // End completaTb
		
	public void completaTbaOrdini(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
      ar1[1-1] = MiscString.trimRight(ar1[1-1]);
      ar1[2-1] = MiscString.trimRight(ar1[2-1]);
			if (ar1[1-1].length() == 0)
				ar1[1-1] = MIGRATION_DATE; //ts_ins
			if (ar1[2-1].length() == 0)
				ar1[2-1] = MIGRATION_DATE; //ts_var
			
      ar1[8-1] = MiscString.trimRight(ar1[8-1]);

      ar1[18-1] = MiscString.trimRight(ar1[18-1]);
      ar1[35-1] = MiscString.trimRight(ar1[35-1]);

			ar1[21-1] = getSequenceIdCapitoloBilancio2(ar1[3-1]+ar1[20-1]+ar1[21-1]); // cd_bib+annoEsercizio+capitolo

// CAMBIO NON VIENE GESTITO
// Solo EU e LI che ma no utilizzato			
//      ar1[25-1] = MiscString.trimRight(ar1[25-1]);
//			if (ar1[25-1].length() == 0)
//				ar1[25-1] = ConvertConstants.MIGRATION_NULL; 
			ar1[25-1] = ConvertConstants.MIGRATION_NULL; 

			
			ar1[28-1] = getSequenceIdSezAcquisBibliografiche(ar1[3-1]+ar1[28-1].trim()); // cd_bib + cod_sez
			
			
      ar1[38-1] = MiscString.trimRight(ar1[38-1]);
			if (ar1[38-1].length() == 0)
				ar1[38-1] = ConvertConstants.MIGRATION_NULL; 
			
      ar1[42-1] = MiscString.trimRight(ar1[42-1]);
			if (ar1[42-1].length() == 0)
				ar1[42-1] = ConvertConstants.MIGRATION_NULL; 
			
			
			ar1[43-1] = Integer.toString(sequenceIdOrdine++);
			salvaSequence(ar1[43-1], ar1[3-1]+ar1[5-1]+ar1[6-1]);
			
			ar1[44-1] = codPolo; 
			ar1[45-1] = ConvertConstants.MIGRATION_FLAG_CANC;
			ar1[47-1] = MIGRATION_DATE; //data_ins
			ar1[48-1] = MIGRATION_DATE; //data_agg
			ar1[49-1] = ConvertConstants.MIGRATION_STAMPATO_NO; 
			ar1[50-1] = ConvertConstants.MIGRATION_RINNOVATO_NO; 
//			if (ar1[??-1].charAt(0) == 'c' || ar1[??-1].charAt(0) == 'c') // se ordine chiuso
//	   			ar1[516-1] = ar1[2-1]; // data ultimo aggiornamento
//			else
	   			ar1[51-1] = ConvertConstants.MIGRATION_NULL;
			ar1[52-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[46-1] = codPolo + "MIG" + "000000"; //ute_var
		}
		else
		{ // Informix
		}
	} // End completaTb

	public void completaTbrFornitori(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
      ar1[2-1] = MiscString.trimRight(ar1[2-1]);
      ar1[4-1] = MiscString.trimRight(ar1[4-1]);
			if (ar1[2-1].length() == 0)
				ar1[2-1] = MIGRATION_DATE; //ts_ins
			if (ar1[4-1].length() == 0)
				ar1[4-1] = MIGRATION_DATE; //ts_var

      ar1[6-1] = MiscString.trimRight(ar1[6-1]);
      ar1[7-1] = MiscString.trimRight(ar1[7-1]);
      ar1[8-1] = MiscString.trimRight(ar1[8-1]);
      ar1[10-1] = MiscString.trimRight(ar1[10-1]);
			
      ar1[14-1] = MiscString.trimRight(ar1[14-1]);

			
			calcoloCles(ar1[6-1], "E");
			ar1[22-1] = ky_cles1_A;
			ar1[23-1] = codPolo;
			ar1[24-1] = ConvertConstants.MIGRATION_FLAG_CANC;
			
		}
		else
		{ // Informix
			ar1[3-1] = ar1[3-1].substring(0, ar1[3-1].indexOf(".")); 
			ar1[22-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[23-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[24-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
	} // End completaTb

	public void completaTbrFornitoriBiblioteche(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
      ar1[1-1] = MiscString.trimRight(ar1[1-1]);
      ar1[2-1] = MiscString.trimRight(ar1[2-1]);
			if (ar1[1-1].length() == 0)
				ar1[1-1] = MIGRATION_DATE; //ts_ins
			if (ar1[2-1].length() == 0)
				ar1[2-1] = MIGRATION_DATE; //ts_var

      ar1[5-1] = MiscString.trimRight(ar1[5-1]);
      ar1[6-1] = MiscString.trimRight(ar1[6-1]);
      ar1[7-1] = MiscString.trimRight(ar1[7-1]);
      ar1[8-1] = MiscString.trimRight(ar1[8-1]);
      ar1[9-1] = MiscString.trimRight(ar1[9-1]);

//			ar1[10-1] = ConvertConstants.MIGRATION_EMPTY;
//      MiscString.trimRight(ar1[10-1]);
//			if (ar1[10-1].length() == 0)
			ar1[10-1] = ConvertConstants.MIGRATION_VALUTA_EUR;

			ar1[11-1] = codPolo;
			ar1[12-1] = codPolo;
			ar1[13-1] = ConvertConstants.MIGRATION_FL_ALLINEA_NO;
			ar1[14-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[15-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[16-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
		else
		{ // Informix
			ar1[4-1] = ar1[4-1].substring(0, ar1[4-1].indexOf(".")); 
				ar1[11-1] = codPolo;
				ar1[12-1] = codPolo;
				ar1[13-1] = ConvertConstants.MIGRATION_FL_ALLINEA_NO;
				ar1[14-1] = codPolo + "MIG" + "000000"; //ute_ins
				ar1[15-1] = codPolo + "MIG" + "000000"; //ute_var
				ar1[16-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
	} // End completaTb

	public void completaTbcCollocazione(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
			{
      ar1[1-1] = MiscString.trimRight(ar1[1-1]);
      ar1[3-1] = MiscString.trimRight(ar1[3-1]);
      ar1[2-1] = MiscString.trimRight(ar1[2-1]);
      ar1[4-1] = MiscString.trimRight(ar1[4-1]);
			ar1[1-1] = codPolo + "MIG" + "000000"; //ute_ins
			if (ar1[2-1].length() == 0)
				ar1[2-1] = MIGRATION_DATE; //ts_ins
			ar1[3-1] = codPolo + "MIG" + "000000"; //ute_var
			if (ar1[4-1].length() == 0)
				ar1[4-1] = MIGRATION_DATE; //ts_var

      ar1[6-1] = MiscString.trimRight(ar1[6-1]);

      ar1[8-1] = MiscString.trimRight(ar1[8-1]);
      ar1[14-1] = MiscString.trimRight(ar1[14-1]);
			
//      ar1[17-1] = ar1[17-1].trim() + ar1[18-1].trim() + ar1[19-1].trim() + MiscString.trimRight(ar1[20-1]); 

			
			ar1[23-1] = ConvertConstants.MIGRATION_CD_SISTEMA_CLASSIFICAZIONE_NULL; 
			ar1[24-1] = ConvertConstants.MIGRATION_CD_EDIZIONE_SISTEMA_CLASSIFICAZIONE_NULL; 
			ar1[25-1] = ConvertConstants.MIGRATION_CLASSIFICAZIONE_NULL; 
			ar1[25-1] = codPolo; // sezione 
			ar1[26-1] = codPolo; // doc 
			
			ar1[27-1] = normalizzaCollocazione(ar1[7-1]); 
				
			ar1[28-1] = ConvertConstants.MIGRATION_CLASSE_NULL;  
			
			ar1[29-1] = normalizzaCollocazione(ar1[6-1]); 
			
			}
		else
		{ // Informix
		   		{
	 	   			ar1[14-1] = ar1[5-1].toUpperCase() ; // upper di spec loc

	 	   			// Right PAD ord_loc
	 	   			ar1[15-1] = MiscString.paddingString(ar1[15-1], 6, ' ', ConvertConstants.RIGHT_PADDING); 
	 	   			ar1[16-1] = MiscString.paddingString(ar1[16-1], 6, ' ', ConvertConstants.RIGHT_PADDING); 
	 	   			ar1[17-1] = MiscString.paddingString(ar1[17-1], 6, ' ', ConvertConstants.RIGHT_PADDING); 
	 	   			ar1[18-1] = MiscString.paddingString(ar1[18-1], 6, ' ', ConvertConstants.RIGHT_PADDING); 
	 	   			
	 	   			ar1[21-1] = ConvertConstants.MIGRATION_CD_SISTEMA_CLASSIFICAZIONE_NULL; 
	 	   			ar1[22-1] = ConvertConstants.MIGRATION_CD_EDIZIONE_SISTEMA_CLASSIFICAZIONE_NULL; 
	 	   			ar1[23-1] = ConvertConstants.MIGRATION_CLASSIFICAZIONE_NULL; 
	 	   			ar1[24-1] = codPolo; // sezione 
	 	   			if (ar1[8-1] == null) //cod_bib_dic
	 	 	   			ar1[25-1] = ConvertConstants.MIGRATION_NULL; // sezione 
	 	   			else
	 	   				ar1[25-1] = codPolo; // doc 
	 	   			ar1[26-1] = ConvertConstants.MIGRATION_ORD_SPEC_EMPTY; 
	 	   			
	 	   			ar1[27-1] = codPolo + "MIG" + "000000"; //ute_ins
	 	   			ar1[28-1] = codPolo + "MIG" + "000000"; //ute_var
	 	   		}
		}
	} // End completaTb

	public void completaTbcEsemplareTitolo(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
			{
      ar1[1-1] = MiscString.trimRight(ar1[1-1]);
      ar1[3-1] = MiscString.trimRight(ar1[3-1]);
      ar1[2-1] = MiscString.trimRight(ar1[2-1]);
      ar1[4-1] = MiscString.trimRight(ar1[4-1]);

      ar1[8-1] = MiscString.trimRight(ar1[8-1]);
      ar1[9-1] = MiscString.trimRight(ar1[9-1]);
      ar1[10-1] = MiscString.trimRight(ar1[10-1]);

			if (ar1[1-1].length() == 0)
				ar1[1-1] = codPolo + "MIG" + "000000"; //ute_ins
			if (ar1[2-1].length() == 0)
				ar1[2-1] = MIGRATION_DATE; //ts_ins
			if (ar1[3-1].length() == 0)
				ar1[3-1] = codPolo + "MIG" + "000000"; //ute_var
			if (ar1[4-1].length() == 0)
				ar1[4-1] = MIGRATION_DATE; //ts_var

			ar1[12-1] = codPolo;
			}
		else
		{ // Informix
			ar1[10-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[11-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[12-1] = codPolo;
		}
	} // End completaTb

	public void completaTbcPossessoreProvenienza(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
      ar1[1-1] = MiscString.trimRight(ar1[1-1]);
      ar1[3-1] = MiscString.trimRight(ar1[3-1]);
      ar1[2-1] = MiscString.trimRight(ar1[2-1]);
      ar1[4-1] = MiscString.trimRight(ar1[4-1]);
			if (ar1[1-1].length() == 0)
				ar1[1-1] = codPolo + "MIG" + "000000"; //ute_ins
			if (ar1[2-1].length() == 0)
				ar1[2-1] = MIGRATION_DATE; //ts_ins
			if (ar1[3-1].length() == 0)
				ar1[3-1] = codPolo + "MIG" + "000000"; //ute_var
			if (ar1[4-1].length() == 0)
				ar1[4-1] = MIGRATION_DATE; //ts_var
			ar1[12-1] = codPolo;

      ar1[7-1] = MiscString.trimRight(ar1[7-1]);
      ar1[15-1] = MiscString.trimRight(ar1[15-1]);
      ar1[17-1] = MiscString.trimRight(ar1[17-1]);
      ar1[18-1] = MiscString.trimRight(ar1[18-1]);
			ar1[23-1] = ConvertConstants.MIGRATION_TIDX_VECTOR;
		}
		else
		{ // Informix
			ar1[21-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[22-1] = codPolo + "MIG" + "000000"; //ute_var
		}
	} // End completaTb

	public void completaTbcProvenienzaInventario(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
			{
      ar1[1-1] = MiscString.trimRight(ar1[1-1]);
      ar1[2-1] = MiscString.trimRight(ar1[2-1]);
//      ar1[7-1] = MiscString.trimRight(ar1[7-1]);
//      ar1[8-1] = MiscString.trimRight(ar1[8-1]);

			if (ar1[1-1].length() == 0)
				ar1[1-1] = MIGRATION_DATE; //ts_ins
//			if (ar1[7-1].length() == 0)
				ar1[7-1] = codPolo + "MIG" + "000000"; //ute_ins
			if (ar1[2-1].length() == 0)
				ar1[2-1] = MIGRATION_DATE; //ts_var
//			if (ar1[8-1].length() == 0)
				ar1[8-1] = codPolo + "MIG" + "000000"; //ute_var

      ar1[5-1] = MiscString.trimRight(ar1[5-1]);
			
			ar1[6-1] = codPolo; 
			ar1[9-1] = ConvertConstants.MIGRATION_FLAG_CANC;
			}
		else
		{ // Informix
			ar1[6-1] = codPolo; 
			ar1[7-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[8-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[9-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
	} // End completaTb



	public void completaTbcSerieInventariale(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
			{
      ar1[1-1] = MiscString.trimRight(ar1[1-1]);
      ar1[3-1] = MiscString.trimRight(ar1[3-1]);
      ar1[2-1] = MiscString.trimRight(ar1[2-1]);
      ar1[4-1] = MiscString.trimRight(ar1[4-1]);

			if (ar1[1-1].length() == 0)
				ar1[1-1] = codPolo + "MIG" + "000000"; //ute_ins
			if (ar1[2-1].length() == 0)
				ar1[2-1] = MIGRATION_DATE; //ts_ins
			if (ar1[3-1].length() == 0)
				ar1[3-1] = codPolo + "MIG" + "000000"; //ute_var
			if (ar1[4-1].length() == 0)
				ar1[4-1] = MIGRATION_DATE; //ts_var

      ar1[7-1] = MiscString.trimRight(ar1[7-1]);

			ar1[15-1] = codPolo;
			ar1[16-1] = ConvertConstants.MIGRATION_FLAG_CANC;
			
			ar1[17-1] = ConvertConstants.MIGRATION_NULL;
			ar1[18-1] = ConvertConstants.MIGRATION_NULL;
			ar1[19-1] = ConvertConstants.MIGRATION_NULL;
			ar1[20-1] = ConvertConstants.MIGRATION_NULL;
			ar1[21-1] = ConvertConstants.MIGRATION_NULL;
			ar1[22-1] = ConvertConstants.MIGRATION_NULL;
			ar1[23-1] = ConvertConstants.MIGRATION_NULL;
			ar1[24-1] = ConvertConstants.MIGRATION_NULL;
			ar1[25-1] = ConvertConstants.MIGRATION_NULL;
			ar1[26-1] = ConvertConstants.MIGRATION_NULL;
			ar1[27-1] = ConvertConstants.MIGRATION_NULL;
			ar1[28-1] = ConvertConstants.MIGRATION_NULL;
			ar1[29-1] = ConvertConstants.MIGRATION_NULL;
			
			
			}
		else
		{ // Informix
			ar1[13-1] = codPolo; 
			ar1[14-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[15-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[16-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
	} // End completaTb

	public void completaTbcSezioneCollocazione(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
			{
      ar1[1-1] = MiscString.trimRight(ar1[1-1]);
      ar1[3-1] = MiscString.trimRight(ar1[3-1]);
      ar1[2-1] = MiscString.trimRight(ar1[2-1]);
      ar1[4-1] = MiscString.trimRight(ar1[4-1]);

			if (ar1[1-1].length() == 0)
				ar1[1-1] = codPolo + "MIG" + "000000"; //ute_ins
			if (ar1[2-1].length() == 0)
				ar1[2-1] = MIGRATION_DATE; //ts_ins
			if (ar1[3-1].length() == 0)
				ar1[3-1] = codPolo + "MIG" + "000000"; //ute_var
			if (ar1[4-1].length() == 0)
				ar1[4-1] = MIGRATION_DATE; //ts_var

      ar1[7-1] = MiscString.trimRight(ar1[7-1]);
      ar1[9-1] = MiscString.trimRight(ar1[9-1]);
			
			
			ar1[14-1] = codPolo; 
			ar1[15-1] = ConvertConstants.MIGRATION_FLAG_CANC;
			}
		else
		{ // Informix
			ar1[12-1] = codPolo; 
			ar1[13-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[14-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[15-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
	} // End completaTb

	public void completaTblUtenti(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{

      ar1[1-1] = MiscString.trimRight(ar1[1-1]);
      ar1[3-1] = MiscString.trimRight(ar1[3-1]);
      ar1[2-1] = MiscString.trimRight(ar1[2-1]);
      ar1[4-1] = MiscString.trimRight(ar1[4-1]);

			if (ar1[1-1].length() == 0)
				ar1[1-1] = codPolo + "MIG" + "000000"; //ute_ins
			if (ar1[2-1].length() == 0)
				ar1[2-1] = MIGRATION_DATE; //ts_ins
			if (ar1[3-1].length() == 0)
				ar1[3-1] = codPolo + "MIG" + "000000"; //ute_var
			if (ar1[4-1].length() == 0)
				ar1[4-1] = MIGRATION_DATE; //ts_var

      ar1[8-1] = MiscString.trimRight(ar1[1-1]);
      ar1[9-1] = MiscString.trimRight(ar1[9-1]);
      ar1[10-1] = MiscString.trimRight(ar1[10-1]);
      ar1[12-1] = MiscString.trimRight(ar1[12-1]);
      ar1[13-1] = MiscString.trimRight(ar1[13-1]);
      ar1[14-1] = MiscString.trimRight(ar1[14-1]);
      ar1[15-1] = MiscString.trimRight(ar1[15-1]);
      ar1[17-1] = MiscString.trimRight(ar1[17-1]);
      ar1[18-1] = MiscString.trimRight(ar1[18-1]);

      ar1[20-1] = MiscString.trimRight(ar1[20-1]);
			if (ar1[20-1].length() == 0)
				ar1[20-1] = MIGRATION_DATE; //ts_ins

      ar1[21-1] = MiscString.trimRight(ar1[21-1]);
			
      ar1[25-1] = MiscString.trimRight(ar1[25-1]);
      ar1[26-1] = MiscString.trimRight(ar1[26-1]);
      ar1[28-1] = MiscString.trimRight(ar1[28-1]);

      ar1[29-1] = MiscString.trimRight(ar1[29-1]);
			if (ar1[29-1].length() == 0)
				ar1[29-1] = MIGRATION_DATE; //ts_ins
			
      ar1[32-1] = MiscString.trimRight(ar1[32-1]);
      ar1[33-1] = MiscString.trimRight(ar1[33-1]);

      ar1[41-1] = MiscString.trimRight(ar1[41-1]);
      ar1[42-1] = MiscString.trimRight(ar1[42-1]);

      ar1[44-1] = MiscString.trimRight(ar1[44-1]);
      ar1[45-1] = MiscString.trimRight(ar1[45-1]);

      ar1[47-1] = MiscString.trimRight(ar1[47-1]);
      ar1[48-1] = MiscString.trimRight(ar1[48-1]);

      ar1[50-1] = MiscString.trimRight(ar1[50-1]);
      ar1[51-1] = MiscString.trimRight(ar1[51-1]);

			if (ar1[57-1] != null)
        ar1[57-1] = MiscString.trimRight(ar1[57-1]);
			else
				ar1[57-1] = MIGRATION_DATE;
				
			ar1[58-1] = codPolo; 
			ar1[59-1] = ConvertConstants.MIGRATION_FLAG_CANC;
			
			ar1[60-1] = Integer.toString(sequenceIdUtente++);
			salvaSequence(ar1[60-1], ar1[5-1]+ar1[6-1]);
			
			
			ar1[61-1] = ConvertConstants.MIGRATION_EMPTY;
			
		}
		else
		{ // Informix
		}

	} // End completaTb

	public void completaTrlUtentiBiblioteca(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
      ar1[1-1] = MiscString.trimRight(ar1[1-1]);
      ar1[3-1] = MiscString.trimRight(ar1[3-1]);
      ar1[2-1] = MiscString.trimRight(ar1[2-1]);
      ar1[4-1] = MiscString.trimRight(ar1[4-1]);

			if (ar1[1-1].length() == 0)
				ar1[1-1] = codPolo + "MIG" + "000000"; //ute_ins
			if (ar1[2-1].length() == 0)
				ar1[2-1] = MIGRATION_DATE; //ts_ins
			if (ar1[3-1].length() == 0)
				ar1[3-1] = codPolo + "MIG" + "000000"; //ute_var
			if (ar1[4-1].length() == 0)
				ar1[4-1] = MIGRATION_DATE; //ts_var

			
      ar1[11-1] = MiscString.trimRight(ar1[11-1]);
      ar1[12-1] = MiscString.trimRight(ar1[12-1]);
      ar1[13-1] = MiscString.trimRight(ar1[13-1]);
      ar1[14-1] = MiscString.trimRight(ar1[14-1]);

			if (ar1[11-1].length() == 0)
				ar1[11-1] = MIGRATION_DATE; //ts_var
			if (ar1[12-1].length() == 0)
				ar1[12-1] = MIGRATION_DATE; //ts_ins
			if (ar1[13-1].length() == 0)
				ar1[13-1] = MIGRATION_DATE; //ts_var
			if (ar1[14-1].length() == 0)
				ar1[14-1] = MIGRATION_DATE; //ts_var
			
			
			ar1[16-1] = getSequenceIdSpecificitaTitolo(ar1[16-1]); //cod specificazione titolo
			ar1[18-1] = getSequenceIdUtenteOccupazione(ar1[18-1]); //cod occupazione

			ar1[9-1] = ar1[8-1] + ar1[9-1].substring(1,9) + '.' + ar1[9-1].substring(9); // Componi segno+numro+decimali
      ar1[10-1] = MiscString.trimRight(ar1[10-1]);
			
			
			
			ar1[22-1] = Integer.toString(sequenceIdUtenteBiblioteca++);
			
			ar1[23-1] = getSequenceIdUtente(ar1[6-1]+ar1[7-1]); // COD_BIB_UT+COD_UTENTE

			ar1[24-1] = codPolo; 
			ar1[25-1] = ConvertConstants.MIGRATION_FLAG_CANC;

		
		}
		else
		{ // Informix
		}
	} // End completaTb





		public void completaTbfBiblioteca(String ar1[])
		{
//			if (db_type == ConvertConstants.DB_BULL)
//			{
        ar1[1-1] = MiscString.trimRight(ar1[1-1]);
        ar1[3-1] = MiscString.trimRight(ar1[3-1]);
        ar1[2-1] = MiscString.trimRight(ar1[2-1]);
        ar1[4-1] = MiscString.trimRight(ar1[4-1]);

				if (ar1[1-1].length() == 0)
					ar1[1-1] = codPolo + "MIG" + "000000"; //ute_ins
				if (ar1[2-1].length() == 0)
					ar1[2-1] = MIGRATION_DATE; //ts_ins
				if (ar1[3-1].length() == 0)
					ar1[3-1] = codPolo + "MIG" + "000000"; //ute_var
				if (ar1[4-1].length() == 0)
					ar1[4-1] = MIGRATION_DATE; //ts_var
        ar1[7-1] = MiscString.trimRight(ar1[7-1]);
        ar1[8-1] = MiscString.trimRight(ar1[8-1]);
        ar1[9-1] = MiscString.trimRight(ar1[9-1]);
        ar1[10-1] = MiscString.trimRight(ar1[10-1]);
        ar1[14-1] = MiscString.trimRight(ar1[14-1]);
        ar1[17-1] = MiscString.trimRight(ar1[17-1]);


				if (ar1[24-1].charAt(0) == 'N')
					ar1[24-1] = ConvertConstants.MIGRATION_CENTRO_SISTEMA;
				
				
				if (ar1[25-1] == null)
					ar1[25-1] = "";
				else
          ar1[25-1] = MiscString.trimRight(ar1[25-1]);
				
				if (ar1[26-1] == null)
					ar1[26-1] = "";
				else
          ar1[26-1] = MiscString.trimRight(ar1[26-1]);

				if (ar1[27-1] == null)
					ar1[27-1] = "";
				else
          ar1[27-1] = MiscString.trimRight(ar1[27-1]);
				
				ar1[28-1] = Integer.toString(sequenceIdBiblioteca++);
				salvaSequence(ar1[28-1], ar1[5-1]+ar1[6-1]);
				
				
				ar1[29-1] = ConvertConstants.MIGRATION_NULL;
				
				calcoloCles(ar1[7-1], "E");
				ar1[26-1] = ky_cles1_A;
				calcoloCles(ar1[8-1], "E");
				ar1[27-1] = ky_cles1_A;
				
				ar1[30-1] = "N"; //fl_canc
				ar1[31-1] = ConvertConstants.MIGRATION_NULL;
				ar1[32-1] = ConvertConstants.MIGRATION_NULL;
//			}
//			else
//			{ // Informix
//				{
////					ar1[28-1] = ConvertConstants.MIGRATION_SEQUENCE;
//		    	ar1[28-1] = Integer.toString(sequenceIdBiblioteca++);
//					ar1[29-1] = ConvertConstants.MIGRATION_CD_ANA_BIBLIOTECA_TEMP;
//					ar1[30-1] = "N"; //fl_canc
//				}
//			}
		} // End completaTb

		
		
	public void completaTbaSezAcquisBibliografiche(String ar1[])
		{
			if (db_type == ConvertConstants.DB_BULL)
			{
        ar1[1-1] = MiscString.trimRight(ar1[1-1]);
        ar1[2-1] = MiscString.trimRight(ar1[2-1]);

				if (ar1[1-1].length() == 0)
					ar1[1-1] = MIGRATION_DATE; //ts_ins
				if (ar1[2-1].length() == 0)
					ar1[2-1] = MIGRATION_DATE; //ts_var
				
        ar1[5-1] = MiscString.trimRight(ar1[5-1]);
        ar1[6-1] = MiscString.trimRight(ar1[6-1]);
				ar1[8-1] = ar1[7-1] + ar1[8-1].substring(1,12) + '.' + ar1[8-1].substring(12); // Componi segno+numro+decimali
	        	
				ar1[9-1] = Integer.toString(sequenceIdSezAcquisBibliografiche++);
				salvaSequence(ar1[9-1], ar1[3-1]+ar1[4-1]);
				
	   			ar1[10-1] = codPolo; 
	   			ar1[11-1] = ConvertConstants.MIGRATION_ANNO_VAL;
	   			ar1[12-1] = codPolo + "MIG" + "000000"; //ute_ins
	   			ar1[13-1] = codPolo + "MIG" + "000000"; //ute_var
				ar1[14-1] = ConvertConstants.MIGRATION_FLAG_CANC;
	   			ar1[15-1] = ConvertConstants.MIGRATION_BUDGET;
			}
			else
			{ // Informix
	        	ar1[8-1] = Integer.toString(sequenceIdSezAcquisBibliografiche++);
	   			ar1[9-1] = codPolo; 
	   			ar1[10-1] = ConvertConstants.MIGRATION_BUDGET;
	   			ar1[11-1] = ConvertConstants.MIGRATION_ANNO_VAL;
	   			ar1[12-1] = codPolo + "MIG" + "000000"; //ute_ins
	   			ar1[13-1] = codPolo + "MIG" + "000000"; //ute_var
				ar1[14-1] = ConvertConstants.MIGRATION_FLAG_CANC;
			}
		} // End completaTb
		


	public void completaTbaProfiliAcquisto(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
			{
      ar1[1-1] = MiscString.trimRight(ar1[1-1]);
      ar1[2-1] = MiscString.trimRight(ar1[2-1]);

			if (ar1[1-1].length() == 0)
				ar1[1-1] = MIGRATION_DATE; //ts_ins
			if (ar1[2-1].length() == 0)
				ar1[2-1] = MIGRATION_DATE; //ts_var

      ar1[5-1] = MiscString.trimRight(ar1[5-1]);

			ar1[6-1] = getSequenceIdSezAcquisBibliografiche(ar1[3-1]+ar1[6-1].trim()); // cd_bib + cod_sez

			ar1[9-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[10-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[11-1] = ConvertConstants.MIGRATION_FLAG_CANC;
			} 	   	  	
		else
		{ // Informix
			ar1[4-1] = ar1[4-1].substring(0, ar1[4-1].indexOf(".")); 
			ar1[6-1] = ConvertConstants.MIGRATION_ID_SEZ_ACQUISIZIONE;
			ar1[3-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[9-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[10-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
	} // End completaTb


	public void completaTbaRichiesteOfferta(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
			{
      ar1[1-1] = MiscString.trimRight(ar1[1-1]);
      ar1[2-1] = MiscString.trimRight(ar1[2-1]);

			if (ar1[1-1].length() == 0)
				ar1[1-1] = MIGRATION_DATE; //ts_ins
			if (ar1[2-1].length() == 0)
				ar1[2-1] = MIGRATION_DATE; //ts_var

      ar1[8-1] = MiscString.trimRight(ar1[2-1]);
			ar1[11-1] = codPolo; 
			
			ar1[12-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[13-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[14-1] = ConvertConstants.MIGRATION_FLAG_CANC;
			} 	   	  	
		else
		{ // Informix
			ar1[11-1] = codPolo; 
			ar1[12-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[13-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[14-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
	} // End completaTb

	public void completaTraOrdiniBiblioteche(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
			{
      ar1[1-1] = MiscString.trimRight(ar1[1-1]);
      ar1[2-1] = MiscString.trimRight(ar1[2-1]);

			if (ar1[1-1].length() == 0)
				ar1[1-1] = MIGRATION_DATE; //ts_ins
			if (ar1[2-1].length() == 0)
				ar1[2-1] = MIGRATION_DATE; //ts_var

			ar1[8-1] = codPolo;
			ar1[9-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[10-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[11-1] = ConvertConstants.MIGRATION_FLAG_CANC;
			} 	   	  	
		else
		{ // Informix
	    	ar1[8-1] = codPolo;
			ar1[9-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[10-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[11-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
	} // End completaTb

	public void completaTraSezAcquisizioneFornitori(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
	 		{
      ar1[1-1] = MiscString.trimRight(ar1[1-1]);
      ar1[2-1] = MiscString.trimRight(ar1[2-1]);

			if (ar1[1-1].length() == 0)
				ar1[1-1] = MIGRATION_DATE; //ts_ins
			if (ar1[2-1].length() == 0)
				ar1[2-1] = MIGRATION_DATE; //ts_var

			ar1[6-1] = codPolo;
			ar1[7-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[8-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[9-1] = ConvertConstants.MIGRATION_FLAG_CANC;
	 		} 	   	  	
		else
		{ // Informix
			ar1[4-1] = ar1[4-1].substring(0, ar1[4-1].indexOf(".")); 
			ar1[5-1] = ar1[5-1].substring(0, ar1[5-1].indexOf(".")); 
			ar1[6-1] = codPolo;
			ar1[7-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[8-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[9-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
	} // End completaTb

	public void completaTbcInventario(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
			{
      ar1[1-1] = MiscString.trimRight(ar1[1-1]);
      ar1[3-1] = MiscString.trimRight(ar1[3-1]);
      ar1[2-1] = MiscString.trimRight(ar1[2-1]);
      ar1[4-1] = MiscString.trimRight(ar1[4-1]);

			
			if (ar1[1-1].length() == 0)
				ar1[1-1] = codPolo + "MIG" + "000000"; //ute_ins
			if (ar1[2-1].length() == 0)
				ar1[2-1] = MIGRATION_DATE; //ts_ins
			if (ar1[3-1].length() == 0)
				ar1[3-1] = codPolo + "MIG" + "000000"; //ute_var
			if (ar1[4-1].length() == 0)
				ar1[4-1] = MIGRATION_DATE; //ts_var


			ar1[8-1] = ar1[8-1].substring(0,9) + "." + ar1[8-1].substring(9);
			ar1[9-1] = ar1[9-1].substring(0,9) + "." + ar1[9-1].substring(9);
			
			
      ar1[11-1] = MiscString.trimRight(ar1[11-1]);
			if (ar1[11-1].length() == 0)
				ar1[11-1] = ConvertConstants.MIGRATION_NULL;

      ar1[12-1] = MiscString.trimRight(ar1[12-1]);
			if (ar1[12-1].length() == 0)
				ar1[12-1] = ConvertConstants.MIGRATION_NULL;

      ar1[16-1] = MiscString.trimRight(ar1[16-1]);
			if (ar1[16-1].length() == 0)
				ar1[16-1] = ConvertConstants.MIGRATION_NULL;

      ar1[28-1] = MiscString.trimRight(ar1[28-1]);
			if (ar1[28-1].length() == 0)
				ar1[28-1] = ConvertConstants.MIGRATION_NULL;
			
			
      ar1[31-1] = MiscString.trimRight(ar1[31-1]);
			if (ar1[31-1].length() == 0)
				ar1[31-1] = ConvertConstants.MIGRATION_NULL;

      ar1[39-1] = MiscString.trimRight(ar1[39-1]);
			if (ar1[39-1].length() == 0)
				ar1[39-1] = ConvertConstants.MIGRATION_NULL;
      ar1[42-1] = MiscString.trimRight(ar1[42-1]);
			if (ar1[42-1].length() == 0)
				ar1[42-1] = ConvertConstants.MIGRATION_NULL;
      ar1[43-1] = MiscString.trimRight(ar1[43-1]);
			if (ar1[43-1].length() == 0)
				ar1[43-1] = ConvertConstants.MIGRATION_NULL;

      ar1[14-1] = ar1[14-1].trim() + MiscString.trimRight(ar1[15-1]); // precis di inv
      ar1[44-1] = MiscString.trimRight(ar1[44-1]); // delibera
				
			ar1[51-1] = codPolo;
			ar1[52-1] = ConvertConstants.MIGRATION_NULL;
			ar1[53-1] = ConvertConstants.MIGRATION_NULL;
			ar1[54-1] = ConvertConstants.MIGRATION_NULL;
			ar1[55-1] = ConvertConstants.MIGRATION_NULL;
			ar1[56-1] = ConvertConstants.MIGRATION_NULL;
			ar1[57-1] = ConvertConstants.MIGRATION_NULL;
			ar1[58-1] = ConvertConstants.MIGRATION_NULL;
			ar1[59-1] = ConvertConstants.MIGRATION_NULL;
			ar1[60-1] = ConvertConstants.MIGRATION_NULL;
			ar1[61-1] = ConvertConstants.MIGRATION_NULL;
			ar1[62-1] = ConvertConstants.MIGRATION_NULL;
			ar1[63-1] = ConvertConstants.MIGRATION_NULL;
			ar1[64-1] = ConvertConstants.MIGRATION_NULL;
			ar1[65-1] = ConvertConstants.MIGRATION_NULL;
			ar1[66-1] = ar1[2-1]; // data_ingresso
			}
		else
		{ // Informix

			ar1[14-1] = ar1[14-1].substring(0, ar1[14-1].indexOf(".")); // Anno di pubblicazione non decimale 
			ar1[20-1] = ar1[20-1].substring(0, ar1[20-1].indexOf(".")); // Codice fornitore 
			ar1[26-1] = ar1[26-1].substring(0, ar1[26-1].indexOf(".")); // Anno ordine 
			ar1[29-1] = ar1[29-1].substring(0, ar1[29-1].indexOf(".")); // Anno fattura 
			if (ar1[37-1].compareTo("12/31/9999") == 0)  
				ar1[37-1] = "31/12/9999";
			if (ar1[40-1].compareTo("12/31/9999") == 0)  
				ar1[40-1] = "31/12/9999";
			if (ar1[41-1].compareTo("12/31/9999") == 0)  
				ar1[41-1] = "31/12/9999";

			ar1[49-1] = codPolo;
			ar1[50-1] = codPolo;
			ar1[51-1] = ar1[3]; // Codice di biblioteca di provenienza
			ar1[52-1] = "-1"; // riga ordine
			ar1[53-1] = ConvertConstants.MIGRATION_CD_SUPPORTO_NULL;
			ar1[54-1] = codPolo + "MIG" + "000000"; //ute_ins prima colloc
			ar1[55-1] = ar1[1-1];
			ar1[56-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[57-1] = codPolo + "MIG" + "000000"; //ute_var
		}
	} // End completaTb



	public void completaTrcFormatiSezioni(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
      ar1[1-1] = MiscString.trimRight(ar1[1-1]);
      ar1[2-1] = MiscString.trimRight(ar1[2-1]);

			if (ar1[1-1].length() == 0)
				ar1[1-1] = MIGRATION_DATE; //ts_ins
			if (ar1[2-1].length() == 0)
				ar1[2-1] = MIGRATION_DATE; //ts_var

			ar1[9-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[10-1] = codPolo + "MIG" + "000000"; //ute_var

			ar1[8-1] = codPolo;
			ar1[11-1] = ConvertConstants.MIGRATION_FLAG_CANC;
			ar1[12-1] = ConvertConstants.MIGRATION_NULL;
			ar1[13-1] = ConvertConstants.MIGRATION_N_PEZZI; 
			 
		}
		else
		{ // Informix
			ar1[3-1] = codPolo; 
			ar1[8-1] = ConvertConstants.MIGRATION_BIB; 
			ar1[9-1] = ConvertConstants.MIGRATION_DESCR_EMPTY;
			ar1[10-1] = ConvertConstants.MIGRATION_N_PEZZI;
			ar1[11-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[12-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[13-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
	} // End completaTb


	public void completaTrcPossProvInventari(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
      ar1[1-1] = MiscString.trimRight(ar1[1-1]);
      ar1[3-1] = MiscString.trimRight(ar1[3-1]);
      ar1[2-1] = MiscString.trimRight(ar1[2-1]);
      ar1[4-1] = MiscString.trimRight(ar1[4-1]);

			if (ar1[1-1].length() == 0)
				ar1[1-1] = codPolo + "MIG" + "000000"; //ute_ins
			if (ar1[2-1].length() == 0)
				ar1[2-1] = MIGRATION_DATE; //ts_ins
			if (ar1[3-1].length() == 0)
				ar1[3-1] = codPolo + "MIG" + "000000"; //ute_var
			if (ar1[4-1].length() == 0)
				ar1[4-1] = MIGRATION_DATE; //ts_var

      ar1[10-1] = MiscString.trimRight(ar1[10-1]);

			ar1[12-1] = codPolo;
		}
		else
		{ // Informix
			ar1[10-1] = codPolo; 
			ar1[11-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[12-1] = codPolo + "MIG" + "000000"; //ute_var
		}
	} // End completaTb




	public void completaTrcPossessoriPossessori(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
      ar1[1-1] = MiscString.trimRight(ar1[1-1]);
      ar1[3-1] = MiscString.trimRight(ar1[3-1]);
      ar1[2-1] = MiscString.trimRight(ar1[2-1]);
      ar1[4-1] = MiscString.trimRight(ar1[4-1]);

			if (ar1[1-1].length() == 0)
				ar1[1-1] = codPolo + "MIG" + "000000"; //ute_ins
			if (ar1[2-1].length() == 0)
				ar1[2-1] = MIGRATION_DATE; //ts_ins
			if (ar1[3-1].length() == 0)
				ar1[3-1] = codPolo + "MIG" + "000000"; //ute_var
			if (ar1[4-1].length() == 0)
				ar1[4-1] = MIGRATION_DATE; //ts_var

      ar1[8-1] = MiscString.trimRight(ar1[8-1]);

		}
		else
		{ // Informix
			ar1[8-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[9-1] = codPolo + "MIG" + "000000"; //ute_var
		}
	} // End completaTb

	
	public void completaTrsTerminiTitoliBiblioteche(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
   		{
			// TO DO
   		}
		else
		{ // Informix
   			ar1[6-1] = ConvertConstants.MIGRATION_CD_THESAURO; 
   			ar1[7-1] = codPolo; 
   			ar1[8-1] = codPolo + "MIG" + "000000"; //ute_ins
   			ar1[9-1] = codPolo + "MIG" + "000000"; //ute_ins
   			ar1[10-1] = MIGRATION_DATE; //ts_ins
   			ar1[11-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
	} // End completaTb
	
	
public void completaTraMessaggi(String ar1[])
	{
//		if (db_type == ConvertConstants.DB_BULL)
//		{
//		}
//		else
//		{ // Informix
   			ar1[16-1] = codPolo; 
   			ar1[17-1] = codPolo + "MIG" + "000000"; //ute_ins
   			ar1[18-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[19-1] = ConvertConstants.MIGRATION_FLAG_CANC;
//		}
	} // End completaTb
	


public void completaTbaCambiUfficiali(String ar1[])
	{
	if (db_type == ConvertConstants.DB_BULL)
	{
    ar1[1-1] = MiscString.trimRight(ar1[1-1]);
    ar1[2-1] = MiscString.trimRight(ar1[2-1]);

		if (ar1[1-1].length() == 0)
			ar1[1-1] = codPolo + "MIG" + "000000"; //ute_ins
		if (ar1[2-1].length() == 0)
			ar1[2-1] = MIGRATION_DATE; //ts_ins
		
		
    	ar1[7-1] = Integer.toString(sequenceIdValuta++);
		salvaSequence(ar1[7-1], ar1[3-1]+ar1[4-1]);
    	
    	
		ar1[8-1] = codPolo; 
		ar1[9-1] = codPolo + "MIG" + "000000"; //ute_ins
		ar1[10-1] = codPolo + "MIG" + "000000"; //ute_var
		ar1[11-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		
	}
	else
	{ // Informix
    	ar1[7-1] = Integer.toString(sequenceIdValuta++);
		ar1[8-1] = codPolo; 
		ar1[9-1] = codPolo + "MIG" + "000000"; //ute_ins
		ar1[10-1] = codPolo + "MIG" + "000000"; //ute_var
		ar1[11-1] = ConvertConstants.MIGRATION_FLAG_CANC;
	}
	} // End completaTb




	public void completaTbaFatture(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
			
		}
		else
		{ // Informix
			ar1[15-1] = ar1[15-1].substring(0, ar1[-1].indexOf(".")); 
	    	ar1[16-1] = Integer.toString(sequenceIdFattura++);
			ar1[17-1] = codPolo; 
			ar1[18-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[19-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[20-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
	} // End completaTb


public void completaTbaRigheFatture(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
			
		}
		else
		{ // Informix
   			ar1[4-1] = ConvertConstants.MIGRATION_ID_FATTURA_DUMMY; 
   			ar1[5-1] = codPolo;
   			ar1[11-1] =  ConvertConstants.MIGRATION_CAPITOLO_BILANCIO_DUMMY;
   			ar1[12-1] = codPolo + "MIG" + "000000"; //ute_ins
   			ar1[15-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[16-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
	} // End completaTb


public void completaTbaOfferteFornitore(String ar1[])
	{
	if (db_type == ConvertConstants.DB_BULL)
	{
		
	}
	else
	{ // Informix
    	ar1[67-1] = Integer.toString(sequenceIdOfferteFornitore++);
		ar1[68-1] = codPolo;
		ar1[69-1] = codPolo + "MIG" + "000000"; //ute_ins
		ar1[70-1] = codPolo + "MIG" + "000000"; //ute_var
		ar1[71-1] = ConvertConstants.MIGRATION_FLAG_CANC;
	}
	} // End completaTb

public void completaTraFornitoriOfferte(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
			
		}
		else
		{ // Informix
   			ar1[3-1] = ar1[3-1].substring(0, ar1[3-1].indexOf(".")); 
   			ar1[5-1] = ar1[5-1].substring(0, ar1[5-1].indexOf(".")); 
   			ar1[11-1] = codPolo;
   			ar1[12-1] = codPolo + "MIG" + "000000"; //ute_ins
   			ar1[13-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[14-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
	} // End completaTb

public void completaTblMaterie(String ar1[])
{
	if (db_type == ConvertConstants.DB_BULL)
	{
		
	}
	else
	{ // Informix
	}
} // End completaTb

public void completaTblOccupazioni(String ar1[])
{
	if (db_type == ConvertConstants.DB_BULL)
	{
		
	}
	else
	{ // Informix
	}
} // End completaTb

public void completaTblSpecificitaTitoliStudio(String ar1[])
{
	if (db_type == ConvertConstants.DB_BULL)
	{
		
	}
	else
	{ // Informix
	}
} // End completaTb





public void completaTbfAnagrafeUtentiProfessionali(String ar1[])
{
	if (db_type == ConvertConstants.DB_BULL)
	{
    ar1[1-1] = MiscString.trimRight(ar1[1-1]);
    ar1[2-1] = MiscString.trimRight(ar1[2-1]);
		if (ar1[1-1].length() == 0)
			ar1[1-1] = MIGRATION_DATE; //ts_ins
		if (ar1[2-1].length() == 0)
			ar1[2-1] = MIGRATION_DATE; //ts_var

		String key = ar1[4-1];
		ar1[16-1] = Integer.toString(sequenceIdUtenteProfessionale++);
		salvaSequence(ar1[16-1], ar1[3-1]+key);
		
		
		
		//ar1[15-1] = ConvertConstants.MIGRATION_EMPTY; // non puo' essere null
		
		
		ar1[12-1] = codPolo + "MIG" + "000000"; //ute_ins
		ar1[13-1] = codPolo + "MIG" + "000000"; //ute_var
		ar1[14-1] = ConvertConstants.MIGRATION_FL_CANC;
		
//		if (ar1[5-1].length() > 19)
// 			ar1[15-1] = ar1[5-1].substring(1,19);
//		else 
//			ar1[15-1] = ar1[5-1];

    //ar1[5-1] = MiscString.trimRight(ar1[5-1]); // secondo nome e cognome
    ar1[5-1] = MiscString.trimRight(ar1[5-1]); // secondo nome e cognome
		
		int idx = ar1[5-1].lastIndexOf(" "); 
		if (idx == -1)
		{
			ar1[15-1] = ar1[5-1]; // Nome
			ar1[5-1] = ConvertConstants.MIGRATION_EMPTY;
		}
		else
		{
			ar1[15-1] = ar1[5-1].substring(idx+1); // Cognome
			ar1[5-1] = ar1[5-1].substring(0, idx); // Nome
		}
	}
	else
	{ // Informix
	}
} // End completaTbfAnagrafeUtentiProfessionali
	
	

public void completaTbfUtentiProfessionaliWeb(String ar1[])
{
	ar1[12-1] = MiscString.trimRight(ar1[12-1]);
	    
	ar1[13-1] = getSequenceIdUtenteProfessionale(ar1[3-1]+ar1[4-1]); // cod_bib + cod_bibliotecario 

	ar1[14-1] = ar1[12-1]; // user id

	passwordEncrypter = new PasswordEncrypter(ar1[12-1]);
	ar1[15-1] = passwordEncrypter.encrypt(ar1[12-1]);
	ar1[16-1] = codPolo + "MIG" + "000000"; //ute_ins
	ar1[17-1] = MIGRATION_DATE; //ts_ins
	ar1[18-1] = codPolo + "MIG" + "000000"; //ute_var
	ar1[19-1] = MIGRATION_DATE; //ts_var
	ar1[20-1] = ConvertConstants.MIGRATION_FLAG_CANC;
	ar1[21-1] = ConvertConstants.MIGRATION_CHANGE_PASSWORD_SI; 
	ar1[22-1] = MIGRATION_DATE; //last_access

}



public void completaTrfUtenteProfessionaleBiblioteca(String ar1[])
{
	ar1[4-1] = getSequenceIdUtenteProfessionale(ar1[3-1]+ar1[4-1]); // cod_bib + cod_bibliotecario 
	ar1[12-1] = codPolo;
	ar1[13-1] = ConvertConstants.MIGRATION_NULL;
    ar1[7-1] = MiscString.trimRight(ar1[7-1]);
    ar1[6-1] = MiscString.trimRight(ar1[6-1]);
} // End completaTb

String getSequenceIdUtenteProfessionale(String codiceBiblioetcario)
{
	String sequence;
	sequence = loadSequence1.getSequence(codiceBiblioetcario);
	if (sequence == null)
		return "";
	return sequence.substring(0,7);
}

	public void completaTbLuogo(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
			ar1[4-1] = ConvertConstants.MIGRATION_TP_FORMA_DES;
			ar1[5-1] = ConvertConstants.MIGRATION_CD_LIVELLO;
			
			ar1[6-1] = ar1[3-1];
			ar1[7-1] = ConvertConstants.MIGRATION_NULL; // cd_paese
			ar1[8-1] = ConvertConstants.MIGRATION_NULL; // nota_luogo
			ar1[9-1] = codPolo + "MIG" + "000000"; //ute_ins
   			ar1[10-1] = MIGRATION_DATE; //ts_ins
   			ar1[11-1] = codPolo + "MIG" + "000000"; //ute_var
   			ar1[12-1] = MIGRATION_DATE; //ts_var
			ar1[13-1] = ConvertConstants.MIGRATION_FLAG_CANC;
			
		}
		else
		{ // Informix
			ar1[10-1] = MIGRATION_DATE; //ts_ins
			ar1[12-1] = MIGRATION_DATE; //ts_var
		}
	} // End completaTb

	public void completaTbRepertorio(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
			
		}
		else
		{ // Informix
   			ar1[6-1] = MIGRATION_DATE; //ts_ins
   			ar1[8-1] = MIGRATION_DATE; //ts_var
   			ar1[10-1] = ""; //tidx_vector
		}
	} // End completaTb

	public void completaTrRepMar(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
			
		}
		else
		{ // Informix
   			ar1[2-1] = (String)mappaCodiciRepertorio.get(ar1[2-1]);
   			ar1[5-1] = "NULL";
   			ar1[6-1] = codPolo + "MIG" + "000000"; //ute_ins
   			ar1[7-1] = MIGRATION_DATE; //ts_ins
   			ar1[8-1] = codPolo + "MIG" + "000000"; //ute_var
   			ar1[9-1] = MIGRATION_DATE; //ts_var
		}
	} // End completaTb

	public void completaTbcEsemplare(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
		}
		else
		{ // Informix
	   			ar1[10-1] = codPolo + ar1[3-1] + "000000"; //ute_ins
 	   			ar1[11-1] = codPolo + ar1[3-1] + "000000"; //ute_var
		}
	} // End completaTb

	public void completaTrfBibliotecheFunzioni(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
		}
		else
		{ // Informix
   			ar1[4-1] = codPolo + "MIG" + "000000"; //ute_ins
   			ar1[5-1] = MIGRATION_DATE; //ts_ins
   			ar1[6-1] = codPolo + "MIG" + "000000"; //ute_var
   			ar1[7-1] = MIGRATION_DATE; //ts_var
   			ar1[8-1] = " ";
		}
	} // End completaTb

	public void completaTrfBibliotecariFunzioni(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
		}
		else
		{ // Informix
			ar1[9-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[10-1] = codPolo + "MIG" + "000000"; //ute_var
			if (ar1[8-1].equals(" "))
	   			ar1[8-1] = MIGRATION_DATE; //ts_var
		}
	} // End completaTb
	public void completaTbfBibliotecario(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
		}
		else
		{ // Informix
	   			ar1[1-1] = ConvertConstants.MIGRATION_ID_PARAMETRO_BIBLIOTECARIO; //id_parametro
 	   			ar1[2-1] = codPolo + "MIG" + "000000"; //ute_ins
 	   			ar1[3-1] = codPolo + "MIG" + "000000"; //ute_ins
				ar1[4-1] = ar1[4-1].substring(0, ar1[4-1].indexOf(".")); 
 	   			ar1[16-1] = ConvertConstants.MIGRATION_ID_PROFILO_ABILITAZIOEN; //id_parametro
		}
	} // End completaTb

	public void completaTbfBibliotecaInPolo(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
		}
		else
		{ // Informix
	 	   	 	   			
   			ar1[3-1] = "1"; // id_parametro 
   			ar1[4-1] = "1"; // id_gruppo_attivita_polo 
   			ar1[5-1] = "1"; // id_biblioetca 
   			ar1[6-1] = "1"; // ky_biblioetca 
   			
   			ar1[7-1] = "cdAnag"; 
   			ar1[8-1] = "desc bib"; 
   			ar1[9-1] = "desc citt"; 

   			ar1[10-1] = codPolo + "MIG" + "000000"; //ute_ins
   			ar1[11-1] = MIGRATION_DATE; //ts_ins
   			ar1[12-1] = codPolo + "MIG" + "000000"; //ute_var
   			ar1[13-1] = MIGRATION_DATE; //ts_var
   			ar1[14-1] = " "; 
		}
	} // End completaTb

	public void completaTrfProfiliAbilFunzioni(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
		}
		else
		{ // Informix
   			ar1[8-1] = ConvertConstants.MIGRATION_ID_FUNZIONE_TEMP; 
   			ar1[9-1] = codPolo + "MIG" + "000000"; //ute_ins
   			ar1[10-1] = codPolo + "MIG" + "000000"; //ute_var
		}
	} // End completaTb
	
	public void completaTrsClassiTitoli(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
		}
		else
		{ // Informix
   			ar1[9-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
	} // End completaTb
	public void completaTrsClassiParole(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
		}
		else
		{ // Informix
   			ar1[6-1] = ar1[1-1]; // ute_ins
   			ar1[7-1] = ar1[2-1]; // ts_ins
   			ar1[8-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
	} // End completaTb
	public void completaTrsDescrittoriDescrittori(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
		}
		else
		{ // Informix
   			ar1[9-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
	} // End completaTb
	public void completaTrsDescrittoriParole(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
		}
		else
		{ // Informix
			ar1[6-1] = ar1[1-1]; // ute_ins
			ar1[7-1] = ar1[2-1]; // ts_ins
			ar1[8-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
	} // End completaTb
	public void completaTrsDescrittoriSoggetti(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
		}
		else
		{ // Informix
   			ar1[6-1] = ar1[1-1]; // ute_ins
   			ar1[7-1] = ar1[2-1]; // ts_ins
   			ar1[8-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
	} // End completaTb
	public void completaTrsSistemiClassiBiblioteche(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
		}
		else
		{ // Informix
   			ar1[4-1] = codPolo + "MIG" + "000000"; //ute_ins
   			ar1[5-1] = MIGRATION_DATE; //ts_ins
   			ar1[6-1] = ConvertConstants.MIGRATION_SOLO_LOCALE_NO;
   			ar1[7-1] = codPolo + "MIG" + "000000"; //ute_var
   			ar1[8-1] = MIGRATION_DATE; //ts_var

   			ar1[9-1] = ConvertConstants.MIGRATION_CD_SISTEMA_CLASSIFICAZIONE;
   			ar1[10-1] = ConvertConstants.MIGRATION_CD_EDIZIONE_SISTEMA_CLASSIFICAZIONE;
   			
   			ar1[11-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
	} // End completaTb
	public void completaTrTitLuo(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
			ar1[3-1] = ConvertConstants.MIGRATION_TIPO_LUOGO; 
			ar1[4-1] = "NULL"; //nota
			ar1[5-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[6-1] = MIGRATION_DATE; //ts_ins
			ar1[7-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[8-1] = MIGRATION_DATE; //ts_var
			ar1[9-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
		else
		{ // Informix
			String lid = (String)fieldValue2IdMapping.get(ar1[2-1] ); // Map luogo in LID
			if (lid == null)
			{
	   			System.out.println ("LID no trovato per luogo: " + ar1[2-1] + "(BID: " + ar1[1-1] + ")");
	   			ar1[2-1] = "";
			}
			else 
				ar1[2-1] = lid;
			ar1[3-1] = ConvertConstants.MIGRATION_TIPO_LUOGO; 
			ar1[4-1] = "NULL"; //nota
			ar1[5-1] = codPolo + "MIG" + "000000"; //ute_ins
			ar1[6-1] = MIGRATION_DATE; //ts_ins
			ar1[7-1] = codPolo + "MIG" + "000000"; //ute_var
			ar1[8-1] = MIGRATION_DATE; //ts_var
			ar1[9-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
	} // End completaTb
	public void completaTrSoggettariBiblioteche(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
		}
		else
		{ // Informix
   			ar1[3-1] = codPolo;   
   			ar1[4-1] = ConvertConstants.MIGRATION_FL_ATT;  
   			ar1[5-1] = ConvertConstants.MIGRATION_FL_AUTLO_LOC;  
   			ar1[6-1] = codPolo + "MIG" + "000000"; //ute_ins
   			ar1[7-1] = MIGRATION_DATE; //ts_ins
   			ar1[8-1] = codPolo + "MIG" + "000000"; //ute_var
   			ar1[9-1] = MIGRATION_DATE; //ts_var
   			ar1[10-1] = ConvertConstants.MIGRATION_FL_CANC;
   			ar1[11-1] = ConvertConstants.MIGRATION_SOLO_LOCALE_NO;
		}
	} // End completaTb

	public void completaTrTitBib(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
			ar1[51-1] = codPolo;

   			ar1[52-1] = ConvertConstants.MIGRATION_FL_GESTIONE_SI;
   			ar1[61-1] = ConvertConstants.MIGRATION_FL_POSSESSO_SI;

   			if (ar1[25-1].charAt(0) == '1')
	   			ar1[61-1] = ConvertConstants.MIGRATION_FL_POSSESSO_NO;
   			
			ar1[53-1] = ConvertConstants.MIGRATION_FL_ALLINEA_NO;
			ar1[54-1] = ConvertConstants.MIGRATION_FL_ALLINEA_NO;
			ar1[55-1] = ConvertConstants.MIGRATION_FL_ALLINEA_NO;
			ar1[56-1] = ConvertConstants.MIGRATION_FL_ALLINEA_NO;
			ar1[57-1] = ConvertConstants.MIGRATION_FL_ALLINEA_NO;
			ar1[58-1] = ConvertConstants.MIGRATION_FL_ALLINEA_NO;
			ar1[59-1] = ConvertConstants.MIGRATION_FL_MUTILO_NO;
			ar1[60-1] = ConvertConstants.MIGRATION_NULL;
   			ar1[62-1] = ConvertConstants.MIGRATION_NULL;
   			ar1[63-1] = ConvertConstants.MIGRATION_NULL;
   			ar1[64-1] = ConvertConstants.MIGRATION_NULL;
   			ar1[65-1] = ConvertConstants.MIGRATION_NULL;
   			ar1[66-1] = ConvertConstants.MIGRATION_NULL;
   			ar1[67-1] = ConvertConstants.MIGRATION_NULL;
   			ar1[68-1] = ConvertConstants.MIGRATION_NULL;
			
   			ar1[69-1] = codPolo + "MIG" + "000000"; //ute_ins
   			ar1[70-1] = MIGRATION_DATE; //ts_ins
   			ar1[71-1] = codPolo + "MIG" + "000000"; //ute_var
   			ar1[72-1] = MIGRATION_DATE; //ts_var
			
		}
		else
		{ // Informix
   			ar1[10-1] = ConvertConstants.MIGRATION_FL_GESTIONE; // 
   			ar1[11-1] = ConvertConstants.MIGRATION_FL_DISP_ELETTR;
   			ar1[12-1] = ConvertConstants.MIGRATION_ALLINEA_SBNMARC;
   			ar1[13-1] = ConvertConstants.MIGRATION_ALLINEA_CLA; 
   			ar1[14-1] = ConvertConstants.MIGRATION_ALLINEA_LUO;
   			ar1[15-1] = ConvertConstants.MIGRATION_ALLINEA_SOG;
   			ar1[16-1] = ConvertConstants.MIGRATION_ALLINEA_REP;
   			ar1[17-1] = "NULL";
   			ar1[18-1] = "NULL";
   			ar1[19-1] = "NULL";
   			ar1[20-1] = "NULL";
   			ar1[21-1] = "NULL";
   			ar1[22-1] = "NULL";
   			ar1[23-1] = codPolo + "MIG" + "000000"; //ute_ins
   			ar1[24-1] = MIGRATION_DATE; //ts_ins
   			ar1[25-1] = codPolo + "MIG" + "000000"; //ute_var
   			ar1[26-1] = MIGRATION_DATE; //ts_var
		}
	} // End completaTb

	public void completaTrAutBib(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
		}
		else
		{ // Informix
   			ar1[6-1] = ConvertConstants.MIGRATION_FL_ALLINEA_SBNMARC;
   			ar1[7-1] = codPolo + "MIG" + "000000"; //ute_ins
   			ar1[8-1] = MIGRATION_DATE; //ts_ins
   			ar1[9-1] = codPolo + "MIG" + "000000"; //ute_var
   			ar1[10-1] = MIGRATION_DATE; //ts_var
		}
	} // End completaTb
	
	public void completaTrMarBib(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
		}
		else
		{ // Informix
   			ar1[6-1] = ConvertConstants.MIGRATION_FL_ALLINEA_SBNMARC;
   			ar1[7-1] = codPolo + "MIG" + "000000"; //ute_ins
   			ar1[8-1] = MIGRATION_DATE; //ts_ins
   			ar1[9-1] = codPolo + "MIG" + "000000"; //ute_var
   			ar1[10-1] = MIGRATION_DATE; //ts_var
		}
	} // End completaTb
	public void completaTrTerminiTermini(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
		}
		else
		{ // Informix
   			ar1[9-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
	} // End completaTb

	public void completaTrThesauriBiblioteche(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
		}
		else
		{ // Informix
   			ar1[4-1] = codPolo;
   			ar1[5-1] = ConvertConstants.MIGRATION_FLG_AUTO_LOC;
   			ar1[6-1] = codPolo + "MIG" + "000000"; //ute_ins
   			ar1[7-1] = MIGRATION_DATE; //ts_ins
   			ar1[8-1] = codPolo + "MIG" + "000000"; //ute_var
   			ar1[9-1] = MIGRATION_DATE; //ts_var
   			ar1[10-1] = ConvertConstants.MIGRATION_FLAG_CANC;
   			ar1[11-1] = ConvertConstants.MIGRATION_SOLO_LOCALE_NO;
		}
	} // End completaTb
	public void completaTbcDefaultInvenSchede(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
		}
		else
		{ // Informix
 	   			ar1[45-1] = Integer.toString(sequenceIdInvenSchede++);
 	   			ar1[46-1] = codPolo;
 	   			ar1[47-1] = ConvertConstants.MIGRATION_INVENTARIO_TIPO;
 	   			ar1[48-1] = codPolo + "MIG" + "000000"; //ute_ins
 	   			ar1[49-1] = MIGRATION_DATE; //ts_ins
 	   			ar1[50-1] = codPolo + "MIG" + "000000"; //ute_var
 	   			ar1[51-1] = MIGRATION_DATE; //ts_var
	   			ar1[52-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
	} // End completaTb
	public void completaTbaBuonoOrdine(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
		}
		else
		{ // Informix
        	ar1[8-1] = Integer.toString(sequenceIdBuonoOrdine++);
   			ar1[9-1] = ConvertConstants.MIGRATION_CAPITOLO_BILANCIO_DUMMY; 
   			ar1[7-1] = ar1[7-1].substring(0, ar1[7-1].indexOf(".")); 
   			ar1[12-1] = codPolo + "MIG" + "000000"; //ute_ins
   			ar1[13-1] = codPolo + "MIG" + "000000"; //ute_var
   			ar1[14-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
	} // End completaTb

	public void completaTraElementiBuonoOrdine(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
		}
		else
		{ // Informix
   			ar1[5-1] = ar1[5-1].substring(0, ar1[5-1].indexOf(".")); 
   			ar1[7-1] = codPolo + "MIG" + "000000"; //ute_ins
   			ar1[8-1] = MIGRATION_DATE; //ts_ins
   			ar1[9-1] = codPolo + "MIG" + "000000"; //ute_var
   			ar1[10-1] = MIGRATION_DATE; //ts_var
   			ar1[11-1] = ConvertConstants.MIGRATION_FLAG_CANC;
		}
	} // End completaTb
	public void completaTbfProfiloAbilitazione(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
		}
		else
		{ // Informix
   			ar1[5-1] = codPolo + "MIG" + "000000"; //ute_ins
   			ar1[6-1] = MIGRATION_DATE; //ts_ins
   			ar1[7-1] = codPolo + "MIG" + "000000"; //ute_var
   			ar1[8-1] = MIGRATION_DATE; //ts_var
   			ar1[9-1] = " ";
   			ar1[10-1] = codPolo;
   			ar1[11-1] = "1"; // id_profilo_abilitazione
		}
	} // End completaTb

	
	
	
	
	/** 
	 * questo tipo di normalizzazione elimina i doppi spazi e i simboli di punteggiatura
	 */
    public static String normalizzazioneGenerica(String nome) { //throws EccezioneSbnDiagnostico
        if (nome==null)
            return "";
        char c;
        for (int i = 0; i < separatoriC.length; i++) {
            c = separatoriC[i];
            nome = nome.replace(c, spazioC);
        }
        for (int i = 0; i < nome.length(); i++) {
            c = nome.charAt(i);
            if ((c != spazioC) && !Character.isLetter(c) && !Character.isDigit(c)) {
                nome = nome.substring(0, i) + nome.substring(i + 1);
            }
        }
        int n;
        nome = ConvertCaratteriSpeciali.convert(nome);
        while ((n = nome.indexOf(spazio + spazio)) > 0)
            nome = nome.substring(0, n) + nome.substring(n + 1);
      
      return nome.toUpperCase();
    }	
	
	
	/*
	public void completaTb(String ar1[])
	{
		if (db_type == ConvertConstants.DB_BULL)
		{
		}
		else
		{ // Informix
		}
	} // End completaTb
*/
	
/*****
    private void salvaSequence (String aSequence, String aKey)
    {

    	String squenceOut = MiscString.paddingString(aSequence, 7, '0', true) + MiscString.paddingString(aKey, sequenceKeyLength, ' ', false) + '\n'; 
    	try {
    		
    		sequenceOut.write(squenceOut);
    		
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}     
    }

    public void apriSequenceFile(String sequenceFilename, int aSequenceKeyLength)
    {
    	this.sequenceFilename  = sequenceFilename;
    	this.sequenceKeyLength = aSequenceKeyLength;
    	
        try {
    		sequenceOut = new BufferedWriter(new FileWriter(this.sequenceFilename));
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}

    	
    }
    public void chiudiSequenceFile()
    {
    	if (sequenceOut != null)
    		try {
    			sequenceOut.close();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    }
*******/

	
	
	
	
	
}
