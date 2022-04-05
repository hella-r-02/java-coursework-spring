package main.exception;

public class CreditNotPositive extends RuntimeException{
    public CreditNotPositive(String message) {
        super(message);
    }
}
