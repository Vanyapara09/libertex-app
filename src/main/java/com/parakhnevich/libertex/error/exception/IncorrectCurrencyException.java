package com.parakhnevich.libertex.error.exception;

public class IncorrectCurrencyException extends ValidationException {

    public IncorrectCurrencyException(String currency, Long accountId) {
        super(String.format("%s is incorrect currency for account with id = %s", currency, accountId));
    }
}
