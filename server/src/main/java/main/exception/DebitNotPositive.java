package main.exception;

public class DebitNotPositive extends RuntimeException{
    public DebitNotPositive(String message) {
        super(message);
    }
}
