package com.parakhnevich.libertex.error.exception;

import com.parakhnevich.libertex.constant.TransactionStatus;

public class TransactionAlreadyProcessedException extends RuntimeException {

    public TransactionAlreadyProcessedException(Long transactionId) {
        super(String.format("Transaction with id = %s has been already processed", transactionId));
    }
}
