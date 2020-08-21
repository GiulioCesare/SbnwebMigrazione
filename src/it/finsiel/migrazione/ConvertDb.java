package it.finsiel.migrazione;

import it.finsiel.misc.MiscString;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

public class ConvertDb {
	private final static char BLANK = (char) 32;
	private final static char c_ok = (char) 254;
	private final static char c_del = (char) 255;

	String codPolo;
    Hashtable mappaCodiciRepertorio;
    Hashtable fieldValue2IdMapping;
    LoadSequence loadSequence1 = null;
    LoadSequence loadSequence2 = null;
    LoadSequence loadSequence3 = null;
	public int sequenceIdFattura = 1;
	public int sequenceIdOfferteFornitore = 1;
	char fileSystemSeparator = '/';
    String ky_cles1_A = null;
    String ky_cles2_A = null;
    static char[] separatoriC = { '\u0020', '\'', '-', '\"', '+', '/', ':', '<', '=', '>', '\\', '&', '@' };
    static String separatori = new String(separatoriC);
    static char spazioC = '\u0020';
    static String spazio = "" + spazioC;
    
	public ConvertDb()
    {
    }	

	String sequenceFilename;
	BufferedWriter sequenceOut;
	int sequenceKeyLength; 
	
	
	
/*	
	public void completaTbfBiblioteca(String ar1[])
	{
		 // Informix
//				ar1[28-1] = ConvertConstants.MIGRATION_SEQUENCE;
//	    	ar1[28-1] = Integer.toString(sequenceIdBiblioteca++);
//			ar1[29-1] = ConvertConstants.MIGRATION_CD_ANA_BIBLIOTECA_TEMP;
//			ar1[30-1] = "N"; //fl_canc
	} // End completaTb
*/

	void apriSequenceFile(String sequenceFilename, int aSequenceKeyLength)
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




    void setFieldValue2IdMapping (Hashtable fieldValue2IdMapping)
    {
    	this.fieldValue2IdMapping = fieldValue2IdMapping;
    }
    
    
    boolean caricaSequenceFiles(String sequenceFilename, int length)
    {
	loadSequence1 = new LoadSequence();
	loadSequence1.run(sequenceFilename, length, fileSystemSeparator);
	return true;
    }
    
    boolean caricaSequenceFiles(String sequenceFilename1, int length1, String sequenceFilename2, int length2)
    {
	loadSequence1 = new LoadSequence();
	loadSequence1.run(sequenceFilename1, length1, fileSystemSeparator);
	loadSequence2 = new LoadSequence();
	loadSequence2.run(sequenceFilename2, length2, fileSystemSeparator);
    return true;
    }
    
    boolean caricaSequenceFiles(String sequenceFilename1, int length1, 
    		String sequenceFilename2, int length2,
    		String sequenceFilename3, int length3)
    {
	loadSequence1 = new LoadSequence();
	loadSequence1.run(sequenceFilename1, length1, fileSystemSeparator);
	loadSequence2 = new LoadSequence();
	loadSequence2.run(sequenceFilename2, length2, fileSystemSeparator);
	loadSequence3 = new LoadSequence();
	loadSequence3.run(sequenceFilename3, length3, fileSystemSeparator);
    return true;
    }


