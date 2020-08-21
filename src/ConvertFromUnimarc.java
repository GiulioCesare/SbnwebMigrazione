import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.xerces.parsers.DOMParser;
import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.MarcStreamWriter;
import org.marc4j.MarcWriter;
import org.marc4j.marc.ControlField;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Leader;
import org.marc4j.marc.MarcFactory;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.marc4j.marc.impl.SubfieldImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.BufferedReader;
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

public class ConvertFromUnimarc {
	public static final int MAX_BYTES_PER_UTF8_CHARACTER = 4;
	public static final boolean LEFT_PADDING = true;
	public static final boolean RIGHT_PADDING = false;
	public static final String UNICODE_UTF8_ENCODING = "50";
	public static final String RIMPIAZZA_MINORE 			= "xxa"; // <
	public static final String RIMPIAZZA_MAGGIORE 			= "xxb"; // >
	public static final String RIMPIAZZA_PUNTO_E_VIRGOLA 	= "xxc"; // ;
	public static final String RIMPIAZZA_APICE 				= "xxd"; // '
	public static final String RIMPIAZZA_TRATTINO 			= "xxe"; // '
	public static final int TIPO_ELENCO_DIGITALE_MAG = 1;
	public static final int TIPO_ELENCO_DIGITALE_PLAIN = 2;
	
	static char charSepArraySpace[] = { ' '};
	static char charSepArrayComma[] = { ','};
	static char charSepArrayEqual[] = { '='};
//	static char charSepArrayQuestionMark[] = { '?'};
//	static String stringSepArrayQuestionMarks[] = { "???"};
	static String delimiters = "=";
	
	
	
	
	Classificazioni classificazioni = null;
	CategorieDiFruizione categorieDiFruizione = null;
	Isbn isbn = null;

	String baseDir, reportDataPrep;
	
	String Bid;
	byte[] Lf = { '\n' };
	byte[] DeleteLf = { 'd','\n' };
	byte[] UpdateLf = { 'u','\n' };
		
	static int TIPO_ELENCO_DIGITALE = TIPO_ELENCO_DIGITALE_PLAIN; // TIPO_ELENCO_DIGITALE_MAG  
	static int recordCounter = 0;
	static int oldMarcWrittenRecordCounter = 0;
	static int oldMarcReadRecordCounter = 0;
	
	static int rejectedCounter = 0;
	static int oldMarcRejectedCounter = 0;
	
	// static int righeLette = 0;
	java.util.Vector vecBfrFiles; // Buffer Reader Files

	FileOutputStream streamOut, streamOutMrk, streamOutBid, streamBidToDeleteUpdateFileOut, reportDataPrepOut; // declare a file output object

	boolean hasLinefeed = false;
	boolean hasLinefeedUpdate = false;
	boolean isAuthorityAutori = false;
	boolean isAuthoritySoggetti = false;
	boolean isAuthorityClassificazioni = false;
	boolean isConvertToUtf8 = true;
	boolean hasDigitalServer = false;
	
	GestioneUpdate gestioneUpdate = null;
//	boolean dataprepIncrementale = false;
	char filesystemSeparator = ' '; // none
	String sortExecutable = "";
	int linefeedSize=1;
	
	LoadDigitalRecords digitalRecords = null;
	Biblioteche biblioteche = null;
	Gestione462_463 gestione462_463 = null;
	Biblioteche950 biblioteche950 = null;
	int biblioteche950NonDiPoloCtr = 0;
	int _463Ctr = 0;
	int _462Ctr = 0;

	int biblioteche950DiPoloCtr = 0;
	boolean isBiblioteche950DiPolo = true;
	String codicePolo = null;
	
	String 	linkDigitalePreBID = "", 
			linkDigitalePostBID = "";
	
	int linksAlDigitale = 0;
	int linksASyndetics = 0;

	int DroppedCtr = 0;
	int BidInDbCtr = 0;

	
	int InventarioConCategoriaDiFruizioneCtr = 0;
	int InventarioSenzaCategoriaDiFruizioneCtr = 0;
	
	
	int updateRecordsCtr = 0;
	int deleteRecordsCtr = 0;
	int deletedRecordsCtr = 0;
	
	boolean _950DiPolo=false;
	boolean _463DiPolo=false;
	boolean _462DiPolo=false;

	boolean isMonografia=false;
	boolean _950BVE = false;
	
	int  recordsCon950 = 0;
	int  recordsConSolo463 = 0;
	int  recordsConSolo462 = 0;
	int  recordsCon463e462 = 0;
	int _463NonLinkatoCtr = 0;
	int _462NonLinkatoCtr = 0;
	int  recordsMonografiaCon950 = 0;
	int  recordsMonografiaConSolo463 = 0;
	int  recordsMonografiaConSolo462 = 0;
	int  recordsMonografiaCon463e462 = 0;
	
	String _462FilenameOut, _463FilenameOut, _950FilenameOut, _463No950FilenameOut, dropFilenameOut, bidFilenameOut, bidFilenameIn;
	
	String 	digitaleFileIn, 
			classificazioniFileIn,
			bidEdizioneFileIn,
			biblioteche950FileIn,
			caratteriSpecialiUnimarcFileIn;

	Vector vector980 = null;
	
	
	public ConvertFromUnimarc() {
//		applicationPath = System.getProperty("user.dir");
//		System.out.println("Application PATH: " + applicationPath);
		vecBfrFiles = new java.util.Vector();
		ConvertMisc.setCharTable(new Hashtable()); //charTable = new Hashtable();
		ConvertMisc.setCharSetsUsed(new Vector());//charSetsUsed = new Vector();
		vector980 = new java.util.Vector();

	}


	private String unescape(String textLine) {
		return "";
	} // End fromEsabyte2Utf8

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
	private String addSpaces(String valore) {
		String tmpString = "000".concat(valore);
		return tmpString.substring(valore.length() - 1);
	}

	private static byte[] trim(byte[] ba, int len) {
		if (len == ba.length)
			return ba;
		byte[] tba = new byte[len];
		System.arraycopy(ba, 0, tba, 0, len);
		return tba;
	}


	public void writeSampleMarcRecord(FileOutputStream streamOut,
			MarcWriter writer) {
		// create a factory instance
		MarcFactory factory = MarcFactory.newInstance();

		// create a record with leader
		Record record = factory.newRecord("00000cam a2200000 a 4500");

		// add a control field
		record.addVariableField(factory.newControlField("001", "12883376"));

		// add a data field
		DataField df = factory.newDataField("245", '1', '0');
		df.addSubfield(factory.newSubfield('a', "Summerland /"));
		df.addSubfield(factory.newSubfield('c', "Michael Chabon."));
		record.addVariableField(df);
		writeMarcRecord(streamOut, writer, record);

	}


	
	

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


