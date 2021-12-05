package data_modelling;

import java.io.*;
import java.util.*;

public class GenerateERD implements IGenerateERD {
	private String databaseName;
	private static Map<String, String> primaryKeys;
	private static Map<String, List<String>> foreignKeys;
	
	public GenerateERD(String databaseName) {
		this.databaseName = databaseName;
	}
	
	public void generateERD() throws Exception {
		//check if database exist
		if(! databaseExist(databaseName)) {
			throw new Exception("Database " + databaseName + " doesn't exist...");
		}
		// get all the tables
		File tables[] = getTables(databaseName);  
		// file to store ERD
		File erdFile = new File("Databases/" + databaseName + "/" + databaseName + "_ERD.txt");
		BufferedWriter bf = new BufferedWriter(new FileWriter(erdFile));
		primaryKeys = new HashMap<String, String>();
		foreignKeys = new HashMap<String, List<String>>();
		
		// loop through all the tables and parse it
		for(File table : tables) {
			if(!(table.getName().contains("_ERD.txt") || table.getName().contains("_dump.sql"))) {
				bf.write(parseTable(table));
			}		
		}
		// call relationship methods
		generateRelationships(primaryKeys, foreignKeys, bf);
		bf.close();
	}
	
	// method to find relationship between tables with cardinalities
	private void generateRelationships(Map<String, String> primaryKeys, Map<String, List<String>> foreignKeys, BufferedWriter bf) throws IOException {
		// iterate through primaryKey map
		for(Map.Entry<String, String> entry : primaryKeys.entrySet()) {
			// get primary key from map
			String primaryKey = entry.getKey();
			// check if primary key is available in foreign key map
			if(foreignKeys.containsKey(primaryKey)) {
				// if primary key is available then get all the associated tables
				List<String> foreignKeyTables = foreignKeys.get(primaryKey);
				// print the relationship between primary key table with foreign key table
				for(String foreignKeyTable : foreignKeyTables) {
					bf.write(entry.getValue() + " - " + primaryKey + " (1) -> " + foreignKeyTable + " - " + primaryKey + " (M) \n");
				}
			}
		}
	}

	// method to parse the table
	private static String parseTable(File table) {
		String text = "";
		// get table name and remove its extension
		String tableName = table.getName();
		tableName = tableName.substring(0, tableName.lastIndexOf('.'));
		text += tableName + "\n";
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(table));
			// read first line
			String columnText = br.readLine();
			
			// list to store name of columns
			List<String> columns = Arrays.asList(columnText.split("/"));
		
			// loop through each column name
			for(String column : columns) {
				// split the column and seperate column name, data type and constrain
				String c[] = column.split(":");
				
				// add column name and data type into text variable
				text += "\t" + c[0] + " " + c[1]; 
				
				// check if this column is primary key 
				if(Arrays.asList(c).contains("pk")) {
					// if primary key then add PK keyword into text
					text += " PK";
					// add table name and primary key into primaryKey map
					primaryKeys.put(c[0], tableName);
				}
				
				// check if this column is foreign key
				if(Arrays.asList(c).contains("fk")) {
					// if foreign key then add FK keyword into text
					text += " FK";
					// if the current column is not available into foreignKey map
					// then add key and create empty list as a value 
					if(!foreignKeys.containsKey(c[0])) {
						foreignKeys.put(c[0], new ArrayList<String>());
					}
					// add table name into foreignKey map 
					foreignKeys.get(c[0]).add(tableName);
				}
				text += "\n";
			}	
			text += "\n";
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}
	
	// method to get all the tables of database
	private static File[] getTables(String databaseName) {
		File database = new File("Databases/" + databaseName);
		File tables[] = database.listFiles();
		return tables;
	}
	
	// method to check if database exist
	private static boolean databaseExist(String databaseName) {
		boolean databaseExist = false;
		File database = new File("Databases/" + databaseName);
		if(database.exists() && database.isDirectory()) {
			databaseExist = true;
		}
		return databaseExist;
	}
}

