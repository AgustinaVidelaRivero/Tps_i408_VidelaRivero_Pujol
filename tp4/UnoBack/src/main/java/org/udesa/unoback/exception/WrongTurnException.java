package org.udesa.unoback.exception;

public class WrongTurnException extends IllegalStateException {
    public WrongTurnException(String message) {
        super(message);
    }
}