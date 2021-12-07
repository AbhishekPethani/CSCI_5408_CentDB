package FileParsing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import log_management.EventLogsImpl;
import log_management.GeneralLogsImpl;
import log_management.constant.Event;
import log_management.model.EventLogsModel;
import log_management.model.GeneralLogsModel;

/**
 * @projectName: centdb_g16
 * @package: FileParsing
 * @className: FileOperation
 * @description: some basic operation to file
 * @author: zhaoling 
 * @createDate: 2021-12-3  
 * @updateUser: zhaoling 
 * @updateDate: 2021-12-6
 * @updateRemark: add create Metadata, get some info .
 * @version: v1.2
 */
public class FileOperation implements FileOperationInterface {

	public TreeMap<String, String> getTableHeader (String tableFileName) {
		File tableFile = new File(tableFileName);
		TreeMap<String, String> tableColumnMap = new TreeMap<String, String>();
		try {
			tableFile.createNewFile();
			BufferedReader in = new BufferedReader(new FileReader(tableFile));
			String header = in.readLine();
			String[] columnKeyType = header.split("/");
			for (String s : columnKeyType) {
				tableColumnMap.put(s.split(":", 2)[0], s.split(":", 2)[1]);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tableColumnMap;
	}
	
	public TreeMap<String, String> getTableContent (String tableFileName) {
		File tableFile = new File(tableFileName);
		TreeMap<String, String> tableRowMap = new TreeMap<String, String>();
		try {
			tableFile.createNewFile();
			BufferedReader in = new BufferedReader(new FileReader(tableFile));
			String row;
			in.readLine();
			while ((row = in.readLine()) != null) {
				tableRowMap.put(row.split("/", 2)[0], row);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tableRowMap;
	}
	
	public TreeMap<String, String> transferRowStringToRowMap (String row, TreeMap<String, String> header) {
		TreeMap<String, String> columnRow = new TreeMap<String, String>();
		String[] rowValue = row.split("/");
		int i = 0;
		for (String column : header.keySet()) {
			columnRow.put(column, rowValue[i]);
			i++;
		}
		return columnRow;
	}
	
	public Boolean checkIsSatisfyCondition (TreeMap<String, List<Object>> conditionColumnAndValue, TreeMap<String, String> columnRow) {
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
			if (conditionColumnAndValue.get(c).get(0).toString().equals("!=")) {
				if (!conditionColumnAndValue.get(c).get(1).toString().equals(columnRow.get(c)))
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
			if (conditionColumnAndValue.get(c).get(0).toString().equals("<=")) {
				if (Integer.parseInt(columnRow.get(c)) <= (Integer)conditionColumnAndValue.get(c).get(1))
					continue;
				else {
					isFit = false;
					break;
				}
			}
			if (conditionColumnAndValue.get(c).get(0).toString().equals(">=")) {
				if (Integer.parseInt(columnRow.get(c)) >= (Integer)conditionColumnAndValue.get(c).get(1))
					continue;
				else {
					isFit = false;
					break;
				}
			}
		}
		return isFit;
	}
	
	public String getHeaderString (String tableFileName) {
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
	
	public void getDatabaseInfo (String databaseName, int tableCount, Long recordCountTotal, List<Map<String, Long>> tables) {
		String dirName = "File/DBDemo/" + databaseName;
		File databaseDir = new File(dirName);
		File[] listFiles = databaseDir.listFiles();
		tableCount = 0;
		recordCountTotal = 0L;
        for (File file : listFiles) {
        	Map<String, Long> table = new HashMap<String, Long>();
            tableCount++;
            LineNumberReader lineNumberReader;
			try {
				lineNumberReader = new LineNumberReader(new FileReader(dirName + "/" + file.getName()));
				lineNumberReader.skip(Long.MAX_VALUE);
				int id = lineNumberReader.getLineNumber() - 1;
				recordCountTotal += id;
				lineNumberReader.close();
				table.put(file.getName().substring(0, file.getName().length()-3), Long.valueOf(id));
				tables.add(table);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
	
	public void reportToLog (String databaseName) {
		int tableCount = 0;
		Long recordCountTotal = 0L;
		List<Map<String, Long>> tables = new ArrayList<Map<String, Long>>();
		getDatabaseInfo(databaseName, tableCount, recordCountTotal, tables);
		
		GeneralLogsImpl generalLogsImpl = new GeneralLogsImpl();
		GeneralLogsModel generalLogsModel = new GeneralLogsModel();
		generalLogsModel.setTableCount(tableCount);
		generalLogsModel.setRecordCount(recordCountTotal);
		generalLogsModel.setTableList(tables);
		generalLogsImpl.generalLogsEntry(generalLogsModel);
	}
	
	public void reportToEventLog (String databaseName, String tableName, String columnName, int recordId, String oldValue, String newValue) {
		EventLogsImpl eventLogsImpl = new EventLogsImpl();
		EventLogsModel eventLogsModel = new EventLogsModel();
		eventLogsModel.setDatabaseName(databaseName);
		eventLogsModel.setTableName(tableName);
		eventLogsModel.setColumnName(columnName);
		eventLogsModel.setRecordID(recordId);
		eventLogsModel.setOldValue(oldValue);
		eventLogsModel.setNewValue(newValue);
		eventLogsModel.setEventType(Event.UPDATE_OPERATION);
		eventLogsImpl.eventLogsEntry(eventLogsModel);
	}
	
	public List<String> getTableNames (String databaseName) {
		List<String> tableNames = new ArrayList<String>();
		String dirName = "File/DBDemo/" + databaseName;
		File databaseDir = new File(dirName);
		File[] listFiles = databaseDir.listFiles();
        for (File file : listFiles) {
			tableNames.add(file.getName().substring(0, file.getName().length()-3));
        }
		return tableNames;
	}
	
	public List<String> getDatabaseNames () {
		List<String> databaseNames = new ArrayList<String>();
		String dirName = "File/DBDemo/";
		File databaseDir = new File(dirName);
		File[] listFiles = databaseDir.listFiles();
        for (File file : listFiles) {
        	if(file.isDirectory())
        		databaseNames.add(file.getName());
        }
		return databaseNames;
	}
	
	public List<Map<String, Object>> getColumnInfo(String tableName) {
		List<Map<String, Object>> columnInfos = new ArrayList<Map<String, Object>>();
		String dir = "File/DBDemo/";
		File Dir = new File(dir);
        for (File file : Dir.listFiles()) {
        	if(!file.isDirectory())
        		continue;
        	String databaseName = file.getName();
        	File databaseDir = new File("File/DBDemo/"+databaseName);
        	for (File table : databaseDir.listFiles()) {
        		if (!table.getName().equals(tableName + ".tb"))
        			continue;
        		TreeMap<String, String> columns = getTableHeader("File/DBDemo/"+databaseName + "/" + table.getName());
        		for (String columnName : columns.keySet()) {
        			List<String> constrain = new ArrayList<String>();
        			constrain = new ArrayList<String>(Arrays.asList(columns.get(columnName).split(":")));
        			Map<String, Object> columnInfo = new HashMap<String, Object>();
        			columnInfo.put("columnName", columnName);
    				columnInfo.put("primaryKey", false);
    				columnInfo.put("foreignKey", false);
    				columnInfo.put("not null", false);
    				columnInfo.put("unique", false);
        			if (constrain.contains("primaryKey"))
        				columnInfo.put("primaryKey", true);
        			if (constrain.contains("foreignKey"))
        				columnInfo.put("foreignKey", true);
        			if (constrain.contains("not null"))
        				columnInfo.put("not null", true);
        			if (constrain.contains("unique"))
        				columnInfo.put("unique", true);
        			columnInfos.add(columnInfo);
        		}
        	}
        }
		return columnInfos;
	}
	
	public void createMetaData () {
		String dir = "File/DBDemo/";
		File Dir = new File(dir);
		File[] listFiles = Dir.listFiles();
    	List<String> MetaDatas = new ArrayList<String>();
        for (File file : listFiles) {
        	String databaseName = "";
        	if(!file.isDirectory())
        		continue;
    		MetaDatas.add("{");
        	databaseName = file.getName();
        	MetaDatas.add("\tdatabase:" + databaseName);
        	MetaDatas.add("\ttable:{");
        	File databaseDir = new File("File/DBDemo/"+databaseName);
        	for (File table : databaseDir.listFiles()) {
        		String tableName = table.getName().substring(0, table.getName().length()-3);
        		MetaDatas.add("\t\t\t" + tableName + ":{");
        		TreeMap<String, String> columns = getTableHeader("File/DBDemo/"+databaseName + "/" + table.getName());
        		//System.out.println(columns);
        		for (String column : columns.keySet()) {
        			String columnInfo = "";
        			columnInfo += "\t\t\t\t\t\t\t" + column + ":" + columns.get(column).replaceAll(":", "|");
        			MetaDatas.add(columnInfo);
        		}
        		MetaDatas.add("\t\t\t\t\t\t}");
        	}
    		MetaDatas.add("\t\t\t}");
    		MetaDatas.add("};");
        }
		File dbFile = new File("File");
		if (!dbFile.exists()) {
			dbFile.mkdirs();
		}
		File tableFile = new File("File/MetaData.tb");
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(tableFile));
			for (String metaData : MetaDatas) {
				out.write(metaData + "\r\n");
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
