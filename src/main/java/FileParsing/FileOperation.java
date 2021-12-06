package FileParsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @projectName: centdb_g16
 * @package: FileParsing
 * @className: FileOperation
 * @description: some basic operation to file
 * @author: zhaoling 
 * @createDate: 2021-12-3  
 * @updateUser: zhaoling 
 * @updateDate: 2021-12-5
 * @updateRemark: add getDatabaseInfo and reportToLog methods .
 * @version: v1.1
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
				tableColumnMap.put(s.split(":")[0], s.split(":")[1]);
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
	
	public void getDatabaseInfo (String databaseName, Long tableCount, Long recordCountTotal, List<Map<String, Long>> tables) {
		String dirName = "File/DBDemo/" + databaseName;
		File databaseDir = new File(dirName);
		File[] listFiles = databaseDir.listFiles();
		tableCount = 0L;
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
		Long tableCount = 0L;
		Long recordCountTotal = 0L;
		List<Map<String, Long>> tables = new ArrayList<Map<String, Long>>();
		getDatabaseInfo(databaseName, tableCount, recordCountTotal, tables);
//		GeneralLogsImpl generalLogsImpl = new GeneralLogsImpl();
//		GeneralLogsModel generalLogsModel = new GeneralLogsModel();
//		generalLogsModel.setTableCount(tableCount);
//		generalLogsModel.setRecordCount(recordCountTotal);
//		generalLogsModel.setTableList(tables);
//		generalLogsImpl.generalLogsEntry(generalLogsModel);
	}
	
}
