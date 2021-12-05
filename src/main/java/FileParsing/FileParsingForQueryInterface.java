package FileParsing;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public interface FileParsingForQueryInterface {
	
	/** *set the current database
	 * @author zhaoling
	 * @param databaseName: the name of current database
	 */
	public void setDatabaseName (String databaseName);
	
	/** *create a database
	 * @author zhaoling
	 * @param databaseName: the name of the database you want to create
	 */
	public void createDatabase (String databaseName);
	
	/** *create a table
	 * @author zhaoling
	 * @param tableName: the name of the table you want to create
	 * @param columnConstrain: store the columns information
	 */
	public void createTable (String tableName, List<Map<String, Object>> columnInfos);
	
	/** *insert one or more pieces of data into table
	 * @author zhaoling
	 * @param insertColumnAndValue: the data to be inserted, the column name and the value of the column are stored in the map as key-value pairs
	 * @param tableName: the name of the table to insert data
	 */
	public void insertIntoTable (TreeMap<String, Object> insertColumnAndValue, String tableName);
	
	/** *update the data that meets the conditions, maybe one or more pieces
	 * @author zhaoling
	 * @param updateColumnAndValue: the data to be updated, the column name and the new value of the column are stored in the map as key-value pairs
	 * @param tableName: the name of the table to update data
	 * @param conditionColumnAndValue: Conditions of the data to be updated, structure like this: {columnName: [condition, value]},
	 * 								   support three conditions: "=", "<", ">". When condition is "<" or ">", the value must be number.
	 */
	public void updateDataTable (TreeMap<String, Object> updateColumnAndValue, String tableName, TreeMap<String, List<Object>> conditionColumnAndValue);
	
	/** *delete the data in the table
	 * @author zhaoling
	 * @param tableName: the name of the table to delete data
	 * @param conditionColumnAndValue: Conditions of the data to be deleted, structure like this: {columnName: [condition, value]},
	 * 								   support three conditions: "=", "<", ">". When condition is "<" or ">", the value must be number.
	 */
	public void deleteDataInTable (String tableName, TreeMap<String, List<Object>> conditionColumnAndValue);
	
	/** *delete the table
	 * @author zhaoling
	 * @param tableName: the name of the table to be deleted
	 */
	public void deleteTable (String tableName);
	
	/** *delete the databse
	 * @author zhaoling
	 * @param databaseName: the name of the database to be deleted
	 */
	public void deleteDatabase (String databaseName);
	
	/** *select data from the table
	 * @author zhaoling
	 * @param selectColumn: store the column name you want to select
	 * @param tableName: the name of the table to select data
	 * @param conditionColumnAndValue: Conditions of the data to be selected, structure like this: {columnName: [condition, value]},
	 * 								   support three conditions: "=", "<", ">". When condition is "<" or ">", the value must be number.
	 * @return: Eligible selected data, multiple rows of data are stored in the form of a list. 
	 * 			For each row, the column name and the value of the column are stored in the map as key-value pairs
	 */
	public List<TreeMap<String, String>> selectFromTable (List<String> selectColumn, String tableName, TreeMap<String, List<Object>> conditionColumnAndValue);

}
