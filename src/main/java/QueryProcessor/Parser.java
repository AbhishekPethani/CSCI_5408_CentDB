package QueryProcessor;

import Exceptions.InvalidSQLQueryException;
import log_management.utils.UserSessionUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Devarshi Vyas (b00878443)
 */

public class Parser {

    Map<String, Object> whereClause(String whereQuery) {
        Map<String, Object> result = new HashMap<>();
        whereQuery = whereQuery.trim();
        Operators operator = null;
        for (Operators value : Operators.values()) {
            if (whereQuery.contains(value.operator)) {
                operator = value;
                break;
            }
        }

        if (operator != null) {
            String[] operands = whereQuery.split(operator.operator);
            result.put("operator", operator);
            result.put("columnOperand", operands[0].trim());
            result.put("valueOperand", operands[1].trim());
        } else {
            throw new InvalidSQLQueryException("Incorrect operator for WHERE clause passed in the SELECT FROM " +
                    "TABLE QUERY.");
        }
        return result;
    }

    Map<String, Object> parseCreateDatabaseQuery(Matcher queryMatcher) {
        Map<String, Object> parsedQueryData = new HashMap<>();
        if (queryMatcher.group(2).contains("[.-@#$%^&*()<>?:\"';,/\\s]") || queryMatcher.group(2).isBlank()) {
            throw new InvalidSQLQueryException("Incorrect database name passed in the CREATE DATABASE QUERY.");
        } else {
            parsedQueryData.put("databaseName", queryMatcher.group(2).trim());
            parsedQueryData.put("queryType", QueryType.CREATE_DATABASE);
        }
        return parsedQueryData;
    }

    Map<String, Object> parseDropDatabaseQuery(Matcher queryMatcher) {
        Map<String, Object> parsedQueryData = new HashMap<>();
        if (queryMatcher.group(2).contains("[.-@#$%^&*()<>?:\"';,/\\s]") || queryMatcher.group(2).isBlank()) {
            throw new InvalidSQLQueryException("Incorrect database name passed in the DROP DATABASE QUERY.");
        } else {
            parsedQueryData.put("databaseName", queryMatcher.group(2).trim());
            parsedQueryData.put("queryType", QueryType.DROP_DATABASE);
        }
        return parsedQueryData;
    }

    Map<String, Object> parseUseDatabaseQuery(Matcher queryMatcher) {
        Map<String, Object> parsedQueryData = new HashMap<>();
        if (queryMatcher.group(1).contains("[.-@#$%^&*()<>?:\"';,/\\s]") || queryMatcher.group(1).isBlank()) {
            throw new InvalidSQLQueryException("Incorrect database name passed in the USE DATABASE QUERY.");
        } else {
            parsedQueryData.put("databaseName", queryMatcher.group(1).trim());
            parsedQueryData.put("queryType", QueryType.USE_DATABASE);
        }
        return parsedQueryData;
    }

    Map<String, Object> parseCreateTableQuery(Matcher queryMatcher) {
        Map<String, Object> parsedQueryData = new HashMap<>();

        if (queryMatcher.group(2).contains("[.-@#$%^&*()<>?:\"';,/\\s]") || queryMatcher.group(2).isBlank()) {
            throw new InvalidSQLQueryException("Incorrect table name passed in the CREATE TABLE QUERY.");
        } else {
            parsedQueryData.put("tableName", queryMatcher.group(2).trim());
            parsedQueryData.put("queryType", QueryType.CREATE_TABLE);
        }

        String[] columns = queryMatcher.group(3).split(",");
        List<Map<String, Object>> allColumnsData = new ArrayList<>();

        for (String column : columns) {
            Map<String, Object> columnData = new HashMap<>();
            String[] data = column.trim().split(" ");
            if (data.length < 2) {
                throw new InvalidSQLQueryException("Incorrect parameters passed in the CREATE TABLE QUERY.");
            } else {
                if (DataTypes.contains(data[1])) {
                    for (int index = 2; index < data.length; index++) {
                        if (ColumnConstraints.contains(data[index])) {
                            columnData.put(data[index].toUpperCase(), true);
                            if (ColumnConstraints.valueOf(data[index]).equals(ColumnConstraints.FOREIGN_KEY_REFERENCES)) {
                                // Foreign Key
                            }
                        } else {
                            throw new InvalidSQLQueryException("Incorrect constraint passed in the CREATE TABLE QUERY" +
                                    ".");
                        }
                    }
                    if (data[0].contains("[.-@#$%^&*()<>?:\"';,/\\s]")) {
                        throw new InvalidSQLQueryException("Incorrect table name passed in the CREATE TABLE QUERY.");
                    } else {
                        columnData.put("columnName", data[0]);
                        columnData.put("dataType", DataTypes.valueOf(data[1]));
                        allColumnsData.add(columnData);
                    }
                } else {
                    throw new InvalidSQLQueryException("Incorrect datatype passed in the CREATE TABLE QUERY.");
                }
            }
        }
        parsedQueryData.put("columns", allColumnsData);
        return parsedQueryData;
    }

