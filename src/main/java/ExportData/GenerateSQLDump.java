package ExportData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GenerateSQLDump implements IGenerateSQLDump {
	private String databaseName;
	
	public GenerateSQLDump(String databaseName) {
		this.databaseName = databaseName;
	}
	
	public void generateSQLDump() throws Exception {
		// get all the tables
		File tables[] = getTables(databaseName);  
		// file to store SQL dump
		File sqlDumpFile = new File("Databases/" + databaseName + "/" + databaseName + "_dump.sql");
		BufferedWriter bf = new BufferedWriter(new FileWriter(sqlDumpFile));
		
		// write query to create database in .sql file
		bf.write("CREATE DATABASE " + databaseName + ";\n\n");
		bf.write("USE " + databaseName + ";\n\n");
		
		// loop through all the tables and create it
		for(File table : tables) {
			if(!(table.getName().contains("_ERD.txt") || table.getName().contains("_dump.sql"))) {
				bf.write(createTable(table, tables));
			}
		}
		bf.close();
	}
	
	// method to create table
	private static String createTable(File table, File[] tables) {
		String query = "";
		// get table name and remove its extension
		String tableName = table.getName();
		tableName = tableName.substring(0, tableName.lastIndexOf('.'));
		
		// write query to create table in .sql file
		query += "DROP TABLE IF EXISTS `"+ tableName +"`;\n";
		query += "CREATE TABLE `" + tableName + "` (\n";
		try {
			BufferedReader br = new BufferedReader(new FileReader(table));
			// read first line
			String columnText = br.readLine();
			// get all the columns
			List<String> columns = Arrays.asList(columnText.split("/"));
			String primaryKey = "";
			String foreignKey = "";
			
			// loop through each column 
			for(String column : columns) {
				// split the column into it's name, data type and constrains
				String splitData[] = column.split(":");
				List<String> splitDataList = Arrays.asList(splitData);
				
				// add table column name and data type into query
				query += "\t`" + splitData[0] + "` " + splitData[1];
				
				// add constrains if its exist in column
				if(splitDataList.contains("not null")) {
					query += " NOT NULL";
				}
				if(splitDataList.contains("unique")) {
					query += " UNIQUE";
				}
				query += ",\n ";
				
				// check if column is primary key
				if(splitDataList.contains("primaryKey")) {
					// create primary key syntax
					primaryKey = "\tPRIMARY KEY (" + splitData[0] +")";
				}
				// check if column is foreign key
				if(splitDataList.contains("foreignKey")) {
					// find foreign key table reference
					String tableReference = findTableReference(splitData[0], tables);
					// create foreign key syntax
					foreignKey = "\tFOREIGN KEY (" + splitData[0] +") REFERENCES " + tableReference + "(" + splitData[0] +")";
					primaryKey += ",";
				}
			}
			
			// format the query and add primary key and foreign key syntax in query
			query += primaryKey + "\n";
			if (foreignKey != "") {
				query += foreignKey + "\n";
			}
			query += ");\n\n";
			
			// add values into table
			query += insertValues(tableName, br);
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return query;
	}
		
	// method to find foreign key's table reference
	private static String findTableReference(String columnName, File[] tables) {
		String referenceTableName = "";
		// loop through each table
		for(File table : tables) {
			if(!(table.getName().contains("_ERD.txt") || table.getName().contains("_dump.sql"))) {
				BufferedReader br;
				try {
					// read the first line
					br = new BufferedReader(new FileReader(table));
					// get all columns
					String columns[] = br.readLine().split("/");
					// loop through each column
					for(String column : columns) {
						// check if column name is same as foreign key and contains primaryKey keyword
						if(column.contains(columnName) && column.contains("primaryKey")) {
							// get the name of table
							referenceTableName = table.getName();
							referenceTableName = referenceTableName.substring(0, referenceTableName.lastIndexOf('.'));
						}
					}
					br.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return referenceTableName;
	}
	
	// method to create insert statement to add values in table
	private static String insertValues(String tableName, BufferedReader br) throws IOException {
		String insertQuery = "INSERT INTO `" + tableName + "` VALUES ";
		String data = "";
		boolean flag = true;
		// read all the line
		while((data = br.readLine()) != null) {
			// flag to check if if the first row 
			if (flag) {
				// if it is first row do nor add comma
				insertQuery += "(";
				flag = false;
			}else {
				// otherwise add comma
				insertQuery += ", (";
			}
			// split the data
			String values[] = data.split("/");
			// add each values in insert statement
			for(int i=0; i < values.length; i++) {
				if(i == values.length -1) {
					insertQuery += "'" + values[i] + "')";
				}else {
					insertQuery += "'" + values[i] + "',";
				}
			}
		}
		insertQuery += ";\n\n";
		return insertQuery;
	}
	
	// method to get all the tables of database
	private static File[] getTables(String databaseName) {
		File database = new File("Databases/" + databaseName);
		File tables[] = database.listFiles();
		return tables;
	}
}
