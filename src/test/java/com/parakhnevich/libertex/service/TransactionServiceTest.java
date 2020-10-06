package com.parakhnevich.libertex.service;

import java.math.BigDecimal;
import java.util.Optional;

import com.parakhnevich.libertex.constant.OperationName;
import com.parakhnevich.libertex.constant.TransactionStatus;
import com.parakhnevich.libertex.error.exception.TransactionNotFoundException;
import com.parakhnevich.libertex.repository.TransactionRepository;
import com.parakhnevich.libertex.repository.entity.AccountEntity;
import com.parakhnevich.libertex.repository.entity.TransactionEntity;
import com.parakhnevich.libertex.service.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private OperationStrategyFactory strategyFactory;
    @Mock
    private Validator transactionValidator;

    private TransactionService transactionService;

    @BeforeEach
    public void init() {
        RetryTemplate retryTemplate = spy(buildRetryTemplate());
        transactionService = new TransactionServiceImpl(transactionRepository, transactionValidator, strategyFactory, retryTemplate);
    }

    @Test
    void testGetTransaction() {
        //given
        TransactionEntity transactionEntity = new TransactionEntity();
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transactionEntity));

        //when
        TransactionEntity transaction = transactionService.getTransaction(1L);
        //then
        assertThat(transaction).isSameAs(transactionEntity);
    }

    @Test
    void testGetTransactionWhenTransactionNotFound() {
        //given
        //when
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> transactionService.getTransaction(1L))
                .isInstanceOf(TransactionNotFoundException.class);
    }

    @Test
    void testCreateTransaction() {
        //given
        TransactionEntity transactionEntity = new TransactionEntity();
        when(transactionRepository.save(any())).thenReturn(transactionEntity);

        //when
        TransactionEntity transaction = transactionService.createTransaction(transactionEntity);
        //then
        assertThat(transaction).isSameAs(transactionEntity);
    }

    @Test
    void testExecuteTransactionWhenTransactionNotFound() {
        //given
        //when
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> transactionService.executeTransaction(1L))
                .isInstanceOf(TransactionNotFoundException.class);
    }

    @Test
    void testExecuteTransactionSuccess() {
        //given
        TransactionEntity transactionEntity = createTransactionEntity();
        OperationStrategy operationStrategy = mock(OperationStrategy.class);

        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transactionEntity));
        doNothing().when(transactionValidator).validate(transactionEntity);
        when(strategyFactory.getStrategy(any(OperationName.class))).thenReturn(operationStrategy);
        when(operationStrategy.execute(any())).thenReturn(transactionEntity);

        //when
        TransactionEntity executedTransaction = transactionService.executeTransaction(1L);
        //then
        assertThat(executedTransaction).isSameAs(transactionEntity);
    }

    @Test
    void testExecuteTransactionFailedWithRetry() {
        //given
        TransactionEntity transactionEntity = createTransactionEntity();
        OperationStrategy operationStrategy = mock(OperationStrategy.class);

        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transactionEntity));
        doNothing().when(transactionValidator).validate(transactionEntity);
        when(strategyFactory.getStrategy(any(OperationName.class))).thenReturn(operationStrategy);
        doThrow(RuntimeException.class)
                .doThrow(RuntimeException.class)
                .when(operationStrategy).execute(any());
        doNothing().when(transactionRepository).updateStatus(anyLong(), any());

        //when
        //then
        assertThatThrownBy(() -> transactionService.executeTransaction(1L)).isInstanceOf(RuntimeException.class);
        verify(transactionRepository).updateStatus(eq(1L), eq(TransactionStatus.FAILED));
        verify(operationStrategy, times(2)).execute(any(TransactionEntity.class));
    }

    private TransactionEntity createTransactionEntity() {
        TransactionEntity transactionEntity = new TransactionEntity();
        AccountEntity account = new AccountEntity();
        account.setId(1L);
        transactionEntity.setAccount(account);
        transactionEntity.setAmount(BigDecimal.ONE);
        transactionEntity.setOperation(OperationName.CREDIT);
        return transactionEntity;
    }

    private RetryTemplate buildRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(new SimpleRetryPolicy(2));
        return retryTemplate;
    }

}
