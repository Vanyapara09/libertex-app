package com.parakhnevich.libertex.service;

import java.math.BigDecimal;

import com.parakhnevich.libertex.constant.OperationName;
import com.parakhnevich.libertex.constant.TransactionStatus;
import com.parakhnevich.libertex.error.exception.NotEnoughFundsException;
import com.parakhnevich.libertex.repository.TransactionRepository;
import com.parakhnevich.libertex.repository.entity.AccountEntity;
import com.parakhnevich.libertex.repository.entity.TransactionEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DebitOperationTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private DebitOperation debitOperation;

    @Test
    void testExecuteWhenNoEnoughFunds() {
        //given
        TransactionEntity transactionEntity = createTransaction(BigDecimal.TEN, BigDecimal.ONE);

        //when then
        assertThatThrownBy(() -> debitOperation.execute(transactionEntity))
                .isInstanceOf(NotEnoughFundsException.class);
    }

    @Test
    void testExecuteWhenAccountHasBalanceMoreThanWithdrawAmount() {
        //given
        TransactionEntity transactionEntity = createTransaction(BigDecimal.ONE, BigDecimal.TEN);

        when(transactionRepository.save(transactionEntity)).thenReturn(transactionEntity);
        //when
        debitOperation.execute(transactionEntity);
        //then
        assertThat(transactionEntity.getAccount().getBalance()).isEqualTo(BigDecimal.TEN.subtract(BigDecimal.ONE));
        assertThat(transactionEntity.getStatus()).isEqualTo(TransactionStatus.SUCCESS);
    }

    @Test
    void testExecuteWhenAccountHasBalanceEqualsWithdrawAmount() {
        //given
        TransactionEntity transactionEntity = createTransaction(BigDecimal.TEN, BigDecimal.TEN);

        when(transactionRepository.save(transactionEntity)).thenReturn(transactionEntity);
        //when
        debitOperation.execute(transactionEntity);
        //then
        assertThat(transactionEntity.getAccount().getBalance()).isEqualTo(BigDecimal.ZERO);
        assertThat(transactionEntity.getStatus()).isEqualTo(TransactionStatus.SUCCESS);
    }

    @Test
    void testGetStrategyName() {
        //given when then
        assertThat(debitOperation.getStrategyName()).isEqualTo(OperationName.DEBIT);
    }

    private TransactionEntity createTransaction(BigDecimal transactionAmount, BigDecimal accountBalance) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAmount(transactionAmount);

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setBalance(accountBalance);
        transactionEntity.setAccount(accountEntity);
        return transactionEntity;
    }

}