	public void writeMarcRecord(FileOutputStream streamOut, MarcWriter writer,
			Record record) {
		String s = record.toString();
		String s1 = null;
		byte bytes[];

		
		if (_950DiPolo == true)
			{
			recordsCon950++;
			if (isMonografia == true)
				recordsMonografiaCon950++;
			}
		if (_463DiPolo == true && _462DiPolo == true)
			{
			recordsCon463e462++;
			if (isMonografia == true)
				recordsMonografiaCon463e462++;
			}
		else if (_463DiPolo == true)
			{
			recordsConSolo463++;
			if (isMonografia == true)
				recordsMonografiaConSolo463++;
			}
		else if (_462DiPolo == true)
			{
			recordsConSolo462++;
			if (isMonografia == true)
				recordsMonografiaConSolo462++;
			}
	
		
		
		
		
		if (isConvertToUtf8 == true)
			bytes = myGetBytesUtf8(s);
		else
			bytes = s.getBytes(); // no utf8 translation

		byte arUtf8[] = null;

		// Write MARC RECORD
		writer.write(record);

		// Write tagged marc record
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


	private void loadCodesTable() throws IOException, SAXException {
		DOMParser parser = new DOMParser();
		// String fileName = applicationPath +File.separator + "src"
		// +File.separator + "CaratteriSpeciali.xml";
//		String fileName = applicationPath + File.separator + "conf"	+ File.separator + "CaratteriSpecialiUnimarc.xml";
//		String fileName = baseDir + "conf"	+ File.separator + "CaratteriSpecialiUnimarc.xml";
		String fileName = baseDir + caratteriSpecialiUnimarcFileIn;
		
		
		// System.out.println("Loading " + fileName);

		// FileReader fr = new FileReader(fileName);
		parser.parse(fileName);

		Document doc = parser.getDocument();

		NodeList nodeList = (NodeList) doc.getElementsByTagName("CARATTERE");

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentElement = (Node) nodeList.item(i);
			String codiceEsabyte = currentElement.getAttributes().getNamedItem("ESABYTE").getNodeValue(); // getTextContent()
			String codiceUnimarc = currentElement.getAttributes().getNamedItem("UNIMARC").getNodeValue(); // getTextContent()
			String codiceUnicode = currentElement.getAttributes().getNamedItem("UNICODE").getNodeValue(); // getTextContent()

			String nome = currentElement.getAttributes().getNamedItem("NAME").getNodeValue(); //getTextContent()
			// String utf16Code = currentElement.getText();
			CharMap cm = new CharMap(codiceEsabyte, codiceUnimarc,
					codiceUnicode, nome);

			// charTable.put(codiceUnimarc, cm);
//			charTable.put(codiceEsabyte, cm);
			ConvertMisc.charTable.put(codiceEsabyte, cm);

		}

		// fr.close();
	} // End loadCodesTable

