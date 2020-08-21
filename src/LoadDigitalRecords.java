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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.Collections;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import it.finsiel.misc.*;



public class LoadDigitalRecords extends DefaultHandler{ // SAXParserExample
	String tipoElencoBid = "";

	// ELENCO MAG
	private String magContent;
	private String filename;
	private HashMap magMap = null; 		
	private MagRecord mr=null; 
	private MagShortRecord msr;
	private String lastBid = "";
	private String lastBidLibrary = "";
	MagShortRecord currentMagShortRecord = null;
	MagShortRecord previousMagShortRecord = null;
	
	// ELENCO BID
	String [] sortedBidArray;	
	BidComparator comp;
	char filesystemSeparator;
	
	 public class BidComparator implements Comparator{

		 public int compare(Object o1, Object o2) {
//		for (int i=0; i < 10; i++)
//		{
//			if (((String)o1).charAt(i) < ((String)o2).charAt(i))
//				return -1;	 	
//			if (((String)o1).charAt(i) > ((String)o2).charAt(i))
//				return 1;	 	
//	 	}
//		 return 0;
		return (((String)o1).compareTo((String)o2));	 
			 
	 	}
	 }	

	
	
	
	public LoadDigitalRecords(){
	}

	private void parseDocument() {
		
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
		
			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();
			boolean isValidating = sp.isValidating();
			//parse the file and also register this class for call backs
			sp.parse(filename, this);
			
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	

	//Event Handlers
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		//reset
		//tempMag = "";
		if(qName.equalsIgnoreCase("record")) {
			if (mr == null)
				mr = new MagRecord(); 
			
			//create a new instance of employee
			currentMagShortRecord = new MagShortRecord();
		}
		else if(qName.equalsIgnoreCase("mag:bib")) {
			String level = attributes.getValue("level");
			currentMagShortRecord.setBibLevel(level.charAt(0));
		}
		else if(qName.equalsIgnoreCase("dc:identifier")) {
			//create a new instance of employee
			String type = attributes.getValue("xsi:type");
			if (type == null)
				type = "undefined";
			currentMagShortRecord.setDcIdentifierType(type);
		}
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		magContent = new String(ch,start,length);
/*		if (magContent.equals("029093"))
			System.out.println("ERROR " + magContent);
*/		
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(qName.equalsIgnoreCase("record")) {
			//add it to the list
			previousMagShortRecord = currentMagShortRecord;	
			mr.addMagShortRecord(previousMagShortRecord);
			currentMagShortRecord = null;
			
		}
		else if(qName.equalsIgnoreCase("dc:identifier")) {
			if (currentMagShortRecord.getDcIdentifierType().equals("SBN"))
			{
				// Prepare the BID
				currentMagShortRecord.setBid(magContent); // magContent.substring(37, 40) + magContent.substring(41, 48)
				String	bidBib = currentMagShortRecord.getBid();
				if (!lastBid.equals(bidBib))
				{
					if (!lastBid.equals("")) // if not first record
					{
						// Store last record
						mr.setBidBib(lastBidLibrary);
						magMap.put(lastBidLibrary, mr);
						mr=new MagRecord(); // Get ready for next one
					}
					lastBid = bidBib; // We are changing Bid (thus changing record)
					lastBidLibrary = lastBid;
				}
			}
			
//			else
//				currentMagShortRecord.setDcIdentifierType("");
				
		}
		else if(qName.equalsIgnoreCase("identifier")) {
			//create a new instance of employee
			currentMagShortRecord.setIdentifier(magContent);

			int start = magContent.indexOf(':')+1;
			String identifierSite = magContent.substring(start);
			int end = identifierSite.indexOf(':');
			if (end <0 )
				System.out.println("ERRORE di PARSING" + magContent);
			else
			{
				String is = identifierSite.substring(0, end);
				currentMagShortRecord.setIdentifierSite(is);
			}
			
		}
	else if(qName.equalsIgnoreCase("library")) {
		//create a new instance of employee
		currentMagShortRecord.setLibrary(magContent.trim());
		lastBidLibrary = lastBid+currentMagShortRecord.getLibrary();
	}
	else if(qName.equalsIgnoreCase("year")) {
		//create a new instance of employee
		currentMagShortRecord.setYear(magContent);
	}
	else if(qName.equalsIgnoreCase("issue")) {
		//create a new instance of employee
		currentMagShortRecord.setIssue(magContent);
	}

	}

