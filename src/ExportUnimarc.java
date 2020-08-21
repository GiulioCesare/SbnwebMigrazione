import it.finsiel.sbn.polo.model.unimarcmodel.SBNMarc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.marc4j.MarcStreamWriter;
import org.marc4j.MarcWriter;
import org.marc4j.marc.ControlField;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Leader;
import org.marc4j.marc.MarcFactory;
import org.marc4j.marc.Record;
import it.finsiel.misc.*;

//import com.sun.org.apache.xerces.internal.parsers.DOMParser;

/*
 Converti i caratteri speciali da set esteso a unicode
 01 = ISO 646, IRV version (basic Latin set)
 02 = ISO Registration # 37 (basic Cyrillic set)
 03 = ISO 5426 (extended Latin set)
 04 = ISO DIS 5427 (extended Cyrillic set)
 05 = ISO 5428 (Greek set)
 06 = ISO 6438 (African coded character set)
 07 = ISO 10586 (Georgian set)
 08 = ISO 8957 (Hebrew set) Table 1
 09 = ISO 8957 (Hebrew set) Table 2
 10 = [Reserved]
 11 = ISO 5426-2 (Latin characters used in minor European languages and obsolete typography)
 50 = ISO 10646 Level 3 (Unicode UTF8)
*/

public class ExportUnimarc {
	public static final int MAX_BYTES_PER_UTF8_CHARACTER = 4;
//	public static final boolean LEFT_PADDING = true;
//	public static final boolean RIGHT_PADDING = false;
//	public static final String UNICODE_UTF8_ENCODING = "50";
//	public static final String RIMPIAZZA_MINORE 			= "xxa"; // <
//	public static final String RIMPIAZZA_MAGGIORE 			= "xxb"; // >
//	public static final String RIMPIAZZA_PUNTO_E_VIRGOLA 	= "xxc"; // ;
//	public static final String RIMPIAZZA_APICE 				= "xxd"; // '
//	public static final String RIMPIAZZA_TRATTINO 			= "xxe"; // '
	//java.util.Vector charSetsUsed;
	//Hashtable charTable;
//	Classificazioni classificazioni;

	//String applicationPath;

	String baseDir;
	String Bid;

	static int recordCounter = 0;
	static int rejectedCounter = 0;

//	java.util.Vector vecBfrFiles; // Buffer Reader Files

	FileOutputStream streamOutReadable, streamOutMrk; // declare a file output object

//	protected SBNMarc root_object = null;
//	boolean hasLinefeed = false;
//	boolean isAuthorityAutori = false;
//	boolean isAuthoritySoggetti = false;
//	boolean isAuthorityClassificazioni = false;
//	boolean isConvertToUtf8 = true;
//	boolean hasDigitalServer = false;

//	LoadDigitalRecords digitalRecords = null;
//	Biblioteche biblioteche = null;
	
//	Biblioteche950 biblioteche950 = null;
//	int biblioteche950NonDiPoloCtr = 0;
//	int biblioteche950DiPoloCtr = 0;
//	boolean isBiblioteche950DiPolo = true;
	
//	String codicePolo = null;

//	int linksAlDigitale = 0;

//	String 	digitaleFileIn, 
//			classificazioniFileIn,
//			biblioteche950FileIn,
//			caratteriSpecialiUnimarcFileIn;

	public ExportUnimarc() {
//		vecBfrFiles = new java.util.Vector();
	}

	public boolean emptyString(String textLine) {
		for (int i = 0; i < textLine.length(); i++) {
			if (textLine.charAt(i) != '\n' && textLine.charAt(i) != '\r'
					&& textLine.charAt(i) != '\t' && textLine.charAt(i) != ' ')
				return false;
		}
		return true;
	}


