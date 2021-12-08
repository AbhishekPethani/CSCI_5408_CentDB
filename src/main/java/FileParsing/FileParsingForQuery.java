package FileParsing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @projectName: centdb_g16
 * @package: file_parsing
 * @className: FileParsingForQuery
 * @description: file parsing for query
 * @author: zhaoling 
 * @createDate: 2021-11-24  
 * @updateUser: zhaoling 
 * @updateDate: 2021-12-5
 * @updateRemark: modify return type.
 * @version: v1.3
 */

public class FileParsingForQuery implements FileParsingForQueryInterface {
	
	private final String path = "Databases/";
	private String databaseName;
	private FileOperation fileOperation = new FileOperation();
	
	public void setDatabaseName (String databaseName) {
		this.databaseName = databaseName;
	}
	
	public Boolean createDatabase (String databaseName) {
		File file = new File(path + databaseName);
		if (!file.exists()) {
			file.mkdirs();
		}
		fileOperation.createMetaData();
		String message = databaseName + "database created";
		fileOperation.reportToLog(databaseName, message);
		return true;
	}
	
	public Boolean createTable (String tableName, List<Map<String, Object>> columnInfos) {
		File dbFile = new File(path + databaseName);
		if (!dbFile.exists()) {
			dbFile.mkdirs();
		}
		File tableFile = new File(path + databaseName + "/" + tableName + ".tb");
		try {
			tableFile.createNewFile();
			String header = "_id:Integer/";
			TreeMap<String, String> columnNames = new TreeMap<String, String>();
			for (Map<String, Object> columnInfo : columnInfos) {
				columnNames.put((String)columnInfo.get("columnName"), "columnName");
			}
			for (String columnName : columnNames.keySet()) {
				for (Map<String, Object> columnInfo : columnInfos) {
					if (!columnName.equals((String)columnInfo.get("columnName")))
						continue;
					header += (String)columnInfo.get("columnName");
					header += ":" + (String)columnInfo.get("dataType");
					if ((Boolean)columnInfo.get("primaryKey"))
						header += ":primaryKey";
					if ((Boolean)columnInfo.get("foreignKey"))
						header += ":foreignKey";
					if ((Boolean)columnInfo.get("not null"))
						header += ":not null";
					if ((Boolean)columnInfo.get("unique"))
						header += ":unique";
					header += "/";
					break;
				}
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(tableFile, true));
			out.write(header + "\r\n");
			out.flush();
			out.close();
			String message = tableName + "table created";
			fileOperation.reportToLog(databaseName, message);
			fileOperation.createMetaData();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Boolean insertIntoTable (TreeMap<String, Object> insertColumnAndValue, String tableName) {
		String tableFileName = path + databaseName + "/" + tableName + ".tb";
		File tableFile = new File(tableFileName);
		TreeMap<String, String> tableColumnMap = fileOperation.getTableHeader(tableFileName);
		try {
			LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(tableFileName));
			lineNumberReader.skip(Long.MAX_VALUE);
			int id = lineNumberReader.getLineNumber();
			lineNumberReader.close();
			String insertLine = "";
			for (String s : tableColumnMap.keySet()) {
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
			String message = "data inserted into " + tableName;
			fileOperation.reportToLog(databaseName, message);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Boolean updateDataTable (TreeMap<String, Object> updateColumnAndValue, String tableName, TreeMap<String, List<Object>> conditionColumnAndValue) {
		String tableFileName = path + databaseName + "/" + tableName + ".tb";
		File tableFile = new File(tableFileName);
		String header = fileOperation.getHeaderString(tableFileName);
		TreeMap<String, String> tableColumnMap = fileOperation.getTableHeader(tableFileName);
		TreeMap<String, String> tableRowMap = fileOperation.getTableContent(tableFileName);
		//每次取表中的一行
		for (String s : tableRowMap.keySet()) {
			TreeMap<String, String> columnRow = fileOperation.transferRowStringToRowMap(tableRowMap.get(s), tableColumnMap);
			Boolean isFit = fileOperation.checkIsSatisfyCondition(conditionColumnAndValue, columnRow);
			if (isFit) {
				for (String update : updateColumnAndValue.keySet()) {
					fileOperation.reportToEventLog(databaseName, tableName, update, Integer.valueOf(s), columnRow.get(update), updateColumnAndValue.get(update).toString());
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
			String message = "data updated into " + tableName;
			fileOperation.reportToLog(databaseName, message);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public Boolean deleteDataInTable (String tableName, TreeMap<String, List<Object>> conditionColumnAndValue) {
		String tableFileName = path + databaseName + "/" + tableName + ".tb";
		File tableFile = new File(tableFileName);
		TreeMap<String, String> tableColumnMap = fileOperation.getTableHeader(tableFileName);
		TreeMap<String, String> tableRowMap = fileOperation.getTableContent(tableFileName);
		String header = fileOperation.getHeaderString(tableFileName);
		//每次取表中的一行
		for (String s : tableRowMap.keySet()) {
			TreeMap<String, String> columnRow = fileOperation.transferRowStringToRowMap(tableRowMap.get(s), tableColumnMap);
			Boolean isFit = fileOperation.checkIsSatisfyCondition(conditionColumnAndValue, columnRow);
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
			String message = "data deleted into " + tableName;
			fileOperation.reportToLog(databaseName, message);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Boolean deleteTable (String tableName) {
		String tableFileName = path + databaseName + "/" + tableName + ".tb";
		File tableFile = new File(tableFileName);
		String message = tableName + "table deleted from " + databaseName;
		fileOperation.reportToLog(databaseName, message);
		fileOperation.createMetaData();
		return tableFile.delete();
	}
	
	public Boolean deleteDatabase (String databaseName) {
		String dirName = path + databaseName;
		File databaseDir = new File(dirName);
		File[] listFiles = databaseDir.listFiles();
        for (File file : listFiles) {
            if (!file.delete())
            	return false;
        }
		String message = databaseName + "database deleted";
		fileOperation.reportToLog(databaseName, message);
        this.databaseName = "";
		fileOperation.createMetaData();
        return databaseDir.delete();
	}
	
	public List<TreeMap<String, String>> selectFromTable (List<String> selectColumn, String tableName, TreeMap<String, List<Object>> conditionColumnAndValue) {
		List<TreeMap<String, String>> resultRows = new ArrayList<TreeMap<String, String>>();
		String tableFileName = path + databaseName + "/" + tableName + ".tb";
		TreeMap<String, String> tableColumnMap = fileOperation.getTableHeader(tableFileName);
		TreeMap<String, String> tableRowMap = fileOperation.getTableContent(tableFileName);
		//每次取表中的一行
		for (String s : tableRowMap.keySet()) {
			TreeMap<String, String> columnRow = fileOperation.transferRowStringToRowMap(tableRowMap.get(s), tableColumnMap);
			Boolean isFit = fileOperation.checkIsSatisfyCondition(conditionColumnAndValue, columnRow);
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
	
}
