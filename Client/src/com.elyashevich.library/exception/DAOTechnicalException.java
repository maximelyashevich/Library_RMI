package com.elyashevich.library.exception;

public class DAOTechnicalException extends Exception {
    public DAOTechnicalException() {
    }

    public DAOTechnicalException(String message, Throwable exception) {
        super(message, exception);
    }

    public DAOTechnicalException(String message) {
        super(message);
    }

    public DAOTechnicalException(Throwable exception) {
        super(exception);
    }

}
