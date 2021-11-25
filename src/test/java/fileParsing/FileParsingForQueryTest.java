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
//		fileParsingForQuery.createTable("studentInfo", columnNameAndDataType);
//	}
//	@Test
//	public void insertIntoTableTest () {
//		FileParsingForQuery fileParsingForQuery = new FileParsingForQuery();
//		TreeMap<String, Object> insertColumnAndValue = new TreeMap<String, Object>();
//		insertColumnAndValue.put("student_id", "B00871417");
//		insertColumnAndValue.put("name", "zhaoling");
//		insertColumnAndValue.put("age", 23);
//		insertColumnAndValue.put("sex", "female");
//		fileParsingForQuery.setDatabaseName("student");
//		fileParsingForQuery.insertIntoTable(insertColumnAndValue, "studentInfo");
//	}
	@Test
	public void updateTableTest () {
		FileParsingForQuery fileParsingForQuery = new FileParsingForQuery();
		TreeMap<String, Object> updateColumnAndValue = new TreeMap<String, Object>();
		updateColumnAndValue.put("age", 24);
		updateColumnAndValue.put("name", "Zhaoling Sun");
		TreeMap<String, List<Object>> conditionColumnAndValue = new TreeMap<String, List<Object>>();
		List<Object> condition = new ArrayList<Object>();
		condition.add("=");
		condition.add("zhaoling");
		conditionColumnAndValue.put("name", condition);
		fileParsingForQuery.setDatabaseName("student");
		fileParsingForQuery.updateTable(updateColumnAndValue, "studentInfo", conditionColumnAndValue);
	}

}