	/*
	 * private String[] fromUnimarc2Unicode(String[] ar) { java.util.Vector
	 * vecCampi = new java.util.Vector(); int i;
	 * 
	 * for (i = 0; i < ar.length; i++) { String s = fromUnimarc2Unicode(ar[i]);
	 * vecCampi.addElement(s); }
	 * 
	 * vecCampi.trimToSize(); String[] arrCampi = new String[vecCampi.size()];
	 * for (i = 0; i < vecCampi.size(); i++) { arrCampi[i] = (String)
	 * vecCampi.elementAt(i); } return arrCampi; } // End fromUnimarc2Unicode
	 */

	/*
	 * // private boolean doTransform(BufferedReader brIn, BufferedWriter brOut)
	 * private boolean doTransform(String fileOutInAr[]) { BufferedReader brIn,
	 * brInGuida; StringBuffer sb = new StringBuffer(); boolean reading = true;
	 * String arInputRecords[] = new String[vecBfrFiles.size()]; String
	 * arInputLine[] = new String[vecBfrFiles.size()]; int rowCtr[] = new
	 * int[vecBfrFiles.size()];
	 * 
	 * for (int i = 0; i < arInputRecords.length; i++) { arInputRecords[i] = new
	 * String(); arInputLine[i] = new String(); }
	 * 
	 * while (reading) { // Read one record from each file (a record can span
	 * over multiple // lines) //
	 * ======================================================================
	 * for (int fileN = 0; fileN < vecBfrFiles.size(); fileN++) { brIn =
	 * (BufferedReader) vecBfrFiles.get(fileN); try { // Leggi riga prima di
	 * entrare nel loop solo a inizio file. // Per le successive le diamo come
	 * lette anticipatamente arInputLine[fileN] = brIn.readLine();
	 * arInputRecords[fileN] = arInputLine[fileN]; rowCtr[fileN]++; if
	 * (arInputLine[fileN] == null) { reading = false; // break; } } // End try
	 * catch (IOException ioEx) { ioEx.printStackTrace(); return false; } // End
	 * catch
	 * 
	 * if (!reading) // Last record? { arInputRecords[fileN] = sb.toString(); //
	 * salva record per // trattamento // successivo } } // End for fileN //
	 * Concatenate the records from each file in one record //
	 * ==================================================== sb.setLength(0); for
	 * (int i = 0; i < vecBfrFiles.size(); i++) { sb.append(arInputRecords[i]); }
	 * if (sb.length() == 0) // break; // No more records to read in
	 * sb.append("\n"); // Remove escaped sequences // ======================== //
	 * String[] ar = //
	 * MiscString.estraiCampiConEscapePerSeparatore(sb.toString(), //
	 * separatoreDiCampoIn, escapeCharacter); String[] ar = new String[1]; ar[0] =
	 * sb.toString(); // Convert esabytes to UTF8 // ======================== //
	 * String[] ar1 = fromEsabyte2Unicode(ar); String[] ar1 =
	 * fromUnimarc2Unicode(ar); String ar2[]; // Create missing fields // ar2 =
	 * createFields(ar1,fileOutInAr[0]); ar2 = ar1; // Recompose record //
	 * ======================== // String s = makeRecord(ar2,fileOutInAr[0]);
	 * String s = ar2[0]; // Write record // ======================== try { byte
	 * arUtf8[] = myGetBytesUtf8(s);
	 * 
	 * streamOut.write(arUtf8, 0, arUtf8.length); streamOut.write('\n'); //
	 * recordCounter++; // righeLette++;
	 * 
	 * String s1 = new String(arUtf8, 0, arUtf8.length, "UTF8"); //
	 * System.out.print(s1 + "\n"); } catch (IOException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); return false; } } // End
	 * while reading
	 * 
	 * return true; } // End doTransform
	 */
//	private String addSpaces(String valore) {
//		String tmpString = "000".concat(valore);
//		return tmpString.substring(valore.length() - 1);
//	}

