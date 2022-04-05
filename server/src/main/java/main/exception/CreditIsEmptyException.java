package main.exception;

public class CreditIsEmptyException extends RuntimeException{
    public CreditIsEmptyException(String message) {
        super(message);
    }
}
