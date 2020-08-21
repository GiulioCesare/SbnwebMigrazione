import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;


/**
 * Super enalotto betting study
 * Argentino Trombin
 * 15/10/2008
 */
public class S8 {
	// ELENCO Estrazioni
	String [] sortedEstrazioniArray;	
	EstrazioniComparator6 comp6;
	EstrazioniComparator5_1 comp5_1;

	public S8(){
		comp6 = new EstrazioniComparator6();
		comp5_1 = new EstrazioniComparator5_1();
	}

	
	 public class EstrazioniComparator6 implements Comparator{

		 public int compare(Object o1, Object o2) {
//		return (((String)o1).compareTo((String)o2));	 
		for (int i=0; i < 12; i++)
		{
			if (((String)o1).charAt(i) < ((String)o2).charAt(i))
				return -1;	 	
			if (((String)o1).charAt(i) > ((String)o2).charAt(i))
				return 1;	 	
	 	}
		 return 0;
	 	}
	 } // End EstrazioniComparator6	
	
	 public class EstrazioniComparator5_1 implements Comparator{

		 public int compare(Object o1, Object o2) {
//		return (((String)o1).compareTo((String)o2));	 
		int i=0;
		for (; i < 10; i++)
		{
			if (((String)o1).charAt(i) < ((String)o2).charAt(i))
				return -1;	 	
			if (((String)o1).charAt(i) > ((String)o2).charAt(i))
				return 1;	 	
	 	}
		i+=2; // Salta il 6to numero
		for (; i < 10; i++)
		{
			if (((String)o1).charAt(i) < ((String)o2).charAt(i))
				return -1;	 	
			if (((String)o1).charAt(i) > ((String)o2).charAt(i))
				return 1;	 	
	 	}
		 return 0;
	 	}
	 } // End EstrazioniComparator6	
	

	
	
	
	private boolean caricaEstrazioni(String estrazioniFilenameIn) // , char filesystemSeparator
	{
		//String invetaryFilenameOutSrt = "";
		//invetaryFilenameOutSrt = bidFilename; // quando non sortiamo 
		// Carica BIDs in secundis
		// ----------------------- 
		
	    // Dalla dimensione file ricava il numero di bids
		long bidToLoad = 0;
		
		BufferedReader sortedBufferReaderIn;
		File f = null;

		try {
 			sortedBufferReaderIn = new BufferedReader(new FileReader(estrazioniFilenameIn));
 		 	System.out.println("Reading " + estrazioniFilenameIn );
 
			f = new File(estrazioniFilenameIn);

//			if (filesystemSeparator == '\\')
				bidToLoad = f.length()/(10+2); // CR/LF
//			else
//				bidToLoad = f.length()/(10+1); // LF 
	
 	 		sortedEstrazioniArray = new String[(int)bidToLoad];
 		 
 			int ctr=0;
 			while (true) {
 				String s;
 	
				if ((ctr & 0x1FF) == 0x1FF)
					System.out.print("Estrazioni lette: " + ctr + "\r");

 				try {
 					s = sortedBufferReaderIn.readLine();
 					if (s == null)
 						break;

 					else if (Misc.emptyString(s))
 						continue;

 					sortedEstrazioniArray[ctr++] = new String (s);
 					}
 				 catch (IOException e) {
 					e.printStackTrace();
 					}
 				}
 			
 			System.out.println("Lette " + ctr + " Estrazioni");
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
 				e.printStackTrace();
 				return false;
 			}

 	 	return true;
	} // End LoadInvetaries
	

	public boolean existsEstrazione6(String combinazione6)
	{
		int pos = Arrays.binarySearch(sortedEstrazioniArray, combinazione6, comp6);
		if (pos < 0)
			return false;
		else
			return true;
	}
	public boolean existsEstrazione5_1(String combinazione5_1)
	{
		int pos = Arrays.binarySearch(sortedEstrazioniArray, combinazione5_1, comp5_1);
		if (pos < 0)
			return false;
		else
			return true;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	
	if (args.length < 1) {
		System.out
				.println("Parametri mancanti. Uso: s8 nomeFileEstrazioni (eg. estrazioni.txt)");
		System.exit(1);
	}
	String inputFile = args[0];
	System.out.println("inp: " + inputFile);

	S8 s8 = new S8();
	s8.caricaEstrazioni(inputFile);

	String combinazione6 = "012030405060";
	boolean trovata = s8.existsEstrazione6(combinazione6);
	
	if (trovata == true)
		System.out.println("TROVATA Combinazione 6" + combinazione6);
	else
		System.out.println("Combinazione 6 " + combinazione6 + " NON TROVATA");

	trovata = s8.existsEstrazione5_1(combinazione);
	
	if (trovata == true)
		System.out.println("TROVATA Combinazione 5+1 " + combinazione);
	else
		System.out.println("Combinazione 5+1 " + combinazione + " NON TROVATA");

	} // End main
} // End S8
