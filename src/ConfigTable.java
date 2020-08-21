import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import it.finsiel.misc.*;

import org.xml.sax.SAXException;

class ConfigTable {
	String procedureName; // tableName
	ArrayList<ConfigQuery> configQueries = new ArrayList<ConfigQuery>();
	
	public String getProcedureName() {
		return procedureName;
	}
	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}
	public ArrayList<ConfigQuery> getConfigQueries() {
		return configQueries;
	}
	public void setConfigQueries(ArrayList<ConfigQuery> configQueries) {
		this.configQueries = configQueries;
	}
	
	public void addQuery(ConfigQuery configQuery)
	{
		configQueries.add(configQuery);
	}
	
	
	public void dump()
	{
		System.out.println("===================");
		System.out.println("procedureName="+procedureName);
		for (ConfigQuery configQuery:configQueries)
		{
			System.out.println("---------------");
			System.out.println("\tsql="+configQuery.getSql());
			System.out.println("\ttypes input data="+configQuery.getTypeList());
			System.out.println("\tnextVal=" + (configQuery.getNextVal()));
		}
	}
	
	
	}

