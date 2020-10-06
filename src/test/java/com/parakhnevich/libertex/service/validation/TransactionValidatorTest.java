package com.parakhnevich.libertex.service.validation;

import com.parakhnevich.libertex.constant.TransactionStatus;
import com.parakhnevich.libertex.error.exception.IncorrectCurrencyException;
import com.parakhnevich.libertex.error.exception.TransactionAlreadyProcessedException;
import com.parakhnevich.libertex.repository.entity.AccountEntity;
import com.parakhnevich.libertex.repository.entity.TransactionEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class TransactionValidatorTest {

    Validator validator = new TransactionValidator();

    @Test
    void testValidateStatusFailed() {
        //given
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setStatus(TransactionStatus.SUCCESS);
        //when then
        assertThatThrownBy(() -> validator.validate(transactionEntity))
                .isInstanceOf(TransactionAlreadyProcessedException.class);
    }

    @Test
    void testValidateCurrencyFailed() {
        //given
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setStatus(TransactionStatus.INITIATED);
        transactionEntity.setCurrency("USD");

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setCurrency("BYN");
        transactionEntity.setAccount(accountEntity);
        //when then
        assertThatThrownBy(() -> validator.validate(transactionEntity))
                .isInstanceOf(IncorrectCurrencyException.class);
    }

    @Test
    void testValidateSuccess() {
        //given
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setStatus(TransactionStatus.INITIATED);
        transactionEntity.setCurrency("USD");

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setCurrency("USD");
        transactionEntity.setAccount(accountEntity);
        //when then
        assertThatCode(() -> validator.validate(transactionEntity))
                .doesNotThrowAnyException();
    }
}
