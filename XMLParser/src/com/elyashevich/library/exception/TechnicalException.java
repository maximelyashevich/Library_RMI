package com.elyashevich.library.exception;

public class TechnicalException extends Exception {
    public TechnicalException() {
    }

    public TechnicalException(String message, Throwable exception) {
        super(message, exception);
    }

    public TechnicalException(String message) {
        super(message);
    }

    public TechnicalException(Throwable exception) {
        super(exception);
    }

}
