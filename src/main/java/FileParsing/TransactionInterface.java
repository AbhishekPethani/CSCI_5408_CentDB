package FileParsing;

import java.util.List;
import java.util.TreeMap;

/**
 * @author zhaoling
 */
public interface TransactionInterface {
	
	public int startTransaction (String transactionName);
	
	public int abortTransaction (String transactionName);
	
	public int commitTransaction (String transactionName);
	
	public int acquireLockTransaction (String transactionName);

	public int blockTransaction (String transactionName);
	
	public void setDatabase (String databaseName);
	
	public void transactionUpdate (String transactionName, String tableName, TreeMap<String, Object> updateColumnAndValue, TreeMap<String, List<Object>> conditionColumnAndValue);
	
	public void transactionInsert (String transactionName, String tableName, TreeMap<String, Object> insertColumnAndValue);
	
	public void transactionDelete (String transactionName, String tableName, TreeMap<String, List<Object>> conditionColumnAndValue);
	
	
	
}