	private static byte[] trim(byte[] ba, int len) {
		if (len == ba.length)
			return ba;
		byte[] tba = new byte[len];
		System.arraycopy(ba, 0, tba, 0, len);
		return tba;
	}


	public void transform(String fileOutIn[]) {
		String marcFileIn, marcFileOut, readableMarkFileOut;

		marcFileIn = ""; // fileOutIn[0];
		marcFileOut = ""; // fileOutIn[1];
		readableMarkFileOut = null;
		int i;

		if (fileOutIn.length < 2) {
			System.out.println("Parametri errati: ..........");
			for ( i=0; i < fileOutIn.length; i++)
				System.out.println("Parametro " + i + ": " + fileOutIn[i]);
			return;
		}

		for (i=0; i < fileOutIn.length; i++) {
			if (fileOutIn[i].startsWith("-SbnMarcFileIn=")) 
				marcFileIn = baseDir + fileOutIn[i].substring("-SbnMarcFileIn=".length());
			else if (fileOutIn[i].startsWith("-MarkFileOut=")) 
				marcFileOut = baseDir + fileOutIn[i].substring("-MarkFileOut=".length());
			else if (fileOutIn[i].startsWith("-MarkFileOutTxt="))
				readableMarkFileOut = baseDir + fileOutIn[i].substring("-MarkFileOutTxt=".length());
		} // end for

//		vecBfrFiles.clear();

		fromBinaryToTagged(marcFileIn, marcFileOut, readableMarkFileOut);

	} // End transform

