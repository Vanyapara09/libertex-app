package com.parakhnevich.libertex.service.validation;

import com.parakhnevich.libertex.constant.TransactionStatus;
import com.parakhnevich.libertex.error.exception.IncorrectCurrencyException;
import com.parakhnevich.libertex.error.exception.TransactionAlreadyProcessedException;
import com.parakhnevich.libertex.repository.entity.TransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionValidator implements Validator {

    @Override
    public void validate(TransactionEntity transactionEntity) {
        validateStatus(transactionEntity);
        validateCurrency(transactionEntity);
    }

    private void validateStatus(TransactionEntity transactionEntity) {
        if (!transactionEntity.getStatus().equals(TransactionStatus.INITIATED)) {
            throw new TransactionAlreadyProcessedException(transactionEntity.getId());
        }
    }

    private void validateCurrency(TransactionEntity transactionEntity) {
        if (!transactionEntity.getAccount().getCurrency().equals(transactionEntity.getCurrency())) {
            throw new IncorrectCurrencyException(transactionEntity.getCurrency(), transactionEntity.getAccount().getId());
        }
    }
}
