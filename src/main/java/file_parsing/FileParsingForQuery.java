package file_parsing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class FileParsingForQuery implements FileParsingForQueryInterface {
	
	private String databaseName;
	
	public void setDatabaseName (String databaseName) {
		this.databaseName = databaseName;
	}
	
	public void createDatabase (String databaseName) {
		File file = new File("File/DBDemo/"+databaseName);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	
	public void createTable (String tableName, TreeMap<String, String> columnNameAndDataType) {
		File dbFile = new File("File/DBDemo/" + databaseName);
		if (!dbFile.exists()) {
			dbFile.mkdirs();
		}
		File tableFile = new File("File/DBDemo/" + databaseName + "/" + tableName + ".txt");
		
		try {
			tableFile.createNewFile();
			Set<String> columnName = columnNameAndDataType.keySet();
			String header = "_id:Integer/";
			for (String i : columnName) {
				header += i + ":" + columnNameAndDataType.get(i) + "/";
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(tableFile, true));
			out.write(header + "\r\n");
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void insertIntoTable (TreeMap<String, Object> insertColumnAndValue, String tableName) {
		String tableFileName = "File/DBDemo/" + databaseName + "/" + tableName + ".txt";
		File tableFile = new File(tableFileName);
		TreeMap<String, String> tableColumnMap = getHeader(tableFileName);
		try {
			LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(tableFileName));
			lineNumberReader.skip(Long.MAX_VALUE);
			int id = lineNumberReader.getLineNumber();
			lineNumberReader.close();
			String insertLine = "";
			for (String s : tableColumnMap.keySet()) {
				System.out.println(s);
				if (insertColumnAndValue.containsKey(s))
					insertLine += insertColumnAndValue.get(s).toString() + "/";
				else if (s.equals("_id"))
					insertLine += String.valueOf(id) + "/";
				else
					insertLine += "NULL/";
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(tableFile, true));
			out.write(insertLine + "\r\n");
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateTable (TreeMap<String, Object> updateColumnAndValue, String tableName, TreeMap<String, List<Object>> conditionColumnAndValue) {
		String tableFileName = "File/DBDemo/" + databaseName + "/" + tableName + ".txt";
		File tableFile = new File(tableFileName);
		String header = "";
		TreeMap<String, String> tableColumnMap = getHeader(tableFileName);
		TreeMap<String, String> tableRowMap = new TreeMap<String, String>();
		try {
			tableFile.createNewFile();
			BufferedReader in = new BufferedReader(new FileReader(tableFile));
			String row;
			header = in.readLine();
			while ((row = in.readLine()) != null) {
				tableRowMap.put(row.split("/", 2)[0], row);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//每次取表中的一行
		for (String s : tableRowMap.keySet()) {
			String[] rowValue = tableRowMap.get(s).split("/");
			TreeMap<String, String> columnRow = new TreeMap<String, String>();
			int i = 0;
			for (String column : tableColumnMap.keySet()) {
				columnRow.put(column, rowValue[i]);
				i++;
			}
			Boolean isFit = true;
			//判断该行是否满足所有的条件
			for (String c : conditionColumnAndValue.keySet()) {
				if (conditionColumnAndValue.get(c).get(0).toString().equals("=")) {
					System.out.println("###############################33");
					if (conditionColumnAndValue.get(c).get(1).toString().equals(columnRow.get(c)))
						continue;
					else {
						isFit = false;
						break;
					}
				}
				if (conditionColumnAndValue.get(c).get(0).toString().equals(">")) {
					if (Integer.parseInt(columnRow.get(c)) > (Integer)conditionColumnAndValue.get(c).get(1))
						continue;
					else {
						isFit = false;
						break;
					}
				}
				if (conditionColumnAndValue.get(c).get(0).toString().equals("<")) {
					if (Integer.parseInt(columnRow.get(c)) < (Integer)conditionColumnAndValue.get(c).get(1))
						continue;
					else {
						isFit = false;
						break;
					}
				}
			}
			if (isFit) {
				for (String update : updateColumnAndValue.keySet()) {
					columnRow.put(update, updateColumnAndValue.get(update).toString());
				}
				String newRow = "";
				for (String column : columnRow.keySet()) {
					newRow += columnRow.get(column) + "/";
				}
				tableRowMap.put(s, newRow);
			}
		}
		//所有符合条件的行都更新完后，将表重新写入文件
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(tableFile));
			out.write(header + "\r\n");
			for (String s : tableRowMap.keySet()) {
				out.write(tableRowMap.get(s) + "\r\n");
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private TreeMap<String, String> getHeader (String tableFileName) {
		File tableFile = new File(tableFileName);
		TreeMap<String, String> tableColumnMap = new TreeMap<String, String>();
		try {
			tableFile.createNewFile();
			BufferedReader in = new BufferedReader(new FileReader(tableFile));
			String header = in.readLine();
			String[] columnKeyType = header.split("/");
			for (String s : columnKeyType) {
				tableColumnMap.put(s.split(":")[0], s.split(":")[1]);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tableColumnMap;
	}
	
}
