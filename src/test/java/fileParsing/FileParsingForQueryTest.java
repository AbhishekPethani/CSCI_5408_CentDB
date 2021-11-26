package fileParsing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;

import file_parsing.FileParsingForQuery;

public class FileParsingForQueryTest {

//	@Test
//	public void createDatabaseTest () {
//		FileParsingForQuery fileParsingForQuery = new FileParsingForQuery();
//		fileParsingForQuery.createDatabase("student");
//	}
//	@Test
//	public void createTableTest () {
//		FileParsingForQuery fileParsingForQuery = new FileParsingForQuery();
//		TreeMap<String, String> columnNameAndDataType = new TreeMap<String, String>();
//		columnNameAndDataType.put("student_id", "String");
//		columnNameAndDataType.put("name", "String");
//		columnNameAndDataType.put("age", "Integer");
//		columnNameAndDataType.put("sex", "String");
//		fileParsingForQuery.setDatabaseName("student");
//		fileParsingForQuery.createTable("student", columnNameAndDataType);
//	}
//	@Test
//	public void insertIntoTableTest () {
//		FileParsingForQuery fileParsingForQuery = new FileParsingForQuery();
//		TreeMap<String, Object> insertColumnAndValue = new TreeMap<String, Object>();
//		insertColumnAndValue.put("student_id", "B00123456789");
//		insertColumnAndValue.put("name", "Tommy");
//		insertColumnAndValue.put("age", 18);
//		insertColumnAndValue.put("sex", "male");
//		fileParsingForQuery.setDatabaseName("student");
//		fileParsingForQuery.insertIntoTable(insertColumnAndValue, "studentInfo");
//	}
//	@Test
//	public void updateDataTableTest () {
//		FileParsingForQuery fileParsingForQuery = new FileParsingForQuery();
//		TreeMap<String, Object> updateColumnAndValue = new TreeMap<String, Object>();
//		updateColumnAndValue.put("age", 24);
//		updateColumnAndValue.put("name", "Zhaoling Sun");
//		TreeMap<String, List<Object>> conditionColumnAndValue = new TreeMap<String, List<Object>>();
//		List<Object> condition = new ArrayList<Object>();
//		condition.add("=");
//		condition.add("zhaoling");
//		conditionColumnAndValue.put("name", condition);
//		fileParsingForQuery.setDatabaseName("student");
//		fileParsingForQuery.updateDataTable(updateColumnAndValue, "studentInfo", conditionColumnAndValue);
//	}
//	@Test
//	public void deleteDataInTableTest () {
//		FileParsingForQuery fileParsingForQuery = new FileParsingForQuery();
//		TreeMap<String, List<Object>> conditionColumnAndValue = new TreeMap<String, List<Object>>();
//		List<Object> condition = new ArrayList<Object>();
//		condition.add("=");
//		condition.add("Tommy");
//		conditionColumnAndValue.put("name", condition);
//		fileParsingForQuery.setDatabaseName("student");
//		fileParsingForQuery.deleteDataInTable("studentInfo", conditionColumnAndValue);
//	}
//	@Test
//	public void deleteTableTest () {
//		FileParsingForQuery fileParsingForQuery = new FileParsingForQuery();
//		fileParsingForQuery.setDatabaseName("student");
//		fileParsingForQuery.deleteTable("delete");
//	}
//	@Test
//	public void deleteDatabaseTest () {
//		FileParsingForQuery fileParsingForQuery = new FileParsingForQuery();
//		fileParsingForQuery.deleteDatabase("delete");
//	}
	@Test
	public void selectFromTableTest () {
		FileParsingForQuery fileParsingForQuery = new FileParsingForQuery();
		List<TreeMap<String, String>> expectResult = new ArrayList<TreeMap<String, String>>();
		TreeMap<String, String> expectRow = new TreeMap<String, String>();
		List<String> selectColumn = new ArrayList<String>();
		selectColumn.add("name");
		selectColumn.add("age");
		TreeMap<String, List<Object>> conditionColumnAndValue = new TreeMap<String, List<Object>>();
		List<Object> condition = new ArrayList<Object>();
		condition.add(">");
		condition.add(18);
		conditionColumnAndValue.put("age", condition);
		expectRow.put("age", "24");
		expectRow.put("name", "Zhaoling Sun");
		expectResult.add(expectRow);
		fileParsingForQuery.setDatabaseName("student");
		Assert.assertEquals(expectResult, fileParsingForQuery.selectFromTable(selectColumn, "studentInfo", conditionColumnAndValue));
	}

}
