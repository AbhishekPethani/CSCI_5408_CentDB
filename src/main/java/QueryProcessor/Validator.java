package QueryProcessor;

import Exceptions.InvalidSQLQueryException;
import Exceptions.InvalidTransactionRequestException;
import FileParsing.FileOperation;
import FileParsing.FileParsingForQuery;

import java.util.*;

/**
 * @author: Devarshi Vyas (B00878443)
 */

public class Validator {

    FileOperation fileOperation;
    FileParsingForQuery fileParsingForQuery;
    String currentDatabase = null;
    String currentTransactionName = null;
    Boolean ongoingTransaction = false;

    Validator() {
        this.fileOperation = new FileOperation();
        this.fileParsingForQuery = new FileParsingForQuery();
    }

    private Boolean doesDatabaseExist(String databaseName) {
        List<String> databases =  this.fileOperation.getDatabaseNames();
        return databases.contains(databaseName);
    }

    private Boolean doesTableExist(String databaseName, String tableName) {
        if (databaseName == null) {
            return false;
        }
        List<String> tables =  this.fileOperation.getTableNames(databaseName);
        return tables.contains(tableName);
    }

    private Boolean doesColumnExist(String tableName, String columnName, String columnType) {

        return false;
    }

    private Boolean isNotNull(String tableName, String columnName, String value) {
        return (value.isBlank() || value == null || value.isEmpty());
    }

    private Boolean isUnique(String tableName, String columnName, String value) {
        ArrayList<String> column = new ArrayList<String>() {{
            add(columnName);
        }};
        TreeMap<String, List<Object>> conditionColumnAndValue = new TreeMap<String, List<Object>>();
		List<Object> condition = new ArrayList<Object>();
		condition.add("=");
		condition.add(value);
		conditionColumnAndValue.put(columnName, condition);
        List<TreeMap<String, String>> rows = this.fileParsingForQuery.selectFromTable(column, tableName, conditionColumnAndValue);
        if (rows.size() == 1) {
            return true;
        } else {
            return false;
        }
    }

    Map<String, Object> validateInsertIntoQuery(Map<String, Object> parsedQueryData) {
        Map<String, Object> validatedQueryData = new HashMap<>();
        if (this.currentDatabase != null) {
            if(!this.doesTableExist(this.currentDatabase,
                    (String) parsedQueryData.get(
                            "tableName"))) {
                throw new InvalidSQLQueryException("Table does not exist");
            }
        } else {
            throw new InvalidSQLQueryException("No database selected");
        }
        String[] columns = (String[]) parsedQueryData.get("columns");
        int successCount = 0;
        int failureCount = 0;
        TreeMap<String, Object> insertColumnAndValue = new TreeMap<String, Object>();
        for (int index = 0; index < ((ArrayList<String>)parsedQueryData.get("rows")).size(); index++) {
            for (int subIndex = 0; subIndex < columns.length; subIndex++) {
                insertColumnAndValue.put(columns[subIndex],
                        ((ArrayList)((ArrayList<?>) parsedQueryData.get("rows")).get(index)).get(subIndex));
            }
            if (this.fileParsingForQuery.insertIntoTable(insertColumnAndValue,
                    (String) parsedQueryData.get("tableName"))) {
                successCount++;
            } else {
                failureCount++;
            }
        }
        validatedQueryData.put("executed", true);
        validatedQueryData.put("queryType", parsedQueryData.get("queryType").toString());
        validatedQueryData.put("successCount", successCount);
        validatedQueryData.put("failureCount", failureCount);
        return validatedQueryData;
    }

