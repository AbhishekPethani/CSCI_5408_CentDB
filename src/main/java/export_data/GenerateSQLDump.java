package export_data;

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
		File tables[] = getTables(databaseName);  
		
		File sqlDumpFile = new File("Databases/" + databaseName + "/" + databaseName + "_dump.sql");
		BufferedWriter bf = new BufferedWriter(new FileWriter(sqlDumpFile));

		for(File table : tables) {
			if(!(table.getName().contains("_ERD.txt") || table.getName().contains("_dump.sql"))) {
				bf.write(createTable(table, tables));
			}
		}
		bf.close();
	}
	
	private static String createTable(File table, File[] tables) {
		String text = "";
		String tableName = table.getName();
		tableName = tableName.substring(0, tableName.lastIndexOf('.'));
		
		text += "DROP TABLE IF EXISTS `"+ tableName +"`;\n";
		text += "CREATE TABLE `" + tableName + "` (\n";
		try {
			BufferedReader br = new BufferedReader(new FileReader(table));
			String columnText = br.readLine();
			
//			List<String> columns = new ArrayList<String>();
//			Pattern pattern = Pattern.compile("<(.*?)>", Pattern.DOTALL);
//			Matcher matcher = pattern.matcher(columnText);
//						
//			while(matcher.find()) {
//				columns.add(matcher.group(1));
//			}
			
			List<String> columns = Arrays.asList(columnText.split("/"));
			String primaryKey = "";
			String foreignKey = "";
			for(String column : columns) {
				String splitData[] = column.split(":");
				// add primary key
				if(splitData.length == 3 && splitData[2].equals("(P)")) {
					primaryKey = "\tPRIMARY KEY (" + splitData[0] +")";
				}
				// add foreign key
				if(splitData.length == 3 && splitData[2].equals("(F)")) {
					String tableReference = findTableReference(splitData[0], tableName, tables);
					foreignKey = "\tFOREIGN KEY (" + splitData[0] +") REFERENCES " + tableReference + "(" + splitData[0] +")";
					primaryKey += ",";
				}
				text += "\t`" + splitData[0] + "` " + splitData[1] + ",\n ";
			}
			text += primaryKey + "\n";
			if (foreignKey != "") {
				text += foreignKey + "\n";
			}
			text += ");\n";
			
			text += insertValues(tableName, br);
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}
	
	private static String findTableReference(String columnName, String tableName, File[] tables) {
		String referenceTableName = "";
		for(File table : tables) {
			if(!(table.getName().contains("_ERD.txt") || table.getName().contains("_dump.sql") || table.getName().equals(tableName))) {
				BufferedReader br;
				try {
					br = new BufferedReader(new FileReader(table));
					String columnText = br.readLine();
					if(columnText.contains(columnName)) {
						String t = table.getName();
						referenceTableName = t.substring(0, t.lastIndexOf('.'));
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

	private static String insertValues(String tableName, BufferedReader br) throws IOException {
		String insertQuery = "INSERT INTO `" + tableName + "` VALUES ";
		String data = "";
		boolean flag = true;
		while((data = br.readLine()) != null) {
			if (flag) {
				insertQuery += "(";
				flag = false;
			}else {
				insertQuery += ", (";
			}
//			Pattern dataPattern = Pattern.compile("\\[(.*?)\\]", Pattern.DOTALL);
//			Matcher dataMatcher = dataPattern.matcher(data);
//			dataMatcher.find();
			String row[] = data.split("/");
			for(int i=0; i < row.length; i++) {
				if(i == row.length -1) {
					insertQuery += "'" + row[i] + "')";
				}else {
					insertQuery += "'" + row[i] + "',";
				}
			}
		}
		insertQuery += ";\n";
		
		return insertQuery;
	}
	
	private static File[] getTables(String databaseName) {
		File database = new File("Databases/" + databaseName);
		File tables[] = database.listFiles();
		return tables;
	}
}
