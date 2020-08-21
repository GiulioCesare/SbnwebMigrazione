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

import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.marc.ControlField;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Leader;
import org.marc4j.marc.MarcFactory;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import it.finsiel.misc.*;


//import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;
	
public class Gestione462_463 {
	char filesystemSeparator;
	String sortExecutable;
	int recordCounter = 0;
	int rejectedCounter = 0;
	String Bid;
	InputStream marcStreamIn = null;
	String _462FilenameOut, _463FilenameOut, _950FilenameOut, _463No950FilenameOut, dropFilenameOut;
	FileOutputStream 	//_462StreamOut, _463StreamOut, _950StreamOut, _463No950StreamOut, 
						dropStreamOut;
	Record record = null;
//	byte[] CrLf = new byte[2]; 
	byte[] Lf = new byte[1]; // 21/09/2009 14.33
	
	boolean _950DiPolo=false;
	boolean _463DiPolo=false;
	boolean _462DiPolo=false;
	
	int _463Ctr = 0;
	int _462Ctr = 0;
	String codBib = null;
	Biblioteche950 biblioteche950;
    //ArrayList list463 = new ArrayList();
 
	int _462StreamOutRows = 0, 
		_463StreamOutRows = 0, 
		_950StreamOutRows = 0, 
		_463No950StreamOutRows = 0,
		dropStreamOutRows = 0;
	
	int _462StreamInRows = 0, 
	_463StreamInRows = 0, 
	_950StreamInRows = 0, 
	_463No950StreamInRows = 0,
	dropStreamInRows = 0;

	
//	String [] Sorted462;	
//	String [] Sorted463;	
//	String [] Sorted950;	
//	String [] Sorted463No950;	
	String [] SortedDrop;	
	
	Gestione462_463(Biblioteche950 biblioteche950, char filesystemSeparator, String sortExecutable)
	{
		this.filesystemSeparator = filesystemSeparator;
		this.biblioteche950 = biblioteche950;
		this.sortExecutable = sortExecutable;
		
//		CrLf[0] = 0x0d;
//		CrLf[1] = 0x0a;
		
		Lf[0] = 0x0a; // 21/09/2009 14.33
		
	};
	

		public void manage462_463()  { //throws Exception 
		MarcFactory factory = MarcFactory.newInstance();
		List fields;
		Iterator iter;
		Leader leader = record.getLeader();
		boolean in462 = false;
		boolean in463 = false;
		
		char []implDef1 = leader.getImplDefined1();
		String bidLinkato = null;

		if (implDef1[0] != 'm')
			return;
		
		_950DiPolo=false;
		_463DiPolo=false;
		_462DiPolo=false;
		
//		list463.clear();

		// Control fields: tags 001 through 009
		fields = record.getControlFields();
		iter = fields.iterator();
		while (iter.hasNext()) {
			ControlField field = (ControlField) iter.next();
			if (field.getTag().compareTo("001") == 0)
				Bid = field.getData();
				break;
		}

		// Data fields tag 010 through 999
		fields = record.getDataFields();
		iter = fields.iterator();
//System.out.println("..  ");
		while (iter.hasNext()) {
			DataField field = (DataField) iter.next();
			in462 = false;
			in463 = false;
			
			String tag = field.getTag();
//System.out.println(" tag = " + field.getTag());


			if ((tag.compareTo("462") == 0) || (tag.compareTo("463") == 0)
			) { // campi linkati
				if (tag.compareTo("462") == 0)
				{
					_462DiPolo=true;
					_462Ctr++;
					in462 = true;
				}
				else if (tag.compareTo("463") == 0)
				{
				_463DiPolo=true;
				_463Ctr++;
				in463 = true;
				}
/*
				List subfields = field.getSubfields();
				Iterator j = subfields.iterator();
				while (j.hasNext()) {
					Subfield sf = (Subfield) j.next();
//System.out.println(sf.toString());
					String data = sf.getData();
					if (sf.getCode() == '1' && data.startsWith("001")) {
						bidLinkato = data.substring(3, 3+10);
						// Scrivi Bid linkato sulla 462 o 463
						if (in462 == true) 
							{
							try {
								_462StreamOut.write(bidLinkato.getBytes());
								_462StreamOut.write(CrLf);
								_462StreamOutRows++;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							}
						else
							{
							try {
								_463StreamOut.write(bidLinkato.getBytes());
								list463.add(new String (bidLinkato));
								_463StreamOut.write(CrLf);
								_463StreamOutRows++;
								
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							}
						break;
					}


				} // end while
*/				
				
			} // end 462, 463

			else if (tag.startsWith("950") == true)
			{
				List subfields = field.getSubfields();
				Iterator s = subfields.iterator();
				while (s.hasNext()) {
					Subfield subfield = (Subfield) s.next();
					if ((subfield.getCode() == 'd' || subfield.getCode() == 'e')) {
						// Get bib code from bib description
						codBib = (subfield.getData().substring(0, 3));
						if (biblioteche950.containsBiblio(codBib) == false)
						{
							break; 
						}
						else
						{
							_950DiPolo = true;
//							try {
//								_950StreamOut.write(Bid.getBytes()); // Scrivi il bid del record che contiene la 950 posseduta
//								_950StreamOut.write(CrLf);
//								_950StreamOutRows++;
//							} catch (IOException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
							break;
						}
					}
				} // End while subfields
			}// end 950

			
			if (_950DiPolo == true)
				{
					break; // finished
				}
		} // End while fields
			
		
//		if (_950DiPolo == false && _463DiPolo == true)
//			{
//			// Se record senza 950 ma con 463 segnala
//				try {
//					_463No950StreamOut.write(Bid.getBytes()); // Scrivi il bid del record che contiene la 950 posseduta
//
//					for (int i=0; i < list463.size(); i++)
//						{
//						_463No950StreamOut.write(" ".getBytes());
//						_463No950StreamOut.write(((String)list463.get(i)).getBytes());
//						}
//					_463No950StreamOut.write(CrLf);
//					_463No950StreamOutRows++;
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}