	public void fromBinaryToTagged(String xmlFileIn, String marcFileOut, String readableMarkFileOut) {
		InputStream in = null;
//		FileOutputStream streamOutMrk = null;
//		FileOutputStream streamOutReadable = null;

		File file = new File(marcFileOut);

		// System.out.println("exists marcFileOut: " + marcFileOut);
		if (file.exists()) {
			// System.out.println("delete marcFileOut: " + marcFileOut);
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return;
		}
		// Apri file in scrittura
		try {
			System.out.println("markFileOut: " + marcFileOut);
			streamOutMrk = new FileOutputStream(marcFileOut); // "data/stream.out"
			if (readableMarkFileOut != null) {
				System.out.println("readableMarkFileOut: " + readableMarkFileOut);
				streamOutReadable = new FileOutputStream(readableMarkFileOut); // "data/stream.out"
			}
		} catch (Exception fnfEx) {
			fnfEx.printStackTrace();
			return;
		}

		try {
			in = new FileInputStream(xmlFileIn); // .xml
			System.out.println("xmlFileIn: " + xmlFileIn + "\n");
			//String FileXml = null;

			FileReader stringReader = new FileReader(xmlFileIn);
			SBNMarc sbnmarc = null;
			try {
				// Traduce la richiesta di Export in un oggetto SBNMarc
				sbnmarc = SBNMarc.unmarshalSBNMarc(stringReader);
			} catch (org.exolab.castor.xml.MarshalException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (org.exolab.castor.xml.ValidationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// Classe SbnmarcUnimarc per la produzione, a partire dall'oggetto 
			// SBNMarc, del record Unimarc corrispondente (ArrayList object)
			SbnmarcUnimarc sbnmarcUnimarc = new SbnmarcUnimarc(sbnmarc);
			sbnmarcUnimarc.elaboraDocumento(sbnmarc.getSbnMessage().getSbnResponse().getSbnResponseTypeChoice().getSbnOutput().getSbnOutputTypeChoice());

			MarcWriter writerMrk = new MarcStreamWriter(streamOutMrk);
			writerMrk.setConverter(null);

			try {
				recordCounter++;
				// if (recordCounter == 1000)
				// break;
				Bid = "??";
//					record = reader.next();
				if ((recordCounter & 0xFF) == 0xFF) System.out.println("Record letti: " + recordCounter);
				// Trasformazione ArrayList --> Record
				Record newRecord = transformMarcRecord(sbnmarcUnimarc.getFileUnimarc()); // recordSbnmarc
				// Scrive il record
				writeMarcRecord(streamOutReadable, writerMrk, newRecord);
			}
			// catch (MarcException e) { // Exception e
			catch (Exception e) {
				// TODO Auto-generated catch block
				rejectedCounter++;
				System.out.println("Record ERRATO: " + recordCounter + " BID=" + Bid + "\n");
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (streamOutReadable != null)
				streamOutReadable.close();

			streamOutMrk.close();
			in.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	} // end fromBinaryToTagged


	private byte[] myGetBytesUtf8(String s) {
		int len = s.length();
		int en = MAX_BYTES_PER_UTF8_CHARACTER * len;
		byte[] ba = new byte[en];
		if (len == 0)
			return ba;

		int ctr = 0;

		for (int i = 0; i < len; i++) {
			char c = s.charAt(i);
			if (c < 0x80) {
				ba[ctr++] = (byte) c;
			} else if (c < 0x800) {
				ba[ctr++] = (byte) (0xC0 | c >> 6);
				ba[ctr++] = (byte) (0x80 | c & 0x3F);
			} else if (c < 0x10000) {
				ba[ctr++] = (byte) (0xE0 | c >> 12);
				ba[ctr++] = (byte) (0x80 | c >> 6 & 0x3F);
				ba[ctr++] = (byte) (0x80 | c & 0x3F);
			} else if (c < 0x200000) {
				ba[ctr++] = (byte) (0xE0 | c >> 18);
				ba[ctr++] = (byte) (0x80 | c >> 12 & 0x3F);
				ba[ctr++] = (byte) (0x80 | c >> 6 & 0x3F);
				ba[ctr++] = (byte) (0x80 | c & 0x3F);
			} else if (c < 0x800) {

			}
		} // end for

		return trim(ba, ctr);
	} // End myGetBytesUtf8
	
	
	


	public void writeMarcRecord(FileOutputStream streamOut, MarcWriter writer, Record record) {
		String s = record.toString();
		String s1 = null;
		byte bytes[];
		byte arUtf8[] = null;

//System.out.println("record length["+s+"]");

		// Write MARC RECORD
		writer.write(record);

		// Write MARC RECORD in CHIARO
		bytes = myGetBytesUtf8(s);

		// System.out.println(s);
		Leader leader = record.getLeader();
		// System.out.println("\nLDR " + leader.toString());

		// Control fields: tags 001 through 009
		List fields = record.getControlFields();
		Iterator i = fields.iterator();
		while (i.hasNext()) {
			ControlField field = (ControlField) i.next();
			// System.out.println(field.toString());
			s = field.toString();
			arUtf8 = myGetBytesUtf8(s);
			// System.out.write(arUtf8, 0, arUtf8.length);
			// System.out.write('\n');
		}

		// Data fields for tags 010 through 999
		fields = record.getDataFields();
		i = fields.iterator();
		while (i.hasNext()) {
			DataField field = (DataField) i.next();
			s = field.toString();
			arUtf8 = myGetBytesUtf8(s);
			// System.out.write(arUtf8, 0, arUtf8.length);
			// System.out.write('\n');
		} // End while

		try {
			if (streamOut != null) {
				streamOut.write('\n');
				streamOut.write(bytes, 0, bytes.length);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	} // End writeMarcRecord



	public Record transformMarcRecord(ArrayList unimarc) throws Exception {
		char dollaro = '$';
		String tag = "";
		char subfield = ' ';
		char indicator1 = ' ';
		char indicator2 = ' ';
		String[] content;
		String line = "";
		Record recordOut = null;
		MarcFactory factory = MarcFactory.newInstance();
		for (int i=0; i<unimarc.size(); i++){
			if ( ((String)unimarc.get(i))==null ) break;
			// replace with dollar
			line = ((String)unimarc.get(i)).replace(SbnmarcUnimarc.IS1char, dollaro);
			tag = line.substring(0, 3);
			if ("000".equals(tag)){
				// create a record with leader
				recordOut = factory.newRecord(line.substring(3));
			} else if ( ("001".equals(tag))||("005".equals(tag)) ){
				// no subfields for tags 001 and 005
				// add a control field
				recordOut.addVariableField(factory.newControlField(tag, line.substring(3)));
			} else {
				// add a data field
				// indicators
				indicator1 = line.substring(3, 4).charAt(0);
				indicator2 = line.substring(4, 5).charAt(0);
				DataField df = factory.newDataField(tag, indicator1, indicator2);
				// content of data field
				line = line.substring(5); //without indicators
				content = line.split("["+dollaro+"]");
				for (int j=1; j<content.length; j++){
					subfield = content[j].substring(0, 1).charAt(0);
					df.addSubfield(factory.newSubfield(subfield, content[j].substring(1)));
				}
				recordOut.addVariableField(df);
			}
		}

		return recordOut;


	// VECCHIA GESTIONE
//		MarcFactory factory = MarcFactory.newInstance();
////		Record recordOut = factory.newRecord();
//		List fields;
//		Iterator i;
////		Leader leader = record.getLeader();
////		recordOut.setLeader(leader);
////		StringBuffer sb = new StringBuffer();
////		String bibRegisterCode = null;
////		String codBib = null;
//		char indicator1 = ' ';
//		char indicator2 = ' ';
//
//		// create a record with leader
//		Record recordOut = factory.newRecord("00000cam a2200000 a 4500");
//
//		// add a control field
//		recordOut.addVariableField(factory.newControlField("001", "12883376"));
//
//		// add a data field
//		DataField df = factory.newDataField("245", '1', '0');
//		df.addSubfield(factory.newSubfield('a', "Summerland /"));
//		df.addSubfield(factory.newSubfield('c', "Michael Chabon."));
//		recordOut.addVariableField(df);
//
//		return recordOut;

	} // End transformMarcRecord


	
	public static void main(String args[]) {
		if (args.length < 1) {
			System.out.println("Parametri mancanti. Uso: ConvertFromUnimarc fileListName (eg. filenames.txt) baseDir");
			System.exit(1);
		}
		String inputFile = args[0];
		System.out.println("inp: " + inputFile);

		ExportUnimarc export = new ExportUnimarc();

		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(inputFile));
			// Leggiamo le configurazioni di base
			// ----------------------------------
			while (true) {
				String s;
				try {
					s = in.readLine();
					if (s == null)
						break;
					else if (export.emptyString(s))
						continue;
					else {
						String ar[] = MiscString.estraiCampi(s, "=");
						if (ar[0].startsWith("basedir"))
							export.baseDir = ar[1];
						else if (ar[0].startsWith("!FineConfigDiBase!"))
							break;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// Inizializziamo l-ambiente di trattamento
			// ----------------------------------------

			// Esegui trattamento
			// ------------------
			while (true) {
				String s;
				try {
					s = in.readLine();
					if (s == null)
						break;
					else {
//						if (s.length() == 0 || )
						if ((	s.length() == 0) 
								||  (s.charAt(0) == '#') 
								|| (export.emptyString(s) == true))
							continue;

						System.out.println("\n\nProcessing: " + s);
						String ar[] = MiscString.estraiCampi(s, " ");

						recordCounter = 0;
//						righeLette = 0;
						export.transform(ar);
//						System.out.println("Righe lette: " + Integer.toString(righeLette));

						System.out.println("\nRecord letti: "	+ Integer.toString(recordCounter));
						System.out.println("Record errati: "	+ Integer.toString(rejectedCounter));

					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			// Convert from binary format to readable format

		}
		// System.out.println("Righe elaborate: " +
		// Integer.toString(rowCounter));
		System.exit(0);

	} // End main


} // End Convert
