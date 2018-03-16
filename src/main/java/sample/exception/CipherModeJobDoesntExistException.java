package sample.exception;

import sample.model.CipherModes;

public class CipherModeJobDoesntExistException extends RuntimeException {
    public CipherModeJobDoesntExistException(CipherModes mode) {
        super(mode.toString());
    }
}
