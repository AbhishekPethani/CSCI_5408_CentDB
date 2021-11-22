package data_modelling;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenerateERD {
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
			// System.out.println(parseTable(table));
			bf.write(parseTable(table));
		}
		bf.close();
	}
	
	public static String parseTable(File table) {
		String text = "";
		String tableName = table.getName();
		text += tableName + "\n";
		try {
			Scanner sc = new Scanner(table);
			String columnText = sc.nextLine();
			
			List<String> columns = new ArrayList<String>();
			Pattern p = Pattern.compile("<(.*?)>", Pattern.DOTALL);
			Matcher m = p.matcher(columnText);
						
			while(m.find()) {
				columns.add(m.group(1));
			}
			
			for(String column : columns) {
				text += "\t" + column + "\n";
			}
			
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return text;
	}
	
	public static File[] getTables(String databaseName) {
		File database = new File("Databases/" + databaseName);
		File tables[] = database.listFiles();
		return tables;
	}
	
	public static boolean databaseExist(String databaseName) {
		boolean databaseExist = false;
		File database = new File("Databases/" + databaseName);
		if(database.exists() && database.isDirectory()) {
			databaseExist = true;
		}
		return databaseExist;
	}
}
