//package it.finsiel.sbn.polo.factoring.base;

import it.finsiel.sbn.polo.oggetti.Autore;
import it.finsiel.sbn.polo.oggetti.Titolo;
import it.finsiel.sbn.exception.InfrastructureException;
//import it.finsiel.sbn.polo.dao.entity.tavole.Tbf_biblioteca_in_poloResult;
import it.finsiel.sbn.polo.exception.EccezioneDB;
import it.finsiel.sbn.polo.exception.EccezioneSbnDiagnostico;
import it.finsiel.sbn.polo.factoring.base.TipiAutore;
//import it.finsiel.sbn.polo.factoring.transactionmaker.EsportaDocumentiFactoring;
import it.finsiel.sbn.polo.model.unimarcmodel.A015;
import it.finsiel.sbn.polo.model.unimarcmodel.A100;
import it.finsiel.sbn.polo.model.unimarcmodel.A152;
import it.finsiel.sbn.polo.model.unimarcmodel.A200;
import it.finsiel.sbn.polo.model.unimarcmodel.A210;
import it.finsiel.sbn.polo.model.unimarcmodel.A210_GType;
import it.finsiel.sbn.polo.model.unimarcmodel.A230;
import it.finsiel.sbn.polo.model.unimarcmodel.A250;
import it.finsiel.sbn.polo.model.unimarcmodel.A260;
import it.finsiel.sbn.polo.model.unimarcmodel.A300;
import it.finsiel.sbn.polo.model.unimarcmodel.A676;
import it.finsiel.sbn.polo.model.unimarcmodel.A686;
import it.finsiel.sbn.polo.model.unimarcmodel.A801;
import it.finsiel.sbn.polo.model.unimarcmodel.A830;
import it.finsiel.sbn.polo.model.unimarcmodel.A921;
import it.finsiel.sbn.polo.model.unimarcmodel.A928;
import it.finsiel.sbn.polo.model.unimarcmodel.A929;
import it.finsiel.sbn.polo.model.unimarcmodel.A930;
import it.finsiel.sbn.polo.model.unimarcmodel.A931;
import it.finsiel.sbn.polo.model.unimarcmodel.Ac_210Type;
import it.finsiel.sbn.polo.model.unimarcmodel.AnticoType;
import it.finsiel.sbn.polo.model.unimarcmodel.ArrivoLegame;
import it.finsiel.sbn.polo.model.unimarcmodel.AutorePersonaleType;
import it.finsiel.sbn.polo.model.unimarcmodel.C012;
import it.finsiel.sbn.polo.model.unimarcmodel.C100;
import it.finsiel.sbn.polo.model.unimarcmodel.C101;
import it.finsiel.sbn.polo.model.unimarcmodel.C102;
import it.finsiel.sbn.polo.model.unimarcmodel.C105;
import it.finsiel.sbn.polo.model.unimarcmodel.C110;
import it.finsiel.sbn.polo.model.unimarcmodel.C116;
import it.finsiel.sbn.polo.model.unimarcmodel.C120;
import it.finsiel.sbn.polo.model.unimarcmodel.C121;
import it.finsiel.sbn.polo.model.unimarcmodel.C123;
import it.finsiel.sbn.polo.model.unimarcmodel.C124;
import it.finsiel.sbn.polo.model.unimarcmodel.C125;
import it.finsiel.sbn.polo.model.unimarcmodel.C128;
import it.finsiel.sbn.polo.model.unimarcmodel.C140;
import it.finsiel.sbn.polo.model.unimarcmodel.C200;
import it.finsiel.sbn.polo.model.unimarcmodel.C205;
import it.finsiel.sbn.polo.model.unimarcmodel.C206;
import it.finsiel.sbn.polo.model.unimarcmodel.C207;
import it.finsiel.sbn.polo.model.unimarcmodel.C208;
import it.finsiel.sbn.polo.model.unimarcmodel.C210;
import it.finsiel.sbn.polo.model.unimarcmodel.C215;
import it.finsiel.sbn.polo.model.unimarcmodel.C230;
import it.finsiel.sbn.polo.model.unimarcmodel.C3XX;
import it.finsiel.sbn.polo.model.unimarcmodel.C801;
import it.finsiel.sbn.polo.model.unimarcmodel.C856;
import it.finsiel.sbn.polo.model.unimarcmodel.C922;
import it.finsiel.sbn.polo.model.unimarcmodel.C923;
import it.finsiel.sbn.polo.model.unimarcmodel.C926;
import it.finsiel.sbn.polo.model.unimarcmodel.C927;
import it.finsiel.sbn.polo.model.unimarcmodel.C950;
import it.finsiel.sbn.polo.model.unimarcmodel.CartograficoType;
import it.finsiel.sbn.polo.model.unimarcmodel.Cf_200Type;
import it.finsiel.sbn.polo.model.unimarcmodel.ClasseType;
import it.finsiel.sbn.polo.model.unimarcmodel.Collocazione950;
import it.finsiel.sbn.polo.model.unimarcmodel.DatiDocType;
import it.finsiel.sbn.polo.model.unimarcmodel.DatiElementoType;
import it.finsiel.sbn.polo.model.unimarcmodel.DescrittoreType;
import it.finsiel.sbn.polo.model.unimarcmodel.DocumentoType;
import it.finsiel.sbn.polo.model.unimarcmodel.DocumentoTypeChoice;
import it.finsiel.sbn.polo.model.unimarcmodel.ElementAutType;
import it.finsiel.sbn.polo.model.unimarcmodel.EnteType;
import it.finsiel.sbn.polo.model.unimarcmodel.Esemplare950;
import it.finsiel.sbn.polo.model.unimarcmodel.GraficoType;
import it.finsiel.sbn.polo.model.unimarcmodel.ImportaInfoType;
import it.finsiel.sbn.polo.model.unimarcmodel.LegameDocType;
import it.finsiel.sbn.polo.model.unimarcmodel.LegameElementoAutType;
import it.finsiel.sbn.polo.model.unimarcmodel.LegameTitAccessoType;
import it.finsiel.sbn.polo.model.unimarcmodel.LegamiType;
import it.finsiel.sbn.polo.model.unimarcmodel.LuogoType;
import it.finsiel.sbn.polo.model.unimarcmodel.MarcaType;
import it.finsiel.sbn.polo.model.unimarcmodel.ModernoType;
import it.finsiel.sbn.polo.model.unimarcmodel.MusicaType;
import it.finsiel.sbn.polo.model.unimarcmodel.NumStdType;
import it.finsiel.sbn.polo.model.unimarcmodel.RepertorioType;
import it.finsiel.sbn.polo.model.unimarcmodel.SBNMarc;
import it.finsiel.sbn.polo.model.unimarcmodel.SbnOutputTypeChoice;
import it.finsiel.sbn.polo.model.unimarcmodel.SoggettoType;
import it.finsiel.sbn.polo.model.unimarcmodel.TitoloUniformeMusicaType;
import it.finsiel.sbn.polo.model.unimarcmodel.TitoloUniformeType;
import it.finsiel.sbn.polo.model.unimarcmodel.types.Indicatore;
import it.finsiel.sbn.polo.model.unimarcmodel.types.SbnAuthority;
import it.finsiel.sbn.polo.model.unimarcmodel.types.SbnFormaNome;
import it.finsiel.sbn.polo.model.unimarcmodel.types.SbnLegameAut;
import it.finsiel.sbn.polo.model.unimarcmodel.types.SbnLegameDoc;
import it.finsiel.sbn.polo.model.unimarcmodel.types.SbnLegameTitAccesso;
import it.finsiel.sbn.polo.model.unimarcmodel.types.SbnLivello;
import it.finsiel.sbn.polo.model.unimarcmodel.types.SbnNaturaDocumento;
import it.finsiel.sbn.polo.model.unimarcmodel.types.SbnTipoNomeAutore;
import it.finsiel.sbn.polo.model.unimarcmodel.types.SbnTipoSTD;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Category;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

/**
 * Classe che si occupa della traduzione da SBNMarc a Unimarc.
 * Se si tratta di documenti produce Unimarc secondo Bibliographic Format, se si
 * tratta di elementi di authority produce Unimarc secondo Authority Format, con
 * le estensioni nazionali definite da SBN
 */
public class SbnmarcUnimarc
{
   // file unimarc bibliografici
   private           ArrayList fileUnimarc   = null;
   private           ArrayList fileUnimarc_0 = null;
   private           ArrayList fileUnimarc_1 = null;
   private           ArrayList fileUnimarc_2 = null;
   private           ArrayList fileUnimarc_3 = null;
   private           ArrayList fileUnimarc_4 = null;
   public  String file_biblio = null;
   // file unimarc authority
   private           ArrayList fileUnimarc_A = null;
   private           ArrayList fileUnimarc_AT = null;
   public  String file_auth = null;
  
   public  String fileOutput_bibl = new String();
   public  String fileOutput_auth = new String();
  
   public  String fileOutput_bibl_file = new String();
   public  String fileOutput_auth_file = new String(); 
   public int contatoreDocumenti = 0;
  
   private  SbnNaturaDocumento natura = null;
  
   private  Hashtable ID;
   private  int partenza = 0;
   private String fileGuida = new String();
   private String fileIndice = new String();
   private String fileEtichette = new String();
  
   private  Hashtable c899 = new Hashtable(500000);
   private  String livelloG = new String();  
   private boolean opac = false;
  
   public static final String IS1  = "\u001f";  // identificativi sottocampi  $
   public static final char IS1char  = '\u001f';  // identificativi sottocampi  $
   public static final String IS2  = "\u001e";  // fine etichetta             @
   public static final String IS3  = "\u001d";  // fine record                %
   public static final String NSB  = "\u0088";  // inizio parte non significativa
   public static final String NSE  = "\u0089";  // fine parte non significativa
   
  
   public SbnmarcUnimarc(Hashtable id_table)
   {
     ID = id_table;
   }
 
   static Category log = Category.getInstance("iccu.serversbnmarc.SbnmarcUnimarc");
  
   public SbnmarcUnimarc(SBNMarc messaggio, Hashtable c899, Hashtable id_table, boolean opac)
   {
	   	this.opac = opac;
	    ID = id_table;
        StringWriter sw = new StringWriter();
        this.c899 = c899;
        if (messaggio.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getSbnOutputTypeChoiceCount() > 0) {
	        log.info("passo");
	        elaboraDocumento(messaggio.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getSbnOutputTypeChoice() );
        }
   }
 
    /**
     * Conversione per la import da sbnmarc
     * @param messaggio
     * @param db_conn
     */
//   public SbnmarcUnimarc(SBNMarc messaggio,String null)
//   {
//    ID = new Hashtable(50);
//    if (messaggio.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getSbnOutputTypeChoiceCount() > 0) {
//        log.info("passo");
//        elaboraDocumento(messaggio.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getSbnOutputTypeChoice() );
//    }
//   }
   public SbnmarcUnimarc(SBNMarc messaggio) {
	   ID = new Hashtable(50);
		if (messaggio.getSbnMessage().getSbnResponse()
					.getSbnResponseTypeChoice().getSbnOutput()
					.getSbnOutputTypeChoiceCount() > 0) {
			elaboraAuthority(messaggio);
		}
	}  

   protected void elaboraDocumento (SbnOutputTypeChoice documento[]) {
      log.info("UNIMARC ----->"+ documento.length+"<<<<<<");
      file_biblio = null;
      file_auth = null;
      String bid = null;
      for (int i = 0; i < documento.length; i++) {
          if (documento[i].getSbnOutputTypeChoiceItem().getDocumento() != null) {
        	  try {
				fileUnimarc = new ArrayList();
				fileUnimarc_0 = null;
				fileUnimarc_1 = null;
				fileUnimarc_2 = null;
				fileUnimarc_3 = null;
				fileUnimarc_4 = null;
				fileUnimarc_A = null;
				DocumentoTypeChoice docTC = documento[i].getSbnOutputTypeChoiceItem().getDocumento().getDocumentoTypeChoice();
//                      log.info("documento Choice>>>"+ii);
                if (docTC.getDatiTitAccesso() != null) {
                	bid = docTC.getDatiTitAccesso().getT001();
                	log.info("titolo di accesso non elaborato >>>"+bid+"<<<<");
                } else {
		            bid = docTC.getDatiDocumento().getT001();
		           	natura = docTC.getDatiDocumento().getNaturaDoc();
		            if (ID.containsKey(formattaID(bid))) {
		                continue;
		            }
                   log.info("UNIMARC natura >>"+natura+"<<>>"+i);

                   // Composizione documento - individuazione TAGs Unimarc
                   fileUnimarc = elaboraDatiDoc(documento[i].getSbnOutputTypeChoiceItem().getDocumento(),fileUnimarc);
                   fileUnimarc = elaboraDatiLegameDoc(documento[i].getSbnOutputTypeChoiceItem().getDocumento(),fileUnimarc);
                   fileUnimarc = elaboraAltriDatiDoc(documento[i].getSbnOutputTypeChoiceItem().getDocumento(),fileUnimarc);
                   // 950
                   fileUnimarc = elabora950(documento[i].getSbnOutputTypeChoiceItem().getDocumento(),fileUnimarc);

                   String fileGuida = new String();
                   String fileIndice = new String();
                   String fileOutput = new String();
                   String fileEtichette = new String();
                   int partenza = 0;
                   Collections.sort(fileUnimarc,new Comparatore());

                   // Scrittura del file unimarc appena composto
                   scriviFileUnimarc(fileUnimarc,"B");
                   if (fileUnimarc_0 != null) {
                       Collections.sort(fileUnimarc_0,new Comparatore());
                       scriviFileUnimarc(fileUnimarc_0,"B");
                   }
                   if (fileUnimarc_1 != null) {
                       Collections.sort(fileUnimarc_1,new Comparatore());
                       scriviFileUnimarc(fileUnimarc_1,"B");
                   }
                   if (fileUnimarc_2 != null) {
                       Collections.sort(fileUnimarc_2,new Comparatore());
                       scriviFileUnimarc(fileUnimarc_2,"B");
                   }
                   if (fileUnimarc_3 != null) {
                       Collections.sort(fileUnimarc_3,new Comparatore());
                       scriviFileUnimarc(fileUnimarc_3,"B");
                   }
                   if (fileUnimarc_4 != null) {
                       Collections.sort(fileUnimarc_4,new Comparatore());
                       scriviFileUnimarc(fileUnimarc_4,"B");
                   }

                   //26.02.2004 non scrive il file authority se la richiesta è
                   // export documenti
                   //if (fileUnimarc_A != null) {
                   //          scriviFileUnimarc(fileUnimarc_A,"A");
                   //}
                }
             } catch (Exception e) {
            	 log.error("ERRORE elaboraDocumento: "+bid,e);
	          }
          }
      }
//          log.info("file UNIMARC BIBLIOGR. >>\n"+this.fileOutput_bibl_file+"\n<<");
//          log.info("file UNIMARC AUTHORITY >>\n"+this.fileOutput_auth_file+"\n<<");
             log.info("esco da traduttore");

//System.out.println("\n\n***fileUnimarc:");
//for (int i=0; i<fileUnimarc.size(); i++){
//	System.out.println(fileUnimarc.get(i));
//}
//System.out.println("***\n\n");

   }


   public ArrayList getFileUnimarc(){
	   return fileUnimarc;
   }


   public void elaboraDocumentoImporta (SBNMarc messaggio) throws InfrastructureException {
 
              ImportaInfoType ndocumento[] = messaggio.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getImportaInfo(); 
              log.info("UNIMARC ----->"+ ndocumento.length+"<<<<<<");
              file_biblio = null;
              file_auth = null;
              for (int j = 0; j < ndocumento.length; j++)
      for (int i = 0; i< ndocumento[j].getOggettiTrovati().getOggettiTrovatiTypeItemCount(); i++){
                        fileUnimarc = new ArrayList();
                        fileUnimarc_0 = null;
                        fileUnimarc_1 = null;
                        fileUnimarc_2 = null;
                        fileUnimarc_3 = null;
                        fileUnimarc_4 = null;
                        fileUnimarc_A = null;
                        DocumentoType documento = ndocumento[j].getOggettiTrovati().getOggettiTrovatiTypeItem(i).getDocumento(); 
                        if (documento == null) {
                                   log.info("null documento");
                        } else {
                        DocumentoTypeChoice docTC = documento.getDocumentoTypeChoice();                                                                                                         
//                      log.info("documento Choice>>>"+ii);
                        if (docTC.getDatiTitAccesso() != null) {
                                   log.info("titolo di accesso non elaborato >>>"+docTC.getDatiTitAccesso().getT001()+"<<<<");
                        } else {
                                   natura = docTC.getDatiDocumento().getNaturaDoc();
            String id = docTC.getDatiDocumento().getT001();
            if (ID.containsKey(formattaID(id))) {
                continue;
            }
                                   log.info("UNIMARC natura >>"+natura+"<<>>"); 
                                   fileUnimarc = elaboraDatiDoc(documento,fileUnimarc);
                                   fileUnimarc = elaboraDatiLegameDoc(documento,fileUnimarc);
                                   fileUnimarc = elaboraAltriDatiDoc(documento,fileUnimarc);
                                   String fileGuida = new String();
                                   String fileIndice = new String();
                                   String fileOutput = new String();
                                   String fileEtichette = new String();
                                   int partenza = 0;
                                   Collections.sort(fileUnimarc,new Comparatore());
                                   scriviFileUnimarc(fileUnimarc,"B");
                                   if (fileUnimarc_0 != null) {
                                               Collections.sort(fileUnimarc_0,new Comparatore());
                                               scriviFileUnimarc(fileUnimarc_0,"B");
                                   }
                                   if (fileUnimarc_1 != null) {
                                               Collections.sort(fileUnimarc_1,new Comparatore());
                                               scriviFileUnimarc(fileUnimarc_1,"B");
                                   }
                                   if (fileUnimarc_2 != null) {
                                               Collections.sort(fileUnimarc_2,new Comparatore());
                                               scriviFileUnimarc(fileUnimarc_2,"B");
                                   }
                                   if (fileUnimarc_3 != null) {
                                               Collections.sort(fileUnimarc_3,new Comparatore());
                                               scriviFileUnimarc(fileUnimarc_3,"B");
                                   }
                                   if (fileUnimarc_4 != null) {
                                               Collections.sort(fileUnimarc_4,new Comparatore());
                                               scriviFileUnimarc(fileUnimarc_4,"B");
                                   }
                                   if (fileUnimarc_A != null) {
                                               scriviFileUnimarc(fileUnimarc_A,"A");
                                   }
                        }
              }        
             }
             log.info("file UNIMARC BIBLIOGR. >>\n"+this.fileOutput_bibl_file+"\n<<");
             log.info("file UNIMARC AUTHORITY >>\n"+this.fileOutput_auth_file+"\n<<");
             log.info("esco da traduttore");
   }  
 
 
   public void elaboraAuthority (SBNMarc messaggio) {
        SbnOutputTypeChoice[] documento = messaggio.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getSbnOutputTypeChoice(); 
        for (int i = 0; i < documento.length; i++) {
	        ElementAutType unimarc = documento[i].getSbnOutputTypeChoiceItem().getElementoAut();
	        if (unimarc != null) {
	              fileUnimarc = new ArrayList();
	              fileUnimarc = elaboraDatiEleAut(unimarc,fileUnimarc);
	              fileUnimarc = elaboraDatiLegameEleAut(unimarc,fileUnimarc);
	              Collections.sort(fileUnimarc,new Comparatore());
	              scriviFileUnimarc(fileUnimarc,"A");
	        }
        }
   }
  
   public static String formattaID(String id) {
	   // Peppe test del 29/02/2008
       // commentato - id nella forma a nr.10 char
/*       int n = 3;//Il polo (primi tre caratteri può avere un numero)
       while (!Character.isDigit(id.charAt(n)))
        n++;
       String alfa = id.substring(0,n);
       String numero = id.substring(n);
       return "IT\\ICCU\\"+alfa+"\\"+numero;*/
       return id;
   }
  
   public static String gestisciTrattiniNumStandard(String numero) {
     String ret = numero.substring(0,2);
     numero = numero.substring(2);
     int primiDue = Integer.parseInt(numero.substring(0,2));
     if (primiDue<20) {
       ret += "-" + numero.substring(0, 2) + "-" + numero.substring(2, 7) + "-" + numero.substring(7);
     } else if (primiDue<70) {
       ret += "-" + numero.substring(0, 3) + "-" + numero.substring(3, 7) + "-" + numero.substring(7);
     } else if (primiDue<85) {
       ret += "-" + numero.substring(0, 4) + "-" + numero.substring(4, 7) + "-" + numero.substring(7);
     } else if (primiDue<90) {
       ret += "-" + numero.substring(0, 5) + "-" + numero.substring(5, 7) + "-" + numero.substring(7);
     } else {
       ret += "-" + numero.substring(0, 6) + "-" + numero.substring(6, 7) + "-" + numero.substring(7);
     }
     return ret;
   }


