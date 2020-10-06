package com.parakhnevich.libertex.service;

import java.math.BigDecimal;

import com.parakhnevich.libertex.constant.OperationName;
import com.parakhnevich.libertex.constant.TransactionStatus;
import com.parakhnevich.libertex.error.exception.NotEnoughFundsException;
import com.parakhnevich.libertex.repository.TransactionRepository;
import com.parakhnevich.libertex.repository.entity.AccountEntity;
import com.parakhnevich.libertex.repository.entity.TransactionEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class DebitOperation implements OperationStrategy {

    private final TransactionRepository transactionRepository;

    public DebitOperation(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public OperationName getStrategyName() {
        return OperationName.DEBIT;
    }

    @Override
    @Transactional
    public TransactionEntity execute(TransactionEntity transactionEntity) {
        AccountEntity account = transactionEntity.getAccount();
        BigDecimal currentBalance = account.getBalance();
        BigDecimal newBalance = currentBalance.subtract(transactionEntity.getAmount());
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughFundsException();
        }
        account.setBalance(newBalance);
        transactionEntity.setStatus(TransactionStatus.SUCCESS);
        return transactionRepository.save(transactionEntity);
    }

}
