package it.finsiel.migrazione;
/**
 * Gestione dei link al digitale
 * 
 * Tipi di file in input: 	MAG RECORDS
 * 							ELENCO BID ordinati
 * 
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import org.xml.sax.helpers.DefaultHandler;
import it.finsiel.misc.*;



public class LoadSequence { //extends DefaultHandler // SAXParserExample

	   public LoadSequence()
	    {
	    }	
	
	// Elenco delle sequence ORDINATE
	String [] sortedsequenceArray;	
	SequenceComparator comp;
	char filesystemSeparator;
	
	 public class SequenceComparator implements Comparator{

		 public int compare(Object o1, Object o2) {
		// Take the shortest length
		 int len1 = ((String)o1).length()-7;
		 int len2 = ((String)o2).length();
			 
		 int len = len1 >  len2 ? len2 : len1;
			 
		for (int i=7, j=0; j < len; i++, j++)
		{
			if (((String)o1).charAt(i) < ((String)o2).charAt(j))
				return -1;	 	
			if (((String)o1).charAt(i) > ((String)o2).charAt(j))
				return 1;	 	
	 	}
		if (len == len1 && len == len2)
			return 0;
		else if (len == len1 && len < len2)
			return -1;
		else
			return 1;
	 	}
	 }	


	
	private boolean loadFromFile(String sequenceFilenameIn, int rowLength) 
	{
		
	    // Dalla dimensione file ricava il numero di bids
		long sequenceToLoad = 0;
		
		BufferedReader sortedBufferReaderIn;
		File f = null;

		try {
 			sortedBufferReaderIn = new BufferedReader(new FileReader(sequenceFilenameIn));
 		 	System.out.println("Reading " + sequenceFilenameIn );
 
			f = new File(sequenceFilenameIn);

//			if (filesystemSeparator == '\\')
//				sequenceToLoad = f.length()/(rowLength+2); // CR/LF
//			else
				sequenceToLoad = f.length()/(rowLength+1); // LF. C'e' un 0x0A finale che non niesco a non produrre 
	
 	 		sortedsequenceArray = new String[(int)sequenceToLoad];
 		 
 			int ctr=0;
 			while (true) {
 				String s;
 	
				if ((ctr & 0x1FF) == 0x1FF)
					System.out.print("Sequenze lette: " + ctr + "\r");

 				try {
 					s = sortedBufferReaderIn.readLine();
 					if (s == null)
 						break;

 					else if (Misc.emptyString(s))
 						continue;

 					sortedsequenceArray[ctr++] = new String (s.trim());
 					}
 				 catch (IOException e) {
 					e.printStackTrace();
 					}
 				}
 			
 			System.out.println("Lette " + ctr + " sequencze");
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
	} // End loadSequence
	

	public boolean existsSequence(String key)
	{
		int pos = Arrays.binarySearch(sortedsequenceArray, key, comp);
		if (pos < 0)
			return false;
		else
			return true;
	}

	public String getSequence(String key)
	{
		int pos = Arrays.binarySearch(sortedsequenceArray, key, comp);
		if (pos < 0)
			return null;
		else
			return sortedsequenceArray[pos];
	}



	public void run(String aFilename, int rowLength, char aFilesystemSeparator) {
		filesystemSeparator = aFilesystemSeparator;
		System.out.println("Stiamo caricando il file delle sequenze: " + aFilename); 
		comp = new SequenceComparator();
		loadFromFile(aFilename, rowLength);
	} // End run





} // End LoadSequence
