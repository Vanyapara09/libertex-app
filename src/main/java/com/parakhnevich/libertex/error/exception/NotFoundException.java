package com.parakhnevich.libertex.error.exception;

public abstract class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

}
