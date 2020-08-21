import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import it.finsiel.misc.*;


public class Classificazioni {
	private HashMap classMap = null; 		
	static char charSepArrayPipe[] = { '|'};

	String [] sortedBidEditionArray;	
	BidEditionComparator bidEditionComparator;	
	
	HashMap[] edizioni; 
	int numEedizioni = 30;
	int edizioneDiDefault; // ultima edizione disponibile
	String edizioneDiDefaultStr; // ultima edizione disponibile

	 private class BidEditionComparator implements Comparator{
		 
		 public int compare(Object o1, Object o2) {
			for (int i=0; i < 10; i++)
			{
				if (((String)o1).charAt(i) < ((String)o2).charAt(i))
					return -1;	 	
				if (((String)o1).charAt(i) > ((String)o2).charAt(i))
					return 1;	 	
		 	}
		 	return 0;
		 	}
		 }	
	
	public Classificazioni()
	{
		edizioni = new HashMap[30];
		for (int i=0; i < numEedizioni; i++)
			edizioni[i] = new HashMap();

		bidEditionComparator = new BidEditionComparator();
		
	}
	
	public void loadDescrizioni(String classificazioniFileIn)
	{
		//		bibMap.put("IEI 02", "RM0255");
		int classCounter = 0;
		String edizione, codiceClassificazione, descrizioneClassificazione;
		int edizioneNum;
		System.out.println("Stiamo caricando il file delle descrizioni delle classificazioni: " + classificazioniFileIn); 
		
		try {
		BufferedReader in = new BufferedReader(new FileReader(classificazioniFileIn));
		while (true) {
			String s;
			try {
				s = in.readLine();
				if (s == null)
					break;
				else {
					if (s.length() == 0 || s.charAt(0) == '#')
						continue;

//					String ar[] = MiscString.estraiCampi(s, "|");
//					String ar[] = MiscString.estraiCampi(s, charSepArrayPipe, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
					String ar[] = MiscString.estraiCampi(s, charSepArrayPipe, MiscStringTokenizer.RETURN_EMPTY_TOKENS_TRUE);

					if (ar.length < 3)
					{
						System.out.println("RIGA SPEZZATA!! Mettere a posto DB: '" + s + "'");
						continue;
					}
					
					
					
					
					edizioneNum = Integer.parseInt(ar[1]);	

					String descrizioneInUnicode = "";
					try {
						descrizioneInUnicode = ConvertMisc.fromUnimarc2UnicodeTranslate(ar[2].trim());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					edizioni[edizioneNum].put(ar[0], descrizioneInUnicode);
					
					classCounter++;
//					if ((classCounter & 0xFF) == 0xFF)
//						System.out.println("\nClasssificazioni letti: " + Integer.toString(classCounter));
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
			}
//		System.out.println("\nClasssificazioni letti: " + Integer.toString(classCounter));
		
		for (int i=1; i < numEedizioni; i++)
		{
			if (edizioni[i].size() > 0)
			{
				System.out.print("\nClassificazioni per edizione " + i +": "+ edizioni[i].size() );
				edizioneDiDefault = i;
			}
		}
		
		edizioneDiDefaultStr = Integer.toString(edizioneDiDefault);
			
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();

		// Convert from binary format to readable format

	}
	} // End load

	public String getDescrizione(String classCode, String classEdition)
	{
		int edizione;
		
		if (classEdition == null)
			edizione = edizioneDiDefault;
		else
			edizione = Integer.parseInt(classEdition);
			
		String desc = (String)edizioni[edizione].get(classCode);
		if (desc == null)
			desc = "";
		return desc;
	} // End getDescrizione
	
	public String getDescrizione(String classCode, int edizione)
	{
		String desc = (String)edizioni[edizione].get(classCode); // DiDefault
		if (desc == null)
			desc = "";
		return desc;
	} // End getDescrizione

	
	public String getEdizioneDiDefaultString() {
		return edizioneDiDefaultStr;
	}

	public int getEdizioneDiDefault() {
		return edizioneDiDefault;
	}

// ---------------------
	
	boolean loadBidEdizione(String bidEditionFilename, char filesystemSeparator)
	{
		String bidEditionFilenameOutSrt = "";
		bidEditionFilenameOutSrt = bidEditionFilename; // quando non sortiamo 
		// Carica BIDs in secundis
		// ----------------------- 
		
	    // Dalla dimensione file ricava il numero di bids
		long bidEditionToLoad = 0;
		
		BufferedReader sortedBufferReaderIn;
		File f = null;

		try {
 			sortedBufferReaderIn = new BufferedReader(new FileReader(bidEditionFilenameOutSrt));
 		 	System.out.println("Reading " + bidEditionFilenameOutSrt );
 
			f = new File(bidEditionFilenameOutSrt);

//			if (filesystemSeparator == '\\')
//				isbnToLoad = f.length()/(13+2); // CR/LF
//			else
//			bidEditionToLoad = f.length()/(49+1); // LF 
			bidEditionToLoad = f.length()/(46+1); // LF 

			System.out.print("Bid/Edizione da caricare : " + bidEditionToLoad + "\r");
	
 	 		sortedBidEditionArray = new String[(int)bidEditionToLoad];
 		 
 			int ctr=0;
// 			int epmtyCtr=0;
 			while (true) {
 				String s;
 	
				if ((ctr & 0x1FF) == 0x1FF)
					System.out.print("Bid/Edizione letti: " + ctr + "\r");

 				try {
 					s = sortedBufferReaderIn.readLine();
 					if (s == null)
 						break;

 					else if (Misc.emptyString(s))
 					{
// 						epmtyCtr++; 					
 						continue;
 					}

 					// Prendiamo il bid e l'edizione solamente
//					String ar[] = MiscString.estraiCampi(s, charSepArrayPipe, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
// 					sortedBidEditionArray[ctr++] = new String (ar[0] + ar[2] );
 					sortedBidEditionArray[ctr++] = new String(s);
 					}
 				 catch (IOException e) {
 					System.out.println("ECCEZIONE: File: " + bidEditionFilenameOutSrt);
 					e.printStackTrace();
 					}
 				}
 			
 			System.out.println("Letti " + ctr + " Bid edizione");
 				try {
 				sortedBufferReaderIn.close();
 			} catch (IOException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 				return false;
 			}
 			}
 	 		catch (FileNotFoundException e) {
 				// TODO Auto-generated catch block
 	 			System.out.println("ECCEZIONE: bidEditionFilenameOutSrt = '" + bidEditionFilenameOutSrt+"'");
 				e.printStackTrace();
 				return false;
 			}

 	 		return true;
	} // End loadBidEdizione

/**
 * Ritorna l'edizone o null
 */
	public String getEdizionePerBid(String bid, String classe)
	{
		int pos;
		try {
			pos = Arrays.binarySearch(sortedBidEditionArray, bid, bidEditionComparator);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		if (pos < 0)
			return null;
		else
		{
			// Siccome un bid puo' avere + classi
			// Verifichiamo che la classe sia quella corretta
			
			if (sortedBidEditionArray[pos].substring(14).startsWith(classe))
				return sortedBidEditionArray[pos].substring(11, 13);

			// Risaliamo all'indietro in quanto BID non univoco e potrei averne beccato uno nel mezzo
			int posBack = pos-1;
			while (posBack >= 0)
			{
				if (sortedBidEditionArray[posBack].startsWith(bid))
				{
					if (sortedBidEditionArray[posBack].substring(14).startsWith(classe))
						return sortedBidEditionArray[posBack].substring(11,13);
					posBack--;
				}
				else
					break;
			}
			int posForward = pos+1;
			while (posForward < sortedBidEditionArray.length)
			{
				if (sortedBidEditionArray[posForward].startsWith(bid))
				{
					if (sortedBidEditionArray[posForward].substring(14).startsWith(classe))
						return sortedBidEditionArray[posForward].substring(11,13);
					posForward++;
				}
				else
					break;
			}
		}
		return null; // EDITION NOT FOUND FOR BID/CLASS
	}
	
	
	
	
}
