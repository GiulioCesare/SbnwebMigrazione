package it.finsiel.migrazione;

public class ConvertConstants {
    public static final int 	MAX_BYTES_PER_UTF8_CHARACTER = 4;
    public static final boolean LEFT_PADDING = true;
    public static final boolean RIGHT_PADDING = false;

    public static final String MIGRATION_BIB = " CR"; // FI
    //public static final String MIGRATION_DATE = "20081101";
//    public static final String MIGRATION_DATE_LOWER = "20081101";
    public static final String MIGRATION_NUMERO_LASRTA = "1234567890";
    public static final String MIGRATION_CD_RESPONSABILITA = "070";
	public static final String MIGRATION_ID_BIBLIOTECA_TEMP  = "999";
	public static final String MIGRATION_ID_FUNZIONE_TEMP  = "999";
	public static final String MIGRATION_CD_ANA_BIBLIOTECA_TEMP  = "999";
	public static final String MIGRATION_SEQUENCE = "DEFAULT";
	public static final String MIGRATION_FLAG_CANC = "N";
	public static final String MIGRATION_FL_CANC = "N";
	public static final String MIGRATION_KEY_PRIMO_DESCRITTORE = "1";
	public static final String MIGRATION_CD_SOGGETTARIO = "FIR";
	public static final String MIGRATION_CD_SOGGETTO = "0";
	public static final String MIGRATION_CD_THESAURO = "1";
	public static final String MIGRATION_FLG_AUTO_LOC = "";
	public static final String MIGRATION_TIPO_LUOGO = "P";  // pubblicazione
	
	public static final String MIGRATION_FL_GESTIONE = "N";
	public static final String MIGRATION_FL_DISP_ELETTR ="N";
	public static final String MIGRATION_ALLINEA_SBNMARC ="N"; 
	public static final String MIGRATION_ALLINEA_CLA ="N";
	public static final String MIGRATION_ALLINEA_LUO ="N";
	public static final String MIGRATION_ALLINEA_SOG ="N";
	public static final String MIGRATION_ALLINEA_REP ="N";
	public static final String MIGRATION_FL_ALLINEA_SBNMARC = "N"; 
	public static final String MIGRATION_UTE_CONDIVISO_NULL = "NULL";
	public static final String MIGRATION_UTE_CONDIVISO = "UTE_MIG";
	public static final String MIGRATION_TS_CONDIVISO_NULL = "NULL";
	
	public static final String MIGRATION_FL_CONDIVISO= "s";
	public static final String MIGRATION_FL_NON_CONDIVISO= "n";
	public static final String MIGRATION_FL_CONDIVISO_SI= "s";

	public static final String MIGRATION_FL_INCERTO_SI= "S";
	public static final String MIGRATION_FL_SUPERFLUO_SI= "S";
	public static final String MIGRATION_FL_INCERTO_NO= "N";
	public static final String MIGRATION_FL_SUPERFLUO_NO= "N";
	
	public static final String MIGRATION_TP_FORMA_DES = "A";
	public static final String MIGRATION_TP_FORMA_THE = "A";
	public static final String MIGRATION_CD_LIVELLO = "71";
	public static final String MIGRATION_TIDX_VECTOR = "NULL";
	public static final String MIGRATION_NULL = "NULL";
	public static final String MIGRATION_FL_CARATTERI_SPECIALI_SI = "S";
	public static final String MIGRATION_FL_CARATTERI_SPECIALI_NO = "N";
	public static final String MIGRATION_EDIZIONE_CLASSE = "21";
	public static final String MIGRATION_CLASSE_NULL = "NULL";
	public static final String MIGRATION_FL_PRIMA_VOCE_NO = "N";
	public static final String MIGRATION_FL_PRIMA_VOCE_SI = "S";
	public static final String MIGRATION_SOLO_LOCALE_SI = "S";
	public static final String MIGRATION_SOLO_LOCALE_NO = "N";
	public static final String MIGRATION_CD_SISTEMA_CLASSIFICAZIONE = "D";
	public static final String MIGRATION_CD_SISTEMA_CLASSIFICAZIONE_NULL = "NULL";
	public static final String MIGRATION_CD_EDIZIONE_SISTEMA_CLASSIFICAZIONE = "21";
	public static final String MIGRATION_CD_EDIZIONE_SISTEMA_CLASSIFICAZIONE_NULL = "NULL";
	public static final String MIGRATION_CLASSIFICAZIONE_NULL = "NULL";
	public static final String MIGRATION_FL_ATT= "";
	public static final String MIGRATION_FL_AUTLO_LOC= "";
	public static final String MIGRATION_ORD_SPEC_NULL = "NULL";
	public static final String MIGRATION_ORD_SPEC_EMPTY = "";
	public static final String MIGRATION_INVENTARIO_TIPO = "??";
	public static final String MIGRATION_CD_SUPPORTO_NULL = "NULL";
	public static final String MIGRATION_DESCR_EMPTY = "";
	public static final String MIGRATION_N_PEZZI = "0";
	public static final String MIGRATION_BUDGET = "0";
	public static final String MIGRATION_ANNO_VAL = "2008";
	