    Map<String, Object> parseDeleteTableQuery(Matcher queryMatcher) {
        Map<String, Object> parsedQueryData = new HashMap<>();
        if (queryMatcher.group(2).contains("[.-@#$%^&*()<>?:\"';,/\\s]") || queryMatcher.group(2).isBlank()) {
            throw new InvalidSQLQueryException("Incorrect table name passed in the DELETE TABLE QUERY.");
        } else {
            parsedQueryData.put("tableName", queryMatcher.group(2).trim());
            parsedQueryData.put("queryType", QueryType.DROP_TABLE);
        }
        return parsedQueryData;
    }

    Map<String, Object> parseInsertIntoQuery(Matcher queryMatcher) {
        Map<String, Object> parsedQueryData = new HashMap<>();

        if (queryMatcher.group(2).contains("[.-@#$%^&*()<>?:\"';,/\\s]") || queryMatcher.group(2).isBlank()) {
            throw new InvalidSQLQueryException("Incorrect table name passed in the INSERT INTO TABLE QUERY.");
        } else {
            parsedQueryData.put("tableName", queryMatcher.group(2).trim());
            parsedQueryData.put("queryType", QueryType.INSERT_INTO);
        }

        String[] columns = queryMatcher.group(3).replace(" ", "").split(",");
        parsedQueryData.put("columns", columns);
        Pattern rawRowsPattern = Pattern.compile("\\((.*?)\\)");
        Matcher rawRowsMatcher = rawRowsPattern.matcher(queryMatcher.group(5).replace(" ",""));
        ArrayList<String> rawRows = new ArrayList<>();
        while (rawRowsMatcher.find()) {
                rawRows.add(rawRowsMatcher.group(0));
        }
        if (rawRows.size() == 0) {
            throw new InvalidSQLQueryException("Incorrect INSERT INTO TABLE QUERY.");
        }
        List<ArrayList<String>> dataRows = new ArrayList<>();
        for (int index = 0; index < rawRows.size(); index++) {
            String[] row = rawRows.get(index).replace("(", "").replace(")","").split(",");
            if (row.length != columns.length) {
                throw new InvalidSQLQueryException("Incorrect VALUES added in INSERT INTO TABLE QUERY.");
            } else {
                dataRows.add(new ArrayList<String>(List.of(row)));
            }
        }
        parsedQueryData.put("rows", dataRows);
        return parsedQueryData;
    }

    Map<String, Object> parseSelectFromQuery(Matcher queryMatcher) {
        Map<String, Object> parsedQueryData = new HashMap<>();

        if (queryMatcher.group(4).contains("[.-@#$%^&*()<>?:\"';,/\\s]") || queryMatcher.group(4).isBlank()) {
            throw new InvalidSQLQueryException("Incorrect table name passed in the SELECT FROM TABLE QUERY.");
        } else {
            parsedQueryData.put("tableName", queryMatcher.group(4).trim());
            parsedQueryData.put("queryType", QueryType.SELECT_FROM);
        }

        if (queryMatcher.group(2).contains("[.-@#$%^&()<>?:\"';/]") || queryMatcher.group(2).isBlank()) {
            throw new InvalidSQLQueryException("Incorrect columns passed in the SELECT FROM TABLE QUERY.");
        } else {
            if (Operators.ASTERISK.operator.equals(queryMatcher.group(2).trim())) {
                parsedQueryData.put("selectAll", true);
                parsedQueryData.put("selectionOperator", Operators.ASTERISK);
            } else {
                parsedQueryData.put("selectAll", false);
                ArrayList<String> columns = new ArrayList<>(List.of(queryMatcher.group(2).replace(" ", "").split(",")));
                parsedQueryData.put("columns", columns);
            }
        }

        if (queryMatcher.group(0).contains("WHERE")) {
            parsedQueryData.put("whereClause", this.whereClause(queryMatcher.group(7)));
        }

        return parsedQueryData;
    }