    void calcoloCles(String temp, String tipoNome)  {
        //CLES: Cavo la roba in più e elimino i doppi spazi
        char c;
        temp = temp.trim();
        //Elimino gli _
        temp = rimuoviCarattere(temp, "_");
        if (tipoNome.equals("A") || tipoNome.equals("C")) {
            int n = temp.indexOf(' ');
            if (n > 0)
                temp = rimuoviCarattere(temp.substring(0, n), "-") + temp.substring(n).replace('-', ' ');
            else
                temp = rimuoviCarattere(temp, "-");
        } else {
            temp = temp.replace('-', ' ');
        }
        //Rimuovo #
        if (tipoNome.equals("A") || tipoNome.equals("B")) {
            temp = temp.replace('#', ' ');
        } else {
            temp = rimuoviCarattere(temp, "#");
        }
        //Tolgo tutti i brutti caratteri residui
        temp = rimuoviCarattere(temp,".");
        for (int i = 0; i < temp.length(); i++) {
            c = temp.charAt(i);
            if ((c != ' ') && !Character.isLetter(c) && !Character.isDigit(c)) {
                temp = temp.replace(c, ' ');
            }
        }
        //Formatto
        temp = formatta(temp);

        if (temp.length() <= 50)
        {
            ky_cles1_A = temp;
            ky_cles2_A = ConvertConstants.MIGRATION_NULL;;
        }
            
        else {
            ky_cles1_A = temp.substring(0, 50);
            if (temp.length() > 80)
                ky_cles2_A = temp.substring(50, 80);
            else
                ky_cles2_A = temp.substring(50);
        }
    }
    
    String rimuoviCarattere(String stringa, String da_rimuovere) {
        int n;
        int l = da_rimuovere.length();
        //Elimino gli _
        while ((n = stringa.indexOf(da_rimuovere)) >= 0)
            stringa = stringa.substring(0, n) + stringa.substring(n + l);
        return stringa;
    }
    
    String formatta(String stringa) {
        if (stringa==null) return null;
        char c;
        stringa = stringa.trim();
        //togli punteggiatura
        //Separatori sostituiti dallo spazio
        for (int i = 0; i < separatoriC.length; i++) {
            c = separatoriC[i];
            stringa = stringa.replace(c, spazioC);
        }
        //Il resto deve essere semplicemente tolto
        for (int i = 0; i < stringa.length(); i++) {
            c = stringa.charAt(i);
            if ((c != spazioC) && !Character.isLetter(c) && !Character.isDigit(c)) {
                //stringa=stringa.replace(c,spazioC);
                stringa = stringa.substring(0, i) + stringa.substring(i + 1);
            }
        }
        int n;
        //elimina doppi spazi(dovuti alla punteggiatura)
        while ((n = stringa.indexOf(spazio + spazio)) > 0)
            stringa = stringa.substring(0, n) + stringa.substring(n + 1);
        //rendi tutto maiuscolo
        return (stringa.toUpperCase());
    }    
    
    String getSequenceIdSezAcquisBibliografiche(String codiceSezione)
    {
    	String sequence;
    	sequence = loadSequence1.getSequence(codiceSezione);
    	if (sequence == null)
    		return "-1";
    	return sequence.substring(0,7);
    }
    
    
	String getSequenceIdCapitoloBilancio(String capitolo)
	{
    	String sequence;
    	sequence = loadSequence1.getSequence(capitolo);
    	if (sequence == null)
    		return "-1";
    	return sequence.substring(0,7);
	}
	String getSequenceIdCapitoloBilancio2(String capitolo)
	{
    	String sequence;
    	sequence = loadSequence2.getSequence(capitolo);
    	if (sequence == null)
    		return "-1";
    	return sequence.substring(0,7);
	}

	String getSequenceIdSpecificitaTitolo(String codiceSpecificazione)
	{
		return ConvertConstants.MIGRATION_NULL; // Da fare
	}
	String getSequenceIdUtenteOccupazione(String codiceOccupazione)
	{
		return ConvertConstants.MIGRATION_NULL; // Da fare
	}

	String getSequenceIdUtente(String codiceUtente)
	{
    	String sequence;
    	sequence = loadSequence1.getSequence(codiceUtente);
    	if (sequence == null)
    		return "-1";
    	return sequence.substring(0,7);
	}