   /**
    * Returns an ArrayList object.
    * Adds unimarc tags from 000 to 300.
    * 
    * @param documento
    * @param fileUnimarc
    * @return ArrayList object
    */
   public ArrayList elaboraDatiDoc (DocumentoType documento,ArrayList fileUnimarc) {
                        DatiDocType unimarc = documento.getDocumentoTypeChoice().getDatiDocumento();      
// 000 - guida
                        String u000 = "000";
                        u000 += "?????"; // da valorizzare
                        if (documento.getStatoRecord() != null) u000 +=documento.getStatoRecord(); else u000 +="n";
                        if (unimarc.getGuida().getTipoRecord()!= null) u000 += unimarc.getGuida().getTipoRecord(); else u000 +="u";
                        u000 += unimarc.getGuida().getLivelloBibliografico();

//ARGE
//Titolo bid_coll = new Titolo();

						try {
							Class.forName("org.postgresql.Driver");
							Connection conn = DriverManager.getConnection(
										"jdbc:postgresql://192.168.104.34:5432/" +
										"sbnwebHib2", "sbnweb", "sbnweb");
							Statement st = conn.createStatement();
							int n1 = 0;
							int n2 = 0;
							String liv_appo = null;
							 ResultSet rs_bidcoll = st.executeQuery("SELECT COUNT(*) " +
							 		"FROM sbnweb.Tr_tit_tit WHERE  fl_canc != 'S' " +
							 		"AND  bid_coll = '"+unimarc.getT001()+"' " +
					 				"AND tp_legame = '01' " +
					 				"AND ( (cd_natura_base = 'C' and cd_natura_coll = 'C') " +
					 				"OR (cd_natura_base != 'C' and cd_natura_coll != 'C') ) " +
					 				"AND ( (cd_natura_base = 'S' and cd_natura_coll = 'S') " +
					 				"OR (cd_natura_base != 'S' and cd_natura_coll != 'S') )");
							 if (rs_bidcoll.next()) n1 = Integer.parseInt(rs_bidcoll.getString("count"));
							 ResultSet rs_bidbase = st.executeQuery("SELECT COUNT(*) " +
							 		"FROM sbnweb.Tr_tit_tit WHERE  fl_canc != 'S' " +
							 		"AND  bid_base = '"+unimarc.getT001()+"' AND tp_legame = '01' " +
					 				"AND ( (cd_natura_base = 'C' and cd_natura_coll = 'C') " +
					 				"OR (cd_natura_base != 'C' and cd_natura_coll != 'C') ) " +
					 				"AND ( (cd_natura_base = 'S' and cd_natura_coll = 'S') " +
					 				"OR (cd_natura_base != 'S' and cd_natura_coll != 'S') )");
							 if (rs_bidbase.next()) n2 = Integer.parseInt(rs_bidbase.getString("count"));
							// senza livelli
							if (n1 == 0 && n2 == 0) liv_appo = "0";
							// livello intermedio  
							if (n1 > 0 && n2 > 0) liv_appo = "3";
							// livello più alto  
							if (n1 > 0 && n2 == 0) liv_appo = "1";
							// livello base: n1 == 0 e n2 > 0
							if (liv_appo==null) liv_appo = "2";
							livelloG = liv_appo;
							rs_bidbase = null;
							rs_bidcoll = null;
						} catch (ClassNotFoundException cnfe) {
						    log.error("Errore caricamento JdbcOdbcDriver, lo ignoro",cnfe);
						    cnfe.printStackTrace();
						    livelloG = "E";
						} catch (java.sql.SQLException sqle) {
						    log.error("Errore lettura titolo JDBC, la ignoro",sqle);
						    sqle.printStackTrace();
						    livelloG = "E";
						}
//                        try {
////ARGE
//livelloG = bid_coll.contaBidPerGerarchia(unimarc.getT001());
//                        			livelloG= null;
//                        } catch (Exception e){                           
//                                   livelloG = "E";
//                        }

                        // livelloG = 3 specifica che è un livello intermedio, necessario per il test su 461 o 462
                        if (livelloG == "3") {
                        	u000 += "2";
                        } else {
                        	u000 += livelloG;
                        }
                        u000 += " 22";
                        u000 += "!!!!!"; // da valorizzare
                        SbnLivello livelloAut = unimarc.getLivelloAutDoc();

                        if (livelloAut.getType() == 0) {
				            u000 += "1";    //05
				        } else {
				            if (livelloAut.getType() > 2) {
				                u000 += " "; // 72-97
				            } else {
				                u000 += "3"; // altri 06-71
				            }
				        }
                        //  u000 +=  " "; unimarc.getLivelloAutDoc();

                        u000 += "i 450 ";
                        fileUnimarc.add(u000);

// 001
        String id = formattaID(unimarc.getT001());
                        String u001 = "001";
                        u001 += id;
                        ID.put(id, unimarc.getT001());
                        fileUnimarc.add(u001);
// 005
                        String u005 = "005";
                        u005 += unimarc.getT005();
                        fileUnimarc.add(u005);  
// Numero Standard
                        NumStdType uNSTD [] = unimarc.getNumSTD();           
                        for (int j = 0; j < uNSTD.length; j++) {
// 010                          
                                   if (uNSTD[j].getTipoSTD() == SbnTipoSTD.VALUE_0) {
                                               String u010 = "010  ";
                if (uNSTD[j].getNotaSTD() != null
                    && (uNSTD[j].getNotaSTD().startsWith("ERR.")
                        || uNSTD[j].getNotaSTD().toUpperCase().indexOf("ERRATO") >= 0)) {
                                                           u010 += IS1+"b"+uNSTD[j].getNotaSTD();
                    String numeroStd = new String();
                    if ((uNSTD[j].getNumeroSTD()).startsWith("88")) {
                        numeroStd = gestisciTrattiniNumStandard(uNSTD[j].getNumeroSTD());
                    } else {
                        numeroStd = uNSTD[j].getNumeroSTD();
                    }
                    u010 += IS1+"z"+numeroStd;
                                                           fileUnimarc.add(u010);  
                                               } else {
                        String numeroStd = new String();
                        if ((uNSTD[j].getNumeroSTD()).startsWith("88")) {
                          numeroStd = gestisciTrattiniNumStandard(uNSTD[j].getNumeroSTD());
                        } else {
                            numeroStd = uNSTD[j].getNumeroSTD();
                        }
                                                                       u010 += IS1+"a"+numeroStd;
                        if (uNSTD[j].getNotaSTD() != null)
                                                                       u010 += IS1+"b"+uNSTD[j].getNotaSTD();
                                                                       fileUnimarc.add(u010);  
                                               }                                             
                                   } else
//          011                             
                                   if (uNSTD[j].getTipoSTD() == SbnTipoSTD.VALUE_1) {
                                               String u011 = "011  ";
                                               if (uNSTD[j].getNotaSTD() != null &&
                    (uNSTD[j].getNotaSTD()).startsWith("ERR.")
                    ) {
                                                           u011 += IS1+"b"+uNSTD[j].getNotaSTD();
                                                           u011 += IS1+"z"+uNSTD[j].getNumeroSTD();
                                                           fileUnimarc.add(u011);  
                                               } else {
                                                              u011 += IS1+"a"+((uNSTD[j].getNumeroSTD()).substring(0,4)+"-"+(uNSTD[j].getNumeroSTD()).substring(4,(uNSTD[j].getNumeroSTD()).length()));
                       if (uNSTD[j].getNotaSTD() != null
                       && ((uNSTD[j].getNotaSTD().trim()).length() > 0))
                                                              u011 += IS1+"b"+uNSTD[j].getNotaSTD();
                                                              fileUnimarc.add(u011);           
                                               }                                             
                                   } else
                       
//          013                             
                                   if (uNSTD[j].getTipoSTD() == SbnTipoSTD.VALUE_2) {
                                               String u013 = "013  ";
                                               if (uNSTD[j].getNotaSTD() != null && (uNSTD[j].getNotaSTD()).startsWith("ERR.")) {
                                                           u013 += IS1+"b"+uNSTD[j].getNotaSTD();
                                                           u013 += IS1+"z"+uNSTD[j].getNumeroSTD();
                                                           fileUnimarc.add(u013);  
                                               } else {
                                                              u013 += IS1+"a"+uNSTD[j].getNumeroSTD();
                       if (uNSTD[j].getNotaSTD() != null)
                                                                  u013 += IS1+"b"+uNSTD[j].getNotaSTD();
                                                              fileUnimarc.add(u013);           
                                               }                                             
                                   } else
//          020                             
                                   if (uNSTD[j].getTipoSTD() == SbnTipoSTD.VALUE_3) {
                                               String u020 = "020  ";
                                               if (uNSTD[j].getNotaSTD() != null && (uNSTD[j].getNotaSTD()).startsWith("ERR.")) {
                                                           u020 += IS1+"b"+uNSTD[j].getNotaSTD();
                                                           u020 += IS1+"z"+uNSTD[j].getNumeroSTD();
                                                           fileUnimarc.add(u020);  
                                               } else {
                       String paese = uNSTD[j].getPaeseSTD();
                       if (paese == null || paese.trim().length() == 0) {
                           u020 += IS1+"a"+"IT";
                       } else {
                           u020 += IS1+"a"+uNSTD[j].getPaeseSTD();
                       }
                                                              u020 += IS1+"b"+uNSTD[j].getNumeroSTD();
                                                              fileUnimarc.add(u020);           
                                               }                                             
                                   } else
//          022                             
                                   if (uNSTD[j].getTipoSTD() == SbnTipoSTD.VALUE_4) {
                                               String u022 = "022  ";
                                               if (uNSTD[j].getNotaSTD() != null && (uNSTD[j].getNotaSTD()).startsWith("ERR.")) {
                                                           u022 += IS1+"b"+uNSTD[j].getNotaSTD();
                                                           u022 += IS1+"z"+uNSTD[j].getNumeroSTD();
                                                           fileUnimarc.add(u022);  
                                               } else {
                                                              u022 += IS1+"a"+uNSTD[j].getPaeseSTD();
                                                              u022 += IS1+"b"+uNSTD[j].getNumeroSTD();
                                                              fileUnimarc.add(u022);           
                                               }                                             
                                   } else
//          071 Lastra                               
                                   if (uNSTD[j].getTipoSTD() == SbnTipoSTD.VALUE_5) {
                                               String u071 = "07120";
                                               if (uNSTD[j].getNumeroSTD() != null) {
                                                  u071 += IS1+"a"+uNSTD[j].getNumeroSTD();
                                                  fileUnimarc.add(u071);           
                                               }                                             
                                   } else
//          071 Editoriale                          
                                   if (uNSTD[j].getTipoSTD() == SbnTipoSTD.VALUE_8) {
                                               String u071 = "07140";
                                               if (uNSTD[j].getNumeroSTD()!= null) {
                                                  u071 += IS1+"a"+uNSTD[j].getNumeroSTD();
                                                  fileUnimarc.add(u071);           
                                               }                                             
                                   } else {
                                   String u017 = "01770";
                                   if (uNSTD[j].getNotaSTD() != null && (uNSTD[j].getNotaSTD()).startsWith("ERR.")) {
                                               u017 += IS1+"b"+uNSTD[j].getNotaSTD();
                                               u017 += IS1+"z"+uNSTD[j].getNumeroSTD();
                                               u017 += IS1+"2"+uNSTD[j].getTipoSTD().toString();
                                               fileUnimarc.add(u017);  
                                   } else {
                                                  u017 += IS1+"a"+uNSTD[j].getNumeroSTD();
                   if (uNSTD[j].getNotaSTD() != null)
                                                       u017 += IS1+"b"+uNSTD[j].getNotaSTD();
                                                  u017 += IS1+"2"+uNSTD[j].getTipoSTD().toString();
                                                  fileUnimarc.add(u017);           
                                   }
                                   }                                                                                                                               
                        }
 
                        if (unimarc instanceof AnticoType)          {
//012                                       
                                    AnticoType antico = (AnticoType) unimarc;       
                                    C012 c012 [] = antico.getT012();
                                    if (c012.length > 0) {
                                                for (int j = 0; j < c012.length; j++) {
                                                            String u012 = "012  "+IS1+"a";
                                                            if (c012[j].getA_012_1()!= null) u012+= c012[j].getA_012_1();
                                                            if (c012[j].getA_012_2()!= null) u012+= c012[j].getA_012_2();
                                                            if (c012[j].getA_012_3()!= null) u012+= c012[j].getA_012_3();
                                                            u012+= IS1+"2fei";
                                                            if (c012[j].getNota()!= null) u012+= IS1+"9"+c012[j].getNota();
                       
                                                            fileUnimarc.add(u012); 
                                                }
                                    }
                        }                                             
 
 
// 100
                        C100 c100 = unimarc.getT100();
                        if (c100 != null) {
                                   String u100 = "100  "+IS1+"a";
                                   if (c100.getA_100_0() != null) {
                                               u100 += c100.getA_100_0().toString().substring(0,4);
                                               u100 += c100.getA_100_0().toString().substring(5,7);
                                               u100 += c100.getA_100_0().toString().substring(8,10);               
                                   }
                                   if (c100.getA_100_8() != null) u100 += c100.getA_100_8(); else u100 += " "; 
                                   if (c100.getA_100_9() != null) u100 += c100.getA_100_9(); else u100 += "    ";
                                   if (c100.getA_100_13()!= null) u100 += c100.getA_100_13(); else u100 += "    ";
           
                                   u100 += elaboraLivelloIntellettuale(unimarc);
                                   if (c100.getA_100_20()!= null) u100 += c100.getA_100_20(); else u100 += "|";
                                   u100 +="0itac50      ba";
                                   fileUnimarc.add(u100);
                        }
                                  
 
// 101              
                        C101 c101 = unimarc.getT101();           
                        if (c101 != null) {
                                   String u101[] = c101.getA_101();
                                   if (u101.length > 0) {     
                                               String u101All = "101| ";
                                               for (int j = 0; j < u101.length; j++) {
                    if (u101[j].trim().length() > 0) {
                        u101All += IS1+"a"+u101[j].toLowerCase();
                    }             
                                               }
                                               if (u101All.length() > 5) fileUnimarc.add(u101All);
                                   }
            }
 
// 102
                        C102 c102 = unimarc.getT102();
                        if (c102 != null) {
                                   String u102 = "102  "+IS1+"a";
                                   if (c102.getA_102() != null) {
                                               u102 += c102.getA_102();
                                               fileUnimarc.add(u102);
                                   }
                        }   
        if (unimarc instanceof ModernoType)          {
                                   ModernoType moderno = (ModernoType) unimarc;
// 105
            C105 c105 = moderno.getT105();
            if (c105 != null) {
                        String u105[] = c105.getA_105_4();
                        String u105All = "105  "+IS1+"a||||";
                char ch = ' ';
                                               for (int j = 0; j < 4; j++) {
                                                           if (j < u105.length && u105[j]!=null) {
                        char c = u105[j].toLowerCase().charAt(0);
                        if (Character.isDigit(c) || c=='x' || c=='y' || c=='r') {
                            c=' ';
                        } else if (c=='z') {
                            ch=c;
                        }
                        u105All += c;
                    }
                    else u105All += " ";
                                               }
                if (ch == 'z') {
                    u105All += "1||||";
                } else {
                    u105All += "|||||";
                }
                                               fileUnimarc.add(u105All);                                             
            }          
        }
 
// 110
                        C110 c110 = unimarc.getT110();
                        if (c110 != null) {
                         String u110 = "110  "+IS1+"a";
                         if (c110.getA_110_0() != null) u110 += c110.getA_110_0(); else u110 += " ";
                         u110 +="|||||||||";
                         fileUnimarc.add(u110);             
                        }          
// 116
                        if (unimarc instanceof GraficoType)         {
                                    GraficoType grafico = (GraficoType) unimarc;
                                   C116 c116 = grafico.getT116();
                                   if (c116 != null) {
                                                String u116 = "116  "+IS1+"a";
                                                if (c116.getA_116_0() != null) u116 += c116.getA_116_0(); else u116 += "|";
                                               if (c116.getA_116_1() != null) u116 += c116.getA_116_1(); else u116 += "|";
                                               u116 += "|";
                                               if (c116.getA_116_3() != null) u116 += c116.getA_116_3(); else u116 += "|";
                                               String u116A4[] = c116.getA_116_4();
                                               for (int j = 0; j < 7; j++) {
                                                           if (j < u116A4.length) u116 += u116A4[j]; else u116 += "|";                     
                                               }
                                               String u116A10[] = c116.getA_116_10();
                                               for (int j = 0; j < 7; j++) {
                                                           if (j < u116A10.length) u116 += u116A10[j]; else u116 += "|";                 
                                               }                                             
                                               if (c116.getA_116_16() != null) u116 += c116.getA_116_16(); else u116 += "||";                                         
                                                fileUnimarc.add(u116);
                                   }                      
                        }
 
                        if (unimarc instanceof CartograficoType)  {
                                   CartograficoType cgrafico = (CartograficoType) unimarc;
// 120                          
                                   C120 c120 = cgrafico.getT120();
                                   if (c120 != null) {
                                               String u120 = "120  "+IS1+"a";
                                               if (c120.getA_120_0() != null) u120+= c120.getA_120_0(); else u120 += "|";
                                               u120 += "||||||||";
                                               if (c120.getA_120_9() != null) u120+= c120.getA_120_9(); else u120 += "  ";
                                               u120 += "  ";                                                                                     
                                               fileUnimarc.add(u120);
                                   }                      
// 121                          
                                    C121 c121 = cgrafico.getT121();
                                    if (c121 != null) {
                                                String u121 = "121  "+IS1+"a";
                                                u121 += "|||";
                                                if (c121.getA_121_3() != null) u121+= c121.getA_121_3(); else u121 += "  ";
                                                if (c121.getA_121_5() != null) u121+= c121.getA_121_5(); else u121 += " ";
                                                if (c121.getA_121_6() != null) u121+= c121.getA_121_6(); else u121 += " ";
                                                u121 += "|";                                        
                                                if (c121.getA_121_8() != null) u121+= c121.getA_121_8(); else u121 += " "; 
                                                u121 += IS1+"b";
                                                if (c121.getB_121_0() != null) u121+= c121.getB_121_0(); else u121 += " ";
                                                u121 += "|||||||";                                                                                 
                                                fileUnimarc.add(u121);
                                    }
// 123                          
                                    C123 c123 = cgrafico.getT123();
                                    if (c123 != null) {
                                                String u123 = "123";
                                                u123+= c123.getId1();
                                                u123 += " ";
                                                if (c123.getA_123() != null) u123+= IS1+"a"+c123.getA_123(); 
                                                if (c123.getB_123() != null) u123+= IS1+"b"+c123.getB_123(); 
                                                if (c123.getC_123() != null) u123+= IS1+"c"+c123.getC_123(); 
                                                if (c123.getD_123() != null) u123+= IS1+"d"+c123.getD_123(); 
                                                if (c123.getE_123() != null) u123+= IS1+"e"+c123.getE_123(); 
                                                if (c123.getF_123() != null) u123+= IS1+"f"+c123.getF_123(); 
                                                if (c123.getG_123() != null) u123+= IS1+"g"+c123.getG_123(); 
                                                fileUnimarc.add(u123);
                                    }         
// 124                          
                                    C124 c124 = cgrafico.getT124();
                                    if (c124 != null) {
                                               String u124 = "124  ";
                                               if (c124.getA_124() != null) u124+= IS1+"a"+c124.getA_124(); 
                                               if (c124.getB_124() != null) u124+= IS1+"b"+c124.getB_124(); 
                                               if (c124.getD_124() != null) u124+= IS1+"d"+c124.getD_124(); 
                                               if (c124.getE_124() != null) u124+= IS1+"e"+c124.getE_124(); 
                                               fileUnimarc.add(u124);
                                     }                                                                                                                             
                        }                                 
                        if (unimarc instanceof MusicaType)         {
                                   MusicaType musica = (MusicaType) unimarc;
// 125                          
                    C125 c125 = musica.getT125();
                                   if (c125 != null) {
                                      String u125 = "125  ";
                                      if (c125.getA_125_0() != null) u125+= IS1+"a"+c125.getA_125_0();                                 
                                      if (c125.getB_125() != null) u125+= IS1+"b"+c125.getB_125(); 
                                      fileUnimarc.add(u125);
                                   }                                                         
// 128                          
                    C128 c128 = musica.getT128();
                                   if (c128 != null) {
                                      String u128 = "128  ";
                                      if (c128.getB_128() != null) u128+= IS1+"b"+c128.getB_128(); 
                                      if (c128.getC_128() != null) u128+= IS1+"c"+c128.getC_128(); 
                                      if (c128.getD_128() != null) u128+= IS1+"9"+c128.getD_128(); 
                                      fileUnimarc.add(u128);
                                   }
                        }          
                        if (unimarc instanceof AnticoType)          {
                                   AnticoType antico = (AnticoType) unimarc;        
// 140                                                  
                                   C140 c140 = antico.getT140();
                                   if (c140 != null) {
                                               String u140 = "140  "+IS1+"a";
                                               // 9 spazi: il contenuto deve partire dalla pos. 9 contando anche la pos. 0
                                               u140 += "|||||||||";
                                               String u1409[] = c140.getA_140_9();
                for (int j = 0; j < 5; j++) {
                    if (j < u1409.length) {
                        String st = u1409[j].toLowerCase();
                        if (Character.isDigit(st.charAt(0)) || st.equals("xx") || st.equals("yy")) {
                            st = "  ";
                        }
                        u140 += st;
                    } else
                        u140 += "  ";
                }
                u140 += "|||||||||||";
                                               fileUnimarc.add(u140);
                                   }
                        }                                 
// 200
                        C200 c200 = unimarc.getT200();
                        if (c200 != null) {
                                   String u200 = elabora200(c200);
                                   fileUnimarc.add(u200);              
                        }                                                                                           
// 205
                        C205 c205 = unimarc.getT205();
                        if (c205 != null) {
                           String u205 = "205  ";
                           if (c205.getA_205() != null) u205+= IS1+"a"+c205.getA_205();
                           String c205B[] = c205.getB_205();
                           if (c205B.length > 0) {
                                      for (int j = 0; j < c205B.length; j++) {
                                                           u205 += IS1+"b"+c205B[j];                   
                                      }
                           }
                           String c205F[] = c205.getF_205();
                           if (c205F.length > 0) {
                                      for (int j = 0; j < c205F.length; j++) {
                                                           u205 += IS1+"f"+c205F[j];                    
                                      }
                           }
                           String c205G[] = c205.getG_205();
                           if (c205G.length > 0) {
                                      for (int j = 0; j < c205G.length; j++) {
                                                           u205 += IS1+"g"+c205G[j];                   
                                      }
                           }                      
                           fileUnimarc.add(u205);                       
                        }
//          206
                        C206 c206[] = unimarc.getT206();
                        if (c206.length > 0) {
                                   String u206All = new String();
                                   for (int j = 0; j < c206.length; j++) {
                                                u206All += "206  "+IS1+"a"+c206[j].getA_206();                       
                                   }
                                   fileUnimarc.add(u206All);                                             
                        }       
//          207
                        C207 c207 = unimarc.getT207();
                        if (c207 != null) {
                                   String c207A[] = c207.getA_207();
                                   // indicatore 2 sempre per dati non formattati
                                    String u207 = "207 1";
                                   if (c207A.length > 0) {
                                               for (int j = 0; j < c207A.length; j++) {
                                                            u207 += IS1+"a"+c207A[j];                  
                                               }
                                   }
                                   fileUnimarc.add(u207);                                     
                        }          
//          208
                        C208 c208 = unimarc.getT208();
                        if (c208 != null) {
                                   String u208 = "208  ";
                                   if (c208.getA_208() != null) u208+= IS1+"a"+c208.getA_208();
                                   String c208D[] = c208.getD_208();
                                   if (c208D.length > 0) {
                                               for (int j = 0; j < c208D.length; j++) {
                                                            u208 += IS1+"d"+c208D[j];                  
                                               }
                                   }
                                   fileUnimarc.add(u208);                                     
                        }
// 210
                        C210 c210[] = unimarc.getT210();
                        if (c210.length > 0) {
                                   String u210All = "210  ";
                                   for (int j = 0; j < c210.length; j++) {
                                                Ac_210Type AC210[] = c210[j].getAc_210();
                                                String C210D [] = c210[j].getD_210();
                                                String C210E [] = c210[j].getE_210();
                                                String C210G [] = c210[j].getG_210();
                                                String C210H [] = c210[j].getH_210();                                                                                                                                     
                                                if (AC210.length > 0) {
                                                           for (int jj = 0; jj < AC210.length; jj++) {
                                                                       String C210A [] = AC210[jj].getA_210();
                                                                       String C210C [] = AC210[jj].getC_210();                                                            
                                                                       for (int jjj = 0; jjj < C210A.length; jjj++) {
                                                                                  u210All += IS1+"a"+C210A[jjj];
                                                                       }
                                                                       for (int jjj = 0; jjj < C210C.length; jjj++) {
                                                                                  u210All += IS1+"c"+C210C[jjj];
                                                                       }                                                                                                                                                       
                                                           }
                                                }
                                               for (int jjj = 0; jjj < C210D.length; jjj++) {
                                                           u210All += IS1+"d"+C210D[jjj];
                                               }
                                               for (int jjj = 0; jjj < C210E.length; jjj++) {
                                                           u210All += IS1+"e"+C210E[jjj];
                                               }                                                         
                                               for (int jjj = 0; jjj < C210G.length; jjj++) {
                                                           u210All += IS1+"g"+C210G[jjj];
                                               }                                                                     
                                               for (int jjj = 0; jjj < C210H.length; jjj++) {
                                                           u210All += IS1+"h"+C210H[jjj];
                                               }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
                                   }
                                   if (u210All.length() > 5) fileUnimarc.add(u210All);            
                        }   
//          215
                        C215 c215 = unimarc.getT215();
                        if (c215 != null) {
                                   String c215A[] = c215.getA_215();
                                   String u215 = "215  ";
                                   if (c215A.length > 0) {
                                               for (int j = 0; j < c215A.length; j++) {
                                                            u215 += IS1+"a"+c215A[j];                  
                                               }
                                   }
                                   if (c215.getC_215() != null) u215+= IS1+"c"+c215.getC_215();
                                   String c215D[] = c215.getD_215();
                                   if (c215D.length > 0) {
                                               for (int j = 0; j < c215D.length; j++) {
                                                            u215 += IS1+"d"+c215D[j];                  
                                               }
                                   }
                                   String c215E[] = c215.getE_215();
                                   if (c215E.length > 0) {
                                               for (int j = 0; j < c215E.length; j++) {
                                                            u215 += IS1+"e"+c215E[j];                  
                                               }
                                   }                                 
                                  
                                   fileUnimarc.add(u215);                                     
                        }          
                        if (unimarc instanceof ModernoType)       {
                                   ModernoType moderno = (ModernoType) unimarc;
//          230
                                   C230 c230[] = moderno.getT230();
                                   if (c230.length > 0) {
                                               String u230All = "230  ";
                                               for (int j = 0; j < c230.length; j++) {
                                                            u230All += IS1+"a"+c230[j].getA_230();                       
                                               }
                                               fileUnimarc.add(u230All);                                             
                                   }          
                        }          
//          300
                        C3XX c300[] = unimarc.getT3XX();
                        if (c300.length > 0) {
                                   for (int j = 0; j < c300.length; j++) {
                if (c300[j].getTipoNota().toString().startsWith("3")) {
                    String u300 = c300[j].getTipoNota().toString() + "  " + IS1 + "a" + c300[j].getA_3XX();
                    //Tolgo l'eventuale punto finale
                    if (u300.endsWith("."))
                        u300=u300.substring(0,u300.length()-1);
                    fileUnimarc.add(u300);
                }
                                   }
                                  
                        }  
        log.info("esco da estrai datiDoc");
            return fileUnimarc;
   }
   private String getStringData(int n) {
       if (n<10)
        return "0"+n;
       return ""+n;
   }




