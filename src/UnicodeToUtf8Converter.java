import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import oracle.xml.parser.v2.DOMParser;
import oracle.xml.parser.v2.XMLDocument;
import oracle.xml.parser.v2.XMLElement;
import oracle.xml.parser.v2.XMLNodeList;

import org.xml.sax.SAXException;
import org.marc4j.converter.*;
//import ConvertFromUnimarc.CharMap;

public class UnicodeToUtf8Converter {
	class CharMap {
		String codiceUtf8;
		String glyphUtf8;
		String codiceUnicode;

		CharMap(String codiceUtf8, 
				String codiceUnicode) 
				{
			this.codiceUtf8 = new String(codiceUtf8);
			this.codiceUnicode = new String(codiceUnicode);
		}

		public String getCodiceUnicode() {
			return codiceUnicode;
		}

		public void setCodiceUnicode(String codiceUnicode) {
			this.codiceUnicode = codiceUnicode;
		}

		public String getCodiceUtf8() {
			return codiceUtf8;
		}

		public void setCodiceUtf8(String codiceUtf8) {
			this.codiceUtf8 = codiceUtf8;
		}

		public String getGlyphUtf8() {
			return glyphUtf8;
		}

		public void setGlyphUtf8(String glyphUtf8) {
			this.glyphUtf8 = glyphUtf8;
		}
	}

	Hashtable charTable;
	String applicationPath;
	static int recordCounter = 0;
	// static int righeLette = 0;
	java.util.Vector vecBfrFiles; // Buffer Reader Files
	FileOutputStream streamOut, streamOutMrk; // declare a file output object

	boolean hasLinefeed = false;

	public static final int MAX_BYTES_PER_UTF8_CHARACTER = 8;




