package FileParsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

/**
 * @author zhaoling
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
	
}
