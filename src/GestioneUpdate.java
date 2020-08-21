import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import it.finsiel.misc.*;


//import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;
	
public class GestioneUpdate {
	String [] SortedBidsArray;	
	String [] SortedBidsToDeleteArray;	
	char filesystemSeparator;
	String sortExecutable = "";
	GestioneUpdate(char filesystemSeparator, String sortExecutable)
	{
		this.filesystemSeparator = filesystemSeparator;
		this.sortExecutable = sortExecutable;
	};

	boolean LoadBids(String BidFilename)
	{
//	boolean ret = true;
		String newBidFilename;
		String bidFilenameOutSrt;
		if (filesystemSeparator == '\\')
		{
			newBidFilename= MiscString.replace(BidFilename, "/", "\\"); 
			bidFilenameOutSrt = newBidFilename.substring(0, newBidFilename.lastIndexOf("\\"))+ "\\bid_out.srt";
		}
		else
		{
			newBidFilename= MiscString.replace(BidFilename, "\\", "/"); 
			bidFilenameOutSrt = newBidFilename.substring(0, newBidFilename.lastIndexOf("/"))+ "/bid_out.srt";
		}
		
//		MiscString.replace(bidFilenameOutSrt, "/", "\\"); 
		
		// Ordina BIDs in primis 
		// --------------------- 
		try {
	 		String sortBid = bidFilenameOutSrt + " " + newBidFilename;

	 		String execString = sortExecutable + " -o " + sortBid;
	 		
		      Process p; 
System.out.println("Start executing: " + execString);		      
		      p = Runtime.getRuntime().exec(execString);
		      p.waitFor();
		      System.out.println(p.exitValue());
		      p.destroy() ;
System.out.println("Stop executing: " + execString);		      
		    }
		    catch (Exception err) {
		      err.printStackTrace();
		      return false;
		    }

		// Carica BIDs in secundis
		// ----------------------- 
		
	    // Dalla dimensione file ricava il numero di bids
		long bidsToLoad = 0;

		
		
		//SortedDrop = new String[dropStreamOutRows];
		//String filename = null;
		BufferedReader sortedBufferReaderIn;
		 File f = null;

		try {
	 	 	 			sortedBufferReaderIn = new BufferedReader(new FileReader(bidFilenameOutSrt));
	 	 	 		 	System.out.println("Reading " + bidFilenameOutSrt );
	 	 	 
						f = new File(bidFilenameOutSrt);
		 	 	 		bidsToLoad = f.length()/11; 
		 	 	 		SortedBidsArray = new String[(int)bidsToLoad];
		 	 	 		
	 	 	 		 
	 	 	 			int ctr=0;
	 	 	 			while (true) {
	 	 	 				String s;
	 	 	 				try {
	 	 	 					s = sortedBufferReaderIn.readLine();
	 	 	 					if (s == null)
	 	 	 						break;
	 	 	 					SortedBidsArray[ctr++] = new String (s);
	 	 	 					}
	 	 	 				 catch (IOException e) {
	 	 	 					e.printStackTrace();
	 	 	 					}
	 	 	 				}
	 	 	 			System.out.println("Read " + ctr + " bids");
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
	} // End load bids
	
	
	public boolean isBidPresent(String Bid)
	{
		int pos = Misc.binarySearch(SortedBidsArray, Bid);
		if (pos < 0)
			return false;
		else
			return true;
	}
	




	boolean LoadBidsToDelete(String BidsToDeleteFilename)
	{
//	boolean ret = true;
		String newBidsToDeleteFilename;
		String bidFilenameOutSrt;
		if (filesystemSeparator == '\\')
		{
			newBidsToDeleteFilename= MiscString.replace(BidsToDeleteFilename, "/", "\\"); 
			bidFilenameOutSrt = newBidsToDeleteFilename.substring(0, newBidsToDeleteFilename.lastIndexOf("\\"))+ "\\bid_del.srt";
		}
		else
		{
			newBidsToDeleteFilename= MiscString.replace(BidsToDeleteFilename, "\\", "/"); 
			bidFilenameOutSrt = newBidsToDeleteFilename.substring(0, newBidsToDeleteFilename.lastIndexOf("/"))+ "/bid_del.srt";
		}

		// Ordina BIDs in primis 
		// --------------------- 
		try {
	 		String sortBid = bidFilenameOutSrt + " " + newBidsToDeleteFilename;
	 		String execString = sortExecutable + " -o " + sortBid;
	 		
System.out.println("Start executing: " + execString);		      
		      Process p; 
		      p = Runtime.getRuntime().exec(execString);
		      p.waitFor();
		      System.out.println(p.exitValue());
		      p.destroy() ;
System.out.println("Stop executing: " + execString);		      
		    }
		    catch (Exception err) {
		      err.printStackTrace();
		      return false;
		    }

		// Carica BIDs in secundis
		// ----------------------- 
		
	    // Dalla dimensione file ricava il numero di bids
		long bidsToLoad = 0;

		
		
		//SortedDrop = new String[dropStreamOutRows];
		//String filename = null;
		BufferedReader sortedBufferReaderIn;
		 File f = null;

		try {
	 	 	 			sortedBufferReaderIn = new BufferedReader(new FileReader(bidFilenameOutSrt));
	 	 	 		 	System.out.println("Reading " + bidFilenameOutSrt );
	 	 	 
						f = new File(bidFilenameOutSrt);
		 	 	 		bidsToLoad = f.length()/12; 
		 	 	 		SortedBidsToDeleteArray = new String[(int)bidsToLoad];
		 	 	 		
	 	 	 		 
	 	 	 			int ctr=0;
	 	 	 			while (true) {
	 	 	 				String s;
	 	 	 				try {
	 	 	 					s = sortedBufferReaderIn.readLine();
	 	 	 					if (s == null)
	 	 	 						break;
	 	 	 					SortedBidsToDeleteArray[ctr++] = new String (s.substring(0,10));
	 	 	 					}
	 	 	 				 catch (IOException e) {
	 	 	 					e.printStackTrace();
	 	 	 					}
	 	 	 				}
	 	 	 			System.out.println("Read " + ctr + " bids");
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
	} // End LoadBidsToDelete



	public boolean isBidToDelete(String Bid)
	{
		int pos = Misc.binarySearch(SortedBidsToDeleteArray, Bid);
		if (pos < 0)
			return false;
		else
			return true;
	}





} // End Gestione GestioneUpdate
