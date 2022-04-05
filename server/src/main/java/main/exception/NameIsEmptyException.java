package main.exception;

public class NameIsEmptyException extends RuntimeException {
    public NameIsEmptyException(String message) {
        super(message);
    }
}
