package file_parsing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 * @projectName: centdb_g16
 * @package: file_parsing
 * @className: FileParsingForQuery
 * @description: file parsing for query
 * @author: zhaoling 
 * @createDate: 2021-11-24  
 * @updateUser: zhaoling 
 * @updateDate: 2021-11-25
 * @updateRemark: add method about delete, select, update query
 * @version: v1.1
 */

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
	
	public void updateDataTable (TreeMap<String, Object> updateColumnAndValue, String tableName, TreeMap<String, List<Object>> conditionColumnAndValue) {
		String tableFileName = "File/DBDemo/" + databaseName + "/" + tableName + ".txt";
		File tableFile = new File(tableFileName);
		String header = getHeaderString(tableFileName);
		TreeMap<String, String> tableColumnMap = getHeader(tableFileName);
		TreeMap<String, String> tableRowMap = getFileContent(tableFileName);
		//每次取表中的一行
		for (String s : tableRowMap.keySet()) {
			TreeMap<String, String> columnRow = transferStringToMap(tableRowMap.get(s), tableColumnMap);
			Boolean isFit = checkIsSatisfyCondition(conditionColumnAndValue, columnRow);
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
	
	public void deleteDataInTable (String tableName, TreeMap<String, List<Object>> conditionColumnAndValue) {
		String tableFileName = "File/DBDemo/" + databaseName + "/" + tableName + ".txt";
		File tableFile = new File(tableFileName);
		TreeMap<String, String> tableColumnMap = getHeader(tableFileName);
		TreeMap<String, String> tableRowMap = getFileContent(tableFileName);
		String header = getHeaderString(tableFileName);
		//每次取表中的一行
		for (String s : tableRowMap.keySet()) {
			TreeMap<String, String> columnRow = transferStringToMap(tableRowMap.get(s), tableColumnMap);
			Boolean isFit = checkIsSatisfyCondition(conditionColumnAndValue, columnRow);
			if (isFit) {
				tableRowMap.remove(s);
			}
		}
		//所有符合条件的行都更新完后，将表重新写入文件
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(tableFile));
			out.write(header + "\r\n");
			int i = 1;
			for (String s : tableRowMap.keySet()) {
				String newline = String.valueOf(i) + "/" + tableRowMap.get(s).split("/", 2)[1];
				out.write(newline + "\r\n");
				i++;
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteTable (String tableName) {
		String tableFileName = "File/DBDemo/" + databaseName + "/" + tableName + ".txt";
		File tableFile = new File(tableFileName);
		tableFile.delete();
	}
	
	public void deleteDatabase (String databaseName) {
		String dirName = "File/DBDemo/" + databaseName;
		File databaseDir = new File(dirName);
		File[] listFiles = databaseDir.listFiles();
        for (File file : listFiles) {
            file.delete();
        }
        databaseDir.delete();
        this.databaseName = "";
	}
	
	public List<TreeMap<String, String>> selectFromTable (List<String> selectColumn, String tableName, TreeMap<String, List<Object>> conditionColumnAndValue) {
		List<TreeMap<String, String>> resultRows = new ArrayList<TreeMap<String, String>>();
		String tableFileName = "File/DBDemo/" + databaseName + "/" + tableName + ".txt";
		TreeMap<String, String> tableColumnMap = getHeader(tableFileName);
		TreeMap<String, String> tableRowMap = getFileContent(tableFileName);
		//每次取表中的一行
		for (String s : tableRowMap.keySet()) {
			TreeMap<String, String> columnRow = transferStringToMap(tableRowMap.get(s), tableColumnMap);
			Boolean isFit = checkIsSatisfyCondition(conditionColumnAndValue, columnRow);
			if (isFit) {
				Object[] temp = columnRow.keySet().toArray();
				for (int n = 0; n < temp.length; n++) {
					if (!selectColumn.contains((String)temp[n]))
						columnRow.remove((String)temp[n]);
				}
				resultRows.add(columnRow);
			}
		}
		return resultRows;
	}
	
	private TreeMap<String, String> getFileContent (String tableFileName) {
		File tableFile = new File(tableFileName);
		TreeMap<String, String> tableRowMap = new TreeMap<String, String>();
		String header = "";
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
		return tableRowMap;
	}
	
	private TreeMap<String, String> transferStringToMap (String row, TreeMap<String, String> header) {
		TreeMap<String, String> columnRow = new TreeMap<String, String>();
		String[] rowValue = row.split("/");
		int i = 0;
		for (String column : header.keySet()) {
			columnRow.put(column, rowValue[i]);
			i++;
		}
		return columnRow;
	}
	
	private Boolean checkIsSatisfyCondition (TreeMap<String, List<Object>> conditionColumnAndValue, TreeMap<String, String> columnRow) {
		Boolean isFit = true;
		for (String c : conditionColumnAndValue.keySet()) {
			if (conditionColumnAndValue.get(c).get(0).toString().equals("=")) {
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
		return isFit;
	}
	
	private String getHeaderString (String tableFileName) {
		File tableFile = new File(tableFileName);
		String headerString = "";
		try {
			tableFile.createNewFile();
			BufferedReader in = new BufferedReader(new FileReader(tableFile));
			headerString = in.readLine();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return headerString;
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
