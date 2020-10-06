package com.parakhnevich.libertex.service;

import com.parakhnevich.libertex.repository.entity.TransactionEntity;

public interface TransactionService {

    TransactionEntity createTransaction(TransactionEntity transactionEntity);

    TransactionEntity executeTransaction(Long transactionId);

    TransactionEntity getTransaction(Long transactionId);

}
