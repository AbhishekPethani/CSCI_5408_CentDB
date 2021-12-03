package data_modelling;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GenerateERD implements IGenerateERD {
	private String databaseName;
	
	public GenerateERD(String databaseName) {
		this.databaseName = databaseName;
	}
	
	public void generateERD() throws Exception {
		if(! databaseExist(databaseName)) {
			throw new Exception("Database " + databaseName + " doesn't exist...");
		}
		File tables[] = getTables(databaseName);  
		File erdFile = new File("Databases/" + databaseName + "/" + databaseName + "_ERD.txt");
		BufferedWriter bf = new BufferedWriter(new FileWriter(erdFile));
		for(File table : tables) {
			if(!(table.getName().contains("_ERD.txt") || table.getName().contains("_dump.sql"))) {
				bf.write(parseTable(table));
			}		
		}
		bf.close();
	}
	
	private static String parseTable(File table) {
		String text = "";
		String tableName = table.getName();
		tableName = tableName.substring(0, tableName.lastIndexOf('.'));
		text += tableName + "\n";
		try {
			BufferedReader br = new BufferedReader(new FileReader(table));
			String columnText = br.readLine();
			
			//List<String> columns = new ArrayList<String>();
//			Pattern pattern = Pattern.compile("", Pattern.DOTALL);
//			Matcher matcher = pattern.matcher(columnText);
//						
//			while(matcher.find()) {
//				columns.add(matcher.group(1));
//			}
			List<String> columns = Arrays.asList(columnText.split("/"));
			
			for(String column : columns) {
				text += "\t" + column + "\n";
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return text;
	}
	
	private static File[] getTables(String databaseName) {
		File database = new File("Databases/" + databaseName);
		File tables[] = database.listFiles();
		return tables;
	}
	
	private static boolean databaseExist(String databaseName) {
		boolean databaseExist = false;
		File database = new File("Databases/" + databaseName);
		if(database.exists() && database.isDirectory()) {
			databaseExist = true;
		}
		return databaseExist;
	}
}
