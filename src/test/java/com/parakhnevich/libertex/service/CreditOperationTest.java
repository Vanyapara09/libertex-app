package com.parakhnevich.libertex.service;

import java.math.BigDecimal;

import com.parakhnevich.libertex.constant.OperationName;
import com.parakhnevich.libertex.repository.TransactionRepository;
import com.parakhnevich.libertex.repository.entity.AccountEntity;
import com.parakhnevich.libertex.repository.entity.TransactionEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreditOperationTest {

    @Mock
    private TransactionRepository accountRepository;

    @InjectMocks
    private CreditOperation creditOperation;

    @Test
    void testExecute() {
        //given
        TransactionEntity transactionEntity = new TransactionEntity();
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setBalance(BigDecimal.ONE);
        transactionEntity.setAccount(accountEntity);
        transactionEntity.setAmount(BigDecimal.TEN);

        when(accountRepository.save(transactionEntity)).thenReturn(transactionEntity);
        //when
        creditOperation.execute(transactionEntity);
        //then
        assertThat(accountEntity.getBalance()).isEqualTo(BigDecimal.ONE.add(BigDecimal.TEN));
    }

    @Test
    void testGetStrategyName() {
        //given when then
        assertThat(creditOperation.getStrategyName()).isEqualTo(OperationName.CREDIT);
    }
}
