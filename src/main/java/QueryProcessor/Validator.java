// Author: Devarshi Vyas (B00878443)

package QueryProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Validator {
//    public Boolean validateQuery(HashMap<String, Object> queryContent) {
//        Set<String> queryKeys = queryContent.keySet();
//        for (String queryKey : queryKeys) {
//            if ("queryType".equals(queryKey)) {
//                if (queryContent.get(queryKey) == QueryType.SELECT)
//                validateSelectQuery();
//            } else if ("databaseName".equals(queryKey)) {
//                // check if the database exists
//            } else if ("tableName".equals(queryKey)) {
//                // check if the table exists
//            }
//        }
//        return false;
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

