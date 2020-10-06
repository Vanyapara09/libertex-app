package com.parakhnevich.libertex.error.exception;

public class NotEnoughFundsException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "There are not enough funds on your balance";

    public NotEnoughFundsException() {
        super(DEFAULT_MESSAGE);
    }

}
