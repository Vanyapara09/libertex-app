package com.parakhnevich.libertex.service;

import java.math.BigDecimal;

import com.parakhnevich.libertex.constant.OperationName;
import com.parakhnevich.libertex.constant.TransactionStatus;
import com.parakhnevich.libertex.repository.TransactionRepository;
import com.parakhnevich.libertex.repository.entity.AccountEntity;
import com.parakhnevich.libertex.repository.entity.TransactionEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreditOperation implements OperationStrategy {

    private final TransactionRepository transactionRepository;

    public CreditOperation(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public OperationName getStrategyName() {
        return OperationName.CREDIT;
    }

    @Override
    @Transactional
    public TransactionEntity execute(TransactionEntity transactionEntity) {
        AccountEntity account = transactionEntity.getAccount();
        BigDecimal currentBalance = account.getBalance();
        BigDecimal newBalance = currentBalance.add(transactionEntity.getAmount());
        account.setBalance(newBalance);
        transactionEntity.setStatus(TransactionStatus.SUCCESS);
        return transactionRepository.save(transactionEntity);
    }

}
