import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import oracle.xml.parser.v2.DOMParser;
import oracle.xml.parser.v2.XMLDocument;
import oracle.xml.parser.v2.XMLElement;
import oracle.xml.parser.v2.XMLNodeList;

import org.xml.sax.SAXException;

public class ExtractCharacterUpperLowerChrSet {
	String applicationPath;
	static int recordCounter = 0;
//	FileOutputStream streamOut; //
	public static final int MAX_BYTES_PER_UTF8_CHARACTER = 8;

	public ExtractCharacterUpperLowerChrSet() {
		applicationPath = System.getProperty("user.dir");
		System.out.println("Application PATH: " + applicationPath);
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		ExtractCharacterUpperLowerChrSet extractCharacterUpperLowerChrSet = new ExtractCharacterUpperLowerChrSet();
		try {
			extractCharacterUpperLowerChrSet.extract();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("FINE");

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

	private static byte[] trim(byte[] ba, int len) {
		if (len == ba.length)
			return ba;
		byte[] tba = new byte[len];
		System.arraycopy(ba, 0, tba, 0, len);
		return tba;
	}

	
	
	private void extract() throws IOException, SAXException {
		String UPPER="", lower="", base="", name="";
		StringBuffer lowerSet=new StringBuffer();
		StringBuffer upperSet=new StringBuffer();
		StringBuffer baseSet=new StringBuffer();
		StringBuffer baseMap=new StringBuffer();
		
		DOMParser parser = new DOMParser();
		String fileName = applicationPath + File.separator + "conf"
				+ File.separator + "CharacterSet.xml";

		String fileNameOut = applicationPath + File.separator + "conf"
		+ File.separator + "CharacterSetUpperLower.htm";
//		String s="", s0="", s1="", s2="", s3="", s4="", s5=""; 

		//FileReader fr = new FileReader(fileName);
		Reader in = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileNameOut), "UTF8"));

		String header = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\""+
		"\"http://www.w3.org/TR/html4/loose.dtd\">"+
		"<html><title>OPAC UPPER/lower character set</title>"+
		"<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>";
				
		String body = "<body><table width=\"10%\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
		String trTd = "  <tr><td >";
		String endTdTd = "</td><td >";
		String endTdTr = "</td></tr>";
		String endTable = "</table>";
		String endBody = "</body></html>";
		out.write(header);		
		out.write(body);		
		
		parser.parse(in);
		XMLDocument doc = parser.getDocument();
		XMLNodeList nodeList = (XMLNodeList) doc.getElementsByTagName("CARATTERE");

		for (int i = 0; i < nodeList.getLength(); i++) {
			XMLElement currentElement = (XMLElement) nodeList.item(i);
			UPPER = currentElement.getAttribute("UPPER");
			lower = currentElement.getAttribute("lower");
			base = currentElement.getAttribute("base");
			name = currentElement.getAttribute("NAME");
			lowerSet.append(lower);
			upperSet.append(UPPER);
			
			if (base.length() > 1)
			{
				baseMap.append("\n<BR>" + "map (" + lower + ") (" + base +")"); 
				baseMap.append("\n<BR>" + "map (" + UPPER + ") (" + base +")");
			}
			else
			{
				baseMap.append("\n<BR>" + "map (" + lower + ") " + base); 
				baseMap.append("\n<BR>" + "map (" + UPPER + ") " + base);
			}
			
			baseSet.append(base);
			System.out.print(UPPER);
			System.out.print(lower);
			System.out.print(base);

			out.write(trTd);		
			out.write(UPPER);		
			out.write(endTdTd);		
			out.write(lower);		
			out.write(endTdTd);		
			out.write(base);		
			out.write(endTdTd);		
			out.write(name);		
			out.write(endTdTr);		
		}
		out.write(endTable);
		out.write("<BR><PRE>");
		out.write(lowerSet.toString());
		out.write("<BR>");
		out.write(upperSet.toString());
		out.write("<BR>");
		out.write(baseSet.toString());
		out.write("<BR></PRE>");

		out.write(baseMap.toString());
		out.write("<BR>");

		
		out.write(endBody);		


		in.close();
		out.close();
	} // End extract


}

