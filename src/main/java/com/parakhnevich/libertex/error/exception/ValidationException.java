package com.parakhnevich.libertex.error.exception;

public abstract class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
