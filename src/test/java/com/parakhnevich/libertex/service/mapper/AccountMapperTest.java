package com.parakhnevich.libertex.service.mapper;

import java.math.BigDecimal;

import com.google.common.collect.Lists;
import com.parakhnevich.libertex.api.dto.response.AccountResponse;
import com.parakhnevich.libertex.context.MapStructTestContext;
import com.parakhnevich.libertex.repository.entity.AccountEntity;
import com.parakhnevich.libertex.repository.entity.TransactionEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = MapStructTestContext.class)
public class AccountMapperTest {

    @Autowired
    private AccountMapper accountMapper;

    @Test
    void testEntityToResponse() {
        //given
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(1L);
        accountEntity.setBalance(BigDecimal.TEN);
        accountEntity.setVersion(1);
        accountEntity.setTransactions(Lists.newArrayList(new TransactionEntity()));
        accountEntity.setCurrency("USD");

        //when
        AccountResponse accountResponse = accountMapper.mapEntityToResponse(accountEntity);

        //then
        assertThat(accountResponse.getId()).isEqualTo(accountEntity.getId());
        assertThat(accountResponse.getCurrency()).isEqualTo(accountEntity.getCurrency());
        assertThat(accountResponse.getBalance()).isEqualTo(new BigDecimal("10.00"));
    }
}
