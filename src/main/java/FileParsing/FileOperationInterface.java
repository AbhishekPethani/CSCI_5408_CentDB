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
	
	public void getDatabaseInfo (String databaseName, Long tableCount, Long recordCountTotal, List<Map<String, Long>> tables);
	
	public void reportToLog (String databaseName);
	
}
