package com.parakhnevich.libertex.error.exception;

public class TransactionNotFoundException extends NotFoundException {

    private static final String DEFAULT_MESSAGE = "Transaction with id: %s is not found";

    public TransactionNotFoundException(Long id) {
        super(String.format(DEFAULT_MESSAGE, id));
    }

}