   /**
    * Returns an ArrayList object.
    * Adds unimarc tags from 801 to 9xx customized.
    * 
    * @param documento
    * @param fileUnimarc
    * @return ArrayList object
    * @throws InfrastructureException
    */
   public ArrayList elaboraAltriDatiDoc(
		   DocumentoType documento, 
		   ArrayList fileUnimarc) throws InfrastructureException {

	   		DatiDocType unimarc = documento.getDocumentoTypeChoice().getDatiDocumento();
	   		String bid = unimarc.getT001();

//          801
	   		C801 c801 = unimarc.getT801();
	   		String u801 = "801 3";
			//se a_801 è vuoto impostare 'IT' , se b_801 è vuoto impostare 'ICCU'
			// aggiungere un elemento u801+= IS1+"c"+c801.xxx dove xxx è la data da T0057)
			if (c801 != null && c801.getA_801()!= null) u801+= IS1+"a"+c801.getA_801();
			else u801+= IS1+"a"+"IT";
			if (c801 != null && c801.getB_801()!= null) u801+= IS1+"b"+c801.getB_801();
			else u801+= IS1+"b"+"ICCU";
			Calendar c = Calendar.getInstance();
			u801+= IS1+"c"+ c.get(Calendar.YEAR)+ getStringData(1+c.get(Calendar.MONTH)) + getStringData(c.get(Calendar.DAY_OF_MONTH));
			// u801+= IS1+"c"+unimarc.getT005().substring(0,8);
			if (u801.length() > 5) fileUnimarc.add(u801);           
//          856
			C856 c856[] = unimarc.getT856();         
			if (c856.length > 0) {
				for (int j = 0; j < c856.length; j++) {
					String u856 = "856  ";
					if (c856[j].getU_856()!= null) u856+= IS1+"u"+c856[j].getU_856();
					fileUnimarc.add(u856);              
				}
			}

//          899
			if (c899.get(bid) != null) {
				int sizeBid = Integer.parseInt((c899.get(bid)).toString());
				if (sizeBid > 0) {
					for (int j = 0; j < sizeBid; j++) {
						String u899 = "899  ";   
						if (c899.get(bid+j)!= null){
							u899+= (c899.get(bid+j)).toString();       
							fileUnimarc.add(u899);
						}
					}
				}
			}

			if (unimarc instanceof MusicaType){
				MusicaType musica = (MusicaType) unimarc;
				//  922
				String u922 = "922  ";
				C922 c922 = musica.getT922();
				if (c922 != null) {
					if (c922.getA_922()!= null) u922+= IS1+"a"+c922.getA_922();
					if (c922.getP_922()!= null) u922+= IS1+"p"+c922.getP_922();
					if (c922.getQ_922()!= null) u922+= IS1+"q"+c922.getQ_922();
					if (c922.getR_922()!= null) u922+= IS1+"r"+c922.getR_922();
					if (c922.getS_922()!= null) u922+= IS1+"s"+c922.getS_922();
					if (c922.getT_922()!= null) u922+= IS1+"t"+c922.getT_922();
					if (c922.getU_922()!= null) u922+= IS1+"u"+c922.getU_922();
					if (u922.length() > 5) fileUnimarc.add(u922);
				}
//          923
				String u923= "923  ";
				if (opac) {
					u923= "925  ";
				}
				C923 c923 = musica.getT923();
				if (c923!= null) {
					if (c923.getB_923()!= null) u923+= IS1+"b"+c923.getB_923();
					if (c923.getC_923()!= null) u923+= IS1+"c"+c923.getC_923();
					if (c923.getD_923()!= null) u923+= IS1+"d"+c923.getD_923();
					if (c923.getE_923()!= null) u923+= IS1+"e"+c923.getE_923();
					if (c923.getG_923()!= null) u923+= IS1+"g"+c923.getG_923();
					if (c923.getH_923()!= null) u923+= IS1+"h"+c923.getH_923();
					if (c923.getI_923()!= null) u923+= IS1+"i"+c923.getI_923();
					if (c923.getL_923()!= null) u923+= IS1+"l"+c923.getL_923();
					if (c923.getM_923()!= null) u923+= IS1+"m"+c923.getM_923();
					if (u923.length() > 5) fileUnimarc.add(u923);       
				}                     
//			926   
				C926 c926[] = musica.getT926();
				if (c926.length > 0) {
					for (int j = 0; j < c926.length; j++) {
						String u926 = "926  ";
						if (c926[j].getA_926()!= null) u926+= IS1+"a"+c926[j].getA_926();
						if (c926[j].getB_926()!= null) u926+= IS1+"b"+c926[j].getB_926();
						if (c926[j].getC_926()!= null) u926+= IS1+"c"+c926[j].getC_926();
						if (c926[j].getF_926()!= null) u926+= IS1+"f"+c926[j].getF_926();
						if (c926[j].getG_926()!= null) u926+= IS1+"g"+c926[j].getG_926();
						if (c926[j].getH_926()!= null) u926+= IS1+"h"+c926[j].getH_926();
						if (c926[j].getI_926()!= null) u926+= IS1+"i"+c926[j].getI_926();
						if (c926[j].getL_926()!= null) u926+= IS1+"l"+c926[j].getL_926();
						if (c926[j].getM_926()!= null) u926+= IS1+"m"+c926[j].getM_926();
						if (c926[j].getN_926()!= null) u926+= IS1+"n"+c926[j].getN_926();
						if (c926[j].getO_926()!= null) u926+= IS1+"o"+c926[j].getO_926();
						if (c926[j].getP_926()!= null) u926+= IS1+"p"+c926[j].getP_926();
						if (c926[j].getQ_926()!= null) u926+= IS1+"q"+c926[j].getQ_926();
						if (c926[j].getR_926()!= null) {
							u926+= IS1+"r"+c926[j].getR_926();

							//ARGE
							try {
								System.out.println("dentro al TRY 1 jdbc di elaboraAltriDatiDoc(...)");
								System.out.println("- costruzione ISBD");
								Class.forName("org.postgresql.Driver");
								Connection conn = DriverManager.getConnection("jdbc:postgresql://192.168.104.34:5432/sbnwebHib2", "sbnweb", "sbnweb");
								Statement st = conn.createStatement();
								ResultSet rs = st.executeQuery("select isbd FROM sbnweb.Tb_titolo WHERE fl_canc !='S' AND bid = '"+c926[j].getR_926()+"'");
								if (rs.next()) {
									//rs.getString("count");
									System.out.println("_bid:"+rs.getString("bid"));
									u926+= IS1+"s"+rs.getString("isbd");
								}
								rs = null;
							} catch (ClassNotFoundException cnfe) {
								log.error("Errore caricamento JdbcOdbcDriver, lo ignoro",cnfe);
								cnfe.printStackTrace();
							} catch (java.sql.SQLException sqle) {
								log.error("Errore lettura titolo JDBC, la ignoro",sqle);
								sqle.printStackTrace();
							}

//							try {
//								Tb_titolo tb= new Titolo().estraiTitoloPerID(c926[j].getR_926());
//							    if (tb != null) u926+= IS1+"s"+tb.getISBD();
//							} catch (EccezioneDB e) {
//								log.error("Errore lettura titolo, la ignoro",e);
//							}

						}
						fileUnimarc.add(u926);
					}
				}

				C927 c927[] = musica.getT927();
				if (c927.length > 0) {
					for (int j = 0; j < c927.length; j++) {
						String u927= "927  ";
						if (c927[j].getA_927()!= null) u927+= IS1+"a"+c927[j].getA_927();
						if (c927[j].getB_927()!= null) u927+= IS1+"b"+c927[j].getB_927();
						if (c927[j].getC3_927()!= null) {

							try {
								System.out.println("dentro al TRY 2 jdbc di elaboraAltriDatiDoc(...)");
								Class.forName("org.postgresql.Driver");
								Connection conn = DriverManager.getConnection("jdbc:postgresql://192.168.104.34:5432/sbnwebHib2", "sbnweb", "sbnweb");
								Statement st = conn.createStatement();
								ResultSet rs = st.executeQuery("select ds_nome_aut FROM sbnweb.Tb_autore WHERE fl_canc !='S' AND vid = '"+c926[j].getR_926()+"'");
								if (rs.next()) {
									//rs.getString("count");
									System.out.println("_bid:"+rs.getString("bid"));
									u927+= IS1+"c"+rs.getString("ds_nome_aut");
								}
								rs = null;
							} catch (ClassNotFoundException cnfe) {
								log.error("Errore caricamento JdbcOdbcDriver, lo ignoro",cnfe);
								cnfe.printStackTrace();
							} catch (java.sql.SQLException sqle) {
								log.error("Errore lettura titolo JDBC, la ignoro",sqle);
								sqle.printStackTrace();
							}

//						//ARGE
//						try {
//							//select ds_nome_aut FROM sbnweb.Tb_autore WHERE fl_canc !='S' AND vid = XXXvid 
//							Autore aut = new Autore();
//							Tb_autore ta = aut.estraiAutorePerID(c927[j].getC3_927());
//							if (ta != null) u927+= IS1+"c"+ta.getDS_NOME_AUT();
//						} catch (EccezioneDB e) {
//							log.error("Errore lettura autore, la ignoro",e);
//						}

							u927+= IS1+"3"+c927[j].getC3_927();
						}
						fileUnimarc.add(u927);
					}
				}
			}

            return fileUnimarc;
   }