	private static byte[] trim(byte[] ba, int len) {
		if (len == ba.length)
			return ba;
		byte[] tba = new byte[len];
		System.arraycopy(ba, 0, tba, 0, len);
		return tba;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UnicodeToUtf8Converter unicodeToUtf8Converter = new UnicodeToUtf8Converter();
		try {
			unicodeToUtf8Converter.loadCodesTable();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("FINE");
	}

	public UnicodeToUtf8Converter() {
		applicationPath = System.getProperty("user.dir");
		System.out.println("Application PATH: " + applicationPath);
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

	
	private void loadCodesTable() throws IOException, SAXException {
		FileOutputStream streamOut;
		StringBuffer sb = new StringBuffer();
		byte ar[] = null;
		
		DOMParser parser = new DOMParser();
		// String fileName = applicationPath +File.separator + "src"
		// +File.separator + "CaratteriSpeciali.xml";
		String fileName = applicationPath + File.separator + "conf"
				+ File.separator + "UnicodeCharacterNameIndex.xml";

		String fileNameOut = applicationPath + File.separator + "conf"
		+ File.separator + "Utf8CharacterNameIndex.xml";
		streamOut = new FileOutputStream(fileNameOut); 
		String s="", s0="", s1="", s2="", s3="", s4="", s5=""; 

		
		String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<UNICODE>\n<TITLE>Unicode Character Name Index</TITLE>\n<CARATTERI>";
		String footer = "</CARATTERI>\n</UNICODE>";
	
		ar = myGetBytesUtf8(header);
		streamOut.write(ar, 0, ar.length);
		
		
		FileReader fr = new FileReader(fileName);
		parser.parse(fr);
		XMLDocument doc = parser.getDocument();
		XMLNodeList nodeList = (XMLNodeList) doc.getElementsByTagName("CARATTERE");

		for (int i = 0; i < nodeList.getLength(); i++) {
			XMLElement currentElement = (XMLElement) nodeList.item(i);
			String codiceUnicode = currentElement.getAttribute("UNICODE");
			String nome = currentElement.getAttribute("NOME");

			// Stampa dei caratteri in UTF8 per costruire file dei caratteri speciali
			if (codiceUnicode.length() == 0)
				codiceUnicode = "0020";	
		
			char Carattere;
			Integer intChar;
		//	prova = Integer.decode("0x00C0");
			intChar = Integer.decode("0x" + codiceUnicode);
			Carattere = (char) intChar.intValue();
			s0 = ""+Carattere;
		//	byte arUtf8[] = myGetBytesUtf8(s);
			byte arUtf8[] = myGetBytesUtf8(s0);
			
			s0 = "<CARATTERE UNICODE=\"" + codiceUnicode + "\"";
			s1 = " NOME=\"" + nome + "\"";
			s2 = " GLYPH=\""; 
			//System.out.write(arUtf8, 0, arUtf8.length);
			s3 = "\""; 
			s4 = " UTF8=\"";
			
//			String s5 = new String(arUtf8, 0, arUtf8.length, "UTF8");
//			streamOut.write(arUtf8, 0, arUtf8.length);
			sb.setLength(0);
			for (int j=0; j < arUtf8.length; j++)
			{
			int utf = arUtf8[j];
			
			s = Integer.toHexString(arUtf8[j]);
			if (s.length() > 6)
				s = s.substring(6);
			sb.append(s + " ");
			}
			s5 = sb.toString() + "\"";
				
			
			s = s0+s4+s5+s2;
			System.out.print(s);
			
			if (s5.compareTo("6 \"") == 0 || 
				s5.compareTo("15 \"") == 0  ||
				s5.compareTo("26 \"") == 0 || // "&amp;"
				s5.compareTo("0 \"") == 0 ||  
				s5.compareTo("8 \"") == 0 || 
				s5.compareTo("7 \"") == 0 || 
				s5.compareTo("17 \"") == 0 ||
				s5.compareTo("10 \"") == 0 ||
				s5.compareTo("11 \"") == 0 ||
				s5.compareTo("19 \"") == 0 ||
				s5.compareTo("3 \"") == 0 ||
				s5.compareTo("4 \"") == 0 ||
				s5.compareTo("5 \"") == 0 ||
				s5.compareTo("1b \"") == 0 ||
				s5.compareTo("c \"") == 0 ||
				s5.compareTo("1c \"") == 0 ||
				s5.compareTo("1d \"") == 0 ||
				s5.compareTo("1 \"") == 0 ||
				s5.compareTo("16 \"") == 0 ||
				s5.compareTo("1f \"") == 0 ||
				s5.compareTo("1e \"") == 0 ||
				s5.compareTo("a \"") == 0 ||
				s5.compareTo("3c \"") == 0 ||
				s5.compareTo("b \"") == 0 ||
				s5.compareTo("22 \"") == 0 ||
				s5.compareTo("ef bf be \"") == 0 ||
				s5.compareTo("ef bf bf \"") == 0 ||
				s5.compareTo("f \"") == 0 ||
				s5.compareTo("e \"") == 0 ||
				s5.compareTo("2 \"") == 0 ||
				s5.compareTo("1a \"") == 0 ||
//				s5.compareTo(" \"") == 0 ||
//				s5.compareTo(" \"") == 0 ||
//				s5.compareTo(" \"") == 0 ||
//				s5.compareTo(" \"") == 0 ||
//				s5.compareTo(" \"") == 0 ||
//				s5.compareTo(" \"") == 0 ||
//				s5.compareTo(" \"") == 0 ||
//				s5.compareTo(" \"") == 0 ||
				s5.compareTo("18 \"") == 0 
				
				)
arUtf8 = myGetBytesUtf8(s5.substring(0, s5.length()-1));


			
			ar = myGetBytesUtf8(s);
			streamOut.write(ar, 0, ar.length);
			System.out.write(arUtf8, 0, arUtf8.length);
			streamOut.write(arUtf8, 0, arUtf8.length);

			s = s3+s1+"></CARATTERE>\n";
			System.out.print(s);

			ar = myGetBytesUtf8(s);
			streamOut.write(ar, 0, ar.length);
		}

		
		ar = myGetBytesUtf8(footer);
		streamOut.write(ar, 0, ar.length);
		
		
		fr.close();
		streamOut.close();
	} // End loadCodesTable

	
	
	
	
	
	
	
	
	
}
