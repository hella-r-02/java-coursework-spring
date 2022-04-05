package main.exception;

public class DebitIsEmptyException extends RuntimeException{
    public DebitIsEmptyException(String message) {
        super(message);
    }
}