   private C950[] get950 (String bid) {
	   C950 c950 = null;
	   Collocazione950 col950 = null;
	   Esemplare950 ese950 = null;
	   ArrayList a950 = new ArrayList();
	   ArrayList arrlistinventari = new ArrayList();
	   ArrayList arrlistColl950 = new ArrayList();
	   ArrayList arrlistEse950 = new ArrayList();

		try {
//			System.out.println("dentro al TRY jdbc di get950(...)");
			Class.forName("org.postgresql.Driver");
			Connection conn = 
				DriverManager.getConnection(
					"jdbc:postgresql://" +
					"192.168.104.34:5432/sbnwebHib2", 
					"sbnweb", "sbnweb");
			Statement st1 = conn.createStatement();
			Statement st2 = conn.createStatement();
			Statement st3 = conn.createStatement();
			Statement st4 = conn.createStatement();
			boolean values = false;

			// -- inizializzazione variabili
			// di gestione bibliografica
			String nomeBiblio = null;
			String codBiblio = null;
			String codPolo = null;
			// di documento fisico
			String bidese = null;
			int cd_docese = 0;
			String consese = null;
			String conscoll = null;
			String codsez = null;
			String collocazione = null;
			String specificazione = null;
			String codbib = null;
			String codserie = null;
			int numinventario = 0;
			String disponibilita = null;
			String datascarico = null;
			String sequenza = null;
			String precisinv = null;

			int key_loc_coll = 0;
			int key_loc_coll_appo = -1;

			ResultSet rs1 = null;
			ResultSet rs2 = null;
			ResultSet rsese = null;
			ResultSet rscol = null;
			ResultSet rsinv = null;
			int count950 = -1;
			int countEsemplare = -1;

			// esemplari / collocazioni / inventari collocati
			ArrayList arrEsemplari;
			ArrayList arrCollocazioniEse;
			ArrayList arrConsCollocazioniEse;
			ArrayList arrInventariCollocatiEse;
			// collocazioni / inventari collocati
			ArrayList arrCollocazioni;
			ArrayList arrConsCollocazioni;
			ArrayList arrInventariCollocati;
			// inventari non collocati
			ArrayList arrInventariNonCollocati;
			String inventario = null;

			// from gestione bibliografica
			// select nome biblioteca, codice polo, codice biblioteca
			ResultSet rsbiblio = st1.executeQuery(
					"SELECT tb1.nom_biblioteca, tb1.cd_bib, tb1.cd_polo " +
					"FROM sbnweb.tbf_biblioteca as tb1 " +
					"INNER JOIN sbnweb.tr_tit_bib as tb2 " +
						"ON tb2.cd_polo=tb1.cd_polo " +
						"and tb2.cd_biblioteca=tb1.cd_bib " +
					"WHERE tb2.bid='"+bid+"' and tb2.fl_canc!='S'" //and tb2.fl_possesso='s'
					);

			while (rsbiblio.next()) {
				// biblioteca presente in polo

//System.out.println("dentro al WHILE rsbiblio.next()");

				values = false;

				// ciclo biblioteca
				nomeBiblio = rsbiblio.getString("nom_biblioteca");
				codBiblio = rsbiblio.getString("cd_bib");
				codPolo = rsbiblio.getString("cd_polo");

				// reinizializza a ogni ciclo ( = nuova biblioteca)
				arrEsemplari = new ArrayList();
				arrCollocazioniEse = new ArrayList();
				arrConsCollocazioniEse = new ArrayList();
				arrInventariCollocatiEse = new ArrayList();
				arrInventariNonCollocati = new ArrayList();
				arrInventariCollocati = new ArrayList();
				arrCollocazioni = new ArrayList();
				arrConsCollocazioni = new ArrayList();

				rs1 = st2.executeQuery(
						// inventari
						"SELECT tbinv.cd_bib, tbinv.cd_serie, tbinv.cd_inven, " +
							"tbinv.flg_disp, tbinv.data_scarico, tbinv.seq_coll, " +
							"tbinv.precis_inv, 0 as tbcolkey_loc, ' ' as tbcolcd_sez, " +
							"' ' as tbcolcd_loc, ' ' as tbcolspec_loc, '$' as tbcolconsis, " +
							"' ' as tbesebid, 0 as tbesecd_doc, '$' as tbesecons_doc " +
						"FROM sbnweb.tbc_inventario as tbinv " +
						"WHERE tbinv.bid = '"+bid+"' " +
							"and tbinv.key_loc is null " +
							"and not tbinv.fl_canc = 'S'" +
						"UNION " +
						// inventari / collocazioni
						"SELECT tbinv.cd_bib, tbinv.cd_serie, tbinv.cd_inven, tbinv.flg_disp, " +
							"tbinv.data_scarico, tbinv.seq_coll, tbinv.precis_inv, tbcol.key_loc, " +
							"tbcol.cd_sez, tbcol.cd_loc, tbcol.spec_loc, tbcol.consis, " +
							"' ' as tbesebid, 0 as tbesecd_doc, '$'  as tbesecons_doc " +
						"FROM sbnweb.tbc_inventario as tbinv, " +
							"sbnweb.tbc_collocazione as tbcol " +
						"WHERE tbinv.bid = '"+bid+"' " +
							"and tbinv.key_loc is not null " +
							"and tbinv.key_loc = tbcol.key_loc " +
							"and tbcol.bid_doc is null " +
							"and not tbinv.fl_canc = 'S' " +
								"and not tbcol.fl_canc ='S'" +
						"UNION " +
						// inventari / collocazioni / esemplari
						"SELECT tbinv.cd_bib, tbinv.cd_serie, tbinv.cd_inven, tbinv.flg_disp, " +
							"tbinv.data_scarico, tbinv.seq_coll, tbinv.precis_inv, tbcol.key_loc, " +
							"tbcol.cd_sez, tbcol.cd_loc, tbcol.spec_loc, tbcol.consis, tbese.bid, " +
							"tbese.cd_doc, tbese.cons_doc " +
						"FROM sbnweb.tbc_inventario as tbinv, " +
							"sbnweb.tbc_collocazione as tbcol, " +
							"sbnweb.tbc_esemplare_titolo as tbese " +
						"WHERE tbinv.bid = '"+bid+"' " +
							"and tbinv.key_loc is not null " +
							"and tbinv.key_loc = tbcol.key_loc " +
							"and tbcol.bid_doc is not null " +
							"and tbcol.cd_biblioteca_doc = tbese.cd_biblioteca " +
							"and tbcol.bid_doc = tbese.bid " +
							"and tbcol.cd_doc = tbese.cd_doc " +
							"and not tbinv.fl_canc = 'S' " +
								"and not tbcol.fl_canc ='S' " +
								"and not tbese.fl_canc ='S'" +
						"ORDER BY 1, 13, 14, 8, 4, 2, 3");

				while (rs1.next()){

//System.out.println("dentro al WHILE rs1.next()");

					if (!values) values = true;

					// campi esemplare
					bidese = rs1.getString("tbesebid");
					cd_docese = rs1.getInt("tbesecd_doc");
					consese = rs1.getString("tbesecons_doc");
					// campi collocazione
					conscoll = rs1.getString("tbcolconsis");
					codsez = rs1.getString("tbcolcd_sez");
					collocazione = rs1.getString("tbcolcd_loc");
					specificazione = rs1.getString("tbcolspec_loc");
					key_loc_coll = rs1.getInt("tbcolkey_loc");
					// campi inventario
					codserie = rs1.getString("cd_serie");
					numinventario = rs1.getInt("cd_inven");
					disponibilita = rs1.getString("flg_disp");
					datascarico = rs1.getString("data_scarico");
					sequenza = rs1.getString("seq_coll");
					precisinv = rs1.getString("precis_inv");

//System.out.println("");
//System.out.println("___[consese:"+consese+"]___");
//System.out.println("___[key_loc_coll:"+key_loc_coll+"]___");
//System.out.println("___[cd_docese:"+cd_docese+"]___");

					// costruzione stringa inventario corrente
					inventario = codBiblio + codserie + numinventario;
					if (disponibilita==" ") inventario += "2";
					else inventario += "5";

//System.out.println("["+datascarico+"]__["+sequenza+"]__["+sequenza.trim()+"]__["+precisinv+"]");

					if (datascarico==null) inventario += "        ";
					else inventario += datascarico;
					// sequenza + precisazione potrebbero essere entrambi 
					// valorizzati con tot. nr. di blank "inutili"
					if (sequenza.trim().length()>0) {
						if ((precisinv==null)||(precisinv.equals("$"))||(precisinv.trim().length()==0)){
							// precisazione di inventario vuota
							// aggiungi sequenza senza spazi inutili
							inventario += sequenza.trim();
						} else {
							// precisazione di inventario valorizzata
							// aggiungi sequenza con spazi eventuali, 
							// necessari per la corretta posizione della 
							// precis. inv.
							inventario += sequenza;
						}
					}
					if ((precisinv!=null)&&(!precisinv.equals("$"))){
						if (precisinv.trim().length()>0) inventario += precisinv.trim();
					}
					///////////////////////////////////////////

					if (key_loc_coll == 0){
						// key_loc su collocazione è zero
						// inventario senza collocazione
						arrInventariNonCollocati.add(inventario);
					} else if (cd_docese == 0){
						// senza esemplare
						// inventario con collocazione
						arrInventariCollocati.add(inventario);
						// collocazioni e consistenze di collocazione
						// controlla la key_loc dell'elemento precedente 
						// e la confronta con quella dell'elemento corrente
						if (key_loc_coll_appo==key_loc_coll){
							// stessa collocazione del precedente
							arrConsCollocazioni.add(null);
							arrCollocazioni.add(null);
						} else {
							if ((conscoll!=null)&&(!conscoll.equals("$"))) arrConsCollocazioni.add(conscoll);
							else arrConsCollocazioni.add(null);
							arrCollocazioni.add(codsez + collocazione + specificazione);
						}
					} else {
//System.out.println("entro in else");
//System.out.println("conscoll         ["+conscoll+"]");
//System.out.println("key_loc_coll_appo["+key_loc_coll_appo+"]");
//System.out.println("key_loc_coll     ["+key_loc_coll+"]");
						// esemplare presente
						// inventario con collocazione
						arrInventariCollocatiEse.add(inventario);
						// esemplari, collocazioni e consistenze di collocazione
						// controlla la key_loc dell'elemento precedente 
						// e la confronta con quella dell'elemento corrente
						if (key_loc_coll_appo==key_loc_coll){
//System.out.println("add null");
							arrEsemplari.add(null);
							// stessa collocazione del precedente
							arrConsCollocazioniEse.add(null);
							arrCollocazioniEse.add(null);
						} else {
//System.out.println("add "+consese);
							arrEsemplari.add(consese);
							if ((conscoll!=null)&&(!conscoll.equals("$"))) arrConsCollocazioniEse.add(conscoll);
							else arrConsCollocazioniEse.add(null);
							arrCollocazioniEse.add(codsez + collocazione + specificazione);
						}
					}

					// conserva l'ultimo valore della key_loc
					key_loc_coll_appo = key_loc_coll;

				} // end while su rs1

// output
//System.out.println("nome biblio: "+nomeBiblio);
//System.out.println("arr. Esemplari: "+arrEsemplari);
//System.out.println("__"+arrEsemplari.size());
//System.out.println("arr. Cons.Collocazioni Ese: "+arrConsCollocazioniEse);
//System.out.println("__"+arrConsCollocazioniEse.size());
//System.out.println("arr. Collocazioni Ese: "+arrCollocazioniEse);
//System.out.println("__"+arrCollocazioniEse.size());
//System.out.println("arr. Inventari Collocati Ese: "+arrInventariCollocatiEse);
//System.out.println("__"+arrInventariCollocatiEse.size());
//System.out.println("arr. Cons.Collocazioni: "+arrConsCollocazioni);
//System.out.println("__"+arrConsCollocazioni.size());
//System.out.println("arr. Collocazioni: "+arrCollocazioni);
//System.out.println("__"+arrCollocazioni.size());
//System.out.println("arr. Inventari Collocati: "+arrInventariCollocati);
//System.out.println("__"+arrInventariCollocati.size());
//System.out.println("arr. Inventari non Collocati: "+arrInventariNonCollocati);
//System.out.println("__"+arrInventariNonCollocati.size());

////////////////// COSTRUZIONE OGGETTO 950
				if (values) {
					c950 = new C950();
					ese950 = new Esemplare950();
					// nome biblioteca
					c950.setA_950(nomeBiblio);
					// inventari non collocati
					if (arrInventariNonCollocati.size()>0){
						c950.setE_950((String[])arrInventariNonCollocati.toArray(new String[arrInventariNonCollocati.size()]));
					}

					// gli arraylist di esemplari, consistenze di 
					// collocazioni, collocazioni e inventari collocati, 
					// hanno tutti la stessa lunghezza.
					// N.B. possibile presenza di valori empty ""

					// esemplare -> collocazioni -> inventari
					if (arrEsemplari.size()>0){
						for (int ies=0; ies<arrEsemplari.size(); ies++){
							// esemplare corrente
							if ( ((String)arrEsemplari.get(ies))!=null ){
								if (ese950==null){
									ese950 = new Esemplare950();
									ese950.setB_950((String)arrEsemplari.get(ies)); //cons.ese.
								}
							}
							// collocazioni / inventari legati all'esemplare corrente
							for (int i=0; i<arrConsCollocazioniEse.size(); i++){
								// se col950 non è null, continua ad operare 
								// sullo stesso oggetto aggiungendo le info
								if ( ((String)arrCollocazioniEse.get(i))!=null ){
									if (col950==null){
										col950 = new Collocazione950();
										col950.setC_950((String)arrConsCollocazioniEse.get(i)); //cons.coll.
										col950.setD_950((String)arrCollocazioniEse.get(i)); //coll.
									}
									arrlistinventari.add((String)arrInventariCollocatiEse.get(i)); //inv.
								} else {
									arrlistinventari.add((String)arrInventariCollocatiEse.get(i)); //inv.
								}
								if (i<(arrCollocazioniEse.size()-1)){
									if (((String)arrCollocazioniEse.get(i+1))!=null){
										// la collocazione all'indice successivo è 
										// valorizzata: sarà una nuova riga
										col950.setE_950((String[])arrlistinventari.toArray(new String[arrlistinventari.size()]));
										arrlistColl950.add(col950);
										col950 = null;
									}
								} else arrlistColl950.add(col950);
							}
							ese950.setCollocazione950((Collocazione950[])arrlistColl950.toArray(new Collocazione950[arrlistColl950.size()]));
							arrlistEse950.add(ese950);
							// reinizializza
							arrlistColl950 = new ArrayList();
							ese950 = null;
						}
						ese950 = null;
						col950 = null;
						c950.setEsemplare950((Esemplare950[])arrlistEse950.toArray(new Esemplare950[arrlistEse950.size()]));
						//c950.setCollocazione950((Collocazione950[])arrlistColl950.toArray(new Collocazione950[arrlistColl950.size()]));
					}

					// reinizializza inventari per il prossimo 
					// trattamento delle collocazioni
					arrlistinventari = new ArrayList();
					//arrlistColl950 = new ArrayList();

					// collocazione -> inventari
					if (arrCollocazioni.size()>0){
						for (int i=0; i<arrConsCollocazioni.size(); i++){
							// se col950 non è null, continua ad operare 
							// sullo stesso oggetto aggiungendo le info
							if ( ((String)arrCollocazioni.get(i))!=null ){
								if (col950==null){
									col950 = new Collocazione950();
									col950.setC_950((String)arrConsCollocazioni.get(i)); //cons.coll.
									col950.setD_950((String)arrCollocazioni.get(i)); //coll.
								}
								arrlistinventari.add((String)arrInventariCollocati.get(i)); //inv.
							} else {
								arrlistinventari.add((String)arrInventariCollocati.get(i)); //inv.
							}
							if (i<(arrCollocazioni.size()-1)){
								if (((String)arrCollocazioni.get(i+1))!=null){
									// la collocazione all'indice successivo è 
									// valorizzata: sarà una nuova riga
									col950.setE_950((String[])arrlistinventari.toArray(new String[arrlistinventari.size()]));
									arrlistColl950.add(col950);
									col950 = null;
								}
							} else if (i==(arrCollocazioni.size()-1)){
								// ultima riga
								col950.setE_950((String[])arrlistinventari.toArray(new String[arrlistinventari.size()]));
								arrlistColl950.add(col950);
							} else arrlistColl950.add(col950);
						}
						col950 = null;
						c950.setCollocazione950((Collocazione950[])arrlistColl950.toArray(new Collocazione950[arrlistColl950.size()]));
					}
					a950.add(c950);
				}
//////////////////////////////////////////

			} // end while su nome biblio

			// Chiudo statements, resultsets e connessione
			st1 = null;
			st2 = null;
			st3 = null;
			st4 = null;
			rsese = null;
			rscol = null;
			rsinv = null;
			conn = null;
/*			if (!rsese.isClosed()) rsese.close();
			if (!rscol.isClosed()) rscol.close();
			if (!rsinv.isClosed()) rsinv.close();
			if (!st1.isClosed()) st1.close();
			if (!st2.isClosed()) st2.close();
			if (!st3.isClosed()) st3.close();
			if (!st4.isClosed()) st4.close();
			if (!conn.isClosed()) conn.close();*/








/*
			// esemplare
			rsese = st.executeQuery(
					"SELECT cons_doc " + //cd_doc, 
					"FROM sbnweb.tbc_esemplare_titolo " +
					"WHERE bid='"+bid+"' " +
						"and cd_biblioteca='"+codBiblio+"' " +
						"and cd_polo='"+codPolo+"' " +
						"and fl_canc!='S'");
			// collocazione
			rscol = st.executeQuery(
					"SELECT consis, cd_sez, cd_loc, spec_loc " +
					"FROM sbnweb.tbc_collocazione " +
					"WHERE bid='"+bid+"' " +
						"and cd_biblioteca_doc=' FI' " +
						"and cd_polo_doc='CSW' " +
						"and fl_canc!='S'");
			// inventario
			rsinv = st.executeQuery(
					"SELECT cd_bib, cd_serie, cd_inven, flg_disp, " +
						"data_scarico, seq_coll, precis_inv " +
					"FROM sbnweb.tbc_inventario " +
					"WHERE key_loc=116 " +
						"and fl_canc!='S'");

*/
		} catch (ClassNotFoundException cnfe) {
			log.error("Errore caricamento JdbcOdbcDriver, lo ignoro",cnfe);
			cnfe.printStackTrace();
		} catch (java.sql.SQLException sqle) {
			log.error("Errore lettura titolo JDBC, la ignoro",sqle);
			sqle.printStackTrace();
		}

		C950[] arr950 = null;
		// se array è valorizzato 	-> return array
		// se array è vuoto 		-> return null
		if (a950.size()==0) a950 = null;
		else arr950 = (C950[])a950.toArray(new C950[a950.size()]);

		return arr950;
   }



	   /**
	* unimarc 950 (ripebitile per ogni biblioteca che possiede inventari)
	* 
	* indicatore1	spazio
	* indicatore2	livello gerarchico della notizia (stessi valori presenti nella Guida)
	* 				0 = senza livelli (monografie senza livelli e periodici)
	* 				1 = livello alto (monografia superiore)
	* 				2 = livello basso (monografia inferiore o intermedia)
	* 
	* $a	descrizione della biblioteca (non ripetibile)
	* $b	consistenza dell'esemplare
	* $c	consistenza della collocazione
	* $d	dati di collocazione [con il seguente tracciato]
	* 		1) sezione (10  caratteri)
	* 		2) collocazione (24  caratteri)
	* 		3) specificazione (12 caratteri)
	* $e	dati di inventariazione [con il seguente tracciato]
	* 		1) identificativo dell'inventario
	* 			 identificativo della biblioteca (2 caratteri) [pos 2]
	* 			 serie inventariale	(3 caratteri) [pos 5]
	* 			 numero di inventario (9 caratteri)  [pos 14]
	* 		2) stato di disponibilità (1 carattere) [pos 15]
	* 			 1 = inventario non catalogato
	* 			 2 = inventario collocato etichettato ma non prestabile
	* 			 3 = inventario catalogato ma non collocato
	* 			 4 = inventario in prestito
	* 			 5 = inventario disponibile
	* 		3) data del movimento (8 caratteri) [da pos 16 a 23 compresa]
	* 		4) sequenza della collocazione (20 caratteri) [da pos 24 a 43 compresa]
	* 		5) precisazioni dell'inventario (fino a 500 caratteri)  [da pos 44 (compresa) in poi]
	* $k	codice biblioteca
	* 
	* @param c950 array di oggetti C950
	*/
   private ArrayList elabora950 (DocumentoType documento, ArrayList fileUnimarc) {

//	   DatiDocType unimarc = documento.getDocumentoTypeChoice().getDatiDocumento();
//	   C950[] c950 = get950(unimarc.getT001());

//C950[] c950 = get950("NAP0085399");
C950[] c950 = get950("CSW0001440");
//if (c950 != null) elabora950(c950);

	if (c950 != null){

		   String u950 = "";
		   String u950total = "";
		   Esemplare950[] ese950;
		   Collocazione950[] col950;

		   for (int i=0; i<c950.length; i++){
			   u950 = "950  ";

			   // nome biblioteca
			   u950+= IS1+"a"+c950[i].getA_950().trim();

			   // inventari non collocati
			   for (int ie=0; ie<c950[i].getE_950Count(); ie++){
				   if (c950[i].getE_950(ie)!=null) u950+= IS1+"e"+c950[i].getE_950(ie);
			   }

			   // consistenze di esemplari
			   ese950 = c950[i].getEsemplare950();
			   for (int is=0; is<ese950.length; is++){
				   if (ese950[is].getB_950()!=null) u950+= IS1+"b"+ese950[is].getB_950();
				   // consistenze di collocazioni
				   // collocazioni
				   // inventari collocati
				   col950 = ese950[is].getCollocazione950();
				   for (int ic=0; ic<col950.length; ic++){
					   if (col950[ic].getC_950()!=null) u950+= IS1+"c"+col950[ic].getC_950();
					   u950+= IS1+"d"+col950[ic].getD_950();
					   for (int ii=0; ii<col950[ic].getE_950Count(); ii++){
						   if (col950[ic].getE_950(ii)!=null) u950+= IS1+"e"+col950[ic].getE_950(ii);
					   }
				   }
			   }

			   // consistenze di collocazioni
			   // collocazioni
			   // inventari collocati
			   col950 = c950[i].getCollocazione950();
			   for (int ic=0; ic<col950.length; ic++){
				   if (col950[ic].getC_950()!=null) u950+= IS1+"c"+col950[ic].getC_950();
				   u950+= IS1+"d"+col950[ic].getD_950();
				   for (int ii=0; ii<col950[ic].getE_950Count(); ii++){
					   if (col950[ic].getE_950(ii)!=null) u950+= IS1+"e"+col950[ic].getE_950(ii);
				   }
			   }

			   u950total += u950;
			   if (u950total.length()>9999) {
				   fileUnimarc.add( u950.substring(0, u950.length()-(u950total.length()-999)) );
				   break;
			   } else {
				   fileUnimarc.add(u950);
			   }
		   }

		   u950 = null;
		   u950total = null;
		   ese950 = null;
		   col950 = null;

		}
		return fileUnimarc;
   }