    Map<String, Object> validateCreateTableQuery(Map<String, Object> parsedQueryData) {
        Map<String, Object> validatedQueryData = new HashMap<>();
        List<Map<String, Object>> columns = new ArrayList<Map<String, Object>>();

        if (this.currentDatabase != null) {
            if (!this.doesTableExist(this.currentDatabase, (String)parsedQueryData.get("tableName"))) {
                for (Map<String, Object> stringObjectMap : ((ArrayList<Map<String, Object>>) parsedQueryData.get("columns"))) {
                    Map<String, Object> column = new HashMap<String, Object>();
                    column.put("columnName", stringObjectMap.get("columnName"));
                    column.put("dataType", stringObjectMap.get("dataType").toString());
                    column.put("primaryKey", false);
                    if (stringObjectMap.containsKey(ColumnConstraints.PRIMARY_KEY.toString())) {
                        column.put("primaryKey", true);
                    }
                    column.put("foreignKey", false);
                    if (stringObjectMap.containsKey(ColumnConstraints.FOREIGN_KEY.toString())) {
                        column.put("foreignKey", true);
                    }
                    column.put("not null", false);
                    if (stringObjectMap.containsKey(ColumnConstraints.NOT_NULL.toString())) {
                        column.put("not null", true);
                    }
                    column.put("unique", false);
                    if (stringObjectMap.containsKey(ColumnConstraints.UNIQUE.toString())) {
                        column.put("unique", true);
                    }
                    columns.add(column);
                }
            } else {
                throw new InvalidSQLQueryException("Database with the same name already exists.");
            }
        } else {
            throw new InvalidSQLQueryException("No database selected");
        }
        if (this.fileParsingForQuery.createTable((String)parsedQueryData.get("tableName"), columns)) {
            validatedQueryData.put("executed", true);
            validatedQueryData.put("queryType", parsedQueryData.get("queryType").toString());
        } else {
            validatedQueryData.put("executed", false);
            validatedQueryData.put("message", "Sorry. Could not process your query currently.");
            validatedQueryData.put("queryType", parsedQueryData.get("queryType").toString());
        }
        return validatedQueryData;
    }

    Map<String, Object> validateDeleteTableQuery(Map<String, Object> parsedQueryData) {
        Map<String, Object> validatedQueryData = new HashMap<>();
        if (this.currentDatabase != null) {
            if(!this.doesTableExist(this.currentDatabase,
                    (String) parsedQueryData.get(
                            "tableName"))) {
                throw new InvalidSQLQueryException("Table does not exist");
            }
        } else {
            throw new InvalidSQLQueryException("No database selected");
        }
        if (this.fileParsingForQuery.deleteTable((String) parsedQueryData.get(
                "tableName"))) {
            validatedQueryData.put("executed", true);
            validatedQueryData.put("queryType", parsedQueryData.get("queryType").toString());
        } else {
            validatedQueryData.put("executed", false);
            validatedQueryData.put("message", "Sorry. Could not process your query currently.");
            validatedQueryData.put("queryType", parsedQueryData.get("queryType").toString());
        }
        return validatedQueryData;
    }

    Map<String, Object> validateDropDatabaseQuery(Map<String, Object> parsedQueryData) {
        Map<String, Object> validatedQueryData = new HashMap<>();
        if (this.doesDatabaseExist((String)parsedQueryData.get("databaseName"))) {
            if (this.fileParsingForQuery.deleteDatabase((String)parsedQueryData.get("databaseName"))) {
                validatedQueryData.put("executed", true);
                validatedQueryData.put("queryType", parsedQueryData.get("queryType").toString());
            } else {
                validatedQueryData.put("executed", false);
                validatedQueryData.put("message", "Sorry. Could not process your query currently.");
                validatedQueryData.put("queryType", parsedQueryData.get("queryType").toString());
            }
        } else {
            throw new InvalidSQLQueryException("Database does not exist");
        }
        return validatedQueryData;
    }

    Map<String, Object> validateCreateDatabaseQuery(Map<String, Object> parsedQueryData) {
        Map<String, Object> validatedQueryData = new HashMap<>();
        if (!this.doesDatabaseExist((String)parsedQueryData.get("databaseName"))) {
            if (this.fileParsingForQuery.createDatabase((String)parsedQueryData.get("databaseName"))) {
                validatedQueryData.put("executed", true);
                validatedQueryData.put("queryType", parsedQueryData.get("queryType").toString());
            } else {
                validatedQueryData.put("executed", false);
                validatedQueryData.put("message", "Sorry. Could not process your query currently.");
                validatedQueryData.put("queryType", parsedQueryData.get("queryType").toString());
            }
        } else {
            throw new InvalidSQLQueryException("Database with the same name already exists.");
        }
        return validatedQueryData;
    }

