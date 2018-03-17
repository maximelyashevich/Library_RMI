package com.elyashevich.library.exception;

public class ConnectionTechnicalException extends Exception {
    public ConnectionTechnicalException() {
    }

    public ConnectionTechnicalException(String message, Throwable exception) {
        super(message, exception);
    }

    public ConnectionTechnicalException(String message) {
        super(message);
    }

    public ConnectionTechnicalException(Throwable exception) {
        super(exception);
    }

}
