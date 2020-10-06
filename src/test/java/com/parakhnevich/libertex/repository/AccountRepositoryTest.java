package com.parakhnevich.libertex.repository;

import java.math.BigDecimal;
import java.util.Optional;

import com.parakhnevich.libertex.repository.entity.AccountEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void testFindById() {
        //given
        //when
        Optional<AccountEntity> result = accountRepository.findById(1L);

        //then
        assertThat(result).isPresent();
        assertThat(result.get().getBalance()).isEqualTo(new BigDecimal("200.00"));
    }

}