    Map<String, Object> validateDeleteFromQuery(Map<String, Object> parsedQueryData) {
        Map<String, Object> validatedQueryData = new HashMap<>();
        if (this.currentDatabase != null) {
            if(!this.doesTableExist(this.currentDatabase,
                    (String) parsedQueryData.get(
                            "tableName"))) {
                throw new InvalidSQLQueryException("Table does not exist");
            }
        } else {
            throw new InvalidSQLQueryException("No database selected");
        }
        TreeMap<String, List<Object>> conditionColumnAndValue = new TreeMap<String, List<Object>>();
        List<Object> condition = new ArrayList<Object>();
        Map<String, Object> whereClause = (Map<String, Object>) parsedQueryData.get("whereClause");
        condition.add(((Operators) whereClause.get("operator")).operator);
        condition.add(((String) whereClause.get("valueOperand")).replace("'", ""));
        conditionColumnAndValue.put(((String) whereClause.get("columnOperand")), condition);
        if(this.fileParsingForQuery.deleteDataInTable((String) parsedQueryData.get("tableName"),
                conditionColumnAndValue)) {
            validatedQueryData.put("executed", true);
        } else {
            validatedQueryData.put("executed", false);
        }
        validatedQueryData.put("queryType", parsedQueryData.get("queryType").toString());
        return validatedQueryData;
    }

    Map<String, Object> validateUpdateQuery(Map<String, Object> parsedQueryData) {
        Map<String, Object> validatedQueryData = new HashMap<>();
        if (this.currentDatabase != null) {
            if(!this.doesTableExist(this.currentDatabase,
                    (String) parsedQueryData.get(
                            "tableName"))) {
                throw new InvalidSQLQueryException("Table does not exist");
            }
        } else {
            throw new InvalidSQLQueryException("No database selected");
        }

        TreeMap<String, Object> updateColumnAndValue = new TreeMap<String, Object>();
        ArrayList<Map<String, Object>> updates = (ArrayList<Map<String, Object>>)parsedQueryData.get("updates");

        for (int index = 0; index < updates.size(); index++) {
            updateColumnAndValue.put((String) updates.get(index).get("columnName"), updates.get(index).get("value"));
        }

        TreeMap<String, List<Object>> conditionColumnAndValue = new TreeMap<String, List<Object>>();
        List<Object> condition = new ArrayList<Object>();
        Map<String, Object> whereClause = (Map<String, Object>) parsedQueryData.get("whereClause");
        condition.add(((Operators) whereClause.get("operator")).operator);
        condition.add(((String) whereClause.get("columnOperand")).replace("'", ""));
        conditionColumnAndValue.put(((String) whereClause.get("columnOperand")), condition);

        if(this.fileParsingForQuery.updateDataTable(updateColumnAndValue, (String) parsedQueryData.get("tableName"),
                conditionColumnAndValue)) {
            validatedQueryData.put("executed", true);
        } else {
            validatedQueryData.put("executed", false);
        }
        validatedQueryData.put("queryType", parsedQueryData.get("queryType").toString());

        return validatedQueryData;
    }

    Map<String, Object> validateSelectFromQuery(Map<String, Object> parsedQueryData) {
        Map<String, Object> validatedQueryData = new HashMap<>();
        if (this.currentDatabase != null) {
            if(!this.doesTableExist(this.currentDatabase,
                    (String) parsedQueryData.get(
                            "tableName"))) {
                throw new InvalidSQLQueryException("Table does not exist");
            }
        } else {
            throw new InvalidSQLQueryException("No database selected");
        }
        TreeMap<String, List<Object>> conditionColumnAndValue = new TreeMap<String, List<Object>>();
        List<Object> condition = new ArrayList<Object>();
        List<TreeMap<String, String>> rows = new ArrayList<>();
        if (parsedQueryData.containsKey("whereClause")) {
            Map<String, Object> whereClause = (Map<String, Object>) parsedQueryData.get("whereClause");
            switch ((Operators) whereClause.get("operator")) {
                case EQUAL:
                case GREATER_THAN:
                case LESS_THAN: {
                    condition.add(((Operators) whereClause.get("operator")).operator);
                    condition.add(((String) whereClause.get("valueOperand")).replace("'", ""));
                    break;
                }
                case BETWEEN:
                    break;
                case NOT_EQUAL:
                    break;
                case LESS_THAN_EQUAL_TO:
                    break;
                case GREATER_THAN_EQUAL_TO:
                    break;
                case ASTERISK:
                case IN:
                case LIKE:
                case NULL:
                    break;
            }
            conditionColumnAndValue.put(((String) whereClause.get("columnOperand")), condition);
        }
        rows = this.fileParsingForQuery.selectFromTable((ArrayList<String>) parsedQueryData.get(
                                "columns"), (String) parsedQueryData.get("tableName"),
                        conditionColumnAndValue);
        validatedQueryData.put("executed", true);
        validatedQueryData.put("queryType", parsedQueryData.get("queryType").toString());
        validatedQueryData.put("rows", rows);
        return validatedQueryData;
    }