    /*
     * Metodo preso da Indice
    */
    String creaIndiceISBD(String isbd, String natura)
    {
        String indiceIsbd = "";
        String areeIsbd = isbd;
        Vector inizioAree = new Vector();
        int posizioneAree = 0;

        if (!isbd.equals(""))
        {
            //AREA DELLE NOTE
            //la punteggiatura che precede l'area delle note è (punto,spazio,doppia tonda aperta)
            //essendo un'area ripetibile, si calcolano le successive
            //ad ogni punteggiatura (punto,spazio,trattino,spazio) che le introduce.
            int posizioneNote = -1;
            String areaNote = "";
            Vector inizioNote = new Vector();

			posizioneNote = isbd.indexOf(" (("); // Arge, da richiesta Mantis MANTIS 735 17/06/04 
            if (posizioneNote != -1)
            {
                areaNote = isbd.substring(posizioneNote);
                areeIsbd = isbd.substring(0, posizioneNote);
				inizioNote.addElement("" + (posizioneNote + 3 + 1)); // No dot 14/09/05

                //si cercano eventuali successive aree delle note
                do
                {
                    posizioneNote = areaNote.indexOf(". - ", posizioneNote);

                    if (posizioneNote != -1)
                    {
//                        inizioNote.addElement("" + (posizioneNote + 4 + 1));
//                        posizioneNote += +4;
						inizioNote.addElement("" + (posizioneNote + 3 + 1)); // No dot 14/09/05
						posizioneNote += +3;
                    }
                }
                while (posizioneNote != -1);
            }

            //fine if (posizioneNote != -1)
            do
            {
                posizioneAree = areeIsbd.indexOf(". - ", posizioneAree);

                if (posizioneAree != -1)
                {
                    inizioAree.addElement("" + (posizioneAree + 4 + 1));
                    posizioneAree += +4;
                }
            }
            while (posizioneAree != -1);

            if (inizioAree.size() != 0)
            {
                if (natura.equals("M") || natura.equals("W"))
                {
                    //MONOGRAFIE
                    //AREA TITOLO E RESPONSABILITA'(UNIMARC 200)
                    //AREA EDIZIONE (UNIMARC 205)
                    //AREA PUBBLICAZIONE (UNIMARC 210)
                    //AEREA DESCRIZIONE FISICA (UNIMARC 215)
                    if (inizioAree.size() == 1)
                    {
                    //AREA TITOLO E RESPONSABILITA', AREA DESCRIZIONE FISICA
                        indiceIsbd = "200-0001;" + "215-" +addSpaces((String) inizioAree.get(0)) + ";";
                    }
                    else if (inizioAree.size() == 2)
                    {
                    //AREA TITOLO E RESPONSABILITA', AREA PUBBLIC., AREA DESCRIZIONE FISICA
                        indiceIsbd = "200-0001;" + "210-" +
                        addSpaces((String) inizioAree.get(0)) + ";" + "215-" +
                        addSpaces((String) inizioAree.get(1)) + ";";
                    }
                    else if (inizioAree.size() == 3)
                    {
                    //AREA TITOLO E RESPONSABILITA', AREA EDIZIONE, AREA PUBBLIC., AREA DESCRIZIONE FISICA
//                        indiceIsbd = "200-0001;" + "206-" + // ex 205
						indiceIsbd = "200-0001;" + "205-" + // 12/09/05 rimesso a posto dopo chiarimento con Franco
                        addSpaces((String) inizioAree.get(0)) + ";" + "210-" +
                        addSpaces((String) inizioAree.get(1)) + ";" + "215-" +
                        addSpaces((String) inizioAree.get(2)) + ";";
                    }
                    else
                    {
                    //presenti più aree di quelle previste
                    //AREA TITOLO E RESPONSABILITA'
                        indiceIsbd = "200-0001;";
                    }
                }
                else if (natura.equals("S"))
                {
                    //PUBBLICAZIONI IN SERIE
                    //AREA TITOLO E RESPONSABILITA' (UNIMARC 200)
                    //AREA EDIZIONE (UNIMARC 205)
                    //AREA NUMERAZIONE (UNIMARC 207)
                    //AREA PUBBLICAZIONE (UNIMARC 210)
                    //AREA DESCRIZIONE FISICA (UNIMARC 215)
                    if (inizioAree.size() == 1)
                    {
                        //AREA TITOLO E RESPONSABILITA', AREA PUBBLICAZIONE
                        indiceIsbd = "200-0001;" + "210-" +
                        addSpaces((String) inizioAree.get(0)) + ";";
                    }
                    else if (inizioAree.size() == 2)
                    {
                        //AREA TITOLO E RESPONSABILITA', AREA PUBBLIC., AREA DESCRIZIONE FISICA
                        indiceIsbd = "200-0001;" + "210-" +
                        addSpaces((String) inizioAree.get(0)) + ";" + "215-" +
                        addSpaces((String) inizioAree.get(1)) + ";";
                    }
                    else if (inizioAree.size() == 3)
                    {
                        //AREA TITOLO E RESPONSABILITA', AREA NUMERAZIONE, AREA PUBBLIC., AREA DESCRIZIONE FISICA
                        indiceIsbd = "200-0001;" + "207-" +
                        addSpaces((String) inizioAree.get(0)) + ";" + "210-" +
                        addSpaces((String) inizioAree.get(1)) + ";" + "215-" +
                        addSpaces((String) inizioAree.get(2)) + ";";
                    }
                    else if (inizioAree.size() == 4)
                    {
                        //AREA TITOLO E RESPONS., AREA EDIZIONE, AREA NUMERAZIONE, AREA PUBBLIC., AREA DESCR. FISICA
                        indiceIsbd = "200-0001;" + "205-" +
                        addSpaces((String) inizioAree.get(0)) + ";" + "207-" +
                        addSpaces((String) inizioAree.get(1)) + ";" + "210-" +
                        addSpaces((String) inizioAree.get(2)) + ";" + "215-" +
                        addSpaces((String) inizioAree.get(3)) + ";";
                    }
                    else
                    {
                        //presenti più aree di quelle previste
                        //AREA TITOLO E RESPONSABILITA'
                        indiceIsbd = "200-0001;";
                    }
                }
                else if (natura.equals("C"))
                {
                    //COLLEZIONE
                    //AREA TITOLO E RESPONSABILITA' (UNIMARC 200)
                    //AREA PUBBLICAZIONE (UNIMARC 210)
                    if (inizioAree.size() == 1)
                    {
                        //AREA TITOLO E RESPONSABILITA', AREA PUBBLICAZIONE
                        indiceIsbd = "200-0001;" + "210-" + addSpaces((String) inizioAree.get(0)) + ";";
                    }
                    else
                    {
                        //presenti più aree di quelle previste
                        //AREA TITOLO E RESPONSABILITA'
                        indiceIsbd = "200-0001;";
                    }
                }
                else
                {
                    //presente non solo area Titolo e Responsab.
                    //ma non trattondosi di una delle nature sopra controllate
                    //si considera solo questa.
                    //AREA TITOLO E RESPONSABILITA'
                    indiceIsbd = "200-0001;";
                }
            } //fine if (inizioAree.size() != 0)
            else
            {
                if (!areeIsbd.equals(""))
                {
                    //PRESENTE SOLO AREA TITOLO E RESPONSAB.
                    //si applica a qualsiasi natura
                    //AREA TITOLO E RESPONSABILITA'
                    indiceIsbd = "200-0001;";
                }
            }

            if (inizioNote.size() != 0)
            {
                indiceIsbd += "300-" + addSpaces(((String)inizioNote.get(0))) + ";";
		/*
                for (int i = 0; i < inizioNote.size(); i++)
                {
                    indiceIsbd = indiceIsbd + "300-" +
                        (String) inizioNote.get(i) + ";";
                }
		*/
            }
        }

	//fine if (!isbd.equals(""))
        return indiceIsbd;
    } // End crteaIndiceISBD
	String addSpaces(String valore)
	{
        String tmpString = "000" .concat(valore);
		return tmpString.substring(valore.length() - 1);
	}

