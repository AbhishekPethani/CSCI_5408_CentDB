package FileParsing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhaoling
 */
public class FileParsingForQueryTest {

//	@Test
//	public void createDatabaseTest () {
//		FileParsingForQuery fileParsingForQuery = new FileParsingForQuery();
//		fileParsingForQuery.createDatabase("student");
//	}
//	@Test
//	public void createTableTest () {
//		FileParsingForQuery fileParsingForQuery = new FileParsingForQuery();
//		List<Map<String, Object>> columnInfos = new ArrayList<Map<String, Object>>();
//		Map<String, Object> columnInfo1 = new HashMap<String, Object>();
//		columnInfo1.put("columnName", "professorId");
//		columnInfo1.put("dataType", "Integer");
//		columnInfo1.put("primaryKey", true);
//		columnInfo1.put("foreignKey", false);
//		columnInfo1.put("not null", true);
//		columnInfo1.put("unique", true);
//		columnInfos.add(columnInfo1);
//		Map<String, Object> columnInfo2 = new HashMap<String, Object>();
//		columnInfo2.put("columnName", "name");
//		columnInfo2.put("dataType", "String");
//		columnInfo2.put("primaryKey", false);
//		columnInfo2.put("foreignKey", false);
//		columnInfo2.put("not null", true);
//		columnInfo2.put("unique", false);
//		columnInfos.add(columnInfo2);
//		Map<String, Object> columnInfo3 = new HashMap<String, Object>();
//		columnInfo3.put("columnName", "courseId");
//		columnInfo3.put("dataType", "Integer");
//		columnInfo3.put("primaryKey", false);
//		columnInfo3.put("foreignKey", true);
//		columnInfo3.put("not null", true);
//		columnInfo3.put("unique", true);
//		columnInfos.add(columnInfo3);
//		fileParsingForQuery.setDatabaseName("Demo");
//		fileParsingForQuery.createTable("professorInfo", columnInfos);
//	}
//	@Test
//	public void insertIntoTableTest () {
//		FileParsingForQuery fileParsingForQuery = new FileParsingForQuery();
//		TreeMap<String, Object> insertColumnAndValue = new TreeMap<String, Object>();
//		insertColumnAndValue.put("professorId", "B001111");
//		insertColumnAndValue.put("name", "aaa");
//		insertColumnAndValue.put("courseId", 5308);
//		//insertColumnAndValue.put("sex", "male");
//		fileParsingForQuery.setDatabaseName("Demo");
//		fileParsingForQuery.insertIntoTable(insertColumnAndValue, "professorInfo");
//	}
//	@Test
//	public void updateDataTableTest () {
//		FileParsingForQuery fileParsingForQuery = new FileParsingForQuery();
//		TreeMap<String, Object> updateColumnAndValue = new TreeMap<String, Object>();
//		updateColumnAndValue.put("courseId", 5308);
//		updateColumnAndValue.put("name", "Zhaoling Sun");
//		TreeMap<String, List<Object>> conditionColumnAndValue = new TreeMap<String, List<Object>>();
//		List<Object> condition = new ArrayList<Object>();
//		condition.add("=");
//		condition.add("zhaoling");
//		conditionColumnAndValue.put("name", condition);
//		fileParsingForQuery.setDatabaseName("Demo");
//		fileParsingForQuery.updateDataTable(updateColumnAndValue, "professorInfo", conditionColumnAndValue);
//	}
//	@Test
//	public void deleteDataInTableTest () {
//		FileParsingForQuery fileParsingForQuery = new FileParsingForQuery();
//		TreeMap<String, List<Object>> conditionColumnAndValue = new TreeMap<String, List<Object>>();
//		List<Object> condition = new ArrayList<Object>();
//		condition.add("=");
//		condition.add("5308");
//		conditionColumnAndValue.put("courseId", condition);
//		fileParsingForQuery.setDatabaseName("Demo");
//		fileParsingForQuery.deleteDataInTable("professorInfo", conditionColumnAndValue);
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
		selectColumn.add("courseId");
		TreeMap<String, List<Object>> conditionColumnAndValue = new TreeMap<String, List<Object>>();
		List<Object> condition = new ArrayList<Object>();
//		condition.add("=");
//		condition.add("5308");
//		conditionColumnAndValue.put("courseId", condition);
		expectRow.put("age", "24");
		expectRow.put("name", "Zhaoling Sun");
		expectResult.add(expectRow);
		fileParsingForQuery.setDatabaseName("Demo");
		System.out.println(fileParsingForQuery.selectFromTable(selectColumn, "professorInfo", conditionColumnAndValue));
		//Assert.assertEquals(expectResult, fileParsingForQuery.selectFromTable(selectColumn, "studentInfo", conditionColumnAndValue));
	}

}
