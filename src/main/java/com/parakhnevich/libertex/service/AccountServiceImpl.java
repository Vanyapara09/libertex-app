package com.parakhnevich.libertex.service;

import com.parakhnevich.libertex.error.exception.AccountNotFoundException;
import com.parakhnevich.libertex.repository.AccountRepository;
import com.parakhnevich.libertex.repository.entity.AccountEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountEntity getAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

}
