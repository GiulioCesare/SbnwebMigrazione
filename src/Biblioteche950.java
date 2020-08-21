import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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

public class Biblioteche950 {
	private HashMap bibMap = null; 		

	
	public Biblioteche950()
	{
		bibMap = new HashMap();
	}
	
	public void load(String biblioteche950FileIn)
	{
		
		int bibCounter = 0;
		System.out.println("\nStiamo caricando il file delle biblioteche per la 950: " + biblioteche950FileIn); 
		
		try {
		BufferedReader in = new BufferedReader(new FileReader(biblioteche950FileIn));
		while (true) {
			String s;
			try {
				s = in.readLine();
				if (s == null)
					break;
				else {
					if (s.length() == 0 || s.charAt(0) == '#')
						continue;
					bibMap.put(s.substring(0,3), "Biblioteca");
					bibCounter++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
			}
		System.out.println("\nBiblioteche per 950: " + Integer.toString(bibMap.size()));
		
			
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();

		// Convert from binary format to readable format
	}
	}

	public HashMap getBibMap() {
		return bibMap;
	}

	public void setBibMap(HashMap bibMap) {
		this.bibMap = bibMap;
	}
	
	public String getBibCode(String bibDescription)
	{
		return (String)bibMap.get(bibDescription);
	}
	
	public boolean containsBiblio(String nomeBiblioteca)
	{
		return bibMap.containsKey(nomeBiblioteca);
	}
	
}
