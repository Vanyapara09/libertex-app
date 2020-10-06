package com.parakhnevich.libertex.service;

import java.util.Optional;

import com.parakhnevich.libertex.error.exception.AccountNotFoundException;
import com.parakhnevich.libertex.repository.AccountRepository;
import com.parakhnevich.libertex.repository.entity.AccountEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void testGetAccount() {
        //given
        AccountEntity accountEntity = new AccountEntity();
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(accountEntity));

        //when
        AccountEntity account = accountService.getAccount(1L);
        //then
        assertThat(account).isSameAs(accountEntity);
    }

    @Test
    void testGetAccountWhenAccountNotFound() {
        //given
        //when
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> accountService.getAccount(1L))
                .isInstanceOf(AccountNotFoundException.class);
    }

}
