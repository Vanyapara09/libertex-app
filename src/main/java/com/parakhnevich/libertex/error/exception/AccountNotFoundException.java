package com.parakhnevich.libertex.error.exception;

public class AccountNotFoundException extends NotFoundException {

    private static final String DEFAULT_MESSAGE = "Account with id: %s is not found";

    public AccountNotFoundException(Long id) {
        super(String.format(DEFAULT_MESSAGE, id));
    }

}
