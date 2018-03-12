package sample.exception;

public class CannotReadUsersException extends RuntimeException {
	public CannotReadUsersException(String message) {
		super(message);
	}
}