    String creaTipoMateriale(String bid, String natura)
    {
        String tipoMateriale = "";
        if (bid.charAt(4-1) == 'E' && (natura.equals("M")  || natura.equals("W") || natura.equals("N") ))
     	   tipoMateriale = "E";
        else if (bid.charAt(4-1) != 'E' && (natura.equals("M") || natura.equals("W") || natura.equals("S") || natura.equals("N")))
     	   tipoMateriale = "M";
        else  	
     	   tipoMateriale = " ";
        return tipoMateriale;
    } // End creaTipoMateriale

	  /*
	   Generazione del tipo record ( _UNI) in base al primo valore rilevato in
	   uno dei campi del genere (CD_GENERE_1 oppure CD_GENERE_2 oppure CD_GENERE_3 oppure CD_GENERE_4).
	   Si applica solo ai titoli con natura 'M', 'S', 'W' o 'N'. Se nessuno dei generi è valorizzato,
	   viene restituito come valore di default 'a'.
	  */
	  String creaTipoRecord(String genere1, String genere2, String genere3, String genere4)
	  {
		  String genere = "";

		  //controllo se è stato passato almeno un genere
		  if (genere1 != null) //  && !genere1.trim().equals("")
		  {
			  genere = genere1.trim();
		  }
		  if (genere2 != null) //  && !genere2.trim().equals("")
		  {
			  genere = genere2.trim();
		  }
		  if (genere3 != null) //  && !genere3.trim().equals("")
		  {
			  genere = genere3.trim();
		  }
		  if (genere4 != null) //  && !genere4.trim().equals("")
		  {
			  genere = genere4.trim();
		  }

		//calcolo del tipo record
		if (genere.equals("0") || genere.equals("00")) {
			return "k";
		}
		else if (genere.equals("1") || genere.equals("01")) {
			return "i";
		}
		else if (genere.equals("2") || genere.equals("02")) {
			return "a";
		}
		else if (genere.equals("3")|| genere.equals("03")) {
			return "b"; // g
		}
		else if (genere.equals("4")|| genere.equals("04")) {
			return "m";
		}
		else if (genere.equals("5")|| genere.equals("05")) {
			return "r";
		}
		else if (genere.equals("6")|| genere.equals("06")) {
			return "j";
		}
		else if (genere.equals("7") || genere.equals("07")) {
			return "g";
		}
		else if (genere.equals("8") || genere.equals("08")) {
			return "d"; // g
		}
		else if (genere.equals("9") || genere.equals("09")) {
			return "c";
		}
		else if (genere.equals("T") || genere.equals("TT")) {
			return "f";
		}
		else if (genere.equals("X") || genere.equals("XX")) {
			return "l";
		}
		else if (genere.equals("Y") || genere.equals("YY")) {
			return "e";
		}
		else {
			return "a";
		}
	} // End tipoRecord

	  
	  
void chiudiSequenceFile(String sortCommand)
{
	if (sequenceOut != null)
	{
		
		try {
			sequenceOut.close();
			
			// Sortiamo il file delle sequence
			String sortFilename;
			int index = this.sequenceFilename.lastIndexOf(".");
			
			sortFilename = this.sequenceFilename.substring(0, index) + ".srt";

			
			// Ordina gli inventaris in primis  
			// --------------------- 
			try {

			      Process p; 
			      String command = sortCommand + sortFilename + " " + this.sequenceFilename;

			      // NMB: exec lunghe sotto windows toppano
			      
			      System.out.println("About to execute: " + command);
			      p = Runtime.getRuntime().exec(command);
			      //		      p = Runtime.getRuntime().exec("sort -o " + sortInventary);
			      p.waitFor();
			      System.out.println(p.exitValue());
			      p.destroy() ;
			    }
			    catch (Exception err) {
			      err.printStackTrace();
			      return;
			    }
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
		
	String normalizzaCollocazione(String src) {

		int pos;
		String coll1, coll2, numeri;
		boolean isSpace, isNum;
		char curr, prev, next;

		if (strIsNull(src))
			return src;

		String coll = src.toUpperCase().trim();
		//coll = convertiNumeriRomani(coll);
		
		isSpace = false;
		isNum = false;
		numeri = "";
		coll1 =	"";
		curr = BLANK; // spazio

		for (pos = 0; pos < coll.length(); pos++) {
			curr = coll.charAt(pos);

			if (Character.isDigit(curr) ) {				
				isSpace = false;
				isNum = true;
				numeri += curr;
			} else {
				if (isNum) {
					coll1 += gruppoNumerico(numeri);
					isNum = false;
					numeri = "";
				}

				switch (curr) {
				case '.':
				case '-':
				case '/':
					if ((!isSpace) && (!isNum)) {
						isSpace = true;
						isNum = false;
						coll1 += BLANK;
					}
					break;
					
				case BLANK:
					if (!isSpace) {
						isSpace = true;
						isNum = false;
						coll1 += BLANK;
					}
					break;
					
				default:
					isSpace = false;
					isNum = false;
					coll1 += curr;
				}
			} // end else
		}

		// Verifico se devo aggiornare la stringa
		if (isNum) {
			coll1 += gruppoNumerico(numeri);
			isNum = false;
			numeri = "";
		}
		
		//Elimino caratteri unicode non previsti
//		coll1 = unicode.convert(coll1).trim();
		coll1 = ConvertCaratteriSpeciali.convert(coll1).trim();
		

		// elimino spazi fra gruppi alfanumerici e numerici
		pos = coll1.indexOf(BLANK);
		while (pos >= 0) {
			prev = coll1.charAt(pos - 1);
			next = coll1.charAt(pos + 1);
			// Devo unire i gruppi solo se sono di tipo opposto
			if ((!Character.isDigit(prev) && Character.isDigit(next))
					|| (Character.isDigit(prev) && !Character.isDigit(next))) {
				coll1 = coll1.substring(0, pos) + c_del
						+ coll1.substring(pos + 1);
			} else {
				coll1 = coll1.substring(0, pos) + c_ok
						+ coll1.substring(pos + 1);
			}
			pos = coll1.indexOf(BLANK);
		}

		coll2 = "";
		// Sostituisco i segnaposto fra i gruppi
		for (pos = 0; pos < coll1.length(); pos++) {

			curr = coll1.charAt(pos);
			switch (curr) {
			case c_ok:
				coll2 += BLANK;
				break;
			case c_del:
				continue;
			default:
				coll2 += curr;
			}
		}
		
		String norm = coll2.trim();
		if (norm.length() > 80)
			return norm.substring(0, 80);

		return norm;

	}
	String gruppoNumerico(String gruppo) {

		// Calcolo la lunghezza del gruppo numerico e del carattere
		// di controllo
		int limit;
		int sestine = 0;

		for (limit = 5; limit < gruppo.length(); limit += 6) {
			sestine++;
		}
		// Formatta il numero a 6 (...12....18..24 Etc) cifre
		// mettendo davanti tutti '0'
		if (sestine > 0) {
			return String.valueOf(sestine) + fillLeft(gruppo, '0', limit);

		} else
			return fillLeft(gruppo, '0', limit + 1);
	}

	
	String fillLeft(String src, char car, int limit) {

		if (src == null)
			return null;
		if (src.length() >= limit)
			return src;

		int diff = limit - src.length();

		String tmp = "";
		for (int i = 0; i < diff; i++) {
			tmp += car;
		}
		return tmp + src;
	}
	
	  
	private boolean strIsNull(String data) {
		return (data == null) || ("".equals(data.trim()));
	}
	  
	  
	/** 
	 * questo tipo di normalizzazione elimina i doppi spazi e i simboli di punteggiatura
	 */
    public static String normalizzazioneGenerica(String nome){ //  throws EccezioneSbnDiagnostico
//      try {
       
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
      
//      }
//      catch (EccezioneSbnDiagnostico eccezione)
//	  {
//			log.error(eccezione) ;
//	  }	
      return nome.toUpperCase();
    }
	  
	void salvaSequence (String aSequence, String aKey)
	{

		String squenceOut = MiscString.paddingString(aSequence, 7, '0', true) + MiscString.paddingString(aKey, sequenceKeyLength, ' ', false) + '\n'; 
		try {
			
			sequenceOut.write(squenceOut);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     
	}
	  
	  
} // End ConvertDb