	public static final String MIGRATION_CD_BIB_SEZ = MIGRATION_BIB;
	public static final String MIGRATION_ID_SEZ_ACQUISIZIONE = "0";
	public static final String MIGRATION_CAPITOLO_BILANCIO_DUMMY = "-1";
	public static final String MIGRATION_STAMPATO_NO = "0";
	public static final String MIGRATION_RINNOVATO_NO = "0";
	
	public static final String MIGRATION_ID_SEZ_ACQUISIZIONE_DUMMY = "-1";
	public static final String MIGRATION_VALUTA_DUMMY = "-1";
	public static final String MIGRATION_VALUTA_EUR = "EUR";
	
	public static final String MIGRATION_CAPITOLO_DUMMY = "-1";
	public static final String MIGRATION_ID_FATTURA_DUMMY = "-1";
	public static final String MIGRATION_ID_PARAMETRO_BIBLIOTECARIO = "1";
	public static final String MIGRATION_ID_PROFILO_ABILITAZIOEN = "1";

	public static final String MIGRATION_FL_COSTRUITO = "N";
	public static final String MIGRATION_FL_SPECIALE = "S"; 
	public static final String MIGRATION_SUFFISSO = "suffisso";
	public static final String MIGRATION_CD_RELAZIONE = "  ";	
	public static final String MIGRATION_KY_CLASSE_ORD = "";
	public static final String MIGRATION_KY_NORMA_DES = "";	
	public static final String MIGRATION_KY_CLES1_S = "";
	public static final String MIGRATION_KY_CLES2_S = "";
	public static final String MIGRATION_KY_PRIMO_DES = "";
	public static final String MIGRATION_KY_TEMINE_THESAURO = "";
	public static final String MIGRATION_FL_POSIZIONE = "0";
	public static final String MIGRATION_CD_STRUMENTO_MUSICALE_NULL = "NULL";
	public static final String MIGRATION_CD_VALUTA = "0";
	public static final String MIGRATION_EMPTY = "";
	public static final String MIGRATION_FL_POSSESSO_SI = "S";
	public static final String MIGRATION_FL_POSSESSO_NO = "N";
	public static final String MIGRATION_FL_GESTIONE_SI = "S";
	public static final String MIGRATION_FL_GESTIONE_NO = "N";
	public static final String MIGRATION_FL_ALLINEA_SI ="S";
	public static final String MIGRATION_FL_ALLINEA_NO = "N";
	public static final String MIGRATION_FL_MUTILO_NO ="N";
	
	public static final int CHARACTER_SEPARATOR = 1;
	public static final int STRING_SEPARATOR = 2;

	public static final int DB_INFORMIX = 1;
	public static final int DB_BULL = 2;

	public static final String MIGRATION_CENTRO_SISTEMA = "C";
	public static final String MIGRATION_CENTRO_AFFILIATA = "A";
	public static final String MIGRATION_NON_SBN = "N";

	public static final String MIGRATION_CHANGE_PASSWORD_SI = "S";
	
	
}
