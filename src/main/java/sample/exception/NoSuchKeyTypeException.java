package sample.exception;

public class NoSuchKeyTypeException extends RuntimeException {
    public NoSuchKeyTypeException(String string) {
        super(string);
    }
}
