package sample.exception;

public class CannotRegisterUserException extends RuntimeException {
	public CannotRegisterUserException(Throwable e) {
		super(e);
	}
}
