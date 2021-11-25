package file_parsing;

import java.util.List;
import java.util.TreeMap;

public interface FileParsingForQueryInterface {
	
	//File parsing for query Section
	public void setDatabaseName (String databaseName);
	
	public void createDatabase (String databaseName);
		
	public void createTable (String tableName, TreeMap<String, String> columnNameAndDataType);
	
	public void insertIntoTable (TreeMap<String, Object> insertColumnAndValue, String tableName);
	
	public void updateTable (TreeMap<String, Object> updateColumnAndValue, String tableName, TreeMap<String, List<Object>> conditionColumnAndValue);
	
}
