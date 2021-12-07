// Author: Devarshi Vyas (B00878443)

package QueryProcessor;

import java.lang.reflect.Array;
import java.util.*;

public class Validator {

    private Boolean doesDatabaseExist(String databaseName) {
//        List<String> databases =  getDatabaseNames();
        List<String> databases = new ArrayList<>();
        databases.add("countries");
        databases.add("places");
        return databases.contains(databaseName);
    }

    private Boolean doesTableExist(String databaseName, String tableName) {
//        List<String> tables =  getTableNames(databaseName);
        List<String> tables = new ArrayList<>();
        tables.add("countries");
        tables.add("places");
        return tables.contains(tableName);
    }

//    private Boolean doesColumnExist() {
//
//    }
//
//    private Boolean isNotNull(String tableName, String columnName) {
//        ArrayList<String> columns = new ArrayList<String>() {{
//            add(columnName);
//        }};
//        List<TreeMap<String, String>> columnData = selectFromTable(columns, tableName);
//
//    }
//
//    private Boolean isUnique(String tableName, String columnName) {
//
//    }

    Map<String, Object> validateInsertIntoQuery(Map<String, Object> parsedQueryData) {
        Map<String, Object> validatedQueryData = new HashMap<>();
        return validatedQueryData;
    }

    Map<String, Object> validateCreateTableQuery(Map<String, Object> parsedQueryData) {
        Map<String, Object> validatedQueryData = new HashMap<>();
        return validatedQueryData;
    }

    Map<String, Object> validateDeleteTableQuery(Map<String, Object> parsedQueryData) {
        Map<String, Object> validatedQueryData = new HashMap<>();
        return validatedQueryData;
    }

    Map<String, Object> validateDropDatabaseQuery(Map<String, Object> parsedQueryData) {
        Map<String, Object> validatedQueryData = new HashMap<>();
        return validatedQueryData;
    }

    Map<String, Object> validateCreateDatabaseQuery(Map<String, Object> parsedQueryData) {
        Map<String, Object> validatedQueryData = new HashMap<>();
        return validatedQueryData;
    }

    public Map<String, Object> validateQuery(Map<String, Object> parsedQueryData) {
        Map<String, Object> validatedQueryData = new HashMap<>();
        QueryType type = (QueryType) parsedQueryData.get("queryType");
        switch(type) {
            case INSERT_INTO:
                validatedQueryData = this.validateInsertIntoQuery(parsedQueryData);
                break;
            case CREATE_TABLE:
                validatedQueryData = this.validateCreateTableQuery(parsedQueryData);
                break;
            case DELETE_TABLE:
                validatedQueryData = this.validateDeleteTableQuery(parsedQueryData);
                break;
            case DROP_DATABASE:
                validatedQueryData = this.validateDropDatabaseQuery(parsedQueryData);
                break;
            case CREATE_DATABASE:
                validatedQueryData = this.validateCreateDatabaseQuery(parsedQueryData);
                break;
        }
        return validatedQueryData;
    }
}