		if (_950DiPolo == false && _463DiPolo == false && _462DiPolo == false)
		{ // Se non ha posseduto e non ha ne 462 e ne 463 segnala
			try {
				dropStreamOut.write(Bid.getBytes());
//				dropStreamOut.write(CrLf);
				dropStreamOut.write(Lf); // 21/09/2009 14.33
				
				dropStreamOutRows++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}


			
			
			return ;
	} // End 

		public int get_462StreamOutRows() {
			return _462StreamOutRows;
		}

		public void set_462StreamOutRows(int streamOutRows) {
			_462StreamOutRows = streamOutRows;
		}

		public int get_463No950StreamOutRows() {
			return _463No950StreamOutRows;
		}

		public void set_463No950StreamOutRows(int no950StreamOutRows) {
			_463No950StreamOutRows = no950StreamOutRows;
		}

		public int get_463StreamOutRows() {
			return _463StreamOutRows;
		}

		public void set_463StreamOutRows(int streamOutRows) {
			_463StreamOutRows = streamOutRows;
		}

		public int get_950StreamOutRows() {
			return _950StreamOutRows;
		}

		public void set_950StreamOutRows(int streamOutRows) {
			_950StreamOutRows = streamOutRows;
		}


	public void run(String marcFileIn, String dropFilenameOut, boolean hasLinefeed) {
		this.dropFilenameOut = dropFilenameOut;
		String newDropFilenameOut = "";

		try {
			marcStreamIn = new FileInputStream(marcFileIn); 
			dropStreamOut = new FileOutputStream(dropFilenameOut);
			
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}


			MarcReader reader = new MarcStreamReader(marcStreamIn);
			while (reader.hasNext()) {
	
				try {
					recordCounter++;
					Bid = "??";
					record = reader.next();
//					record = reader.nextLfInDati_Arge();
					
					if ((recordCounter & 0x1FF) == 0x1FF)
//						System.out.println("Scarta non posseduto - Record letti: " + recordCounter);
						System.out.print("Scarta non posseduto - Record letti: " + recordCounter + "\r");
					
					manage462_463();
				
				}
				// catch (MarcException e) { // Exception e
				catch (Exception e) {
					// TODO Auto-generated catch block
					rejectedCounter++;
					System.out.println("Record ERRATO: " + recordCounter + " BID=" + Bid + "\n");
					e.printStackTrace();
					// Read to end of line and continue
					if (hasLinefeed) {
						try {
							int readByte;
							while (true) {
								readByte = marcStreamIn.read();
// 21/09/2009 17.29
								if (readByte == 0x000A) // LF
								{
									break;
								}
								else if (readByte == 0x000D) // CR
								{
									readByte = marcStreamIn.read(); // Get LF
									break;
								}

							} // End while not EOL
							continue;
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}

				// Skip CR/LF
				try {
					if (hasLinefeed) {
//						int bytesRead = marcStreamIn.read(CrLf);
						int bytesRead = marcStreamIn.read(Lf); // 21/09/2009 14.33
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					recordCounter++;
					// if ((recordCounter & 0xFF) == 0xFF)
					System.out.println("NO CR/LF: " + recordCounter);
					e.printStackTrace();

				}
			}


		try {
			if (marcStreamIn != null) marcStreamIn.close();
			if (dropStreamOut != null) dropStreamOut.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Fai sort dei file
		   try {
			   String dropFilenameOutSrt;
			   
				if (filesystemSeparator == '\\')
				{
					newDropFilenameOut= MiscString.replace(dropFilenameOut, "/", "\\"); 
		 	 		dropFilenameOutSrt = newDropFilenameOut.substring(0, newDropFilenameOut.indexOf("."))+ ".srt";
				}
				else
				{
					newDropFilenameOut= MiscString.replace(dropFilenameOut, "\\", "/"); 
		 	 		dropFilenameOutSrt = newDropFilenameOut.substring(0, newDropFilenameOut.indexOf("."))+ ".srt";
				}
			   
				String sortDropped = dropFilenameOutSrt + " " + newDropFilenameOut;

				String execString = sortExecutable + " -o " + sortDropped; 
System.out.println("Start executing: " + execString);		      
			    Process p; 
			    p = Runtime.getRuntime().exec(execString);  // Problemi con ambiente
			    p.waitFor();
			    System.out.println(p.exitValue());
			    p.destroy() ;
System.out.println("Stop executing: " + execString);		      
	      
			    }
			    catch (Exception err) {
			      err.printStackTrace();
			    }
		    
			    // Lets load the linked 462, 463
			    loadSortedFiles();
	}	// End pass1_462_463


private void loadSortedFiles()
{

	
		File f = new File(dropFilenameOut);
		long droppedBidsToLoad;
	
//	if (this.filesystemSeparator == '\\') 
//		droppedBidsToLoad = f.length()/(10+1+1); // CR+LF Per sviluppo su windows 
//	else
		droppedBidsToLoad = f.length()/(10+1); // LF unix
	
	
	
	
//	SortedDrop = new String[dropStreamOutRows];
	SortedDrop = new String[(int)droppedBidsToLoad];
	String filename = null;
	BufferedReader sortedBufferReaderIn;
 	 	 		filename = dropFilenameOut.substring(0, dropFilenameOut.indexOf("."))+ ".srt";
 	 	 		try {
 	 	 			sortedBufferReaderIn = new BufferedReader(new FileReader(filename));
 	 	 		 	System.out.println("Reading " + filename );
 	 	 			int ctr=0;
 	 	 			while (true) {
 	 	 				String s;
 	 	 				try {
 	 	 					s = sortedBufferReaderIn.readLine();
 	 	 					if (s == null)
 	 	 						break;
 	 	 					SortedDrop[ctr++] = new String (s);
 	 	 					}
 	 	 				 catch (IOException e) {
 	 	 					e.printStackTrace();
 	 	 					}
 	 	 				}
 	 	 			System.out.println("Read " + ctr + " lines");
 	 	 				try {
 	 	 				sortedBufferReaderIn.close();
 	 	 			} catch (IOException e) {
 	 	 				// TODO Auto-generated catch block
 	 	 				e.printStackTrace();
 	 	 			}
 	 	 			}
 	 	 	 		catch (FileNotFoundException e) {
 	 	 				// TODO Auto-generated catch block
 	 	 				e.printStackTrace();

 	 	 				// Convert from binary format to readable format
 	 	 			}


} // End loadSortedFiles



/**
* Reallocates an array with a new size, and copies the contents
* of the old array to the new array.
* @param oldArray  the old array, to be reallocated.
* @param newSize   the new array size.
* @return          A new array with the same contents.
*/
private static Object resizeArray (Object oldArray, int newSize) {
   int oldSize = java.lang.reflect.Array.getLength(oldArray);
   Class elementType = oldArray.getClass().getComponentType();
   Object newArray = java.lang.reflect.Array.newInstance(
         elementType,newSize);
   int preserveLength = Math.min(oldSize,newSize);
   if (preserveLength > 0)
      System.arraycopy (oldArray,0,newArray,0,preserveLength);
   return newArray; }





public boolean isBidDropped(String Bid)
{
	int pos = Misc.binarySearch(SortedDrop, Bid);
	if (pos < 0)
		return false;
	else
		return true;
}



} // End Gestione462_463
