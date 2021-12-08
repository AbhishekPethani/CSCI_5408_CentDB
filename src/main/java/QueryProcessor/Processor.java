// Author: Devarshi Vyas (B00878443)

package QueryProcessor;

import Exceptions.InvalidSQLQueryException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Processor {
    Parser parser;
    Validator validator;

    Processor() {
        this.parser = new Parser();
        this.validator = new Validator();
    }

    public Map<String, Object> submitQuery(String query) {
        Map<String, Object> result = new HashMap<>();

        long queryId = System.currentTimeMillis();
        System.out.println("Query id: " + System.currentTimeMillis() + " Timestamp: " + new Date(queryId) + " Submitted");

        try {
            Map<String, Object> parsedQueryData = this.parser.parseQuery(queryId, query);
            result = this.validator.validateQuery(parsedQueryData);
        } catch (InvalidSQLQueryException error) {
            throw error;
        }

        return result;
    }
}

enum QueryType {
    CREATE_DATABASE("(CREATE\\s+DATABASE)\\s+(.*?)\\s*;"),
    USE_DATABASE("USE\\s+(.*?)\\s*;"),
    DROP_DATABASE("(DROP\\s+DATABASE)\\s+(.*)\\s*;"),
    CREATE_TABLE("(CREATE\\s+TABLE)\\s+(.*?)\\s+\\((.*?)\\)\\s*;"),
    DROP_TABLE("(DROP\\s+TABLE)\\s+(.*?)\\s*;"),
    INSERT_INTO("(INSERT INTO)\\s+(\\S+)*\\s+\\((.*?)\\)\\s+(VALUES)\\s+(\\(.*?\\),{0,1})*?\\s*;"),
    SELECT_FROM("(SELECT)\\s+(.*?)\\s+(FROM)\\s+(.*?)(\\s+(WHERE)\\s+(.*?))?\\s*;"),
    UPDATE("(UPDATE)\\s+(.*?)\\s+(SET)\\s+(.*?)(\\s+(WHERE)\\s+(.*?))?\\s*;"),
    DELETE_FROM("(DELETE\\s+FROM)\\s+(.*?)(\\s+WHERE\\s+(.*?))?\\s*;"),
    BEGIN_TRANSACTION("(BEGIN\\s+TRANSACTION\\s+)(.*?)\\s*;"),
    COMMIT("(COMMIT\\s+TRANSACTION\\s+)(.*?)\\s*;"),
    ROLLBACK("(ROLLBACK\\s+TRANSACTION\\s+)(.*?)\\s*;");

    public String regex;

    QueryType(String s) {
        this.regex = s;
    }
}

enum DataTypes {
    INT("INT"),
    LONG("LONG"),
    DOUBLE("DOUBLE"),
    TEXT("TEXT"),
    DATETIME("DATETIME");

    public String datatype;

    DataTypes(String s) {
        this.datatype = s;
    }

    public static Boolean contains(String s) {
        for (DataTypes value : DataTypes.values()) {
            if (s.equalsIgnoreCase(value.datatype)) {
                return true;
            }
        }
        return false;
    }
}

enum ColumnConstraints {
    PRIMARY_KEY("PRIMARY_KEY"),
    NOT_NULL("NOT_NULL"),
    UNIQUE("UNIQUE"),
    FOREIGN_KEY_REFERENCES("FOREIGN_KEY_REFERENCES");

    public String constraint;

    ColumnConstraints(String s) {
        this.constraint = s;
    }

    public static Boolean contains(String s) {
        for (ColumnConstraints value : ColumnConstraints.values()) {
            if (s.equalsIgnoreCase(value.constraint)) {
                return true;
            }
        }
        return false;
    }
}

enum Operators {
    ASTERISK("*"),
    EQUAL("="),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_THAN_EQUAL_TO(">="),
    LESS_THAN_EQUAL_TO("<"),
    NOT_EQUAL("<>"),
    BETWEEN("BETWEEN"),
    LIKE("LIKE"),
    IN("IN"),
    NULL("NULL");

    public String operator;

    Operators(String s) {
        this.operator = s;
    }

    public static Boolean contains(String s) {
        for (Operators value : Operators.values()) {
            if (s.equalsIgnoreCase(value.operator)) {
                return true;
            }
        }
        return false;
    }
}

//a. Create database
//b. Use database
//c. Create table
//d. Insert into table
//e. Select from table with single where condition (AND || OR || NOT are not required)
//f. Update one column with single where condition (AND not required)
//g. Delete a row with single where condition
//h. Drop table