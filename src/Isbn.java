import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Arrays;
import it.finsiel.misc.*;


	
public class Isbn {
	String [] sortedIsbnArray;	
	IsbnComparator comp;
	
	 public class IsbnComparator implements Comparator{
	 
	 public int compare(Object o1, Object o2) {
		for (int i=0; i < 13; i++)
		{
			if (((String)o1).charAt(i) < ((String)o2).charAt(i))
				return -1;	 	
			if (((String)o1).charAt(i) > ((String)o2).charAt(i))
				return 1;	 	
	 	}
	 	return 0;
	 	}
	 }	
	
	 Isbn()
	{
	comp = new IsbnComparator();
		
	};

	boolean LoadIsbn(String invetaryFilename, char filesystemSeparator)
	{
		String invetaryFilenameOutSrt = "";
		invetaryFilenameOutSrt = invetaryFilename; // quando non sortiamo 
		// Carica BIDs in secundis
		// ----------------------- 
		
	    // Dalla dimensione file ricava il numero di bids
		long isbnToLoad = 0;
		
		BufferedReader sortedBufferReaderIn;
		File f = null;

		try {
 			sortedBufferReaderIn = new BufferedReader(new FileReader(invetaryFilenameOutSrt));
 		 	System.out.println("Reading " + invetaryFilenameOutSrt );
 
			f = new File(invetaryFilenameOutSrt);

//			if (filesystemSeparator == '\\')
//				isbnToLoad = f.length()/(13+2); // CR/LF
//			else
				isbnToLoad = f.length()/(13+1); // LF 
	
 	 		sortedIsbnArray = new String[(int)isbnToLoad];
 		 
 			int ctr=0;
// 			int epmtyCtr=0;
 			while (true) {
 				String s;
 	
				if ((ctr & 0x1FF) == 0x1FF)
					System.out.print("Isbn letti: " + ctr + "\r");

 				try {
 					s = sortedBufferReaderIn.readLine();
 					if (s == null)
 						break;

 					else if (Misc.emptyString(s))
 					{
// 						epmtyCtr++; 					
 						continue;
 					}

 	 					sortedIsbnArray[ctr++] = new String (s);
 					}
 				 catch (IOException e) {
 					e.printStackTrace();
 					}
 				}
 			
 			System.out.println("Letti " + ctr + " Isbn");
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
	
	public boolean existsIsbn(String isbn)
	{
/* 15/10/08 Sempre vero per la demo di 30 gg 		
		int pos = Arrays.binarySearch(sortedIsbnArray, isbn, comp);
		if (pos < 0)
			return false;
		else
			return true;
*/		
			return true;
	}

} // End Isbn
