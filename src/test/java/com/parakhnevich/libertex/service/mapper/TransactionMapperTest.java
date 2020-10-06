package com.parakhnevich.libertex.service.mapper;

import java.math.BigDecimal;
import java.util.Optional;

import com.parakhnevich.libertex.api.dto.request.TransactionCreateRequest;
import com.parakhnevich.libertex.api.dto.response.TransactionResponse;
import com.parakhnevich.libertex.constant.OperationName;
import com.parakhnevich.libertex.constant.TransactionStatus;
import com.parakhnevich.libertex.context.MapStructTestContext;
import com.parakhnevich.libertex.error.exception.AccountNotFoundException;
import com.parakhnevich.libertex.repository.AccountRepository;
import com.parakhnevich.libertex.repository.entity.AccountEntity;
import com.parakhnevich.libertex.repository.entity.TransactionEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = MapStructTestContext.class)
public class TransactionMapperTest {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionMapper transactionMapper;

    @Test
    void testMapEntityToResponse() {
        //given
        TransactionEntity transactionEntity = createTransactionEntity();

        //when
        TransactionResponse transactionResponse = transactionMapper.mapEntityToResponse(transactionEntity);

        //then
        assertThat(transactionResponse.getId()).isEqualTo(transactionEntity.getId());
        assertThat(transactionResponse.getStatus()).isEqualTo(transactionEntity.getStatus());
        assertThat(transactionResponse.getCurrency()).isEqualTo(transactionEntity.getCurrency());
        assertThat(transactionResponse.getAmount()).isEqualTo(new BigDecimal("10.00"));
        assertThat(transactionResponse.getAccountId()).isEqualTo(transactionEntity.getAccount().getId());
        assertThat(transactionResponse.getOperation()).isEqualTo(transactionEntity.getOperation());
    }

    @Test
    void testMapTransactionCreateRequestToEntity() {
        //given
        TransactionCreateRequest request = createTransactionCreateRequest();

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(1L);
        when(accountRepository.findById(request.getAccountId())).thenReturn(Optional.of(accountEntity));
        //when
        TransactionEntity entity = transactionMapper.mapRequestToEntity(request);
        //then
        assertThat(entity.getOperation()).isEqualTo(request.getOperation());
        assertThat(entity.getCurrency()).isEqualTo(request.getCurrency());
        assertThat(entity.getAmount()).isEqualTo(new BigDecimal("10.00"));
        assertThat(entity.getStatus()).isEqualTo(TransactionStatus.INITIATED);
        assertThat(entity.getAccount().getId()).isEqualTo(request.getAccountId());
    }

    @Test
    void testMapTransactionCreateRequestToEntityWhenAccountNotFound() {
        //given
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when then
        assertThatThrownBy(() -> transactionMapper.mapRequestToEntity(createTransactionCreateRequest()))
                .isInstanceOf(AccountNotFoundException.class);
    }

    private TransactionEntity createTransactionEntity() {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(1L);
        transactionEntity.setOperation(OperationName.CREDIT);
        transactionEntity.setAmount(BigDecimal.TEN);
        AccountEntity account = new AccountEntity();
        account.setId(1L);
        transactionEntity.setAccount(account);
        return transactionEntity;
    }

    private TransactionCreateRequest createTransactionCreateRequest() {
        TransactionCreateRequest request = new TransactionCreateRequest();
        request.setAccountId(1L);
        request.setAmount(BigDecimal.TEN);
        request.setOperation(OperationName.CREDIT);
        return request;
    }

}
