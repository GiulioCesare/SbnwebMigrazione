import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Arrays;
import it.finsiel.misc.*;


	
public class CategorieDiFruizione {
	String [] sortedCategorieDiFruizioneArray;	
	HashMap mapCategories = null;
	public static final char NON_COLLOCATO = '1';
	public static final char COLLOCATO = '2';
	CategorieDiFruizioneComparator comp;
	static char charSepArrayPipe[] = { '|'};
	
	
	 public class CategorieDiFruizioneComparator implements Comparator{
	 
	 public int compare(Object o1, Object o2) {
		 if (o1 == null)
		 {
	 		 	System.out.println("compare con o1 a NULL" );
		 		return -2;
		 }
		 else if (o2 == null)
		 {
	 		 	System.out.println("compare con o1 a NULL" );
	 		return -2;
		 }
			  
		 
		for (int i=0; i < 15; i++)
		{
			if (((String)o1).charAt(i) < ((String)o2).charAt(i))
				return -1;	 	
			if (((String)o1).charAt(i) > ((String)o2).charAt(i))
				return 1;	 	
	 	}
	 	return 0;
	 	}
	 }	
	
	CategorieDiFruizione()
	{
	comp = new CategorieDiFruizioneComparator();
		
	};

	boolean LoadInvetaries(String invetaryFilename, char filesystemSeparator)
	{
		String invetaryFilenameOutSrt = "";
/*
		// Ordina gli inventaris in primis  
		// --------------------- 
		String invetaryFilenameOutSrt = invetaryFilename.substring(0, invetaryFilename.lastIndexOf("."))+ ".srt";
		try {
	 		String sortInventary = invetaryFilenameOutSrt + " " + invetaryFilename;

		      Process p; 
		      p = Runtime.getRuntime().exec("unixsort -o " + sortInventary);
		      //		      p = Runtime.getRuntime().exec("sort -o " + sortInventary);
		      p.waitFor();
		      System.out.println(p.exitValue());
		      p.destroy() ;
		    }
		    catch (Exception err) {
		      err.printStackTrace();
		      return false;
		    }
*/
		invetaryFilenameOutSrt = invetaryFilename; // quando non sortiamo 
		// Carica BIDs in secundis
		// ----------------------- 
		
	    // Dalla dimensione file ricava il numero di bids
		long inventariesToLoad = 0;
		
		BufferedReader sortedBufferReaderIn;
		File f = null;

		try {
 			sortedBufferReaderIn = new BufferedReader(new FileReader(invetaryFilenameOutSrt));
 		 	System.out.println("Reading " + invetaryFilenameOutSrt );
 
			f = new File(invetaryFilenameOutSrt);
// solo per test treccani
//if (filesystemSeparator == '\\')
//	inventariesToLoad = f.length()/(64+2); // CR/LF
//else
			inventariesToLoad = f.length()/(64+1); // LF 
	
 	 		sortedCategorieDiFruizioneArray = new String[(int)inventariesToLoad];
// 	 		SortedInventoryCategory = new String[(int)inventariesToLoad];
 		 
 			int ctr=0;
 			while (true) {
 				String s;
 	
				if ((ctr & 0x1FF) == 0x1FF)
					System.out.print("Inventari letti: " + ctr + "\r");

 				try {
 					s = sortedBufferReaderIn.readLine();
 					if (s == null)
 						break;
// 					sortedInventariesArray[ctr] = new String (s.substring(40, 43) + s.substring(44, 47) + s.substring(48, 57));
// 					SortedInventoryCategory[ctr++] = new String (s.substring(58,62));
 					
 					sortedCategorieDiFruizioneArray[ctr++] = new String (s.substring(40, 43) + // Cod bib 
 																s.substring(44, 47) + // Serie inventariale
 																s.substring(48, 57) + // Num inventario
 																s.substring(57, 62)  // Codice di categoria + Codice di siponibilita'
 																);
 					
 					//SortedInventoryCategory[ctr++] = new String (s.substring(58,62));
 					
 					
 					}
 				 catch (IOException e) {
 					e.printStackTrace();
 					}
 				}
 			
 			
 			System.out.println("Read " + ctr + " inventaries");
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
	
	
	
//	public String getInventoryCategory(String inventary)
//	{
////		int pos = Misc.binarySearch(sortedInventariesArray, inventary);
//		int pos = Arrays.binarySearch(sortedCategorieDiFruizioneArray, inventary, comp);
//
//		if (pos < 0)
//			return null;
//		else
//			{
//			// Cerchiamo la categoria di fruizione
////			return SortedInventoryCategory[pos].substring(0, 2);
//			return sortedCategorieDiFruizioneArray[pos].substring(16, 18);
//			}
//	}

	public String getAvailability(String inventary)
	{
//		int pos = Misc.binarySearch(sortedInventariesArray, inventary);
		int pos = Arrays.binarySearch(sortedCategorieDiFruizioneArray, inventary, comp);
		if (pos < 0)
			return null;
		else
			{
			// Cerchiamo la categoria di fruizione
//			return SortedInventoryAvailability[pos];
			
//			return SortedInventoryCategory[pos].substring(3,4);
			return sortedCategorieDiFruizioneArray[pos].substring(19,20);
			}
	}

	public String getInventoryCategoryAndAvailability(String inventary)
	{
		int pos = Arrays.binarySearch(sortedCategorieDiFruizioneArray, inventary, comp);

		//int pos = Misc.binarySearch(sortedInventariesArray, inventary);
		if (pos < 0)
			return null;
		else
			{
			// Cerchiamo la categoria di fruizione
			//return SortedInventoryCategory[pos];
			return sortedCategorieDiFruizioneArray[pos].substring(16,20);
			}
	}
	
	
	
	public String getCategoryDescription(String category)
	{
//		int pos = Misc.binarySearch(sortedInventariesArray, inventary);
		int pos = Arrays.binarySearch(sortedCategorieDiFruizioneArray, category, comp);
		if (pos < 0)
			return null;
		else
			{
			// Cerchiamo la categoria di fruizione
			
//			String category = (String)mapCategories.get(SortedInventoryCategory[pos]);
			String description = (String)mapCategories.get(sortedCategorieDiFruizioneArray[pos].substring(16,18));
			if (description == null)
				return "";
			return description;

			                        
			}
	}



	boolean LoadFruitionCategories(String fruitionCategoryFilename)
	{
	    // Dalla dimensione file ricava il numero di bids
		long bidsToLoad = 0;
		mapCategories = new HashMap();
		
		
		//SortedDrop = new String[dropStreamOutRows];
		//String filename = null;
		BufferedReader readerIn;
		 File f = null;

		try {
			readerIn = new BufferedReader(new FileReader(fruitionCategoryFilename));
 		 	System.out.println("Reading " + fruitionCategoryFilename );
	 	 	 
 	 			int ctr=0;
 	 			String[] sa = null;
 	 			while (true) {
 	 				String s;
 	 				try {
 	 					s = readerIn.readLine();
 	 					if (s == null)
 	 						break;
 	 					if (Misc.emptyOrCommentedString(s) == true)
 	 						continue;
 	 					//SortedBidsToDeleteArray[ctr++] = new String (s.substring(0,10));
// 	 					sa = MiscString.estraiCampi(s,"|");
 						sa = MiscString.estraiCampi(s, charSepArrayPipe, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
 	 					
 	 					mapCategories.put(sa[0], sa[1]);
 	 					}
 	 				 catch (IOException e) {
 	 					e.printStackTrace();
 	 					}
 	 				}
 	 			System.out.println("Read " + ctr + " categories");
 	 				try {
 	 				readerIn.close();
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
	} // End LoadBidsToDelete



	public boolean isCategoriaDiFruizionePresent(String inventary)
	{
//		int pos;
//		pos = Misc.binarySearch(sortedInventariesArray, Bid);

		int pos = Arrays.binarySearch(sortedCategorieDiFruizioneArray, inventary, comp);
		
		if (pos < 0)
			return false;
		else
			return true;
	}





} // End Gestione CategorieDiFruizione
