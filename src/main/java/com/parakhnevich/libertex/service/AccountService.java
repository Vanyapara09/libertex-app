package com.parakhnevich.libertex.service;

import com.parakhnevich.libertex.repository.entity.AccountEntity;

public interface AccountService {

    AccountEntity getAccount(Long id);

}