	/**
	 * Iterate through the list and print
	 * the contents
	 */
	private void printData(){
		int ctr = 0, blockCtr=0;
		Iterator it;
		ArrayList <MagShortRecord> magShortRecordList;
		it = magMap.values().iterator();
		while(it.hasNext()) {
			ctr++;
			mr  = (MagRecord) it.next();
			magShortRecordList = (ArrayList <MagShortRecord>) mr.getMagShortRecordList();
			
			Iterator itr = magShortRecordList.iterator();
			blockCtr = 0;
			while(itr.hasNext()) {
				blockCtr++;
				msr = (MagShortRecord) itr.next();
				System.out.println("\nRecord: " + ctr + " Block " + blockCtr + "\n" + msr.toString());
			}
		}
	}


	public void setMagMap(HashMap magMap) {
		this.magMap = magMap;
	}


	
	
	
	
	
	
	public HashMap getMagMap() {
		if (tipoElencoBid.equals("MAG"))
			return magMap;
		else
			return null;
	}


	private boolean LoadBids(String bidFilenameIn) // , char filesystemSeparator
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
 			sortedBufferReaderIn = new BufferedReader(new FileReader(bidFilenameIn));
 		 	System.out.println("Reading " + bidFilenameIn );
 
			f = new File(bidFilenameIn);

//			if (filesystemSeparator == '\\')
//				bidToLoad = f.length()/(10+2); // CR/LF
//			else

			bidToLoad = f.length()/(10+1); // LF 
			if (f.length()%(10+1) > 0 )
				bidToLoad++;
				
 	 		sortedBidArray = new String[(int)bidToLoad];
 		 
 			int ctr=0;
 			while (true) {
 				String s;
 	
				if ((ctr & 0x1FF) == 0x1FF)
					System.out.print("Bid letti: " + ctr + "\r");

 				try {
 					s = sortedBufferReaderIn.readLine();
 					if (s == null)
 						break;

 					else if (Misc.emptyString(s))
 						continue;

 					sortedBidArray[ctr++] = new String (s);
 					}
 				 catch (IOException e) {
 					e.printStackTrace();
 					}
 				}
 			
 			System.out.println("Letti " + ctr + " Bid");
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
	

	public boolean existsBid(String bid)
	{
		int pos = Arrays.binarySearch(sortedBidArray, bid, comp);
		if (pos < 0)
			return false;
		else
			return true;
	}




	public void run(String aFilename, String aTipoElencoBid, char aFilesystemSeparator) {
		filename = aFilename;
		filesystemSeparator = aFilesystemSeparator;
		tipoElencoBid = aTipoElencoBid;

		System.out.println("Stiamo caricando il file delle copie digitali: " + aFilename); 
		// Stiamo trattando i MAG? 
		if (tipoElencoBid.equals("MAG"))
		{
			magMap = new HashMap();
			parseDocument();
			if (mr != null) // last one?
			{
//				mr.addMagShortRecord(currentMagShortRecord);
				mr.setBidBib(lastBidLibrary);
				magMap.put(lastBidLibrary, mr);
				//printData();
			}
		}
		// Stiamo trattando un elenco ordinato di BID? 
		else if (tipoElencoBid.equals("PLAIN"))
		{
		// Gestione dei link al digitale tramite elenco di bid
			comp = new BidComparator();
			LoadBids(aFilename);
		}
		else 
			System.out.println("Tipo elenco BID sconosciuto: " + tipoElencoBid); 
		
	} // End run





} // End LoadDigitalRecords