    Map<String, Object> validateUseDatabaseQuery(Map<String, Object> parsedQueryData) {
        Map<String, Object> validatedQueryData = new HashMap<>();
        if (this.doesDatabaseExist((String)parsedQueryData.get("databaseName"))) {
            this.fileParsingForQuery.setDatabaseName((String)parsedQueryData.get("databaseName"));
            validatedQueryData.put("executed", true);
            validatedQueryData.put("queryType", parsedQueryData.get("queryType").toString());
            this.currentDatabase = (String)parsedQueryData.get("databaseName");
        } else {
            throw new InvalidSQLQueryException("Database does not exist");
        }
        return validatedQueryData;
    }

    Map<String, Object> validateTranasactionQueries(Map<String, Object> parsedQueryData) throws InvalidTransactionRequestException {
        Map<String, Object> validatedQueryData = new HashMap<>();

        if (parsedQueryData.get("queryType").equals(QueryType.BEGIN_TRANSACTION)) {
            if (this.ongoingTransaction != null) {
                throw new InvalidTransactionRequestException("Invalid Transaction Command - Only one transaction can " +
                        "be run at a time from a single client");
            }
            this.ongoingTransaction = true;
            this.currentTransactionName = (String) parsedQueryData.get("transactionName");
            // go to the transaction manager
        } else if (parsedQueryData.get("queryType").equals(QueryType.COMMIT)) {

        } else if (parsedQueryData.get("queryType").equals(QueryType.ROLLBACK)) {
            if (this.ongoingTransaction == null) {
                throw new InvalidTransactionRequestException("Invalid Transaction Command - No transaction is ongoing" +
                        " on this client.");
            }
            this.ongoingTransaction = true;
            this.currentTransactionName = (String) parsedQueryData.get("transactionName");
        } else {
            throw new InvalidTransactionRequestException("Invalid Transaction Command");
        }

        return validatedQueryData;
    }

    public Map<String, Object> validateQuery(Map<String, Object> parsedQueryData) throws InvalidTransactionRequestException {
        Map<String, Object> validatedQueryData = new HashMap<>();
        QueryType type = (QueryType) parsedQueryData.get("queryType");
        switch (type) {
            case CREATE_DATABASE:
                validatedQueryData = this.validateCreateDatabaseQuery(parsedQueryData);
                break;
            case DROP_DATABASE:
                validatedQueryData = this.validateDropDatabaseQuery(parsedQueryData);
                break;
            case USE_DATABASE:
                validatedQueryData = this.validateUseDatabaseQuery(parsedQueryData);
                break;
            case INSERT_INTO:
                validatedQueryData = this.validateInsertIntoQuery(parsedQueryData);
                break;
            case CREATE_TABLE:
                validatedQueryData = this.validateCreateTableQuery(parsedQueryData);
                break;
            case DROP_TABLE:
                validatedQueryData = this.validateDeleteTableQuery(parsedQueryData);
                break;
            case SELECT_FROM:
                validatedQueryData = this.validateSelectFromQuery(parsedQueryData);
                break;
            case UPDATE:
                validatedQueryData = this.validateUpdateQuery(parsedQueryData);
                break;
            case DELETE_FROM:
                validatedQueryData = this.validateDeleteFromQuery(parsedQueryData);
                break;
            case BEGIN_TRANSACTION:
            case COMMIT:
            case ROLLBACK: {
                try {
                    validatedQueryData = this.validateTranasactionQueries(parsedQueryData);
                } catch (InvalidTransactionRequestException e) {
                    e.printStackTrace();
                    throw e;
                }
                break;
            }
            default:
                throw new InvalidSQLQueryException("Invalid SQL Query Entered");
        }
        return validatedQueryData;
    }
}