   private String elaboraLivelloIntellettuale(DatiDocType unimarc){
       if (unimarc instanceof ModernoType)  {
           ModernoType moderno = (ModernoType) unimarc;
           C105 c105 = moderno.getT105();
           if (c105 != null) {
               String u105[] = c105.getA_105_4();
               for (int j = 0; j < u105.length; j++) {
                   if (u105[j] != null && u105[j].equalsIgnoreCase("R"))
                    return "a  ";
               }
           }   
       }      
       return "|||";
   }
   public ArrayList elaboraDatiEleAut (ElementAutType documento,ArrayList fileUnimarc) {                      
                        DatiElementoType unimarc = documento.getDatiElementoAut();
//          000 - guida
                         String u000 = "000";
                         u000 +="?????"; // da valorizzare
                         u000 +="n";
                         u000 +="x";
                         u000 +="  ";
         if (unimarc instanceof AutorePersonaleType) {
            u000 +="a";
         } else if (unimarc instanceof EnteType) {
             u000 +="b";
         } else if (unimarc instanceof TitoloUniformeType || unimarc instanceof TitoloUniformeMusicaType ) {
             u000 +="f";
         } else if (unimarc instanceof SoggettoType) {
             u000 +="j";
         } else if (unimarc instanceof LuogoType) {
             u000 +="k";
         } else {
             u000 +=" ";
         }
                         u000 +="22";
                         u000 +="!!!!!"; // da valorizzare
                         u000 +=" ";
                         u000 +="  45  ";          
                         fileUnimarc.add(u000);             
// 001
     String id;
      if (unimarc instanceof ClasseType || unimarc instanceof RepertorioType) {
        id = unimarc.getT001();
      } else {
        id = formattaID(unimarc.getT001());
      }
                        String u001 = "001";
                        u001 += id;
                        ID.put(id, unimarc.getT001());
                        fileUnimarc.add(u001);
// 005
                        String u005 = "005";
                        u005 += unimarc.getT005();
                        fileUnimarc.add(u005);  
//          015
                A015 a015 = null;
                        String u015 = "015";
                        if (unimarc instanceof TitoloUniformeType) {
                                   TitoloUniformeType titoloU = (TitoloUniformeType) unimarc;
                                   a015 = titoloU.getT015();
                        } else if (unimarc instanceof TitoloUniformeMusicaType) {
                                   TitoloUniformeMusicaType titoloUM = (TitoloUniformeMusicaType) unimarc;
                                   a015 = titoloUM.getT015();
                        } else if (unimarc instanceof EnteType) {
                                   EnteType ente = (EnteType) unimarc;
                                   a015 =  ente.getT015();
                        } else if (unimarc instanceof AutorePersonaleType) {
                                   AutorePersonaleType autoreP = (AutorePersonaleType) unimarc;
                                   a015 = autoreP.getT015();
                        }
                        if (a015 != null) {
                                   u015 += IS1+"a"+a015.getA_015().trim();
                                   fileUnimarc.add(u015);
                        }
// 100
                        A100 c100 = unimarc.getT100();
                        if (c100 != null) {
                                   String u100 = "100  "+IS1+"a";
                                   u100 += c100.getA_100_0().toString().substring(0,4);
                                   u100 += c100.getA_100_0().toString().substring(5,7);
                                   u100 += c100.getA_100_0().toString().substring(8,10);
            if (unimarc.getLivelloAut().equals(SbnLivello.valueOf("97"))) {
                u100 +="a";
            } else {
                u100 +="x";
            }
                                   u100 +="itaa50      ba0";
                                   fileUnimarc.add(u100);
                        }                                 
// 101   
                        C101 c101 = null;
                        if (unimarc instanceof TitoloUniformeType) {
                                   TitoloUniformeType titoloU = (TitoloUniformeType) unimarc;        
                                   c101 = titoloU.getT101();          
                        }
                        if (unimarc instanceof TitoloUniformeMusicaType) {
                                   TitoloUniformeMusicaType titoloUM = (TitoloUniformeMusicaType) unimarc;        
                                   c101 = titoloUM.getT101();        
                        }                      
                        if (unimarc instanceof AutorePersonaleType) {
                                   AutorePersonaleType autoreP = (AutorePersonaleType) unimarc;           
                                   c101 = autoreP.getT101();        
                        }
                        if (unimarc instanceof EnteType) {
                                   EnteType ente = (EnteType) unimarc;    
                                   c101 = ente.getT101();
                        }          
                        if (c101 != null) {           
                                   String u101[] = c101.getA_101();
                                   if (u101.length > 0) {     
                                               String u101All = "101  ";
                                               for (int j = 0; j < u101.length; j++) {
                                                           u101All += IS1+"a"+u101[j];                 
                                               }
                                   fileUnimarc.add(u101All);
                                   }
                        }                      
// 102
                        if (unimarc instanceof AutorePersonaleType) {
                                   AutorePersonaleType autoreP = (AutorePersonaleType) unimarc;           
                                   String u102 = "102  "+IS1+"a";
                                   C102 c102 = autoreP.getT102();
                                   if (c102 != null) {           
                                               if (c102.getA_102() != null) {
                                                           u102 += c102.getA_102();
                                                           fileUnimarc.add(u102);
                                               }
                                   }
                        }
                        if (unimarc instanceof EnteType) {
                                   EnteType ente = (EnteType) unimarc;    
                                   String u102 = "102  "+IS1+"a";
                                   C102 c102 = ente.getT102();     
                                   if (c102 != null) {           
                                               if (c102.getA_102() != null) {
                                                           u102 += c102.getA_102();
                                                           fileUnimarc.add(u102);
                                               }
                                   } 
                        }    
//          152      
                        A152 a152 = null;
                        if (unimarc instanceof TitoloUniformeType) {
                                   TitoloUniformeType titoloU = (TitoloUniformeType) unimarc;        
                                    a152 = titoloU.getT152();          
                        }
                        if (unimarc instanceof TitoloUniformeMusicaType) {
                                   TitoloUniformeMusicaType titoloUM = (TitoloUniformeMusicaType) unimarc;        
                                   a152 = titoloUM.getT152();        
                        }                      
                        if (unimarc instanceof AutorePersonaleType) {
                                   AutorePersonaleType autoreP = (AutorePersonaleType) unimarc;           
                                   a152 = autoreP.getT152();        
                        }
                        if (unimarc instanceof EnteType) {
                                   EnteType ente = (EnteType) unimarc;    
                                   a152 = ente.getT152();
                        }          
                        if (a152 != null) {           
                                   if (a152.getA_152() != null) {      
                                       String u152 = "152  ";
                                               u152 += IS1+"a"+a152.getA_152();                   
                                fileUnimarc.add(u152);
                                   }
                        }
 
// 200
                        if (unimarc instanceof AutorePersonaleType) {
                                    AutorePersonaleType autoreP = (AutorePersonaleType) unimarc;           
                                   A200 a200 = autoreP.getT200();
                                   if (a200 != null) {
                                               String u200 = "200 ";
                                               u200 = elabora200EleAut(u200, a200);
                                               fileUnimarc.add(u200);              
                                   }
                        }                                                                                           
 
// 210
                        if (unimarc instanceof EnteType) {
                                   EnteType ente = (EnteType) unimarc;    
                                   int tipoNome = ente.getTipoNome().getType();
                                   A210 a210 = ente.getT210();
                                   String u210 = "210";
                                   u210 = elabora210EleAut(u210, a210, tipoNome);                                                                                                                                                                                                                                                                                                                
                                   if (u210.length() > 5) fileUnimarc.add(u210);                   
                        }   
//          230
                        A230 a230 = null;
                        if (unimarc instanceof TitoloUniformeType)          {
                                   TitoloUniformeType titoloU = (TitoloUniformeType) unimarc;
                                   a230 = titoloU.getT230();
                        }
                        if (unimarc instanceof TitoloUniformeMusicaType)            {
                                   TitoloUniformeMusicaType titoloUM = (TitoloUniformeMusicaType) unimarc;
                                   a230 = titoloUM.getT230();
                        }
                        if (a230 != null) {
                                   String u230 = null;
                                   String u240 = elabora240EleAut(documento);
                                   if (u240.length() > 0) {
                                    u230 = "240  "+u240;
                                               if (a230.getA_230() != null) u230+= IS1+"1"+"230  "+IS1+"a"+a230.getA_230();
                                   } else {             
                                               if (a230.getA_230() != null) u230+= "230  "+IS1+"a"+a230.getA_230();
                                   }
                                   fileUnimarc.add(u230);
                        }          
 
//          250
                        if (unimarc instanceof SoggettoType) {
                                   SoggettoType soggetto = (SoggettoType) unimarc;        
                                   A250 a250 = soggetto.getT250();
                                   if (a250 != null) {
                                               String u250 = "250  ";
                                               if (a250.getA_250() != null) u250+= IS1+"a"+a250.getA_250();    
                                               String A250X [] = a250.getX_250();
                                               if (A250X.length > 0) {
                                                           for (int j = 0; j < A250X.length; j++) {
                                                                                  u250 += IS1+"x"+A250X[j];
                                                           }
                                               }  
                                               if (a250.getC2_250() != null) u250+= IS1+"2"+a250.getC2_250();                                                
                                               fileUnimarc.add(u250);              
                                    }
                        }                                                                                           
 
//          260
                        if (unimarc instanceof LuogoType) {
                                   LuogoType luogo = (LuogoType) unimarc;          
                                   A260 a260 = luogo.getT260();
                                   if (a260 != null) {
                                               String u260 = "260  ";
                                               u260 = elabora260EleAut(u260, a260);                                      
                                               fileUnimarc.add(u260);              
                                    }
                        }                                                                                                        
//          300
                        A300 a300 = null;
                        if (unimarc instanceof TitoloUniformeType) {
                                   TitoloUniformeType titoloU = (TitoloUniformeType) unimarc;        
                                   a300 = titoloU.getT300();          
                        }
                        if (unimarc instanceof TitoloUniformeMusicaType) {
                                   TitoloUniformeMusicaType titoloUM = (TitoloUniformeMusicaType) unimarc;        
                                   a300 = titoloUM.getT300();        
                        }                      
                        if (unimarc instanceof AutorePersonaleType) {
                                   AutorePersonaleType autoreP = (AutorePersonaleType) unimarc;           
                                   a300 = autoreP.getT300();        
                        }
                        if (unimarc instanceof EnteType) {
                                   EnteType ente = (EnteType) unimarc;    
                                   a300 = ente.getT300();
                        }
                        if (unimarc instanceof LuogoType) {
                                   LuogoType luogo = (LuogoType) unimarc;          
                                   a300 = luogo.getT300();
                        }          
                        String u300 = new String();
                        if (a300 != null) {
                                   if (a300.getA_300()!= null) u300+= "300  "+IS1+"a"+a300.getA_300();
                                   fileUnimarc.add(u300);              
                        }
//          676
                        A676 a676 = null;
                        if (unimarc instanceof ClasseType) {
                                   ClasseType classe = (ClasseType) unimarc;      
                                   a676 = classe.getClasseTypeChoice().getT676();           
                        }          
                        elabora676(a676,fileUnimarc);               
 
//          686
                        A686 a686 = null;
                        if (unimarc instanceof ClasseType) {
                                   ClasseType classe = (ClasseType) unimarc;      
                                   a686 = classe.getClasseTypeChoice().getT686();           
                        }                      
                        elabora686(a686,fileUnimarc);               
 
//          801
                        A801 a801 = null;
                        if (unimarc instanceof TitoloUniformeType) {
                                   TitoloUniformeType titoloU = (TitoloUniformeType) unimarc;        
                                   a801 = titoloU.getT801();          
                        }
                        if (unimarc instanceof TitoloUniformeMusicaType) {
                                   TitoloUniformeMusicaType titoloUM = (TitoloUniformeMusicaType) unimarc;        
                                   a801 = titoloUM.getT801();        
                        }                      
                        if (unimarc instanceof AutorePersonaleType) {
                                   AutorePersonaleType autoreP = (AutorePersonaleType) unimarc;           
                                   a801 = autoreP.getT801();        
                        }
                        if (unimarc instanceof EnteType) {
                                   EnteType ente = (EnteType) unimarc;    
                                   a801 = ente.getT801();
                        }
 
                        String u801 = "801 3";
                        if (a801 != null && a801.getA_801()!= null) u801+= IS1+"a"+a801.getA_801();
        else u801+= IS1+"a"+"IT";
            if (a801 != null && a801.getB_801()!= null) u801+= IS1+"b"+a801.getB_801();
        else u801+= IS1+"b"+"ICCU";
        //u801+= IS1+"c"+unimarc.getT005().substring(0,8);
        Calendar c = Calendar.getInstance();
        u801+= IS1+"c"+ c.get(Calendar.YEAR)+ c.get(Calendar.MONTH) + c.get(Calendar.DAY_OF_MONTH);
        fileUnimarc.add(u801);   
           
//          830
                        A830 a830 = null;
                        if (unimarc instanceof TitoloUniformeType) {
                                   TitoloUniformeType titoloU = (TitoloUniformeType) unimarc;        
                                   a830 = titoloU.getT830();          
                        }
                        if (unimarc instanceof TitoloUniformeMusicaType) {
                                   TitoloUniformeMusicaType titoloUM = (TitoloUniformeMusicaType) unimarc;        
                                   a830 = titoloUM.getT830();        
                        }                      
                        if (unimarc instanceof AutorePersonaleType) {
                                   AutorePersonaleType autoreP = (AutorePersonaleType) unimarc;           
                                   a830 = autoreP.getT830();        
                        }
                        if (unimarc instanceof EnteType) {
                                   EnteType ente = (EnteType) unimarc;    
                                   a830 = ente.getT830();
                        }
                        String u830 = "830  ";
                        if (a830 != null) {
                                    if (a830.getA_830()!= null) u830+= IS1+"a"+a830.getA_830();
                                   if (u830.length() > 5) fileUnimarc.add(u830);
                        }
//          856
                        C856 c856[] = null;
                        if (unimarc instanceof MarcaType) {
                                   MarcaType marca = (MarcaType) unimarc;        
                                   c856 = marca.getT856();           
                        }                      
                        String u856 = "856  ";
                        if (c856 != null) {
                                   if (c856.length > 0) {
                                               for (int j = 0; j < c856.length; j++) {
                                                           if (c856[j].getU_856()!= null) u856+= IS1+"u"+c856[j].getU_856();
                                               }
                                               fileUnimarc.add(u856);                                     
                                   }
                        }          
//          921
                        A921 a921 = null;
                        String t001 = new String();
                        if (unimarc instanceof MarcaType) {
                                   MarcaType marca = (MarcaType) unimarc;        
                                   a921 = marca.getT921();
                                   t001 = formattaID(marca.getT001());      
                        }                      
                        String u921 = "921  "+IS1+"a"+t001;
                        if (a921 != null) {
                                   u921 = elabora921(u921, a921, documento.getLegamiElementoAut());
                                   fileUnimarc.add(u921);                                     
                        }                      
//          928
                        A928 a928 = null;
                        if (unimarc instanceof TitoloUniformeMusicaType) {
                                   TitoloUniformeMusicaType titoloUM = (TitoloUniformeMusicaType) unimarc;        
                                   a928 = titoloUM.getT928();        
                        }                      
                        String u928 = "928  ";
                        if (a928 != null) {
                                   String A928A [] = a928.getA_928();
                                   if (A928A.length > 0) {
                                               for (int j = 0; j < A928A.length; j++) {
                                                                       u928 += IS1+"a"+A928A[j];
                                               }
                                   }  
                                   if (a928.getB_928()!= null) u928+= IS1+"b"+a928.getB_928();
                                   if (a928.getC_928()!= null) u928+= IS1+"c"+a928.getC_928();
                                   if (u928.length() > 5) fileUnimarc.add(u928);
                        }
//          929
                        A929 a929 = null;
                        if (unimarc instanceof TitoloUniformeMusicaType) {
                                   TitoloUniformeMusicaType titoloUM = (TitoloUniformeMusicaType) unimarc;        
                                   a929 = titoloUM.getT929();        
                        }                      
                        String u929 = "929  ";
                        if (a929 != null) {
                                   if (a929.getA_929()!= null) u929+= IS1+"a"+a929.getA_929();
                                   if (a929.getB_929()!= null) u929+= IS1+"b"+a929.getB_929();
                                   if (a929.getC_929()!= null) u929+= IS1+"c"+a929.getC_929();
                                   if (a929.getD_929()!= null) u929+= IS1+"d"+a929.getD_929();
                                   if (a929.getE_929()!= null) u929+= IS1+"e"+a929.getE_929();
                                   if (a929.getF_929()!= null) u929+= IS1+"f"+a929.getF_929();
                                   if (a929.getG_929() != null) {
                String g929 = a929.getG_929();
                int iAst = g929.indexOf('*');
                String g;
                if (iAst < 0) {
                    g = g929;
                } else {
                    if (iAst > 0) {
                        g = NSB + g929.substring(0, iAst) + NSE
                                + g929.substring(iAst + 1);
                    } else {
                        g = g929.substring(1);
                    }
                    //Rimuovo anche tutti gli altri asterischi:
                    while ((iAst = g.indexOf('*')) >= 0) {
                        g = g.substring(0, iAst) + g.substring(iAst + 1);
                    }
                }
                u929 += IS1 + "g" + g;
            }
                                   if (a929.getH_929()!= null) u929+= IS1+"h"+a929.getH_929();
                                   if (a929.getI_929()!= null) u929+= IS1+"i"+a929.getI_929();
                                   if (u929.length() > 5) fileUnimarc.add(u929);
                        }
//          930
                        A930 a930 = null;
                        if (unimarc instanceof RepertorioType) {
                                   RepertorioType repertorio = (RepertorioType) unimarc;    
                                   a930 = repertorio.getT930();      
                        }                      
                        String u930 = "930  ";
                        if (a930 != null) {
                                   if (a930.getA_930()!= null) u930+= IS1+"a"+a930.getA_930();
                                   if (a930.getB_930()!= null) u930+= IS1+"b"+a930.getB_930();
                                   if (a930.getC2_930()!= null) u930+= IS1+"2"+a930.getC2_930();
                                   fileUnimarc.add(u930);                                     
                        }                                 
//          931
                        A931 a931 = null;
                        if (unimarc instanceof DescrittoreType) {
                                   DescrittoreType descrittore = (DescrittoreType) unimarc;
                                   a931 = descrittore.getT931();    
                        }                      
                        String u931 = "931  ";
                        if (a931 != null) {
                                   if (a931.getA_931()!= null) u931+= IS1+"a"+a931.getA_931();
                                   if (a931.getB_931()!= null) u931+= IS1+"b"+a931.getB_931();
                                   if (a931.getC2_931()!= null) u931+= IS1+"2"+a931.getC2_931();
                                   fileUnimarc.add(u931);                                     
                        }
 
                        return fileUnimarc;
   }


   /**
    * Returns an ArrayList object.
    * Adds unimarc tags from 311 to 791.
    * Adds ties too.
    * 
    * @param documento
    * @param fileUnimarc
    * @return ArrayList object
    * @throws InfrastructureException
    */
   public ArrayList elaboraDatiLegameDoc (DocumentoType documento,ArrayList fileUnimarc) throws InfrastructureException {                                              
// andiamo sui legami del documento
        String bid = null;
        if (documento.getDocumentoTypeChoice().getDatiDocumento()!= null)
            bid =documento.getDocumentoTypeChoice().getDatiDocumento().getT001();
        else
           bid = documento.getDocumentoTypeChoice().getDatiTitAccesso().getT001();
        log.info("ElaboraDatiLegameDoc-> doc: "+bid);
        LegamiType legDocUnimarc [] = documento.getLegamiDocumento();
        if (legDocUnimarc.length > 0) {
                   for (int j = 0; j < legDocUnimarc.length; j++) {
                               ArrivoLegame aLegame[] = legDocUnimarc[j].getArrivoLegame();
                               if (aLegame.length > 0) {
                                           for (int jj = 0; jj < aLegame.length; jj++) {
                                                       LegameDocType legDoc = aLegame[jj].getLegameDoc();
                                                       // se è presente la nota il secondo indicatore deve valere 0, altrimenti 1
                                                       String indicatore = " 1"; 
                                                       if (legDoc != null) {
//  311
                                                                  if (legDoc.getNoteLegame()!= null) {
                                                                              String u311 = "311  ";
                                                                              u311 += IS1+"a"+legDoc.getNoteLegame();       
                                                                              u311 += IS1+"9"+formattaID(legDoc.getIdArrivo());          
                                                                              fileUnimarc.add(u311);
                                                                              indicatore = " 0";                      
                                                                  }
                                                                  if (legDoc.getTipoLegame().getType() == SbnLegameDoc.VALUE_1_TYPE) { 
//  410
                                                                              String u410 = "410" + indicatore;
                                                                              DocumentoType L410 = legDoc.getDocumentoLegato();
                                                                              u410 += IS1+"1001"+formattaID(L410.getDocumentoTypeChoice().getDatiDocumento().getT001());
                                                                              C200 L200 = L410.getDocumentoTypeChoice().getDatiDocumento().getT200();
                                                                              u410 += IS1+"1"+elabora200(L200);      
                                                                              if (legDoc.getSequenza() != null) u410 += IS1+"v"+legDoc.getSequenza().trim();
                                                                              fileUnimarc.add(u410);
                if (opac == false)
                                                                              elaboraLegame400(L410);                                
                                                                  }
                                                                  if (legDoc.getTipoLegame().getType() == SbnLegameDoc.VALUE_2_TYPE) { 
//  422
                                                                              String u422 = "422" + indicatore;
                                                                              DocumentoType L422 = legDoc.getDocumentoLegato();
                                                                              u422 += IS1+"1001"+formattaID(L422.getDocumentoTypeChoice().getDatiDocumento().getT001());
                                                                              C200 L200 = L422.getDocumentoTypeChoice().getDatiDocumento().getT200();
                                                                              u422 += IS1+"1"+elabora200(L200);      
                                                                              fileUnimarc.add(u422);                          
                                                                  }
                                                                  if (legDoc.getTipoLegame().getType() == SbnLegameDoc.VALUE_3_TYPE) { 
//          430
                                                                              String u430 = "430" + indicatore;
                                                                              DocumentoType L430 = legDoc.getDocumentoLegato();
                                                                              u430 += IS1+"1001"+formattaID(L430.getDocumentoTypeChoice().getDatiDocumento().getT001());
                                                                              C200 L200 = L430.getDocumentoTypeChoice().getDatiDocumento().getT200();
                                                                              u430 += IS1+"1"+elabora200(L200);      
                                                                              fileUnimarc.add(u430);                          
                                                                  }                                                                     
                                                                  if (legDoc.getTipoLegame().getType() == SbnLegameDoc.VALUE_4_TYPE) { 
//          431
                                                                              String u431 = "431" + indicatore;
                                                                              DocumentoType L431 = legDoc.getDocumentoLegato();
                                                                              u431 += IS1+"1001"+formattaID(L431.getDocumentoTypeChoice().getDatiDocumento().getT001());
                                                                              C200 L200 = L431.getDocumentoTypeChoice().getDatiDocumento().getT200();
                                                                              u431 += IS1+"1"+elabora200(L200);      
                                                                              fileUnimarc.add(u431);                          
                                                                  }                                                                                            
                                                                  if (legDoc.getTipoLegame().getType() == SbnLegameDoc.VALUE_5_TYPE) { 
//          434
                                                                              String u434 = "434" + indicatore;
                                                                              DocumentoType L434 = legDoc.getDocumentoLegato();
                                                                              u434 += IS1+"1001"+formattaID(L434.getDocumentoTypeChoice().getDatiDocumento().getT001());
                                                                              C200 L200 = L434.getDocumentoTypeChoice().getDatiDocumento().getT200();
                                                                              u434 += IS1+"1"+elabora200(L200);      
                                                                              fileUnimarc.add(u434);                          
                                                                  }                                                                                            
                                                                  if (legDoc.getTipoLegame().getType() == SbnLegameDoc.VALUE_6_TYPE) { 
//          440
                                                                              String u440 = "440" + indicatore;
                                                                              DocumentoType L440 = legDoc.getDocumentoLegato();
                                                                              u440 += IS1+"1001"+formattaID(L440.getDocumentoTypeChoice().getDatiDocumento().getT001());
                                                                              C200 L200 = L440.getDocumentoTypeChoice().getDatiDocumento().getT200();
                                                                              u440 += IS1+"1"+elabora200(L200);      
                                                                              fileUnimarc.add(u440);                          
                                                                  }                                                                                            
                                                                  if (legDoc.getTipoLegame().getType() == SbnLegameDoc.VALUE_7_TYPE) { 
//          447
                                                                              String u447 = "447" + indicatore;
                                                                              DocumentoType L447 = legDoc.getDocumentoLegato();
                                                                              u447 += IS1+"1001"+formattaID(L447.getDocumentoTypeChoice().getDatiDocumento().getT001());
                                                                              C200 L200 = L447.getDocumentoTypeChoice().getDatiDocumento().getT200();
                                                                              u447 += IS1+"1"+elabora200(L200);      
                                                                              fileUnimarc.add(u447);                          
                                                                  }                                                         
                                                                  if (legDoc.getTipoLegame().getType() == SbnLegameDoc.VALUE_8_TYPE) { 
//          451
                                                                              String u451 = "451" + indicatore;
                                                                              DocumentoType L451 = legDoc.getDocumentoLegato();
                                                                              u451 += IS1+"1001"+formattaID(L451.getDocumentoTypeChoice().getDatiDocumento().getT001());
                                                                              C200 L200 = L451.getDocumentoTypeChoice().getDatiDocumento().getT200();
                                                                              u451 += IS1+"1"+elabora200(L200);      
                                                                              fileUnimarc.add(u451);                          
                                                                  }
                                                                  if (legDoc.getTipoLegame().getType() == SbnLegameDoc.VALUE_9_TYPE) {  
/*/  461 - 462 - 463
 * si imposta 462 quando il titolo di arrivo del legame è un livello intermedio (ICCU 26.2.2004)
 * si imposta 463 quando il legame il titolo base è uno spoglio (natura N)
 */
                                                                              String u461 = null;
                                                                              if (natura == SbnNaturaDocumento.valueOf("N")) {
                                                                                          u461 = "463" + indicatore;
                                                                              }else {
                                                                                          if (livelloG =="3") {
                                                                                                       u461 = "462" + indicatore;
                                                                                          } else {
                                                                                                       u461 = "461" + indicatore;
                                                                                          }
                                                                              }
                                                                              DocumentoType L461 = legDoc.getDocumentoLegato();
                                                                              u461 += IS1+"1001"+formattaID(L461.getDocumentoTypeChoice().getDatiDocumento().getT001());
                                                                              C200 L200 = L461.getDocumentoTypeChoice().getDatiDocumento().getT200();
                                                                              u461 += IS1+"1"+elabora200(L200);      
                                                                              if (legDoc.getSequenza() != null) u461 += IS1+"v"+legDoc.getSequenza().trim();
                if (L461.getLegamiDocumento().length > 0) {                                
                    u461 = elaboraDatiLegameTitAcc (L461, u461);
                }
                                                                              fileUnimarc.add(u461);  
                if (opac == false)
                                                                              elaboraLegame400(L461);
                                                                  }
                                                                  // 20.11.2003 Iccu (Roveri) ha deciso di non estrarre le 463
                                                                  /*if (legDoc.getTipoLegame().getType() == SbnLegameDoc.VALUE_10_TYPE) { 
//          463
                                                                                              String u463 = "463 1";
                                                                                              DocumentoType L463 = legDoc.getDocumentoLegato();
                                                                                              u463 += IS1+"1001"+formattaID(L463.getDocumentoTypeChoice().getDatiDocumento().getT001());
                                                                                              C200 L200 = L463.getDocumentoTypeChoice().getDatiDocumento().getT200();
                                                                                              u463 += IS1+"1"+elabora200(L200);      
                                                                                              if (legDoc.getSEQUENZA() != null) u463 += IS1+"v"+legDoc.getSEQUENZA();
                                                                                              fileUnimarc.add(u463);
                                                                                              elaboraLegame400(L463);                                
                                                                                  }           */
                                                                  if (legDoc.getTipoLegame().getType() == SbnLegameDoc.VALUE_11_TYPE) { 
//          464
                                                                              String u464 = "464" + indicatore;
                                                                              DocumentoType L464 = legDoc.getDocumentoLegato();
                                                                              u464 += IS1+"1001"+formattaID(L464.getDocumentoTypeChoice().getDatiDocumento().getT001());
                                                                              C200 L200 = L464.getDocumentoTypeChoice().getDatiDocumento().getT200();
                                                                              u464 += IS1+"1"+elabora200(L200);      
                if (L464.getLegamiDocumento().length > 0) {                                
                    u464 = elaboraDatiLegameTitAcc (L464, u464);
                }
                                                                              fileUnimarc.add(u464);                          
                //elaboraLegame400(L464);
                                                                  }
                                                       }
                                                       LegameTitAccessoType legTitA = aLegame[jj].getLegameTitAccesso();
                                                       indicatore = " 1";
                                                       if (legTitA != null) {
                                                                  //       considera 423 e 454 con type 1 e 2
                                                                  if (legTitA.getTipoLegame().getType() < 3) { 
                                                                              if (legTitA.getNoteLegame()!= null) {
//  311
                                                                                          String u311 = "311  ";
                                                                                          u311 += IS1+"a"+legTitA.getNoteLegame();       
                                                                                          u311 += IS1+"9"+formattaID(legTitA.getIdArrivo());           
                                                                                          fileUnimarc.add(u311);
                                                                                          indicatore = " 0";                      
                                                                              }
                                                                              if (legTitA.getTipoLegame().getType() == SbnLegameTitAccesso.VALUE_1_TYPE) { 
//          423
                                                                                          String u423 = "423" + indicatore;
                                                                                          DocumentoType L423 = legTitA.getTitAccessoLegato();
                                                                                          u423 += IS1+"1001"+formattaID(L423.getDocumentoTypeChoice().getDatiTitAccesso().getT001());
                                                                                          C200 L200 = L423.getDocumentoTypeChoice().getDatiTitAccesso().getTitAccessoTypeChoice().getT423().getT200();
                    u423 += IS1+"1"+elabora200(L200);
                                                                                          if (L423.getLegamiDocumento().length > 0) {                                                                                                    
                                                                                                      u423 = elaboraDatiLegameTitAcc (L423, u423);
                                                                                          }
                                                                                          // MARCO RANIERI MODIFICA:
                                                                                          // Spostata la riga dopo if in modo da accordare i legami
                                                                                          fileUnimarc.add(u423);
                                                                              }
                                                                              if (legTitA.getTipoLegame().getType() == SbnLegameTitAccesso.VALUE_2_TYPE) { 
//          454
                                                                                          String u454 = "454" + indicatore;
                                                                                          DocumentoType L454 = legTitA.getTitAccessoLegato();
                                                                                          u454 += IS1+"1001"+formattaID(L454.getDocumentoTypeChoice().getDatiTitAccesso().getT001());
                                                                                          C200 L200 = L454.getDocumentoTypeChoice().getDatiTitAccesso().getTitAccessoTypeChoice().getT454();
                                                                                          u454 += IS1+"1"+elabora200(L200);      
                                                                                          if (L454.getLegamiDocumento().length > 0) {                                                                                                    
                                                                                                      u454 = elaboraDatiLegameTitAcc (L454, u454);
                                                                                          }
                                                                                          fileUnimarc.add(u454);
                                                                                                                            
                                                                              }                                                                                                                                                                                                      
                                                                  } else {
                                                                              //   considera 510 e 517 con type 3 e 4                                                            
//  312                                                                                    
                                                                              if (legTitA.getNoteLegame() != null) {
                                                                                          String u312 = "312  ";
                                                                                          u312 += IS1+"a"+legTitA.getNoteLegame();       
                                                                                          u312 += IS1+"9"+formattaID(legTitA.getIdArrivo());           
                                                                                          fileUnimarc.add(u312);              
                                                                              }          
                                                                              if (legTitA.getTipoLegame().getType() == SbnLegameTitAccesso.VALUE_3_TYPE) { 
//          510
                                                                                          String u510 = "510";
                                                                                          DocumentoType L510 = legTitA.getTitAccessoLegato();
                                                                                          u510 += L510.getDocumentoTypeChoice().getDatiTitAccesso().getTitAccessoTypeChoice().getT510().getId1()+" ";
                                                                                          String c200A [] = L510.getDocumentoTypeChoice().getDatiTitAccesso().getTitAccessoTypeChoice().getT510().getA_200();
                                                                                          if (c200A.length > 0) {
                                                                                                      for (int h = 0; h < c200A.length; h++) {
                                                                              //                                 u510 += IS1+"a"+c200A[h];                 
                            String C200 =  new String();
                            C200 =  c200A[h];
                            int iAst = C200.indexOf("*");
                            if (iAst < 0) {
                                u510 += IS1+"a"+c200A[h]+IS1+"9"+formattaID(legTitA.getIdArrivo());   
                            } else {
                                String C200N = new String();
                                if (iAst > 0) {
                                    C200N = NSB+C200.substring(0,iAst)+NSE+C200.substring(iAst+1,C200.length());
                                } else {
                                    C200N = C200.substring(1);   
                                }
                                u510 += IS1+"a"+C200N+IS1+"9"+formattaID(legTitA.getIdArrivo());  
                            }  
                                                                                                      }
                                                                                          }
                                                                                          fileUnimarc.add(u510);
                                                                              }                                                                                            
                                                                              if (legTitA.getTipoLegame().getType() == SbnLegameTitAccesso.VALUE_4_TYPE) { 
//          517
                                                                                          String u517 = "517";
                                                                                          DocumentoType L517 = legTitA.getTitAccessoLegato();
                                                                                          u517 += L517.getDocumentoTypeChoice().getDatiTitAccesso().getTitAccessoTypeChoice().getT517().getId1()+" ";
                                                                                          String c200A [] = L517.getDocumentoTypeChoice().getDatiTitAccesso().getTitAccessoTypeChoice().getT517().getA_200();
                                                                                          if (c200A.length > 0) {
                                                                                                     for (int h = 0; h < c200A.length; h++) {
                                                                                                      //          u517 += IS1+"a"+c200A[h];     
                            String C200 =  new String();
                            C200 =  c200A[h];
                            int iAst = C200.indexOf("*");
                            if (iAst < 0) {
                                u517 += IS1+"a"+c200A[h];   
                            } else {
                                String C200N = new String();
                                if (iAst > 0) {
                                    C200N = NSB+C200.substring(0,iAst)+NSE+C200.substring(iAst+1,C200.length());
                                } else {
                                    C200N = C200.substring(1);   
                                }
                                u517 += IS1+"a"+C200N;  
                            }
                                                                                                      }
                        String e200[]=L517.getDocumentoTypeChoice().getDatiTitAccesso().getTitAccessoTypeChoice().getT517().getE_200();
                        for (int i=0;i<e200.length;i++) {
                            u517+=" : "+e200[i];
                        }
                        if (legTitA.getSottoTipoLegame() != null) {
                            u517+=IS1+"e"+legTitA.getSottoTipoLegame();
                        }
                        u517+=IS1+"9"+formattaID(legTitA.getIdArrivo());
                                                                                          }
                                                                                          fileUnimarc.add(u517);
                                                                              }                                                                     
                                                                  }
                                                       }
                                                       LegameElementoAutType legEleA = aLegame[jj].getLegameElementoAut();
                                                       if (legEleA != null) {
                                                                  if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_3_TYPE) { 
                                                                              String u500 = new String();
                                                                              if (natura.getType() == SbnNaturaDocumento.S_TYPE ||
                                                                                          natura.getType() == SbnNaturaDocumento.C_TYPE) {
//          530
                                                                                          u500 = "5300 ";
                                                                              } else { 
//          500
                                                                                          u500 = "50010";                                   
                                                                              }
                                                                              DatiElementoType L500 = legEleA.getElementoAutLegato().getDatiElementoAut();
                String a230 = null;
                                                                              if (L500 instanceof TitoloUniformeType) {
                    a230 = ((TitoloUniformeType) L500).getT230().getA_230();
                                                                              }
                                                                              if (L500 instanceof TitoloUniformeMusicaType) {
                                                                                          a230 = ((TitoloUniformeMusicaType) L500).getT230().getA_230();
                                                                              }
                if (a230 != null) {
                    int iAst = a230.indexOf("*");
                    if (iAst < 0) {
                        u500 += IS1+"a"+a230;   
                    } else {
                        String C200N = new String();
                        if (iAst > 0) {
                            C200N = NSB+a230.substring(0,iAst)+NSE+a230.substring(iAst+1);
                        } else {
                            C200N = a230.substring(1);   
                        }
                        u500 += IS1+"a"+C200N;
                    }  
                    u500 += IS1+"9"+formattaID(legEleA.getIdArrivo());
                }
                                                                              if (u500.length() > 5) {
                                                                                          fileUnimarc.add(u500);
                                                                                          fileUnimarc_AT = new ArrayList();
                                                                                          if (fileUnimarc_A == null) fileUnimarc_A = new ArrayList();
                                                                                          //if (ID.get(legEleA.getElementoAutLegato().getDatiElementoAut().getT001()) == null) {
                                                                                                      elaboraDatiEleAut (legEleA.getElementoAutLegato() , fileUnimarc_AT);
                                                       Collections.sort(fileUnimarc_AT,new Comparatore());      
                                                                                                      for (int v = 0; v < fileUnimarc_AT.size(); v++) {
                                                                                                                  fileUnimarc_A.add((String)fileUnimarc_AT.get(v));
                                                                                                                 // si inseriscono nel documento anche i tag 928 e 929
                                                                                                                 if (((String)fileUnimarc_AT.get(v)).startsWith("928")) fileUnimarc.add((String)fileUnimarc_AT.get(v));
                                                                                                                 if (((String)fileUnimarc_AT.get(v)).startsWith("929")) fileUnimarc.add((String)fileUnimarc_AT.get(v));
                                                                                                      }
                                                                                          //}                                                                               
                                                                              }
                                                                  }
// 606
                                                                  if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_4_TYPE) {
                                                                              DatiElementoType L606 = legEleA.getElementoAutLegato().getDatiElementoAut(); 
                                                                              if (L606 instanceof SoggettoType) {
                                                                                 SoggettoType soggetto = (SoggettoType) L606;          
                                                                                 A250 a250 = soggetto.getT250();
                                                                                 if (a250 != null) {
                                                                                                      String u250 = "606  ";
                        String s250 = a250.getA_250();
                                                                                                      if (a250 != null){
                            s250 = s250.replace('#',' ');
                             u250+= IS1+"a"+s250;
                        }     
                                                                                                      String A250X [] = a250.getX_250();
                                                                                                      if (A250X.length > 0) {
                                                                                                                  for (int jjj = 0; jjj < A250X.length; jjj++) {
                                                                                                                    u250 += IS1+"x"+A250X[jjj];
                                                                                                                  }
                                                                                                      }  
                                                                                                      if (a250.getC2_250() != null) u250+= IS1+"2"+a250.getC2_250();                                                      
                        if (L606.getT001() != null) u250+= IS1+"3"+formattaID(L606.getT001());
                                                                                                      fileUnimarc.add(u250);
                                                                                                      fileUnimarc_AT = new ArrayList();
                                                                                                      if (fileUnimarc_A == null) fileUnimarc_A = new ArrayList();
                                                                                                      //if (ID.get(legEleA.getElementoAutLegato().getDatiElementoAut().getT001()) == null) {
                                                                                                                 elaboraDatiEleAut (legEleA.getElementoAutLegato() , fileUnimarc_AT);
                            Collections.sort(fileUnimarc_AT,new Comparatore());         
                                                                                                                 if (fileUnimarc_A == null) fileUnimarc_A = new ArrayList();
                                                                                                                 for (int v = 0; v < fileUnimarc_AT.size(); v++) {
                                                                                                                             fileUnimarc_A.add((String)fileUnimarc_AT.get(v));
                                                                                                                 }
                                                                                                      //}                                                                               
                                                                                          }
                                                                              }
                                                                  }                      
//          620
                                                                  if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_5_TYPE) {
                                                                              DatiElementoType L620 = legEleA.getElementoAutLegato().getDatiElementoAut(); 
                                                                              if (L620 instanceof LuogoType) {
                                                                                          LuogoType luogo = (LuogoType) L620;   
                                                                                          A260 a260 = luogo.getT260();
                                                                                          if (a260 != null) {
                                                                                                      String u260 = "620  ";
                                                                                                      u260 = elabora260EleAut(u260, a260);                                     
                                                                                                      fileUnimarc.add(u260);              
                                                                                          }
                                                                              }
                                                                  }
//          676
                                                                   if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_6_TYPE) {
                                                                              DatiElementoType L676 = legEleA.getElementoAutLegato().getDatiElementoAut(); 
                                                                              if (L676 instanceof ClasseType) {
                                                                                          ClasseType classe = (ClasseType) L676;          
                                                                                          A676 a676 = classe.getClasseTypeChoice().getT676(); 
                                                                                          elabora676(a676,fileUnimarc);   
                                                                              }
                                                                  }                      
//          686
                                                                  if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_7_TYPE) {
                                                                              DatiElementoType L686 = legEleA.getElementoAutLegato().getDatiElementoAut(); 
                                                                              if (L686 instanceof ClasseType) {
                                                                                          ClasseType classe = (ClasseType) L686;          
                                                                                          A686 a686 = classe.getClasseTypeChoice().getT686(); 
                                                                                          elabora686(a686,fileUnimarc);               
                                                                              }
                                                                  }                                                                                                        
//          921
                                                                                  if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_16_TYPE) {
                                                                                              DatiElementoType L921 = legEleA.getElementoAutLegato().getDatiElementoAut(); 
                                                                                              if (L921 instanceof MarcaType) {
 
                                                                                                          MarcaType marca = (MarcaType) L921; 
                                                                                                          A921 a921 = marca.getT921(); 
                                                                                                          String t001 = formattaID(marca.getT001());
                                                                                                          if (a921 != null) {
                                                                                                             String u921 = "921  "+IS1+"a"+t001;
                                                                                             u921 = elabora921(u921, a921, legEleA.getElementoAutLegato().getLegamiElementoAut());
                                                                                             fileUnimarc.add(u921);
                                                                                          }                                                                     
                                                                              }
                                                                  }                                                                                                                               
// 700 - 701 - 702
                                                                 
