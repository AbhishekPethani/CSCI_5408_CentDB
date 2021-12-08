package Exceptions;

public class InvalidSQLQueryException extends RuntimeException {
    public InvalidSQLQueryException(String message) {
        super(message);
    }
}

