package Exceptions;

public class InvalidTransactionRequestException extends Exception {
    public InvalidTransactionRequestException(String message) {
        super(message);
    }
}