                                                                  DatiElementoType L700 = legEleA.getElementoAutLegato().getDatiElementoAut();
                                                                 
                                                                  if (L700 != null) {
                                                                              if (L700 instanceof AutorePersonaleType) {
                                                                                          AutorePersonaleType autoreP = (AutorePersonaleType) L700;   
                                                                                          A200 a200 = autoreP.getT200();
                                                                                          if (a200 != null) {
                                                                                                      String u200 = null;
                                                                                                      if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_8_TYPE) {
                                                                                                                 u200 = "700 ";
                                                                                                      }
                                                                                                      if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_9_TYPE) {
                                                                                                                 u200 = "701 ";
                                                                                                      }
                                                                                                      if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_10_TYPE) {
                                                                                                                 u200 = "702 ";
                                                                                                      }
                                                                                                      if (u200 != null) {                                                                                                          
                                                                                                          u200 = elabora200EleAut(u200, a200);
                                                                                                                 u200 += IS1+"3"+formattaID(legEleA.getIdArrivo());
                                                                                                                 if (legEleA.getRelatorCode() != null && legEleA.getRelatorCode().length() > 0)
                                                                                                                                         u200 += IS1+"4"+legEleA.getRelatorCode();                                                                                                                 
                                                                                                                 fileUnimarc.add(u200);              
                                                                                                                 fileUnimarc_AT = new ArrayList();
                                                                                                                 if (fileUnimarc_A == null) fileUnimarc_A = new ArrayList();
                                                                                                                 //if (ID.get(legEleA.getElementoAutLegato().getDatiElementoAut().getT001()) == null) {
                                                                                                                             elaboraDatiEleAut (legEleA.getElementoAutLegato() , fileUnimarc_AT);      
                                                                                                                             elaboraDatiLegameEleAut (legEleA.getElementoAutLegato(), fileUnimarc_AT);
                                                                                                                             Collections.sort(fileUnimarc_AT,new Comparatore());      
                                                                                                                             if (fileUnimarc_A == null) fileUnimarc_A = new ArrayList();
                                                                                                                             for (int v = 0; v < fileUnimarc_AT.size(); v++) {
                                                                                                                                         fileUnimarc_A.add((String)fileUnimarc_AT.get(v));
                                                                                                                             }
                                                                                                                 //}                                                                                          
                                                                                                      }
                                                                                          }
                                                                              }
//          710 - 711 - 712
                        if (L700 instanceof EnteType) {
                                                                                          EnteType ente = (EnteType) L700;        
                                                                                          int tipoNome = ente.getTipoNome().getType();
                                                                                          A210 a210 = ente.getT210();
                                                                                          if (a210 != null) {
                                                                                                      String u210 = null;
                                                                                                      if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_11_TYPE) {
                                                                                                                 u210 = "710";
                                                                                                      }
                                                                                                      if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_12_TYPE) {
                                                                                                                 u210 = "711";
                                                                                                      }
                                                                                                      if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_13_TYPE) {
                                                                                                                 u210 = "712";
                                                                                                      }
                                                                                                      if (u210 != null) {
                                                                                                                 u210 = elabora210EleAut(u210, a210, tipoNome);                                                                                                                                                                                                                                                                                                              
                                                                                                                 u210 += IS1+"3"+formattaID(legEleA.getIdArrivo());
                                                                                                                 if (legEleA.getRelatorCode() != null && legEleA.getRelatorCode().length() > 0)
                                                                                                                             u210 += IS1+"4"+legEleA.getRelatorCode();                                                                                                                 
                                                                                                                 fileUnimarc.add(u210);              
                                                                                                                 fileUnimarc_AT = new ArrayList();
                                                                                                                 if (fileUnimarc_A == null) fileUnimarc_A = new ArrayList();
                                                                                                                 //if (ID.get(legEleA.getElementoAutLegato().getDatiElementoAut().getT001()) == null) {
                                                                                                                             elaboraDatiEleAut (legEleA.getElementoAutLegato() , fileUnimarc_AT);      
                                                                                                                             elaboraDatiLegameEleAut (legEleA.getElementoAutLegato(), fileUnimarc_AT);
                                                                                                                             Collections.sort(fileUnimarc_AT,new Comparatore());      
                                                                                                                             if (fileUnimarc_A == null) fileUnimarc_A = new ArrayList();
                                                                                                                             for (int v = 0; v < fileUnimarc_AT.size(); v++) {
                                                                                                                                         fileUnimarc_A.add((String)fileUnimarc_AT.get(v));
                                                                                                                             }
                                                                                                                 //}                                                                              
                                                                                                      }
                                                                                          }
                                                                              }   
                                                                  }
// 314 note al legame titolo-autore                                                                                            
                                                                  if (legEleA.getNoteLegame() != null) {
                                                                              String u314 = "314";
                                                                              u314 += IS1+"a"+legEleA.getNoteLegame();      
                                                                              u314 += IS1+"9"+formattaID(legEleA.getIdArrivo());        
                                                                              fileUnimarc.add(u314);              
                                                                  }          
// 790 - 791
                                                                  if (legEleA.getElementoAutLegato().getDatiElementoAut().getTipoAuthority().equals(SbnAuthority.valueOf("AU"))) {
                LegamiType L79X [] = legEleA.getElementoAutLegato().getLegamiElementoAut(); 
                for (int y = 0; y < L79X.length; y++) {
                    ArrivoLegame ALegame[] = L79X[y].getArrivoLegame();
                    if (ALegame.length > 0) {
                        for (int yy = 0; yy < ALegame.length; yy++) {
                            LegameElementoAutType legEleA7 = ALegame[yy].getLegameElementoAut();
                            if (legEleA7 != null) {
                                DatiElementoType LL79X =
                                    legEleA7.getElementoAutLegato().getDatiElementoAut();
                                String u79X = null;
                                if (LL79X instanceof AutorePersonaleType) {
                                    A200 a200 = ((AutorePersonaleType)LL79X).getT200();
                                    u79X = "790 ";
                                    u79X = elabora200EleAut(u79X, a200);
                                }
                                if (LL79X instanceof EnteType) {
                                    A210 a210 = ((EnteType)LL79X).getT210();
                                    u79X = "791";
                                    u79X = elabora210EleAut(u79X, a210, ((EnteType)LL79X).getTipoNome().getType());                                                                                                      
                                }
                                  
                                if (u79X != null) {
                                    // 26.02.2004 spostato $3 prima di $z o $y
                                	u79X += IS1 + "3" + formattaID(legEleA7.getIdArrivo());
                                    String nomeAut = estraiNomeAutoreDaId(L79X[y].getIdPartenza(), legEleA.getElementoAutLegato());
                                    if (nomeAut != null) {
                                    	nomeAut = replaceAll(nomeAut,"*","");
                                        nomeAut = replaceAll(nomeAut,"#"," ");
                                        nomeAut = replaceAll(nomeAut,"_"," ");
                                        if (legEleA7.getTipoLegame().getType() == SbnLegameAut.VALUE_1_TYPE)
                                            u79X += IS1 + "z" + nomeAut;//taut.getDs_nome_aut();
                                        else
                                            u79X += IS1 + "y" + nomeAut; //taut.getDs_nome_aut();
                                    }
                                    fileUnimarc.add(u79X);
                                }
                            }
                        }
                    }
                }
            }
//fine    
                                                       }                                                                     
                                           }                      
                               }
                   }
                       
                        }
                        return fileUnimarc;
      }
     
      /**
       * Scorre il documento per estrarre la descrizione del nome dell'autore il cui bid è quello indicato
       * @param id dell'autore cercato
       * @return
       */
    private String estraiNomeAutoreDaId(String id,ElementAutType elem) {
        String ret = null;
        DatiElementoType dati = elem.getDatiElementoAut();
        if (dati.getT001().equals(id)) {
//ARGE
// controlla
// class TipiAutore ha problemi di compilazione
// check metodo valutaNomeAutore
        	try {
	            if(dati instanceof AutorePersonaleType) {
	                // ret = new TipiAutore().valutaNomeAutore((AutorePersonaleType)dati,null);
	            	ret = valutaNomeAutore((AutorePersonaleType)dati, null);
	            } else if(dati instanceof EnteType) {
                    //ret = new TipiAutore().valutaNomeAutore((EnteType)dati,null);
	            	ret = valutaNomeAutore((EnteType)dati, null);
	            }
            } catch (EccezioneSbnDiagnostico ecc) {
                log.error("Errore valutazione nome autore",ecc);
            }
//
        }
        return ret;
    }

    public ArrayList elaboraDatiLegameEleAut (ElementAutType documento,ArrayList fileUnimarc) {

//          andiamo sui legami dell'elemento di authority
		LegamiType legDocUnimarc [] = documento.getLegamiElementoAut();
         if (legDocUnimarc.length > 0) {
            for (int j = 0; j < legDocUnimarc.length; j++) {
                ArrivoLegame aLegame[] = legDocUnimarc[j].getArrivoLegame();
                if (aLegame.length > 0) {
                    for (int jj = 0; jj < aLegame.length; jj++) {
                        LegameElementoAutType legEleA = aLegame[jj].getLegameElementoAut();
                        if (legEleA != null) {
                           DatiElementoType L400 = legEleA.getElementoAutLegato().getDatiElementoAut();
// 400 - 500 Autore
                           if (L400 instanceof AutorePersonaleType) {
                              AutorePersonaleType autoreP = (AutorePersonaleType) L400;           
                              A200 a200 = autoreP.getT200(); 
                              if (a200 != null) {
                                  String u200 = null;
                                  if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_1_TYPE) {
                                      if (autoreP.getFormaNome().getType() == SbnFormaNome.R_TYPE) {
                                    	  u200 = "400 ";
                                      }
                                  } else {
                                      if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_2_TYPE) {
                                         if (autoreP.getFormaNome().getType() == SbnFormaNome.A_TYPE) {
                                        	 u200 = "500 ";
                                         }
                                      }
                                  }
                                  if (u200 != null) {
                                      u200 = elabora200EleAut(u200, a200);
                                      u200+= IS1+"3"+formattaID(legEleA.getIdArrivo());                                                                    
                                      fileUnimarc.add(u200);
                                  }
                               }
                           }
// 410 - 510 Ente
                           if (L400 instanceof EnteType) {
                              EnteType ente = (EnteType) L400;        
                              int tipoNome = ente.getTipoNome().getType();
                              A210 a210 = ente.getT210(); 
                              if (a210 != null) {
                            	  String u210 = null;
                                  if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_1_TYPE) {
                                  if (ente.getFormaNome().getType() == SbnFormaNome.R_TYPE) {
                                	  u210 = "410";
                                  }
                                  } else {
                                              if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_2_TYPE) {
                                                         if (ente.getFormaNome().getType() == SbnFormaNome.A_TYPE) {
                                                                     u210 = "510";
                                              }
                                              }
                                  }
                                  if (u210 != null) {
                                     u210 = elabora210EleAut(u210, a210, tipoNome);                                                                                                                                     
                                     u210+= IS1+"3"+formattaID(legEleA.getIdArrivo());                       
                                     fileUnimarc.add(u210);
                                  }
                              }      
                           }
// 460 - 560 Luogo                                                                                 
                           if (L400 instanceof LuogoType) {
                              LuogoType luogo = (LuogoType) L400;   
                              A260 a260 = luogo.getT260(); 
                              if (a260 != null) {
                                  String u260 = null;
                                  if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_1_TYPE) {
                                     if (luogo.getFormaNome().getType() == SbnFormaNome.R_TYPE) {
                                        u260 = "460  ";
                                     } else {
                                         if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_2_TYPE) {
                                             if (luogo.getFormaNome().getType() == SbnFormaNome.A_TYPE) {
                                                 u260 = "560  ";
                                             }
                                         }                                                                                            
                                     }
                                     if (u260 != null) {
                                        u260 = elabora260EleAut(u260, a260);
                                        u260+= IS1+"3"+formattaID(legEleA.getIdArrivo());                                                                    
                                        fileUnimarc.add(u260);
                                     }
                                  }
                              }
                           }
