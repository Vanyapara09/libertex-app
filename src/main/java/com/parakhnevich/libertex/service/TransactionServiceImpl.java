package com.parakhnevich.libertex.service;

import com.parakhnevich.libertex.constant.TransactionStatus;
import com.parakhnevich.libertex.error.exception.NotFoundException;
import com.parakhnevich.libertex.error.exception.TransactionNotFoundException;
import com.parakhnevich.libertex.error.exception.ValidationException;
import com.parakhnevich.libertex.repository.TransactionRepository;
import com.parakhnevich.libertex.repository.entity.TransactionEntity;
import com.parakhnevich.libertex.service.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final Validator transactionValidator;
    private final OperationStrategyFactory operationStrategyFactory;
    private final RetryTemplate retryTemplate;

    public TransactionServiceImpl(TransactionRepository transactionRepository, Validator transactionValidator,
            OperationStrategyFactory operationStrategyFactory, RetryTemplate retryTemplate) {
        this.transactionRepository = transactionRepository;
        this.transactionValidator = transactionValidator;
        this.operationStrategyFactory = operationStrategyFactory;
        this.retryTemplate = retryTemplate;
    }

    @Override
    @Transactional
    public TransactionEntity createTransaction(TransactionEntity transactionEntity) {
        return transactionRepository.save(transactionEntity);
    }

    /**
     * @implNote The main idea to use retryTemplate if {@link javax.persistence.OptimisticLockException} was thrown.
     * {@link Exception} is caught because we need a failed transaction for any exception.
     *
     * @param transactionId transaction id
     * @return transaction
     */
    @Override
    public TransactionEntity executeTransaction(Long transactionId) {
        try {
            return retryTemplate.execute(retryContext -> execute(transactionId));
        } catch (Exception ex) {
            log.warn("Transaction with id = {} failed", transactionId, ex);
            transactionRepository.updateStatus(transactionId, TransactionStatus.FAILED);
            throw ex;
        }
    }

    private TransactionEntity execute(Long transactionId) {
        TransactionEntity transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));
        transactionValidator.validate(transaction);
        OperationStrategy strategy = operationStrategyFactory.getStrategy(transaction.getOperation());
        strategy.execute(transaction);
        return transaction;
    }

    @Override
    @Transactional
    public TransactionEntity getTransaction(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));
    }

}
