package main.exception;

public class AmountIsEmptyException extends RuntimeException{
    public AmountIsEmptyException(String message) {
        super(message);
    }
}