// 810 - 815 Repertorio                                                                           
                           if (L400 instanceof RepertorioType) {
                                      RepertorioType repertorio = (RepertorioType) L400;        
                                      A930 a930 = repertorio.getT930(); 
                                      if (a930 != null) {
                                                  String u800 = null;
                                                  if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_14_TYPE) {
                                                                u800 = "810  ";
                                         } else {
                                                              if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_15_TYPE) {
                                                                         u800 = "815  ";
                                                              }
              
                                         }
                                         if (u800 != null) {
                                                    if (a930.getA_930()!= null) u800+= IS1+"a"+a930.getA_930();;                               
                                                    fileUnimarc.add(u800);
                                         }       
                                      }                                                                                            
                           }
// 921                          
                           if (L400 instanceof MarcaType) {
                               MarcaType marca = (MarcaType) L400;
                                A921 a921 = marca.getT921(); 
                               String t001 = formattaID(marca.getT001());
                               if (a921 != null) {
                                   String u921 = null;
                                   if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_16_TYPE) {
                                        u921 = "921  "+IS1+"a"+t001;
                                   }
                                   if (u921 != null) {
                                      u921 = elabora921(u921, a921, legEleA.getElementoAutLegato().getLegamiElementoAut());
                                      fileUnimarc.add(u921);
                                   }
                               }
                           }                                                                                
// 932 Descrittore                                                                                  
                           if (L400 instanceof DescrittoreType) {
                              DescrittoreType descrittore = (DescrittoreType) L400;     
                              A931 a931 = descrittore.getT931(); 
                              if (a931 != null) {
                                 String u932 = null;
                                 if (legEleA.getTipoLegame().toString().equals("USE")  ||
                                     legEleA.getTipoLegame().toString().equals("UF")   ||
                                     legEleA.getTipoLegame().toString().equals("RT")   ||
                                     legEleA.getTipoLegame().toString().equals("BT")   ||
                                     legEleA.getTipoLegame().toString().equals("NT")) {
                                	 u932 = "932  ";
                                 }
                                 if (u932 != null) {
                                    if (a931.getA_931()!= null) u932+= IS1+"a"+a931.getA_931();
                                    if (a931.getB_931()!= null) u932+= IS1+"a"+a931.getB_931();
                                    if (a931.getC2_931()!= null) u932+= IS1+"2"+a931.getC2_931();
                                    u932 += IS1+"5"+legEleA.getTipoLegame().toString();           
                                    u932 += IS1+"3"+formattaID(legEleA.getIdArrivo());
                                    fileUnimarc.add(u932);
                                 }
                              }
                           }
// fine
                        }
                    }
                }
            }
       
         }
         return fileUnimarc;
    }
 
    /**
     * 
     * @param L400
     * @param t001
     * @param fileUnimarc_0
     * @throws InfrastructureException
     */
    private void elaboraFileUnimarc(
		DocumentoType L400, String t001, 
		ArrayList fileUnimarc_0) throws InfrastructureException {

    	fileUnimarc_0 = elaboraDatiDoc(L400,fileUnimarc_0);
        if (L400.getLegamiDocumento() != null) {
            fileUnimarc_0 = elaboraDatiLegameDoc(L400,fileUnimarc_0);
        }
        fileUnimarc_0 = elaboraAltriDatiDoc(L400,fileUnimarc_0);

/*
due select - public static ArrayList listaLocalizzazioni(bid, filtraLocalizza){...}
	if (filtraLocalizza != null) vl_bib_titResult.executeCustom("selectPerFiltraLocalizza", "order1");
	else vl_bib_titResult.executeCustom("selectPerBid", "order1");

-- usa questa (in qst metodo filtraLocalizza è sempre null)
selectPerBid
	WHERE bid = XXXbid AND fl_canc != 'S'
selectPerFiltraLocalizza
	WHERE bid = XXXbid
/////
selectPerAnagrafe (public static Hashtable estraiC899)
	WHERE cd_ana_biblioteca = XXXcd_ana_biblioteca AND fl_canc != 'S'
*/

//      try {
////    ARGE
//            	ArrayList v = EsportaDocumentiFactoring.listaLocalizzazioni(t001,null);
//                Hashtable c899 = EsportaDocumentiFactoring.estraiC899(new Hashtable(),v,t001);
		try {
			System.out.println("dentro al TRY jdbc di elaboraFileUnimarc(...)");
			Class.forName("org.postgresql.Driver");
			Connection conn = DriverManager.getConnection(
					"jdbc:postgresql://192.168.104.34:5432/sbnwebHib2", 
					"sbnweb", "sbnweb");
			Statement st = conn.createStatement();
			// ArrayList v = EsportaDocumentiFactoring.listaLocalizzazioni(t001,null);
			ResultSet rs1 = st.executeQuery(
					"select * FROM sbnweb.tr_tit_bib " +
					"WHERE fl_canc !='S' AND bid = '"+t001+"'");
			if (rs1.next()) {
System.out.println("esiste bid ["+t001+"]");

				// campi da tr_tit_bib
				// necessari per select su tbf_biblioteca_in_polo
				String rs1cdpolo = rs1.getString("cd_polo");
				String rs1cdbiblio = rs1.getString("cd_biblioteca");
				String rs1bid = rs1.getString("bid");
				String rs1dsfondo = rs1.getString("ds_fondo");
				String rs1dsconsistenza = rs1.getString("ds_consistenza");
				String rs1dssegn = rs1.getString("ds_segn");
				String rs1fldispelettr = rs1.getString("fl_disp_elettr");
				String rs1flposs = rs1.getString("fl_possesso");
				String rs1flgest = rs1.getString("fl_gestione");
				String rs1notatitbib = rs1.getString("nota_tit_bib");
				String rs1flmutilo = rs1.getString("fl_mutilo");
				String rs1dsanticasegn = rs1.getString("ds_antica_segn");
				String rs1uricopia = rs1.getString("uri_copia");
				String rs1tpdigitalizz = rs1.getString("tp_digitalizz");

				// CHECK se la biblioteca esiste in db
				ResultSet rs2 = st.executeQuery(
						"select cd_ana_biblioteca, ds_biblioteca, ds_citta " +
						"FROM sbnweb.tbf_biblioteca_in_polo " +
						"WHERE fl_canc !='S'" +
						" AND cd_polo = '"+rs1cdpolo+"'" +
						" AND cd_biblioteca = '"+rs1cdbiblio+"'");
				String cd_ana_biblio = null;
				if (rs2.next()) cd_ana_biblio = rs2.getString("cd_ana_biblioteca");

// begin codice - metodo estraiC899() di EsportaDocumentiFactoring
				Hashtable c899 = new Hashtable();
				c899.put(rs1bid, "1");

				String c899S = "";
	            //ArrayList vB = tb_bibliotecaResult.getElencoRisultati();

	            if ( (cd_ana_biblio!=null)
	            		&& (cd_ana_biblio.trim().length()>0))
	                c899S += IS1 + "1" + cd_ana_biblio;
	            if (rs1cdpolo != null
	                    && rs1cdpolo.trim().length() > 0)
	                c899S += IS1 + "2" + rs1cdpolo
	                        + rs1cdbiblio;
	            if (rs1dsfondo != null
	                    && rs1dsfondo.trim().length() > 0)
	                c899S += IS1 + "3" + rs1dsfondo;
	            if (rs1dsconsistenza != null
	                    && rs1dsconsistenza.trim().length() > 0)
	                c899S += IS1 + "4" + rs1dsconsistenza;

//	            if (vB.size() != 0) {
	                //String nome = ((Tbf_biblioteca_in_polo) vB.get(0)).getDs_biblioteca();
	            	String nome = rs2.getString("ds_biblioteca");
	                if (nome.indexOf("**") >= 0)
	                    nome = nome.substring(0, nome.indexOf("**"));
	                c899S += IS1 + "a" + nome;
//	            }

	            if (rs1dssegn != null
	                    && rs1dssegn.trim().length() > 0)
	                c899S += IS1 + "c" + rs1dssegn;
	            if (rs2.getString("ds_citta") != null
	                    && rs2.getString("ds_citta").trim().length() > 0)
	                c899S += IS1 + "d" + rs2.getString("ds_citta").trim();
	            if (rs1fldispelettr != null
	                    && rs1fldispelettr.trim().length() > 0
	                    && rs1fldispelettr.trim().equalsIgnoreCase("S"))
	                c899S += IS1 + "e" + rs1fldispelettr;
	            if (!rs1flposs.equals("N")
	                    && rs1flgest.equals("N"))
	                c899S += IS1 + "fPOSSESSO";
	            else if (!rs1flposs.equals("N")
	                    && !rs1flgest.equals("N"))
	                c899S += IS1 + "fPOSSESSO/GESTIONE";
	            else if (rs1flposs.equals("N")
	                    && !rs1flgest.equals("N"))
	                c899S += IS1 + "fGESTIONE";
	            if (rs1notatitbib != null
	                    && rs1notatitbib.trim().length() > 0)
	                c899S += IS1 + "n" + rs1notatitbib;
	            if (rs1flmutilo != null
	                    && rs1flmutilo.trim().length() > 0) {
	                if (rs1flmutilo.equals("X")) c899S += IS1 + "qS";
	                else c899S += IS1 + "q" + rs1flmutilo;
	            }
	            if (rs1dsanticasegn != null
	                    && rs1dsanticasegn.trim().length() > 0)
	                c899S += IS1 + "s" + rs1dsanticasegn;
	            if (rs1uricopia != null
	                    && rs1uricopia.trim().length() > 0)
	                c899S += IS1 + "u" + rs1uricopia;
	            if (rs1tpdigitalizz != null
	                    && rs1tpdigitalizz.trim().length() > 0)
	                c899S += IS1 + "t" + rs1tpdigitalizz;
	            c899.put(rs1bid + 0, c899S);
// end codice - metodo estraiC899() di EsportaDocumentiFactoring

	            //rs.getString("count");
				System.out.println("_bid:"+rs1bid);
				System.out.println("_nomebiblio:"+nome);
				System.out.println("_899:"+c899S);

				if (c899.get(t001) != null) {
					int sizeBid = Integer.parseInt((c899.get(t001)).toString());
					if (sizeBid > 0) {
						for (int j = 0; j < sizeBid; j++) {
							String u899 = "899  "; 
							if (c899.get(t001+j)!= null){
								u899+= (c899.get(t001+j)).toString();   
								fileUnimarc_0.add(u899);
							}
						}
					}
				}
				rs2 = null;
			} else System.out.println("NON esiste bid ["+t001+"]");
			rs1 = null;
		} catch (ClassNotFoundException cnfe) {
			log.error("Errore caricamento JdbcOdbcDriver, lo ignoro",cnfe);
			cnfe.printStackTrace();
		} catch (java.sql.SQLException sqle) {
			log.error("Errore lettura titolo JDBC, la ignoro",sqle);
			sqle.printStackTrace();
		}

//            if (c899.get(t001) != null) {
//                int sizeBid = Integer.parseInt((c899.get(t001)).toString());
//                if (sizeBid > 0) {
//                    for (int j = 0; j < sizeBid; j++) {
//                        String u899 = "899  "; 
//                        if (c899.get(t001+j)!= null){
//                            u899+= (c899.get(t001+j)).toString();   
//                            fileUnimarc_0.add(u899);
//                        }
//                    }
//                }      
//            }
////ARGE
//        } catch (EccezioneDB ecc) {
//            log.error(ecc);
//        }

    }
 
   public void elaboraLegame400 (DocumentoType L400) throws InfrastructureException {
            if (L400.getDocumentoTypeChoice().getDatiDocumento() != null) {                       
                        String t001 = formattaID(L400.getDocumentoTypeChoice().getDatiDocumento().getT001());
        log.info("ID.get(t001): " + ID.get(t001));
                        if (ID.get(t001) != null) return;
        log.info("ID.get(t001");
                        ID.put(t001, t001);        
                        if (fileUnimarc_0 == null) {
                                   log.info("hash vuota -0-"+(String)ID.get(t001)+"----"+t001+"----");   
                                   fileUnimarc_0 = new ArrayList();
            elaboraFileUnimarc(L400,t001,fileUnimarc_0);
                        } else {
                                   log.info("hash piena -0-"+(String)ID.get(t001)+"----"+t001+"----");  
                                   if (fileUnimarc_1 == null) {
                                               log.info("hash vuota -1-"+(String)ID.get(t001)+"----"+t001+"----");   
                                               fileUnimarc_1 = new ArrayList();
                elaboraFileUnimarc(L400,t001,fileUnimarc_1);
                                   } else {
                                               log.info("hash piena -1-"+(String)ID.get(t001)+"----"+t001+"----");
                                               if (fileUnimarc_2 == null) {
                                                           log.info("hash vuota -2-"+(String)ID.get(t001)+"----"+t001+"----");   
                                                           fileUnimarc_2 = new ArrayList();
                    elaboraFileUnimarc(L400,t001,fileUnimarc_2);
                                               } else {
                                                           log.info("hash piena -2-"+(String)ID.get(t001)+"----"+t001+"----");
                                                           if (fileUnimarc_3 == null) {
                                                                       log.info("hash vuota -3-"+(String)ID.get(t001)+"----"+t001+"----");   
                                                                       fileUnimarc_3 = new ArrayList();
                        elaboraFileUnimarc(L400,t001,fileUnimarc_3);
                                                           } else {
                                                                       log.info("hash piena -3-"+(String)ID.get(t001)+"----"+t001+"----");
                                                                       if (fileUnimarc_4 == null) {
                                                                                  log.info("hash vuota -4-"+(String)ID.get(t001)+"----"+t001+"----");   
                                                                                  fileUnimarc_4 = new ArrayList();
                            elaboraFileUnimarc(L400,t001,fileUnimarc_4);
                                                                       } else {
                                                                                  log.info("hash piena -4-"+(String)ID.get(t001)+"----"+t001+"----");
                                                                       }
                                                           }
                                               }
                                   }                                                                                
                        }
            }          
   }  
   
   public String elabora200 (C200 c200) {
              String u200 = "200";
              u200 += c200.getId1()+" ";
              String c200A[] = c200.getA_200();
              if (c200A.length > 0) {
                          for (int j = 0; j < c200A.length; j++) {
               String C200 =  new String();
               C200 =  c200A[j];
               int iAst = C200.indexOf("*");
               if (iAst < 0) {
                   u200 += IS1+"a"+c200A[j];   
               } else {
                   String C200N = new String();
                   if (iAst > 0) {
                       C200N = NSB+C200.substring(0,iAst)+NSE+C200.substring(iAst+1);
                   } else {
                       C200N = C200.substring(1);   
                   }
                   u200 += IS1+"a"+C200N;  
               }       
                          }
              }
              String c200B[] = c200.getB_200();
              if (c200B.length > 0) {
                          for (int j = 0; j < c200B.length; j++) {
                                      u200 += IS1+"b"+c200B[j];                
                          }
              }                              
              String c200D[] = c200.getD_200();
              if (c200D.length > 0) {
                          for (int j = 0; j < c200D.length; j++) {
                                      u200 += IS1+"d"+c200D[j];                
                          }
              }  
              String c200E[] = c200.getE_200();
              // 26.02.2004 inserita gestione per * in $3
              if (c200E.length > 0) {
                          for (int j = 0; j < c200E.length; j++) {
               int iAst = c200E[j].indexOf("*");
               if (iAst < 0) {
                   u200 += IS1+"e"+c200E[j];   
               } else {
                   String C200N = new String();
                   if (iAst > 0) {
                       C200N = NSB+c200E[j].substring(0,iAst)+NSE+c200E[j].substring(iAst+1);
                   } else {
                       C200N = c200E[j].substring(1);   
                   }
                   u200 += IS1+"e"+C200N;
               }
                          }
              }  
              String c200F[] = c200.getF_200();
              if (c200F.length > 0) {
                          for (int j = 0; j < c200F.length; j++) {
                                      u200 += IS1+"f"+c200F[j];                 
                          }
              }  
              String c200G[] = c200.getG_200();
              if (c200G.length > 0) {
                          for (int j = 0; j < c200G.length; j++) {
                                      u200 += IS1+"g"+c200G[j];                
                          }
              }  
      Cf_200Type cf200C[] = c200.getCf_200();
      if (cf200C.length > 0) {
          for (int i = 0; i < cf200C.length; i++) {
               u200 += IS1+"c"+cf200C[i].getC_200();       
              for (int j = 0; j < cf200C[i].getF_200Count(); j++) {
                   u200 += IS1+"f"+cf200C[i].getF_200(j);       
              }
              for (int j = 0; j < cf200C[i].getG_200Count(); j++) {
                   u200 += IS1+"g"+cf200C[i].getG_200(j);       
              }
          }
      }  
              String c200H[] = c200.getH_200();
              if (c200H.length > 0) {
                          for (int j = 0; j < c200H.length; j++) {
                                      u200 += IS1+"h"+c200H[j];                
                          }
              }  
              String c200I[] = c200.getI_200();
              if (c200I.length > 0) {
                          for (int j = 0; j < c200I.length; j++) {
                                      u200 += IS1+"i"+c200I[j];                  
                          }
              }
      String u200trim = u200.trim();
      if (u200trim.endsWith(". -")) {
          u200 = u200trim.substring(0,u200trim.length()-3);
      }
              return u200;
   }
   public String elabora210EleAut(String u210, A210 a210, int tipoNome) {
                        u210 += a210.getId1();
                        u210 += a210.getId2();
                        int len = u210.length();
        boolean aperta = false;
                        if (a210.getA_210() != null) u210+= IS1+"a"+a210.getA_210();    
                        if (tipoNome == SbnTipoNomeAutore.G_TYPE) {
                                    A210_GType A210G[] = a210.getA210_G();
                        String A210C [] = a210.getC_210();
                            for (int jjj = 0; jjj < A210C.length; jjj++) {
                                      u210 += IS1+"c"+(aperta?" ; ":" <")+A210C[jjj];
               aperta = true;
                            }                              
                                   if (A210G.length > 0) {
                                      for (int jj = 0; jj < A210G.length; jj++) {
                                                 if (A210G[jj].getB_210() != null) {
                      if (aperta) {
                          u210 += ">";
                          aperta = false;
                      }
                      u210+= IS1+"b"+" : "+A210G[jj].getB_210();
                  }
                                                 String A210CG [] = A210G[jj].getC_210();                   
                                                 for (int jjj = 0; jjj < A210CG.length; jjj++) {
                                                             u210 += IS1+"c"+(aperta?" ; ":" <")+A210CG[jjj];
                      aperta = true;
                                                 }                                                                                                                                                     
                                      }
                                   }
                        } else {
                            String A210C [] = a210.getC_210();
                            for (int jjj = 0; jjj < A210C.length; jjj++) {
                                      u210 += IS1+"c"+(aperta?" ; ":" <")+A210C[jjj];
               aperta = true;
                            }                             
                        }
                        String A210D [] = a210.getD_210();                                                      
                        for (int jjj = 0; jjj < A210D.length; jjj++) {
                           u210 += IS1+"d"+(aperta?" ; ":" <")+A210D[jjj];
           aperta = true;
                        }
                        String A210E [] = a210.getE_210();                                                      
                        for (int jjj = 0; jjj < A210E.length; jjj++) {
                   u210 += IS1+"e"+(aperta?" ; ":" <")+A210E[jjj];
           aperta = true;
                        }                                                         
                        if (a210.getF_210() != null) {
            u210+= IS1+"f"+(aperta?" ; ":" <")+a210.getF_210();
            aperta = true;
        }
        if (aperta)
            u210+=">";
        // 26.02.2004 il primo asterisco deve essere racchiuso tra i caratteri di non sort
        int iAst = u210.indexOf("*");
        // solo se l'asterisco non è al primo posto
        if (iAst > len + 3) {
           u210 = u210.substring(0,(len + 2))+NSB+u210.substring(len + 2,iAst)+NSE+u210.substring(iAst+1);
        }
 
                        u210 = replaceAll(u210,"*","");
                        u210 = replaceAll(u210,"#"," ");
        u210 = replaceAll(u210,"_"," ");
                return u210;
   }
   public String elabora200EleAut(String u200, A200 a200){
            u200+= a200.getId2();
            int len = u200.length();
           
                        if (a200.getA_200() != null) u200+= IS1+"a"+a200.getA_200();                
                        if (a200.getB_200() != null) u200+= IS1+"b"+a200.getB_200();
                       
        // 26.02.2004 il primo asterisco deve essere racchiuso tra i caratteri di non sort
        int iAst = u200.indexOf("*");
        // solo se l'asterisco non è al primo posto
        if (iAst > len + 3) {
           u200 = u200.substring(0,(len + 2))+NSB+u200.substring(len + 2,iAst)+NSE+u200.substring(iAst+1);
        }
 
                        u200 = replaceAll(u200,"*","");
                        u200 = replaceAll(u200,"#"," ");
        u200 = replaceAll(u200,"_"," ");
                       
                        String A200C [] = a200.getC_200();
        boolean aperta = false;
                        if (A200C.length > 0) {
            aperta = true;
                                    for (int jjj = 0; jjj < A200C.length; jjj++) {
                                     u200 += IS1+"c"+(jjj==0?" <":" ; ")+A200C[jjj];
                                    }
                        }  
                        if (a200.getF_200() != null) {
            u200+= IS1+"f"+(aperta?" ; ":" <")+a200.getF_200();
            aperta = true;
        }
        if (aperta)
            u200 += ">";
                        return u200;
   }                   
   public String elabora260EleAut(String u260, A260 a260) {
            if (a260.getA_260() != null && a260.getA_260().trim().length()>0) u260+= IS1+"a"+a260.getA_260();         
                        if (a260.getD_260() != null) u260+= IS1+"d"+a260.getD_260();
                        return u260;
   }       
   public String elabora240EleAut(ElementAutType documento) {
      String u240 = "";
              LegamiType legDocUnimarc [] = documento.getLegamiElementoAut();
              if (legDocUnimarc.length > 0) {
                          for (int j = 0; j < legDocUnimarc.length; j++) {
                                     ArrivoLegame aLegame[] = legDocUnimarc[j].getArrivoLegame();
                                     if (aLegame.length > 0) {
                                                 for (int jj = 0; jj < aLegame.length; jj++) {
                                                             LegameElementoAutType legEleA = aLegame[jj].getLegameElementoAut();
                                                             if (legEleA != null) {
                                                                         DatiElementoType L400 = legEleA.getElementoAutLegato().getDatiElementoAut();
//700 - 701 - 702 Autore
                                                                         if (L400 instanceof AutorePersonaleType) {
                                                                                    AutorePersonaleType autoreP = (AutorePersonaleType) L400;   
                                                                                   A200 a200 = autoreP.getT200(); 
                                                                                   if (a200 != null) {
                                                                                               if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_8_TYPE ||
                                                                                                   legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_9_TYPE ||
                                                                                                           legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_10_TYPE) {
                                                                                                                       u240 += IS1+"1"+"200 ";
                                                                                               }
                                                                                               if (u240 != null) {
                                                                                                           u240 = elabora200EleAut(u240, a200);
                                                                                                           u240+= IS1+"3"+formattaID(legEleA.getIdArrivo());                        
                                                                                               }
                                                                                    }
                                                                         }
//710 - 711 - 712 Ente                                                                         
                                                                         if (L400 instanceof EnteType) {
                                                                                   EnteType ente = (EnteType) L400;       
                                                                                   int tipoNome = ente.getTipoNome().getType();
                                                                                   A210 a210 = ente.getT210(); 
                                                                                   if (a210 != null) {
                                                                                               u240 = null;
                                                                                               if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_11_TYPE ||
                                                                                                           legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_12_TYPE ||
                                                                                                           legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_13_TYPE) {
                                                                                                                       u240 = IS1+"1"+"210 ";
                                                                                               }
                                                                                               if (u240 != null) {
                                                                                                          u240 = elabora210EleAut(u240, a210, tipoNome);                                                                                                                                     
                                                                                                          u240+= IS1+"3"+formattaID(legEleA.getIdArrivo());           
                                                                                               }                     
                                                                                   }         
                                                                         }
                                                             }
                                                 }
                                     }
                          }
              }
                        return u240;
   }                              
   public String elabora921(String u921, A921 a921, LegamiType[] legame810) {
       if (a921.getA_921()!= null) u921+= IS1+"b"+a921.getA_921();
               String A921B [] = a921.getB_921();
               if (legame810.length > 0) {
                        for (int j = 0; j < legame810.length; j++) {
                                     ArrivoLegame aLegame810[] = legame810[j].getArrivoLegame();
                                     for (int jj = 0; jj < aLegame810.length; jj++) {
                                               if (aLegame810[jj].getLegameElementoAut().getTipoLegame().getType() == SbnLegameAut.VALUE_14_TYPE)
                                                                       u921 += IS1+"c"+aLegame810[jj].getLegameElementoAut().getIdArrivo()+aLegame810[jj].getLegameElementoAut().getCitazione(); 
                                     }
                          }
               }           
               if (a921.getD_921()!= null) u921+= IS1+"d"+a921.getD_921();
               if (a921.getE_921()!= null) u921+= IS1+"e"+a921.getE_921();
               if (A921B.length > 0) {
                        for (int j = 0; j < A921B.length; j++) {
                                                  u921 += IS1+"f"+A921B[j];
                                    }
                        }
                return u921;
   }       
   public void elabora676 (A676 a676, ArrayList fileUnimarc) {      
                        String u676 = "676  ";
                        if (a676 != null) {
                                    if (a676.getA_676()!= null) u676+= IS1+"a"+a676.getA_676();
                                    if (a676.getC_676()!= null) u676+= IS1+"c"+a676.getC_676();
                                    if (a676.getV_676()!= null) u676+= IS1+"v"+a676.getV_676();
                                    if (a676.getC9_676()!= null) u676+= IS1+"9"+a676.getC9_676();  
                                    if (u676.length() > 5) fileUnimarc.add(u676);
            }
            }
            public void elabora686(A686 a686, ArrayList fileUnimarc) {                      
                        String u686 = "686  ";
                        if (a686 != null) {
                                   if (a686.getA_686()!= null) u686+= IS1+"a"+a686.getA_686();
                                   if (a686.getC_686()!= null) u686+= IS1+"c"+a686.getC_686();
                                   if (a686.getC2_686()!= null) u686+= IS1+"2"+a686.getC2_686();
                                   if (u686.length() > 5) fileUnimarc.add(u686);                   
                        }
            }          
            public String elaboraDatiLegameTitAcc (DocumentoType documento, String u2XX) {                                           
//          andiamo sui legami del documento
                         LegamiType legDocUnimarc [] = documento.getLegamiDocumento();
                         if (legDocUnimarc.length > 0) {
                                    for (int j = 0; j < legDocUnimarc.length; j++) {
                                                ArrivoLegame aLegame[] = legDocUnimarc[j].getArrivoLegame();
                                                if (aLegame.length > 0) {
                                                            for (int jj = 0; jj < aLegame.length; jj++) {
                                                                        LegameDocType legDoc = aLegame[jj].getLegameDoc();          
 
                                                                        LegameElementoAutType legEleA = aLegame[jj].getLegameElementoAut();
                                                                        if (legEleA != null) {                                                                                                     
//          700 - 701 - 702
                                                                                   DatiElementoType L700 = legEleA.getElementoAutLegato().getDatiElementoAut(); 
                                                                                   if (L700 != null) {
                                                                                               if (L700 instanceof AutorePersonaleType) {
                                                                                                           AutorePersonaleType autoreP = (AutorePersonaleType) L700;   
                                                                                                           A200 a200 = autoreP.getT200();
                                                                                                           if (a200 != null) {
                                                                                                                       if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_8_TYPE) {
                                                                                                                                  u2XX += IS1+"1700 ";
                                                                                                                       }
                                                                                                                       if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_9_TYPE) {
                                                                                                                                  u2XX += IS1+"1701 ";
                                                                                                                       }
                                                                                                                       if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_10_TYPE) {
                                                                                                                                  u2XX += IS1+"1702 ";
                                                                                                                       }
                                                                                                                       if (u2XX != null) {                                                                                                         
                                                                                                                                  u2XX = elabora200EleAut(u2XX, a200);
                                                                                                                                  u2XX += IS1+"3"+formattaID(legEleA.getIdArrivo());
                                                                                                                                  u2XX += IS1+"4"+legEleA.getRelatorCode();                                                                                                                                         
                                                                                                                       }
                                                                                                           }
                                                                                                }
//          710 - 711 - 712
                                                                                               if (L700 instanceof EnteType) {
                                                                                                           EnteType ente = (EnteType) L700;       
                                                                                                           int tipoNome = ente.getTipoNome().getType();
                                                                                                           A210 a210 = ente.getT210();
                                                                                                           if (a210 != null) {
                                                                                                                       if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_11_TYPE) {
                                                                                                                                  u2XX += IS1+"1710";
                                                                                                                       }
                                                                                                                       if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_12_TYPE) {
                                                                                                                                  u2XX += IS1+"1711";
                                                                                                                       }
                                                                                                                       if (legEleA.getTipoLegame().getType() == SbnLegameAut.VALUE_13_TYPE) {
                                                                                                                                  u2XX += IS1+"1712";
                                                                                                                       }
                                                                                                                       if (u2XX != null) {
                                                                                                                                  u2XX = elabora210EleAut(u2XX, a210, tipoNome);                                                                                                                                                                                                                                                                                                              
                                                                                                                                  u2XX += IS1+"3"+formattaID(legEleA.getIdArrivo());
                                                                                                                                  u2XX += IS1+"4"+legEleA.getRelatorCode();                                                                                                                 
                                                                                                                       }
                                                                                                           }
                                                                                               }   
                                                                                   }                                                                               
// fine   
                                                                        }                                                                    
                                                            }                     
                                                }
                                    }
                       
                         }
                         return u2XX;
               }
            /**
             * Method replaceAll.
             */
            private String replaceAll(String stringa, String oldStr, String newStr){
              String strResult = new String();
              int i, k;
 
              k=oldStr.length();
              i= stringa.indexOf(oldStr);
           
              while (i!=-1){
                        strResult =strResult+stringa.substring(0, i)+ newStr;
                        stringa =stringa.substring(i+k);
                        i= stringa.indexOf(oldStr);
              }
              strResult=strResult+stringa;
 
              return strResult;   
 
            }
            public void scriviFileUnimarc(ArrayList fileUnimarc,String tipo) {
                fileGuida = new String();
                fileIndice = new String();
                fileEtichette = new String();
                partenza = 0;
                for (int v = 0; v < fileUnimarc.size(); v++) {
                   log.info("UNIMARC   >>"+v+"<<>>"+(String)fileUnimarc.get(v)+"<<");
                   String etichetta = ((String)fileUnimarc.get(v)).substring(0,3);        
                   String datiInd   = ((String)fileUnimarc.get(v)).substring(3);
                   if (etichetta.equals("000")) {
                       if (fileGuida.length() > 0) {
                    	   registraFile(fileGuida,fileIndice,fileEtichette,tipo);
/*                    	   log.info("p>>"+partenza);
                    	   log.info("g>>"+fileGuida);
                    	   log.info("i>>"+fileIndice);
                    	   log.info("e>>"+fileEtichette);*/
                       }
                       fileGuida = datiInd;
                   } else {
						String sp = Integer.toString(partenza);
						if (sp.length() == 1) sp = "0000"+ sp;
						else if (sp.length() == 2) sp = "000"+ sp;
						else if (sp.length() == 3) sp = "00"+ sp; 
						else if (sp.length() == 4) sp = "0"+ sp;
						int lung;
						try {
						    lung = datiInd.getBytes("UTF8").length;
						} catch (UnsupportedEncodingException e) {
						    lung = datiInd.length();
						}
						String sl = Integer.toString(lung+1);
						if (sl.length() == 1) sl = "000"+ sl;
						else if (sl.length() == 2) sl = "00"+ sl;
						else if (sl.length() == 3) sl = "0"+ sl;
						fileIndice += etichetta+sl+sp;
						partenza += lung+1;
						fileEtichette += datiInd+IS2;
                   }
                }
                registraFile(fileGuida,fileIndice,fileEtichette,tipo);
            }

            public void registraFile(String fileG,String fileI,String fileE,String tipo) {
            	contatoreDocumenti++;
            	partenza = 0;
		        //File guida e indice non hanno caratteri speciali. Credo.
		        int fileGl = fileG.length();
		        int fileIl = fileI.length();       
		        int fileEl;
		        try {
		            fileEl = fileE.getBytes("UTF8").length;
		        } catch (UnsupportedEncodingException e) {
		            fileEl = fileE.length();
		        }
                partenza = fileGl+fileIl+1;
                String sp = Integer.toString(partenza);
                if (sp.length() == 1) sp = "0000"+ sp;
                else if (sp.length() == 2) sp = "000"+ sp;  
		        else if (sp.length() == 3) sp = "00"+ sp; 
		        else if (sp.length() == 4) sp = "0"+ sp;
                fileG = replaceAll(fileG, "!!!!!", sp);
                partenza = fileGl+fileIl+1+fileEl+1;
                sp = Integer.toString(partenza);
                if (sp.length() == 1) sp = "0000"+ sp;
		        else if (sp.length() == 2) sp = "000"+ sp;  
		        else if (sp.length() == 3) sp = "00"+ sp; 
		        else if (sp.length() == 4) sp = "0"+ sp;
                fileG = replaceAll(fileG, "?????", sp);
                if (tipo.equals("B")) {
                   fileOutput_bibl_file = fileG+fileI+IS2+fileE+IS3;
                   if (fileOutput_bibl_file.length() > 2) {
                       fileOutput_bibl += fileOutput_bibl_file;
                   }
                }
                else if (tipo.equals("A")){
                   fileOutput_auth_file = fileG+fileI+IS2+fileE+IS3;
                   if (fileOutput_auth_file.length() > 2) {
                	   fileOutput_auth += fileOutput_auth_file;
                   }
                }
                fileGuida = new String();
                fileIndice = new String();
                fileEtichette = new String();
                partenza = 0;   
            }
   
    class Comparatore implements Comparator{
        public int compare (Object obj1,Object obj2) {
            return obj1.toString().substring(0,3).compareTo(obj2.toString().substring(0,3));
        }
    }


