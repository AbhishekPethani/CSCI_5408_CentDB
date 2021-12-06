// Author: Devarshi Vyas (B00878443)

package QueryProcessor;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParserTest {
//        parser.parseQuery(System.currentTimeMillis(), "INSERT INTO table_name (c1,c2,c3) VALUES (abc,def,ghi), (jkl," +
//                "mno,pqr);");

    @Test
    public void parseCreateDatabaseQueryTest() {
        Parser parser = new Parser();
        Map<String, Object> parsedQueryData = parser.parseQuery(System.currentTimeMillis(), "CREATE DATABASE " +
                "thisissomedb;");
        Map<String, Object> expectedData = new HashMap<>();
        expectedData.put("databaseName", "thisissomedb");
        expectedData.put("queryType", QueryType.CREATE_DATABASE);
        assertEquals("Parsing - CREATE DATABASE Query Failing", parsedQueryData, expectedData);
    }

    @Test
    public void parseDropDatabaseQueryTest() {
        Parser parser = new Parser();
        Map<String, Object> parsedQueryData = parser.parseQuery(System.currentTimeMillis(), "DROP DATABASE " +
                "thisissomedb;");
        Map<String, Object> expectedData = new HashMap<>();
        expectedData.put("databaseName", "thisissomedb");
        expectedData.put("queryType", QueryType.DROP_DATABASE);
        assertEquals("Parsing - DROP DATABASE Query Failing", parsedQueryData, expectedData);
    }

    @Test
    public void parseDeleteTableQueryTest() {
        Parser parser = new Parser();
        Map<String, Object> parsedQueryData = parser.parseQuery(System.currentTimeMillis(), "DELETE TABLE " +
                "sometable;");
        Map<String, Object> expectedData = new HashMap<>();
        expectedData.put("tableName", "sometable");
        expectedData.put("queryType", QueryType.DELETE_TABLE);
        assertEquals("Parsing - DELETE TABLE Query Failing", parsedQueryData, expectedData);
    }

    @Test
    public void parseCreateTableQueryTest() {
        Parser parser = new Parser();
        Map<String, Object> parsedQueryData = parser.parseQuery(System.currentTimeMillis(), "CREATE TABLE " +
                "people (PersonID INT PRIMARY_KEY, LastName STRING, FirstName STRING NOT_NULL);");
        List<Map<String, Object>> subList = new ArrayList<>();
        Map<String, Object> subData = new HashMap<>();

        subData.put("columnName", "PersonID");
        subData.put("dataType", DataTypes.INT);
        subData.put("PRIMARY_KEY", true);
        subList.add(subData);

        subData = new HashMap<>();
        subData.put("columnName", "LastName");
        subData.put("dataType", DataTypes.TEXT);
        subList.add(subData);

        subData = new HashMap<>();
        subData.put("columnName", "FirstName");
        subData.put("dataType", DataTypes.TEXT);
        subData.put("NOT_NULL", true);
        subList.add(subData);

        Map<String, Object> expectedData = new HashMap<>();
        expectedData.put("tableName", "people");
        expectedData.put("queryType", QueryType.CREATE_TABLE);
        expectedData.put("columns", subList);
        assertEquals("Parsing - CREATE TABLE Query Failing", parsedQueryData, expectedData);
    }

    @Test
    public void parseInsertIntoQueryTest() {
        Parser parser = new Parser();
        Map<String, Object> parsedQueryData = parser.parseQuery(System.currentTimeMillis(), "INSERT INTO table_name (c1,c2,c3) VALUES (abc,def,ghi), (jkl," +
                "mno, NULL), (NULL, vwx, yza);");
//        List<Map<String, Object>> subList = new ArrayList<>();
//        Map<String, Object> subData = new HashMap<>();
//
//        subData.put("columnName", "PersonID");
//        subData.put("dataType", DataTypes.INT);
//        subData.put("PRIMARY_KEY", true);
//        subList.add(subData);
//
//        subData = new HashMap<>();
//        subData.put("columnName", "LastName");
//        subData.put("dataType", DataTypes.STRING);
//        subList.add(subData);
//
//        subData = new HashMap<>();
//        subData.put("columnName", "FirstName");
//        subData.put("dataType", DataTypes.STRING);
//        subData.put("NOT_NULL", true);
//        subList.add(subData);
//
//        Map<String, Object> expectedData = new HashMap<>();
//        expectedData.put("tableName", "people");
//        expectedData.put("queryType", QueryType.CREATE_TABLE);
//        expectedData.put("columns", subList);
//        assertEquals("Parsing - CREATE TABLE Query Failing", parsedQueryData, expectedData);
    }

    @Test
    public void parseSelectFromQueryTest() {
        Parser parser = new Parser();
//        Map<String, Object> parsedQueryData = parser.parseQuery(System.currentTimeMillis(), "SELECT * FROM Customers\n" +
//                "WHERE Country = 'Mexico' ;");
        Map<String, Object> parsedQueryData = parser.parseQuery(System.currentTimeMillis(), "SELECT Country, " +
                "UserName" +
                " FROM Customers\n" +
                "WHERE Country LIKE 'Mexico' ;");
    }

    @Test
    public void parseUpdateQueryTest() {
        Parser parser = new Parser();
        Map<String, Object> parsedQueryData = parser.parseQuery(System.currentTimeMillis(), "UPDATE Customers\n" +
                "SET ContactName = 'Alfred Schmidt', City= 'Frankfurt'\n" +
                "WHERE CustomerID = 1;");
    }

    @Test
    public  void parseDeleteFromQueryTest() {
        Parser parser = new Parser();
//        Map<String, Object> parsedQueryData = parser.parseQuery(System.currentTimeMillis(),
//                "DELETE FROM Customers WHERE CustomerName='Alfreds Futterkiste';");
        Map<String, Object> parsedQueryData = parser.parseQuery(System.currentTimeMillis(),
                "DELETE FROM Customers;");

    }

    @Test
    public  void parseUseDatabaseQueryTest() {
        Parser parser = new Parser();
        Map<String, Object> parsedQueryData = parser.parseQuery(System.currentTimeMillis(),
                "USE someDB;");

    }

}
