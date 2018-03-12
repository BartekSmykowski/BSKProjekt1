package sample.exception;

public class CannotSaveUsersException extends RuntimeException {
	public CannotSaveUsersException(String message) {
		super(message);
	}
}
