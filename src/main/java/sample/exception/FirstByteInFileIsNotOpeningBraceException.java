package sample.exception;

public class FirstByteInFileIsNotOpeningBraceException extends RuntimeException {
    public FirstByteInFileIsNotOpeningBraceException(char character) {
        super(String.valueOf(character));
    }
}
