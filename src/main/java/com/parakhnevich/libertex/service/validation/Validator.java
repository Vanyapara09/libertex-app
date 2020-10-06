package com.parakhnevich.libertex.service.validation;

import com.parakhnevich.libertex.repository.entity.TransactionEntity;

public interface Validator {

    void validate(TransactionEntity transactionEntity);
}
