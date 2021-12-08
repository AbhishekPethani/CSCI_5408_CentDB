package FileParsing;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author zhaoling
 */
public interface FileOperationInterface {

	public TreeMap<String, String> getTableHeader (String tableFileName);
	
	public TreeMap<String, String> getTableContent (String tableFileName);
	
	public TreeMap<String, String> transferRowStringToRowMap (String row, TreeMap<String, String> header);
	
	public Boolean checkIsSatisfyCondition (TreeMap<String, List<Object>> conditionColumnAndValue, TreeMap<String, String> columnRow);
	
	public String getHeaderString (String tableFileName);
	
	public Map<String,Object> getDatabaseInfo (String databaseName);
	
	public void reportToLog (String databaseName, String message);
	
	public void reportToEventLog (String databaseName, String tableName, String columnName, int recordId, String oldValue, String newValue);
	
	public List<String> getTableNames (String databaseName);
	
	public List<String> getDatabaseNames();
	
	public List<Map<String, Object>> getColumnInfo(String tableName);
	
	public void createMetaData ();
	
}