	public Record transformMarcRecord(Record record) throws Exception { // 
		MarcFactory factory = MarcFactory.newInstance();
		Record recordOut = factory.newRecord();
		List fields;
		Iterator i;
		Leader leader = record.getLeader();
		recordOut.setLeader(leader);
		StringBuffer sb = new StringBuffer();
		StringBuffer sb200 = new StringBuffer();
		StringBuffer sb7xx = new StringBuffer();
		StringBuffer sbK = new StringBuffer();
		StringBuffer categoriaDiFruizione = new StringBuffer();
		StringBuffer disponibilitaInventario = new StringBuffer();
		boolean in200 = false;
		boolean in7xx = false;
		boolean in950 = false;
		boolean field140 = false;
		boolean gotDigitalInfo = false;
		String AutoreAccettato = "";
		String bibRegisterCode = null;
		String codBib = null;
		Subfield sfNonLinkato = null;
		char []implDef1 = leader.getImplDefined1();
		char stato = leader.getRecordStatus();
		
		
		vector980.clear();

		if (implDef1[0] == 'm')
			isMonografia = true;
		else
			isMonografia=false;
		
		_950DiPolo=false;
		_463DiPolo=false;
		_462DiPolo=false;
		

		// Control fields: tags 001 through 009
		fields = record.getControlFields();
		i = fields.iterator();
		while (i.hasNext()) {
			ControlField field = (ControlField) i.next(); // tag 001
			recordOut.addVariableField(field);
			if (field.getTag().compareTo("001") == 0)
				{
				Bid = field.getData();
//if (Bid.equals("CFI0014434") == true)					
//	System.out.println("Bid: " + Bid);
				
				// 07/04/08 Da scarico Braidense risultano esserci record da cancellare anche per gli scarichi integrali
				if (stato == 'd' || stato == 'D')
				{
					if (gestioneUpdate != null)
					{
						streamBidToDeleteUpdateFileOut.write(Bid.getBytes());	
						streamBidToDeleteUpdateFileOut.write(DeleteLf);
						deleteRecordsCtr++;
					}
					else
					{
						DroppedCtr++;
					}
					
					return null;
				}
				
				if (gestioneUpdate != null && gestioneUpdate.isBidPresent(Bid))
				{
						streamBidToDeleteUpdateFileOut.write(Bid.getBytes());	
						streamBidToDeleteUpdateFileOut.write(UpdateLf);
						updateRecordsCtr++;
				}
				
				if (gestione462_463 != null && gestione462_463.isBidDropped(Bid))
					{
					DroppedCtr++;
					return null;
					}
				else
				{
					
					streamOutBid.write(Bid.getBytes());
					streamOutBid.write(Lf);
					BidInDbCtr++;

				}
				}
			
		}

		// Data fields tag 010 through 999
		DataField Df980 = null;
		fields = record.getDataFields();
		i = fields.iterator();
		while (i.hasNext()) {
			DataField field = (DataField) i.next();
			char indicator1 = field.getIndicator1();
			char indicator2 = field.getIndicator2();
			String tag = field.getTag();
			List subfields = field.getSubfields();

			DataField df = factory.newDataField(tag, indicator1, indicator2);
			

			// Iterator s = subfields.iterator();

			if (tag.compareTo("950") == 0) 
				in950=true;
			else
				in950=false;
			
			if (isAuthorityAutori == true
					&& (tag.compareTo("200") == 0 
						|| tag.compareTo("400") == 0
						|| tag.compareTo("500") == 0
						|| tag.compareTo("700") == 0 
						|| tag.compareTo("210") == 0
						|| tag.compareTo("410") == 0
						|| tag.compareTo("510") == 0 
						|| tag.compareTo("710") == 0

					)) { // crea la 499 (forme varianti)
				Subfield sba = field.getSubfield('a');
				Subfield sbb = field.getSubfield('b');

				String data = "";

				if (sbb != null)
					data = sba.getData() + sbb.getData();
				else
					data = sba.getData();


				List locSubfields = field.getSubfields();
				Iterator locIterSubfields = locSubfields.iterator();
				boolean append=false;
				while (locIterSubfields.hasNext()) {
					Subfield subfield = (Subfield) locIterSubfields.next();
					if (subfield.getCode() == 'c' 
						|| subfield.getCode() == 'd'
						|| subfield.getCode() == 'e'
						|| subfield.getCode() == 'f'
						) {
						if (append == true)
							data = data + " "+RIMPIAZZA_PUNTO_E_VIRGOLA+" "+ subfield.getData(); // xxc = ';'
						else
							data = data + subfield.getData(); // "ARGE" +
						append = true;  
					}
				}
				
				
				if ((tag.compareTo("200") == 0) || (tag.compareTo("210") == 0))
					AutoreAccettato = data;
				else
					data = data + " yyy " + AutoreAccettato;

				
				// Rimpiazza le '<' con xxa
				data = MiscString.replace(data, "<", RIMPIAZZA_MINORE);
				// Rimpiazza le '>' con xxb
				data = MiscString.replace(data, ">", RIMPIAZZA_MAGGIORE);
				// Rimpiazza le '\'' con xxa
				data = MiscString.replace(data, "'", RIMPIAZZA_APICE);
				data = MiscString.replace(data, "-", RIMPIAZZA_TRATTINO);

				// Rimuovi ; in quanto li metto io sopra
				data = MiscString.replace(data, " ; ", "");


				Subfield sf = new SubfieldImpl('a', data + " yyy " + Bid);
				DataField dataField = factory.newDataField("499", ' ', ' ');
				dataField.addSubfield(sf);
				recordOut.addVariableField(dataField);
			}

			else if (isAuthoritySoggetti == true && (tag.compareTo("250") == 0)) {
				List locSubfields = field.getSubfields();
				Iterator locIterSubfields = locSubfields.iterator();
				while (locIterSubfields.hasNext()) {
					Subfield subfield = (Subfield) locIterSubfields.next();
					if (subfield.getCode() == 'a' || subfield.getCode() == 'x') {
						Subfield sf = new SubfieldImpl(subfield.getCode(),
								subfield.getData() + " yyy " + Bid);
						DataField dataField = factory.newDataField("499", ' ',
								' ');
						dataField.addSubfield(sf);
						recordOut.addVariableField(dataField);
					}
				}
			}

			else if ((tag.compareTo("410") == 0)  // Campi piu frequenti
					||(tag.compareTo("423") == 0)  
					||(tag.compareTo("454") == 0)	
					// (tag.compareTo("410") == 0) ||
					|| (tag.compareTo("421") == 0) 
					|| (tag.compareTo("422") == 0)
					// (tag.compareTo("423") == 0) ||
					|| (tag.compareTo("430") == 0) 
					|| (tag.compareTo("431") == 0)
					|| (tag.compareTo("434") == 0)
					|| (tag.compareTo("440") == 0)
					|| (tag.compareTo("447") == 0)
					|| (tag.compareTo("451") == 0) 
					// (tag.compareTo("454") == 0) ||
					|| (tag.compareTo("461") == 0) 
					|| (tag.compareTo("462") == 0)
					|| (tag.compareTo("463") == 0)
					|| (tag.compareTo("464") == 0)
					|| (tag.compareTo("488") == 0)

			) { // campi linkati
				// Subfield
				// List sfs = field.getSubfields('1');
				
				Iterator j = subfields.iterator();

				sb200.setLength(0);
				sb7xx.setLength(0);
				sfNonLinkato = null;
				while (j.hasNext()) {
					Subfield sf = (Subfield) j.next();
					// System.out.println(sf.toString());
					String data = sf.getData();
					if (sf.getCode() != 'a' && sf.getCode() != 'b'
							&& sf.getCode() != 'c' && sf.getCode() != 'd'
							&& sf.getCode() != 'e' && sf.getCode() != '1') {
						in200 = false;
						in7xx = false;
					} else if (sf.getCode() == '1' && data.startsWith("200")) {
						in200 = true;
						continue;
					} else if (sf.getCode() == '1' && data.startsWith("7")) { // 7xx
						in200 = false;
						in7xx = true;
						continue;
					}

					else if ((sf.getCode() == 'a' || sf.getCode() == 'c'
							|| sf.getCode() == 'd' || sf.getCode() == 'e')
							&& in200 == true) {
						if (sb200.length() != 0)
							sb200.append(" ; " + data);
						else
							sb200.append(data);
					} else if ((sf.getCode() == 'a' || sf.getCode() == 'b')
							&& in7xx == true) {
						sb7xx.append(data);
					}
					else if (gestione462_463 != null && sf.getCode() == '1' && tag.compareTo("463") == 0)
							{
						 	_463DiPolo=true;
						 	_463Ctr++;
						 	String bidLinkato = data.substring(3);
							if (gestione462_463.isBidDropped(bidLinkato))
							{
							_463NonLinkatoCtr++;
							sfNonLinkato = new SubfieldImpl('y', "Non linkato");
							}
							}
					else if (gestione462_463 != null && sf.getCode() == '1' && tag.compareTo("462") == 0)
					{
				 	_462DiPolo=true;
				 	_462Ctr++;
				 	String bidLinkato = data.substring(3);
					if (gestione462_463.isBidDropped(bidLinkato))
					{
					_462NonLinkatoCtr++;
					sfNonLinkato = new SubfieldImpl('y', "Non linkato");
					}
					}

				} // end while

				if (sb200.length() > 0) {
					Subfield sf = new SubfieldImpl('k', sb200.toString());
					field.addSubfield(sf);
				}
				if (sb7xx.length() > 0) {
					Subfield sf = new SubfieldImpl('j', sb7xx.toString());
					field.addSubfield(sf);
				}
				if (sfNonLinkato != null)
					{
					field.addSubfield(sfNonLinkato);
					}

			}
			else if (tag.compareTo("010") == 0) { // ISBN
				sbK.setLength(0); // clear
				Subfield subfield = field.getSubfield('a');
				if (subfield == null)
					subfield = field.getSubfield('z'); // Prendiamo dalla $z
				
				if (subfield != null)
				{
				sbK.append(subfield.getData());
				String compactIsbn = MiscString.substitute(sbK.toString(), "-", ""); 
				String isbn13;
				if (compactIsbn.length() == 10)
					isbn13 = compactIsbn+"   "; // portale a 13
				else
					isbn13 = compactIsbn; 
				if ( isbn!= null && isbn.existsIsbn(isbn13)) // Questo isbn e' da linkare a syndetics?
				{
					Subfield sf = new SubfieldImpl('k', "http://syndetics.com/index.aspx?isbn="+compactIsbn+"/index.html&client=iccusbn&type=rn12" ); 
					field.addSubfield(sf);
					linksASyndetics++;
					
				}
				}
			}
			
			else if (tag.compareTo("606") == 0) {
				sbK.setLength(0); // clear
				Subfield subfield = field.getSubfield('a');
				sbK.append(subfield.getData());
				Iterator s = subfields.iterator();
				while (s.hasNext()) {
					subfield = (Subfield) s.next();
					if (subfield.getCode() == 'x') {
						sbK.append(" yyy " + subfield.getData()); // ' - '
					}
				}
				Subfield sf = new SubfieldImpl('k', MiscString.substitute(sbK
						.toString(), "-", "yyy")); // _[]_ = trattino (-)
				field.addSubfield(sf);
			}

			else if (tag.compareTo("676") == 0) { // Aggiungiamo l'edizione e la descrizione della classificazione  
				sbK.setLength(0); // clear
				Subfield subfield = field.getSubfield('a');
String edizione;
if (_950BVE) // PER BULL BVE
{
	int len  = subfield.getData().length()-1;
	char chr = subfield.getData().charAt(len); 
	if (chr == ':')
		edizione = "15"; 
	else if (chr == '(')
		edizione = "16"; 
	else if (chr == '?')
		edizione = "16"; 
	else if (chr == '!')
		edizione = "17"; 
	else if (chr == '/')
		edizione = "18"; 
	else if (chr == '+')
		edizione = "19"; 
	else if (chr == '-')
		edizione = "20"; 
	else
		edizione = classificazioni.getEdizionePerBid(Bid, subfield.getData());
}
else

				edizione = classificazioni.getEdizionePerBid(Bid, subfield.getData());

//				if (edizione == null || edizione.isEmpty())
				if (edizione == null || MiscString.isEmpty(edizione)) // 07/07/09
					//edizione = classificazioni.getEdizioneDiDefaultString();
					System.out.println("WARNING: Edizione della classe non trovata per bid " + Bid + " " + subfield.getData());
				else
				{
				Subfield sf = new SubfieldImpl('k', edizione); 
				field.addSubfield(sf);
				sf = new SubfieldImpl('j', classificazioni.getDescrizione(subfield.getData(), Integer.parseInt(edizione)));
				field.addSubfield(sf);
				}
				
			}
			
			else if (tag.compareTo("100") == 0) {

				Subfield subfield = field.getSubfield('a');
				String data = subfield.getData();
				String chrarset1 = "  "; // default
				String chrarset2 = "  "; // default
				String chrarset3 = "  "; // default

				int ndx = 28;
				if (isAuthorityAutori == true || isAuthoritySoggetti)
					ndx = 15;

				if (data.length() > ndx) {
					chrarset1 = data.substring(ndx, ndx + 2);// the second
					chrarset2 = data.substring(ndx + 2, ndx + 4);
					chrarset3 = data.substring(ndx + 4, ndx + 6);

					ConvertMisc.charSetsUsed.clear();
					if (chrarset1.compareTo("  ") != 0)
						ConvertMisc.charSetsUsed.add(chrarset1);
					if (chrarset2.compareTo("  ") != 0)
						ConvertMisc.charSetsUsed.add(chrarset2);
					if (chrarset3.compareTo("  ") != 0)
						ConvertMisc.charSetsUsed.add(chrarset3);

					String newData = data.substring(0, ndx - 2)
							+ UNICODE_UTF8_ENCODING + "      "
							+ data.substring(ndx - 2 + 8);
					// Set new character set
					field.removeSubfield(subfield);
					subfield.setData(newData);
					field.addSubfield(subfield);

					if (isAuthorityAutori || isAuthoritySoggetti)
						sb.append(data.substring(0, 4)); // prendi la data
					else
						sb.append(data.substring(9, 13)); // prendi la data

					Subfield sf = new SubfieldImpl('k', sb.toString());
					field.addSubfield(sf);
					sb.setLength(0); // clear
				}

				recordOut.addVariableField(field);
				continue;

			}

			boolean noTranslation = false;

			if (tag.startsWith("140")) {
				field140 = true;
				noTranslation = true;
			}

			else if (tag.startsWith("010")
					|| tag.startsWith("011")
					|| ((tag.compareTo("012") > 0) && (tag.compareTo("073") < 0))
					|| tag.startsWith("=101") || tag.startsWith("=102")
					|| tag.startsWith("=950")) {
				noTranslation = true;
			}
			sb.setLength(0); // clear
			gotDigitalInfo = false;

			// subfields = field.getSubfields(); // In case of new subfields
			Iterator s = subfields.iterator();
			isBiblioteche950DiPolo = true;
			categoriaDiFruizione.setLength(0); // Clear
			disponibilitaInventario.setLength(0);
			//disponibilitaInventario = "N";
//int subfields950Ctr = 0;
			while (s.hasNext()) {
				Subfield subfield = (Subfield) s.next();

				if ((subfield.getCode() == 'd' || subfield.getCode() == 'e') && tag.startsWith("950")) {
					
//subfields950Ctr++;
//System.out.println("subfields950Ctr = " + subfields950Ctr);

					
					// Get bib code from bib description
					if (_950BVE == true)
					{
						try {
							codBib = " " + (subfield.getData().substring(0, 2));
						} catch (Exception e) {
							// TODO: handle exception
							System.out.println("codBib non trovato per sub field!!!" + subfield.getData());
							throw new Exception("errore in 950");
						}
						
					}
						
					else	
						codBib = (subfield.getData().substring(0, 3));
					
					
					if (biblioteche950.containsBiblio(codBib) == false)
					{
						biblioteche950NonDiPoloCtr++;
						isBiblioteche950DiPolo = false;
						break; // SKIP inclusion of 950 in case libray is not in POLO
					}
					
					if (subfield.getCode() == 'e' && categorieDiFruizione != null && subfield.getData().charAt(15) == '5') // 5=collocato 2=non collocato 
					{

					char c = subfield.getData().charAt(15);
	
					String s1 = categorieDiFruizione.getInventoryCategoryAndAvailability(subfield.getData().substring(0,15));
					if (s1 != null)
					{
						if (categoriaDiFruizione.length() > 0)
						{
							categoriaDiFruizione.append("|"+s1.substring(0,2));
							disponibilitaInventario.append("|"+s1.substring(3,4));
						}
						else
						{
							categoriaDiFruizione.append(s1.substring(0,2));
							disponibilitaInventario.append(s1.substring(3,4));
						}
						InventarioConCategoriaDiFruizioneCtr++;
					}
					else
					{ // In caso che non troviamo la categoria di fruizione
						if (categoriaDiFruizione.length() > 0)
						{
							categoriaDiFruizione.append("|  ");
							disponibilitaInventario.append ("| ");
						}
						else 
						{
							categoriaDiFruizione.append("  ");
							disponibilitaInventario.append (" ");
						}
						InventarioSenzaCategoriaDiFruizioneCtr++;
					}
					
					}	
					bibRegisterCode = biblioteche.getBibCode(codicePolo	+ codBib);
					if (bibRegisterCode == null)
						bibRegisterCode = "";

					if (hasDigitalServer == true && gotDigitalInfo == false)
						gotDigitalInfo = true;
				}  // fine $d o $e per 950

				// Skip translation
				if (noTranslation == true || isConvertToUtf8 == false) {
					
					subfield.setData(ConvertMisc.replaceNoSortSequence(subfield.getData()));
					df.addSubfield(subfield);
					// Concatena sottocampi e creane uno concatenato
					if (tag.startsWith("7") // 25/11/2009 13.57 Mi perdevo la $k della 7xx
							&& (subfield.getCode() == 'a'
									|| subfield.getCode() == 'b'
									|| subfield.getCode() == 'c'
									|| subfield.getCode() == 'd'
									|| subfield.getCode() == 'e' 
									|| subfield.getCode() == 'f')) {
						String s1 = subfield.getData();
						if (s1 != null && s1.length() > 0 && s1.charAt(0) != ' ') sb.append(" ");
							sb.append(s1);

					}
					//df.addSubfield(subfield);
					
					
				} else {

					String subfieldData = subfield.getData();

					if (in950 == true && subfield.getCode() == 'd' && _950BVE == true) // Trattiamo i dati di collocazione per BVE
					{
						sb.setLength(0); // clear
						
						sb.append(	" " + subfieldData.substring(0,2) + 	// Biblioteca
									subfieldData.substring(2,7)+ "     "); 	// sezione
						// abbiamo una collocazione?
						if (subfieldData.length() > 8)
						{
							int len=0;
							if (subfieldData.length() > 16)
							{
								sb.append(subfieldData.substring(7,17));		// Collocazione
								len = 10;
							}
							else
							{
								sb.append(subfieldData.substring(7));		// Collocazione
								// Quanti spazi mancano per arrivare a 24 caratteri
								len = subfieldData.length()-7;
						}
							for (int j = len; j < 24; j++)
								sb.append(' ');
						}
						// abbiamo una specificazione?
						if (subfieldData.length() > 16)
						{
							int len = 0;
							if (subfieldData.length() < 27)
							{
								sb.append(subfieldData.substring(17));		// Specificazione
								len = subfieldData.length()-17; // Riempire specificazione fino a 10 caratteri
						}
						else
							{
								sb.append(subfieldData.substring(17,27));		// Specificazione
								len = 10; // Riempire specificazione fino a 10 caratteri
						}
//								for (int j = len; j < 10; j++)
								for (int j = len; j < 12; j++) // 10 dice Rossana 12 dice il manuale per la lunghezza della specificazione
								sb.append(' ');

						}
						
						subfieldData = sb.toString();	
					}
					else if (in950 == true && subfield.getCode() == 'e' && _950BVE == true) // Trattiamo i dati di inventariazione per BVE
					{
						sb.setLength(0); // clear
						sb.append(" " + subfieldData.substring(0,2));  	// Biblioteca
						sb.append("   "); 								// serie inventariale
						sb.append(subfieldData.substring(2,11)); 		// inventario
						sb.append(subfieldData.substring(11,12));		// stato
						sb.append(subfieldData.substring(24,32));		// data di inventariazione
						sb.append(subfieldData.substring(19,23));		// sequenza di collocazione
						if (subfieldData.length() > 32)
							sb.append(subfieldData.substring(32));		// precisazione di inventario
						subfieldData = sb.toString();	
					}
					
					
					if (in950 == true && (subfield.getCode() == 'b' || subfield.getCode() == 'd')) 
						//subfieldData = subfieldData.trim(); // Rimuoviamo un po di spazi in coda per evitare di sfondare il massimo di 9999 bytes di un singolo campo
						subfieldData = MiscString.trimRight(subfieldData); // Rimuoviamo un po di spazi in coda per evitare di sfondare il massimo di 9999 bytes di un singolo campo
					try {
						subfieldData = ConvertMisc.fromUnimarc2UnicodeTranslate(subfieldData);
//						if (subfieldData == null)
//							subfieldData = "";
							
					} catch (Exception e) {
						// TODO Auto-generated catch block
						// e.printStackTrace();
						throw new Exception(e);
					}
					subfield.setData(subfieldData);
					// Concatena sottocampi e creane uno concatenato
					if (tag.startsWith("7")
							&& (subfield.getCode() == 'a'
									|| subfield.getCode() == 'b'
									|| subfield.getCode() == 'c'
									|| subfield.getCode() == 'd'
									|| subfield.getCode() == 'e' 
									|| subfield.getCode() == 'f')) {
						String s1 = subfield.getData();
						if (s1 != null && s1.length() > 0 && s1.charAt(0) != ' ') sb.append(" ");
							sb.append(s1);

					}
					df.addSubfield(subfield);
				} // end else
				
			} // end while has next
			
			
			if (tag.startsWith("950") && isBiblioteche950DiPolo == true) {
				// Aggiungi il codice biblioteca
				Subfield sf = new SubfieldImpl('k', codBib);
				df.addSubfield(sf);
				biblioteche950DiPoloCtr++;
				_950DiPolo = true;
				
				if (hasDigitalServer == true) {
					if (TIPO_ELENCO_DIGITALE == TIPO_ELENCO_DIGITALE_MAG) //  && codicePolo.equals("AQ1"
					{
						if (digitalRecords.getMagMap().containsKey(Bid + bibRegisterCode) == true) {
							MagRecord mr = (MagRecord) digitalRecords.getMagMap().get(Bid + bibRegisterCode);
							ArrayList<MagShortRecord> magShortRecordList = (ArrayList<MagShortRecord>) mr.getMagShortRecordList();
							Iterator itr = magShortRecordList.iterator();
							int blockCtr = 0;
							while (itr.hasNext()) {
								blockCtr++;
								MagShortRecord msr = (MagShortRecord) itr.next();
								if (msr.getBibLevel() == 's' || msr.getBibLevel() == 'S') // Periodici
								{
									String block = msr.getYear() + ", " + msr.getIssue();
									sf = new SubfieldImpl(
											'j',
											//"<a href=\"http://opac.internetculturale.it/cgi-bin/logserv.cgi?type=digitale&url=http://www.internetculturale.it/bdi-ntc/digital/risorsa_digitale.jsp?magid="
											linkDigitalePreBID
											+ msr.getIdentifier()
											+ linkDigitalePreBID + "\" title=\"" // %26language=it
											+ msr.getIssue()
											+ "\" target='WinDigitale'>"
											+ "(dig.)" + msr.getYear()
											+ "</a>");
									df.addSubfield(sf);
									linksAlDigitale++;
								}
								if (msr.getBibLevel() == 'm' || msr.getBibLevel() == 'M') // Monografie
								{
									sf = new SubfieldImpl(
											'j',
											//"<a href=\"http://opac.internetculturale.it/cgi-bin/logserv.cgi?type=digitale&url=http://www.internetculturale.it/bdi-ntc/digital/risorsa_digitale.jsp?magid="
											linkDigitalePreBID
											+ msr.getIdentifier()
											+ linkDigitalePreBID + "\" title=\"" // %26language=it
											+ "Scheda digitale"
											+ "\"  target='WinDigitale'>"
											+ "copia digitale" // +
											+ "</a>");
									df.addSubfield(sf);
									linksAlDigitale++;
								}
							} // end while in short record list
						} // End se trovato bid
					} // End if tipo file = MAG
					
//					else if (TIPO_ELENCO_DIGITALE == TIPO_ELENCO_DIGITALE_PLAIN && codicePolo.equals("MIL"))
//					{
//						if (digitalRecords.existsBid(Bid))
//							{
//							sf = new SubfieldImpl(
//									'j',
//									"<a href=\"http://www.urfm.braidense.it/cataloghi/rdpdf.php?bid="
//									+ Bid
//									+ "\"  target='WinDigitale'>"
//									+ "copia digitale" // +
//									+ "</a>");
//							df.addSubfield(sf);
//							linksAlDigitale++;
//							}
//					}
					else if (TIPO_ELENCO_DIGITALE == TIPO_ELENCO_DIGITALE_PLAIN) //  && codicePolo.equals("IEI"
					{
						if (digitalRecords.existsBid(Bid))
							{
							// eg. per IEI http://192.168.104.78:8080/tecaDigitale/consultazione-new/tecadigitale_scheda_new.jsp?idRisorsa=GEA0023440&l=it
							// eg. per MIL http://www.urfm.braidense.it/cataloghi/rdpdf.php?bid=BA1E000405
							sf = new SubfieldImpl(
									'j',
									"<a href=\"" 
									+ linkDigitalePreBID 
									+ Bid
									+ linkDigitalePostBID 
									+ "\"  target='WinDigitale'>"
									+ "copia digitale"
									+ "</a>");
							df.addSubfield(sf);
							linksAlDigitale++;
							}
					}

				} // End if digital records
			
				
				
			if (categoriaDiFruizione.length() > 0)
				{
				Df980 = factory.newDataField("980", ' ', ' ');					

				sf = new SubfieldImpl('k', codBib);
				Df980.addSubfield(sf);

				sf = new SubfieldImpl('y', categoriaDiFruizione.toString());
				Df980.addSubfield(sf);

				sf = new SubfieldImpl('z', disponibilitaInventario.toString());
				Df980.addSubfield(sf);
				vector980.add(Df980);
				}

			}
			else if (tag.startsWith("7")) {
				Subfield sf = new SubfieldImpl('k', sb.toString()); // Subfield 
				df.addSubfield(sf);
			} else if (tag.startsWith("140")) {
				Subfield sf = new SubfieldImpl('k', "E"); // Flag antico // Subfield 
				df.addSubfield(sf);
			}
			sb.setLength(0); // clear

			if (tag.startsWith("950") && isBiblioteche950DiPolo != true) 
				continue; // Do not add the data field that is to be skipped
			recordOut.addVariableField(df);
		}

		if (field140 == false) {
			// Check id ANTICO
			if (Bid.charAt(3) == 'E') { // Crea campo per antico
				DataField dataField = factory.newDataField("140", ' ', ' ');
				String Antico = "..........................E."; // Record
				// generated
				Subfield sf = new SubfieldImpl('a', Antico);
				dataField.addSubfield(sf);
				sf = new SubfieldImpl('k', "E");
				dataField.addSubfield(sf);

				recordOut.addVariableField(dataField);
			}

		}
		
		for (int j=0; j < vector980.size(); j++)
			recordOut.addVariableField((DataField)vector980.get(j));
				
		return recordOut;
	} // End transformMarcRecord

	

	
	