// metodi estrapolati dalle classi dei package 
// che interagiscono con il protocollo e non possono 
// essere istanziate in questo package

////////////////// begin from TipiAutore.java
    /**
     * Estrae il nome dell'autore partendo da un tipo autorepersonaleType
     */
    private String valutaNomeAutore(AutorePersonaleType autore, String tipoAutore)
        throws EccezioneSbnDiagnostico {
        if (autore.getT200() == null)
            throw new EccezioneSbnDiagnostico(3290,"Nome errato");
//            return null;
        if (tipoAutore == null)
            tipoAutore = valutaTipoAutore(autore);
        String nome = autore.getT200().getA_200().trim();
        if (nome.length()==0) {
            throw new EccezioneSbnDiagnostico(3290,"Nome errato");
        }
        String b_200 = autore.getT200().getB_200();
        String[] c_200 = autore.getT200().getC_200();
        String f_200 = autore.getT200().getF_200();
        if (b_200 != null && !b_200.equals("")) {
            if (tipoAutore.equals("C") || tipoAutore.equals("D")) {
                if (b_200.startsWith(", "))
                    b_200 = b_200.substring(2);
                else if (b_200.startsWith(","))
                    throw new EccezioneSbnDiagnostico(3235,"Punteggiatura errata");
                if (!nome.endsWith(","))
                    nome += ",";
            } else {
                throw new EccezioneSbnDiagnostico(3262, "Nome non congruente");
            }
            nome += " " + b_200;
        } else {
            if ((tipoAutore.equals("C") || tipoAutore.equals("D")) && nome.indexOf(",")<0) {
                throw new EccezioneSbnDiagnostico(3262, "Nome non congruente");
            } else if ((tipoAutore.equals("A") || tipoAutore.equals("B")) && nome.indexOf(",")>=0) {
                throw new EccezioneSbnDiagnostico(3262, "Nome non congruente");
            }
        }
        String tra_angolari = "";
        if (c_200 != null && c_200.length > 0) {
            for (int i = 0; i < c_200.length; i++)
                if (i == 0)
                    tra_angolari += c_200[i];
                else
                    tra_angolari += " ; " + c_200[i];
        }
        if (f_200 != null) {
            if (tra_angolari.length()>0)
                tra_angolari += " ; ";
            tra_angolari += f_200;
        }
        if (tra_angolari.length() > 0) {
            //Controllo che le parentesi angolari non siano già nel nome
            int unc = tra_angolari.indexOf('<');
            if (unc>=0) {
                tra_angolari = tra_angolari.substring(unc+1);
            }
            unc = tra_angolari.indexOf('>');
            if (unc>=0) {
                tra_angolari = tra_angolari.substring(0,unc);
            }
            nome += " <" + tra_angolari + ">";
        }
        return nome;
    }

    /** Valuta il nome dell'autore per un tipo EnteType */
    private String valutaNomeAutore(EnteType autore, String tipo) throws EccezioneSbnDiagnostico {
        A210 t210 = autore.getT210();
        if (t210 == null || t210.getA_210() == null || t210.getA_210().trim().length()==0)
            throw new EccezioneSbnDiagnostico(3290,"Nome errato");
        if (tipo == null)
            tipo = valutaTipoAutore(autore);
        String nome = (t210.getA_210() == null ? "" : t210.getA_210().trim());
        A210_GType[] a_210G = t210.getA210_G();
        String tra_angolari = "";
        for (int i = 0; i < t210.getC_210Count(); i++) {
            if (tra_angolari.length() > 0)
                tra_angolari += " ; ";
            tra_angolari += t210.getC_210(i);
        }
        if (tipo.equals("R")) {
            if (t210.getD_210Count() > 0) {
                if (tra_angolari.length() > 0)
                    tra_angolari += " ; ";
                for (int i = 0; i < t210.getD_210Count(); i++) {
                    tra_angolari += t210.getD_210(i);
                    if (!tra_angolari.endsWith("."))
                        if (!tra_angolari.endsWith(". ;"))
                            tra_angolari += ".";
                }
            }
            if (t210.getF_210() != null) {
                if (tra_angolari.length() > 0
                    && !tra_angolari.trim().endsWith(" ;")
                    && !t210.getF_210().startsWith("; "))
                    tra_angolari += " ; ";
                else if (tra_angolari.endsWith(" ;") || t210.getF_210().startsWith("; "))
                    tra_angolari += " ";
                tra_angolari += t210.getF_210();
            }
            for (int i = 0; i < t210.getE_210Count(); i++) {
                if (tra_angolari.length() > 0
                    && !tra_angolari.trim().endsWith(" ;")
                    && !t210.getE_210(i).startsWith("; "))
                    tra_angolari += " ; ";
                else if (tra_angolari.endsWith(" ;") || t210.getE_210(i).startsWith("; "))
                    tra_angolari += " ";
                tra_angolari += t210.getE_210(i);
            }
        } else if (tipo.equals("E")) {
        if (t210.getD_210Count() > 0) {
            if (tra_angolari.length() > 0)
                tra_angolari += " ; ";
            for (int i = 0; i < t210.getD_210Count(); i++) {
                tra_angolari += t210.getD_210(i);
                if (!tra_angolari.endsWith("."))
                    if (!tra_angolari.endsWith(". ;"))
                        tra_angolari += ".";
            }
        }
        for (int i = 0; i < t210.getE_210Count(); i++) {
            if (tra_angolari.length() > 0
                && !tra_angolari.trim().endsWith(" ;")
                && !t210.getE_210(i).startsWith("; "))
                tra_angolari += " ; ";
            else if (tra_angolari.endsWith(" ;") || t210.getE_210(i).startsWith("; "))
                tra_angolari += " ";
            tra_angolari += t210.getE_210(i);
        }
        if (t210.getF_210() != null) {
            if (tra_angolari.length() > 0
                && !tra_angolari.trim().endsWith(" ;")
                && !t210.getF_210().startsWith("; "))
                tra_angolari += " ; ";
            else if (tra_angolari.endsWith(" ;") || t210.getF_210().startsWith("; "))
                tra_angolari += " ";
            tra_angolari += t210.getF_210();
        }
    }

        if (tra_angolari.length() > 0) {
            //Controllo che le parentesi angolari non siano già nel nome
            int unc = tra_angolari.indexOf('<');
            if (unc>=0) {
                tra_angolari = tra_angolari.substring(unc+1);
            }
            unc = tra_angolari.indexOf('>');
            if (unc>=0) {
                tra_angolari = tra_angolari.substring(0,unc);
            }
            nome += " <" + tra_angolari + ">";
        }

        //Questo ci dovrebbe essere se non c'è la parte prima
        if (a_210G != null && a_210G.length > 0) {
            for (int i = 0; i < a_210G.length; i++) {
                if (a_210G[i].getB_210() != null) { 
                    if ((a_210G[i].getB_210().startsWith(" : ") == false)) {
                        nome += " : ";
                    }
                    nome += a_210G[i].getB_210();
                }
                if (a_210G[i].getC_210Count() > 0) {
                    nome += " <";
                    for (int c = 0; c < a_210G[i].getC_210Count(); c++) {
                        if (c == 0)
                            nome += a_210G[i].getC_210(c);
                        else
                            nome += " ; " + a_210G[i].getC_210(c);
                    }
                    nome += ">";
                }
            }
        }
        return nome;
    }

    private String valutaTipoAutore(AutorePersonaleType autore) throws EccezioneSbnDiagnostico {
        SbnTipoNomeAutore tipo = autore.getTipoNome();
        if (tipo != null)
            return tipo.toString();
        if (autore.getT200() == null || autore.getT200().getA_200() == null)
            throw new EccezioneSbnDiagnostico(3045, "Tipo di autore non conosciuto.");
        String a_200 = autore.getT200().getA_200().trim();
        int n = a_200.indexOf(" : ");
        if (n >= 0)
            a_200 = a_200.substring(0, n);
        if (autore.getT200().getId2() == null
            || autore.getT200().getId2().equals(Indicatore.valueOf(" "))
            || autore.getT200().getId2().equals(Indicatore.valueOf("0")))
            if (a_200.indexOf(" ") > 0)
                return "B";
            else
                return "A";
        else if (autore.getT200().getId2().equals(Indicatore.valueOf("1"))) {
            if (autore.getT200().getB_200() == null || autore.getT200().getB_200().trim().equals("")) {
                int virgola = a_200.indexOf(",");
                if (virgola >=0) {
                    a_200 = a_200.substring(0,virgola);
                } else {
                    throw new EccezioneSbnDiagnostico(3056, "Tipo di autore incongruente.");
                }
            }
            if (a_200.indexOf(" ") > 0)
                return "D";
            else
                return "C";
        }
        throw new EccezioneSbnDiagnostico(3045, "Tipo di autore non conosciuto.");

    }
    /** Valuta di quale tipo di autore si tratta */
    private String valutaTipoAutore(EnteType ente) throws EccezioneSbnDiagnostico {
        SbnTipoNomeAutore tipo = ente.getTipoNome();
        if (tipo != null)
            return tipo.toString();
        if (ente.getT210() != null) {
            if (ente.getT210().getId1() == null
                || ente.getT210().getId1().equals(Indicatore.valueOf(" "))
                || ente.getT210().getId1().equals(Indicatore.valueOf("0"))) {
                if (ente.getT210().getId2().equals(Indicatore.valueOf("0")))
                    return "E";
                else if (ente.getT210().getId2().equals(Indicatore.valueOf("1")))
                    return "G";
                else if (ente.getT210().getId2().equals(Indicatore.valueOf("2"))) {
                    if (ente.getT210().getA210_GCount() > 0)
                        return "G";
                    else
                        return "E";
                }
            } else if (ente.getT210().getId1().equals(Indicatore.valueOf("1")))
                return "R";
        }
        log.error("Tipo di autore non conosciuto.");
        throw new EccezioneSbnDiagnostico(3045);
    }
////////////////// end from TipiAutore.java

}