    Map<String, Object> parseUpdateQuery(Matcher queryMatcher) {
        Map<String, Object> parsedQueryData = new HashMap<>();

        if (queryMatcher.group(2).contains("[.-@#$%^&*()<>?:\"';,/\\s]") || queryMatcher.group(2).isBlank()) {
            throw new InvalidSQLQueryException("Incorrect table name passed in the UPDATE TABLE QUERY.");
        } else {
            parsedQueryData.put("tableName", queryMatcher.group(2).trim());
            parsedQueryData.put("queryType", QueryType.UPDATE);
        }

        if (queryMatcher.group(4).isBlank()) {
            throw new InvalidSQLQueryException("Invalid UPDATE TABLE QUERY.");
        } else {
            List<Map<String, String>> updates = new ArrayList<>();
            String[] setValues = queryMatcher.group(4).split(",");
            for (int index = 0; index < setValues.length; index++) {
                Map<String, String> update = new HashMap<>();
                if (setValues[index].contains(Operators.EQUAL.operator)) {
                    update.put("columnName", setValues[index].split(Operators.EQUAL.operator)[0].trim());
                    update.put("value", setValues[index].split(Operators.EQUAL.operator)[1].trim());
                    updates.add(update);
                } else {
                    throw new InvalidSQLQueryException("Incorrect update values passed in UPDATE TABLE QUERY");
                }
            }
            parsedQueryData.put("updates", updates);
        }

        if (queryMatcher.group(0).contains("WHERE")) {
            parsedQueryData.put("whereClause", this.whereClause(queryMatcher.group(7)));
        }

        return parsedQueryData;
    }

    Map<String, Object> parseDeleteFromQuery(Matcher queryMatcher) {
        Map<String, Object> parsedQueryData = new HashMap<>();

        if (queryMatcher.group(2).contains("[.-@#$%^&*()<>?:\"';,/\\s]") || queryMatcher.group(2).isBlank()) {
            throw new InvalidSQLQueryException("Incorrect table name passed in the UPDATE TABLE QUERY.");
        } else {
            parsedQueryData.put("tableName", queryMatcher.group(2).trim());
            parsedQueryData.put("queryType", QueryType.DELETE_FROM);
        }

        if (queryMatcher.group(0).contains("WHERE")) {
            parsedQueryData.put("whereClause", this.whereClause(queryMatcher.group(4)));
        }

        return parsedQueryData;
    }

    Map<String, Object> parseTranasactionQueries(Matcher queryMatcher, QueryType type) {
        Map<String, Object> parsedQueryData = new HashMap<>();

        if (queryMatcher.group(2).contains("[.-@#$%^&*()<>?:\"';,/\\s]") || queryMatcher.group(2).isBlank()) {
            throw new InvalidSQLQueryException("Incorrect transaction name passed in the TRANSACTION QUERY.");
        } else {
            parsedQueryData.put("transactionName", queryMatcher.group(2).trim());
            parsedQueryData.put("queryType", type);
        }

        return parsedQueryData;
    }

    public Map<String, Object> parseQuery(long queryId, String query) {
        Map<String, Object> parsedQueryData = new HashMap<>();
        int index = 0;
        for (QueryType type: QueryType.values()) {
            Pattern queryPattern = Pattern.compile(type.regex, Pattern.MULTILINE);
            Matcher queryMatcher = queryPattern.matcher(query);

            if (queryMatcher.find()) {
                System.out.println("QueryId: " + queryId + " Matched with type: " + type.toString());
                try {
                    switch (type) {
                        case CREATE_DATABASE:
                            parsedQueryData = this.parseCreateDatabaseQuery(queryMatcher);
                            break;
                        case DROP_DATABASE:
                            parsedQueryData = this.parseDropDatabaseQuery(queryMatcher);
                            break;
                        case USE_DATABASE:
                            parsedQueryData = this.parseUseDatabaseQuery(queryMatcher);
                            UserSessionUtils.setDatabaseName((String) parsedQueryData.get("databaseName"));
                            break;
                        case INSERT_INTO:
                            parsedQueryData = this.parseInsertIntoQuery(queryMatcher);
                            break;
                        case CREATE_TABLE:
                            parsedQueryData = this.parseCreateTableQuery(queryMatcher);
                            break;
                        case DROP_TABLE:
                            parsedQueryData = this.parseDeleteTableQuery(queryMatcher);
                            break;
                        case SELECT_FROM:
                            parsedQueryData = this.parseSelectFromQuery(queryMatcher);
                            break;
                        case UPDATE:
                            parsedQueryData = this.parseUpdateQuery(queryMatcher);
                            break;
                        case DELETE_FROM:
                            parsedQueryData = this.parseDeleteFromQuery(queryMatcher);
                            break;
                        case BEGIN_TRANSACTION:
                        case COMMIT:
                        case ROLLBACK: {
                            parsedQueryData = this.parseTranasactionQueries(queryMatcher, type);
                            break;
                        }
                        default:
                            throw new InvalidSQLQueryException("Invalid SQL Query Entered");
                    }
                } catch (InvalidSQLQueryException error) {
                    System.out.println("Parsing Error - Query id: " + System.currentTimeMillis() + " Timestamp: " + new Date(queryId));
                    throw error;
                }
            } else {
                if (index == QueryType.values().length) {
                    throw new InvalidSQLQueryException("Invalid SQL Query Entered");
                }
            }
            index++;
        }
        if (index == QueryType.values().length && parsedQueryData.size() == 0) {
            throw new InvalidSQLQueryException("Invalid SQL Query Entered");
        }
        return parsedQueryData;
    }
}

// TODO: remove the ' replacement. this may be needed for type checking.