	// public void transform(String inputFile, String outputFile)
	public void transform(String fileOutIn[]) {
		String marcFileIn, marcFileOut, readableMarkFileOut, updateMarcFileIn, updateMarcFileOut, bidToDeleteUpdateFileOut;
		String categorieDiFruizioneFileIn, fruizioneInventariFileIn, isbnFileIn, tipoElencoBidAlDigitale;				
		
		marcFileIn = ""; 
		marcFileOut = ""; 
		bidEdizioneFileIn = "";
		updateMarcFileIn = "";
		updateMarcFileOut = "";
		bidToDeleteUpdateFileOut = "";
		categorieDiFruizioneFileIn = ""; 
		fruizioneInventariFileIn = "";
		isbnFileIn = "";
		tipoElencoBidAlDigitale = "MAG";
		digitaleFileIn = "";
		hasLinefeed 				= false; 				// default
		isAuthorityAutori 			= false;
		isAuthorityClassificazioni 	= false;
		isAuthoritySoggetti 		= false;
		hasDigitalServer 			= false;
		isConvertToUtf8 			= true;
		readableMarkFileOut = null;
		int i;
		


		for (i=0; i < fileOutIn.length; i++)
		{
			if (fileOutIn[i].startsWith("-MarkFileIn=")) 
				marcFileIn = baseDir + fileOutIn[i].substring(12);
			else if (fileOutIn[i].startsWith("-MarkFileOut=")) 
				marcFileOut = baseDir + fileOutIn[i].substring(13);
			else if (fileOutIn[i].startsWith("-DropFileOut=")) 
				dropFilenameOut = baseDir + fileOutIn[i].substring(13);
			else if (fileOutIn[i].startsWith("-BidFileIn=")) 
				bidFilenameIn = baseDir + fileOutIn[i].substring(11);
			else if (fileOutIn[i].startsWith("-BidFileOut=")) 
				bidFilenameOut = baseDir + fileOutIn[i].substring(12);
			else if (fileOutIn[i].startsWith("-UpdateMarkFileIn=")) 
				updateMarcFileIn = baseDir + fileOutIn[i].substring(18);
			else if (fileOutIn[i].startsWith("-UpdateMarkFileOut=")) 
				updateMarcFileOut = baseDir + fileOutIn[i].substring(19);
			else if (fileOutIn[i].startsWith("-BidToDeleteUpdate=")) 
				bidToDeleteUpdateFileOut = baseDir + fileOutIn[i].substring(19);
			
			else if (fileOutIn[i].startsWith("-CategorieDiFruizione=")) 
				categorieDiFruizioneFileIn = baseDir + fileOutIn[i].substring(22);
			else if (fileOutIn[i].startsWith("-FruizioneInventari=")) 
				fruizioneInventariFileIn = baseDir + fileOutIn[i].substring(20);
			else if (fileOutIn[i].startsWith("-Isbn=")) 
				isbnFileIn = baseDir + fileOutIn[i].substring(6);
			
			
			else if (fileOutIn[i].startsWith("-TipoElencoBidAlDigitale="))
			{
				tipoElencoBidAlDigitale = fileOutIn[i].substring(25);
				
				if (fileOutIn[i].substring(25).equals("MAG"))
					TIPO_ELENCO_DIGITALE = TIPO_ELENCO_DIGITALE_MAG;
				else if (fileOutIn[i].substring(25).equals("PLAIN"))
					TIPO_ELENCO_DIGITALE = TIPO_ELENCO_DIGITALE_PLAIN;
				
System.out.println("-TipoElencoBidAlDigitale: " + TIPO_ELENCO_DIGITALE);				
				
								
			}

			else if (fileOutIn[i].startsWith("-Digitale=")) {
				hasDigitalServer = true;
				digitaleFileIn = baseDir + fileOutIn[i].substring(10);
System.out.println("-Digitale");				
			}
			else if (fileOutIn[i].startsWith("-MarkFileOutTxt="))
				readableMarkFileOut = baseDir + fileOutIn[i].substring(16);
			
			else if (fileOutIn[i].compareToIgnoreCase("-CrLf") == 0)
				hasLinefeed = true;
			else if (fileOutIn[i].compareToIgnoreCase("-NoCrLf") == 0)
				hasLinefeed = false;
			
			else if (fileOutIn[i].compareToIgnoreCase("-NoCrLfUpddate") == 0)
				hasLinefeedUpdate = true;
			else if (fileOutIn[i].compareToIgnoreCase("-NoCrLfUpddate") == 0)
				hasLinefeedUpdate = false;
			else if (fileOutIn[i].compareToIgnoreCase("-AuthAutori") == 0) {
				isAuthorityAutori = true;
				isAuthorityClassificazioni = false;
				isAuthoritySoggetti = false;
			} else if (fileOutIn[i].compareToIgnoreCase("-AuthSoggetti") == 0) {
				isAuthoritySoggetti = true;
				isAuthorityClassificazioni = false;
				isAuthorityAutori = false;
			} else if (fileOutIn[i].compareToIgnoreCase("-AuthClassificazioni") == 0) {
				isAuthorityClassificazioni = true;
				isAuthoritySoggetti = false;
				isAuthorityAutori = false;
			} else if (fileOutIn[i].compareToIgnoreCase("-AuthDocument") == 0) {
				isAuthorityAutori = false;
				isAuthoritySoggetti = false;
			}
			else if (fileOutIn[i].compareToIgnoreCase("-NoUtf8Conv") == 0)
				isConvertToUtf8 = false;
			else if (fileOutIn[i].compareToIgnoreCase("-Utf8Conv") == 0)
				isConvertToUtf8 = true;
			else if (fileOutIn[i].compareToIgnoreCase("-950BVE") == 0)
				_950BVE = true;
			
			else if (fileOutIn[i].startsWith("-BidEdizioneFileIn=")) 
				bidEdizioneFileIn = baseDir + fileOutIn[i].substring(19);
			
		} // end for

		
		// Carica i bid associati alle edizioni dewey
		classificazioni.loadBidEdizione(bidEdizioneFileIn, filesystemSeparator);
		
		
		
		if (hasDigitalServer == true)
		{
			digitalRecords = new LoadDigitalRecords();
			digitalRecords.run(digitaleFileIn, tipoElencoBidAlDigitale, filesystemSeparator);
//			codicePolo = "IEI"; // ?? per ora
		}

		vecBfrFiles.clear();

		if (isbnFileIn.length() > 0)
		{
			isbn = new Isbn();
			isbn.LoadIsbn(isbnFileIn, filesystemSeparator);
		} 
		
	if (fruizioneInventariFileIn.length() > 0 || categorieDiFruizioneFileIn.length() > 0)
	{
		categorieDiFruizione = new CategorieDiFruizione();
		categorieDiFruizione.LoadInvetaries(fruizioneInventariFileIn, filesystemSeparator);
		if (categorieDiFruizioneFileIn.length() > 0)
			categorieDiFruizione.LoadFruitionCategories(categorieDiFruizioneFileIn);
	} 


	// Stiamo facendo una indicizzazione incrementale?
	if (updateMarcFileIn.length() > 0)
		{
		gestioneUpdate = new GestioneUpdate(filesystemSeparator, sortExecutable);
		// Carichiamo la lista dei bid attuali
		if (!gestioneUpdate.LoadBids(bidFilenameIn))
			return;
		//dataprepIncrementale = true;
		}
		else
		{
		// 
		//dataprepIncrementale = false;
		}

	// Estrai dati per la gestione delle 462 e 463 linkate
	if (dropFilenameOut != null)
	{
		gestione462_463 = new Gestione462_463(biblioteche950, filesystemSeparator, sortExecutable);
		if (gestioneUpdate == null)
		{
			gestione462_463.run(marcFileIn, dropFilenameOut, hasLinefeed); // drop 462e 463 su scarico integrale
		}
		else
			gestione462_463.run(updateMarcFileIn, dropFilenameOut, hasLinefeed); // drop 462e 463 solo sugli aggiornamenti
		
	}

	if (gestioneUpdate == null)
		fromBinaryToTagged(marcFileIn, marcFileOut, bidToDeleteUpdateFileOut, readableMarkFileOut, digitaleFileIn);
	else
	{
		hasLinefeed = hasLinefeedUpdate;
		fromBinaryToTagged(updateMarcFileIn, updateMarcFileOut, bidToDeleteUpdateFileOut, readableMarkFileOut, digitaleFileIn);
	}

	// Concatena nuovo+vecchio
	if (gestioneUpdate != null)
		ConcatAndUpdateMarc(updateMarcFileOut, marcFileOut, bidToDeleteUpdateFileOut, bidFilenameOut);
	

	
	
	} // End transform


