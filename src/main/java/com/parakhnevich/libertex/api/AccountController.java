package com.parakhnevich.libertex.api;

import com.parakhnevich.libertex.api.dto.response.AccountResponse;
import com.parakhnevich.libertex.repository.entity.AccountEntity;
import com.parakhnevich.libertex.service.AccountService;
import com.parakhnevich.libertex.service.mapper.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    public AccountController(AccountService accountService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    /**
     * Retrieve account by id
     *
     * @return transaction response dto
     */
    @GetMapping(value = "/{accountId}")
    public AccountResponse getAccount(@PathVariable Long accountId) {
        log.debug("Retrieve account by id: {}", accountId);
        AccountEntity account = accountService.getAccount(accountId);
        return accountMapper.mapEntityToResponse(account);
    }

}
