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

public class Biblioteche {
	private HashMap bibMap = null; 		

	
	public Biblioteche()
	{
		
	}
	
	public void load()
	{
		bibMap = new HashMap();
		bibMap.put("IEI 02", "RM0255");
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
	
}