	private void ConcatAndUpdateMarc(String marcFileNew, String marcFileOld, String bidToDeleteUpdateFileOut, String bidFilenameOut){ // , String readableMarkFileOut
		InputStream in = null;
		boolean bidToDelete = false;

		// Ordiniamo il file dei bid da cancellare/ordinare
		gestioneUpdate.LoadBidsToDelete(bidToDeleteUpdateFileOut);
		System.out.println("Caricato file ordinato dei bids da cancellare: ");

		// Apri files in scrittura
		try {
			System.out.println("marcFileNew: " + marcFileNew);
			streamOutMrk = new FileOutputStream(marcFileNew, true); // append

			streamOutBid = new FileOutputStream(bidFilenameOut, true); // append
			System.out.println("bidFilenameOut: "	+ bidFilenameOut);
		
		} catch (Exception fnfEx) {
			fnfEx.printStackTrace();
			return;
		}

		
		try {
			in = new FileInputStream(marcFileOld);

			System.out.println("marcFileOld: " + marcFileOld + "\n");
//			MarcWriter writer = new MarcStreamWriter(streamOutMrk);
			MarcWriter writer = new MarcStreamWriter(streamOutMrk, "UTF8");

			// Read the CR/LF (non Standard Unimarc)
//			byte[] CrLf = new byte[2];
			Record record = null;

			MarcReader reader = new MarcStreamReader(in);
isConvertToUtf8 = false;			
			while (reader.hasNext()) {
				try {
					oldMarcReadRecordCounter++;
					Bid = "??";
					record = reader.next();
//					record = reader.nextLfInDati_Arge();
					
					if ((oldMarcReadRecordCounter & 0xFF) == 0xFF)
						System.out.println("Old marc record letti: " + oldMarcReadRecordCounter);


					// Control fields: tags 001 through 009
					List fields = record.getControlFields();
					Iterator i = fields.iterator();
					while (i.hasNext()) {
						ControlField field = (ControlField) i.next();
					
						if (field.getTag().compareTo("001") == 0)
						{
						Bid = field.getData();
						if (gestioneUpdate.isBidToDelete(Bid) == true)
							bidToDelete = true;
						else
							bidToDelete = false;
						}
						break;
					}

					if (bidToDelete == true)
					{
						deletedRecordsCtr++;
						continue; // don't write record
					}
						
					// Write MARC RECORD
					writer.write(record);

					// Write bid of written record in Bid log file
					streamOutBid.write(Bid.getBytes());
					streamOutBid.write(Lf);
					
					oldMarcWrittenRecordCounter++;
					
					
				}
				// catch (MarcException e) { // Exception e
				catch (Exception e) {
					// TODO Auto-generated catch block
					oldMarcRejectedCounter++;
					System.out.println("Record ERRATO: " + oldMarcReadRecordCounter+ " BID=" + Bid + ", PASSO AL PROSSIMO\n");
					e.printStackTrace();
					
				}

				}
			writer.close();
			try {
				streamOutBid.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	} // End ConcatAndUpdateMarc
	

	
	private void fromBinaryToTagged(String marcFileIn, String marcFileOut, String bidToDeleteUpdateFileOut, 
			String readableMarkFileOut, String digitaleFileIn) {
		InputStream in = null;
		BidInDbCtr = 0;


		// System.out.println("new File marcFileOut: " + marcFileOut);
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
				System.out.println("readableMarkFileOut: "	+ readableMarkFileOut);
			streamOut = new FileOutputStream(readableMarkFileOut); 
			}
			streamOutBid = new FileOutputStream(bidFilenameOut); // 
			System.out.println("bidFilenameOut: "	+ bidFilenameOut);

			if (bidToDeleteUpdateFileOut.length() > 0)
			{
				streamBidToDeleteUpdateFileOut = new FileOutputStream(bidToDeleteUpdateFileOut); // 
				System.out.println("bidToDeleteUpdateFileOut: "	+ bidToDeleteUpdateFileOut);
			}
			
		} catch (Exception fnfEx) {
			fnfEx.printStackTrace();
			return;
		}

		try {
			in = new FileInputStream(marcFileIn); // "summerland.mrc"
//			in = new FileInputStream(applicationPath + "/" + marcFileIn); // "summerland.mrc"

			System.out.println("marcFileIn: " + marcFileIn + "\n");
			MarcWriter writer = new MarcStreamWriter(streamOutMrk);

			if (isConvertToUtf8 == true)
				writer.setConverter(new Utf16ToUtf8Converter());
			else
				writer.setConverter(null);

			// Read the CR/LF (non Standard Unimarc)
//			byte[] CrLf = new byte[2];
//			byte[] CrLf = new byte[1];
			byte[] CrLf = new byte[linefeedSize];
			
						
			
			
			Record record = null;
			// writeSampleMarcRecord(streamOut, writer);

//			MarcReader reader = new MarcStreamReader(in);
			MarcReader reader = new MarcStreamReader(in, "8859_1"); // 29/02/08
			
			
			
			while (reader.hasNext()) {
				try {
					recordCounter++;
//if (recordCounter == 30)
//	 break;
					Bid = "??";
					record = reader.next();
if ((recordCounter & 0xFF) == 0xFF)
						System.out.print("Letto record " + recordCounter + "\r");
					Record newRecord = transformMarcRecord(record);
					if (newRecord != null)
						writeMarcRecord(streamOut, writer, newRecord);
						
				}
				// catch (MarcException e) { // Exception e
				catch (Exception e) {
					// TODO Auto-generated catch block
					rejectedCounter++;
					System.out.println("Problemi di lettura: " + recordCounter + " BID=" + Bid + "\n");
					e.printStackTrace();
					// Read to end of line and continue
					if (hasLinefeed) {
						try {
							int readByte;
							while (true) {
								readByte = in.read();
			if (readByte == -1)
				break; // EOF
			
								if (readByte == 0x0A) // LF
									break;
			
								if (readByte == 0x000D) // CR
								{
									readByte = in.read(); // Get LF
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
						int bytesRead = in.read(CrLf);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					recordCounter++;
					// if ((recordCounter & 0xFF) == 0xFF)
					System.out.println("NO CR/LF: " + recordCounter);
					e.printStackTrace();

				}

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			if (streamOut != null)
				streamOut.close();

			if (streamOutMrk != null)
				streamOutMrk.close();
			
			if (streamOutBid != null)
				streamOutBid.close();
			
			if (streamBidToDeleteUpdateFileOut != null)
				streamBidToDeleteUpdateFileOut.close();
			in.close();
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	} // end fromBinaryToTagged

	
	public static void main(String args[]) { //  

		if (args.length < 1) {
			System.out
					.println("Parametri mancanti. Uso: ConvertFromUnimarc fileListName (eg. filenames.txt)");
			System.exit(1);
		}
		String inputFile = args[0];
		System.out.println("inp: " + inputFile);

		ConvertFromUnimarc convert = new ConvertFromUnimarc();
//		convert.baseDir = args[1].substring("-baseDir=".length());
//		convert.classificazioniFileIn = args[2].substring("-classificazioniFileIn=".length());
	

		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(inputFile));
			// Leggiamo le configurazioni di base
			// ----------------------------------
			convert.reportDataPrep = null;
			convert.reportDataPrepOut = null;
			while (true) {
				String s;
				try {
					s = in.readLine();
					if (s == null)
						break;
					else if (Misc.emptyOrCommentedString(s)) //  Misc.emptyString
						continue;
					else {
						//String ar[] = MiscString.estraiCampi(s, "=");
						String ar[];
						//ar = MiscString.estraiCampi(s, charSepArrayEqual, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);
						ar = MiscString.estraiCampiDelimitatiENon(s, delimiters, '"', '"', MiscString.KEEP_DELIMITERS_FALSE, MiscString.KEEP_GROUP_DELIMITERS_FALSE, MiscString.TRIM_FALSE, MiscString.HAS_ESCAPED_CHARACTERS_FALSE, MiscString.KEEP_ESCAPE_FALSE);
						
						
						
						if (ar[0].startsWith("basedir"))
							convert.baseDir = ar[1];
						if (ar[0].startsWith("reportDataPrep"))
							convert.reportDataPrep = ar[1];
						else if (ar[0].startsWith("biblioteche950"))
							convert.biblioteche950FileIn = ar[1];
						else if (ar[0].startsWith("classificazioniFileIn"))
							convert.classificazioniFileIn = ar[1];
						else if (ar[0].startsWith("caratteriSpecialiUnimarcFileIn"))
							convert.caratteriSpecialiUnimarcFileIn = ar[1];
						else if (ar[0].startsWith("filesystemSeparator"))
							convert.filesystemSeparator = ar[1].charAt(0);
						else if (ar[0].startsWith("sortExecutable"))
							convert.sortExecutable = ar[1];
						else if (ar[0].startsWith("linefeedSize"))
							convert.linefeedSize = Integer.parseInt(ar[1]);
						
//						if (ar[0].startsWith("polo"))
//							convert.codicePolo = ar[1];
						
						if (ar[0].startsWith("linkDigitalePreBID"))
							convert.linkDigitalePreBID = ar[1];
						if (ar[0].startsWith("linkDigitalePostBID"))
						{
							if (ar.length > 1)
								convert.linkDigitalePostBID = ar[1];
							// else empty string
						}
												

						else if (ar[0].startsWith("!FineConfigDiBase!"))
							break;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// Inizializziamo l'ambiente di trattamento
			// ----------------------------------------
			try {

				convert.loadCodesTable();
				ConvertMisc.charSetsUsed.clear();
				ConvertMisc.charSetsUsed.add(ConvertMisc.RESERVED_ESABYTE_ENCODING);
				convert.loadClasssificazioni();
				ConvertMisc.charSetsUsed.clear();
				
			} catch (IOException ioe) {
				System.out.println("ERRORE di IO");
				ioe.printStackTrace();
				System.exit(1);
			} catch (SAXException se) {
				se.printStackTrace();
				System.exit(1);
			}

			// Load library 950 codes
			convert.biblioteche950 = new Biblioteche950();
			convert.biblioteche950.load(convert.baseDir + convert.biblioteche950FileIn);
			
			// Load library codes
			convert.biblioteche = new Biblioteche();
			convert.biblioteche.load();
			
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
								|| (Misc.emptyString(s) == true))
							continue;

						System.out.println("\n\nProcessing: " + s);
//						String ar[] = MiscString.estraiCampi(s, " ");
						String ar[] = MiscString.estraiCampi(s, charSepArraySpace, MiscStringTokenizer.RETURN_EMPTY_TOKENS_FALSE);

						recordCounter = 0;
						// righeLette = 0;
						convert.transform(ar);
// System.out.println("Righe lette: " +
// Integer.toString(recordCounter));

						if (convert.reportDataPrep != null)
							convert.reportDataPrepOut = new FileOutputStream(convert.baseDir + convert.reportDataPrep); 

						StringBuffer sb = new StringBuffer();

						
						
						sb.append("\nRapporto dataprep del " + DateUtil.getDate() + " " + DateUtil.getTime());
						sb.append("\n---------------------------------------------");
						sb.append("\n#Elaborazione per: "	+ s);

						sb.append("\n#Record letti: "	+ Integer.toString(recordCounter));
						sb.append("\n#Record errati: "	+ Integer.toString(rejectedCounter));
						sb.append("\n#Dropped records: "	+ Integer.toString(convert.DroppedCtr));
						sb.append("\n#Bids in db: "	+ Integer.toString(convert.BidInDbCtr));
						sb.append("\n#Links al digitale: "	+ Integer.toString(convert.linksAlDigitale));
						sb.append("\n#Links a Syndetics: "	+ Integer.toString(convert.linksASyndetics));
						sb.append("\n#Possseduto (950) biblioteche di polo: "	+ Integer.toString(convert.biblioteche950DiPoloCtr));
						sb.append("\n#Possseduto (950) biblioteche non di polo: "	+ Integer.toString(convert.biblioteche950NonDiPoloCtr));
						sb.append("\n#Inventari con categoria di fruizione: "	+ Integer.toString(convert.InventarioConCategoriaDiFruizioneCtr));
						sb.append("\n#Inventari con categoria di fruizione mancante: "	+ Integer.toString(convert.InventarioSenzaCategoriaDiFruizioneCtr));
						System.out.println(sb.toString());

						if (convert.reportDataPrepOut != null)
							convert.reportDataPrepOut.write(sb.toString().getBytes());
						
						
						if (convert.gestioneUpdate != null)
						{
							sb.setLength(0); // Clear
							sb.append("\n\n#GESTIONE UPDATE ");
							sb.append("#Bids nuovi (buoni o no che siano): "	+ Integer.toString(convert.recordCounter - convert.updateRecordsCtr - convert.deleteRecordsCtr));
							sb.append("#Bids da cancellare: "	+ Integer.toString(convert.deleteRecordsCtr));
							sb.append("#Bids da aggiornare: "	+ Integer.toString(convert.updateRecordsCtr));
							sb.append("#Bids rimossi dal vecchio DB (per cancellazione o aggiornamento): "	+ Integer.toString(convert.deletedRecordsCtr));
							sb.append("#Bids del vecchio marc letti: "	+ Integer.toString(convert.oldMarcReadRecordCounter));
							sb.append("#Bids del vecchio marc scritti: "	+ Integer.toString(convert.oldMarcWrittenRecordCounter));
							sb.append("#Tot Bids in nuovo db: "	+ 
									Integer.toString(convert.BidInDbCtr + convert.oldMarcWrittenRecordCounter) + 
									"\n#\tBIDs nuovi validi[" +convert.BidInDbCtr+"] + (BIDs vecchi["+convert.oldMarcReadRecordCounter +
									"] - BIDs vecchi rimossi per cancellazione (se esistenti) o per aggiornamento [" + 
									convert.deletedRecordsCtr + "] )");
							System.out.println(sb.toString());
							if (convert.reportDataPrepOut != null)
								convert.reportDataPrepOut.write(sb.toString().getBytes());
						}

						if (convert.gestione462_463 != null)
						{
							sb.setLength(0); // Clear
							sb.append("\n\n#GESTIONE 462_463 (Posseduto) ");
							sb.append("#recordsCon950 posseduti: "	+ Integer.toString(convert.recordsCon950));
							sb.append("#recordsConSolo463: "	+ Integer.toString(convert.recordsConSolo463));
							sb.append("#recordsConSolo462: "	+ Integer.toString(convert.recordsConSolo462));
							sb.append("#recordsCon463e462: "	+ Integer.toString(convert.recordsCon463e462));
							sb.append("#recordsMonografiaCon950 posseduti: "	+ Integer.toString(convert.recordsMonografiaCon950));
							sb.append("#recordsMonografiaConSolo463: "	+ Integer.toString(convert.recordsMonografiaConSolo463));
							sb.append("#recordsMonografiaConSolo462: "	+ Integer.toString(convert.recordsMonografiaConSolo462));
							sb.append("#recordsMonografiaCon463e462: "	+ Integer.toString(convert.recordsMonografiaCon463e462));
							sb.append("#Tot 463: "	+ Integer.toString(convert._463Ctr));
							sb.append("#Tot 462: "	+ Integer.toString(convert._462Ctr));
							sb.append("#463 non linkati: "	+ Integer.toString(convert._463NonLinkatoCtr));
							sb.append("#462 non linkati: "	+ Integer.toString(convert._462NonLinkatoCtr));
							System.out.println(sb.toString());
							if (convert.reportDataPrepOut != null)
								convert.reportDataPrepOut.write(sb.toString().getBytes());
						}
						
						
						if (convert.reportDataPrepOut != null)
							convert.reportDataPrepOut.close();
					
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

	private void loadClasssificazioni()  {
	classificazioni = new Classificazioni();
	classificazioni.loadDescrizioni(baseDir+classificazioniFileIn);
	} // End loadCodesTable
	
	
	
	

} // End Convert
