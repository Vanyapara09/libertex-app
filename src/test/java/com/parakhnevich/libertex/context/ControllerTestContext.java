package com.parakhnevich.libertex.context;

import com.parakhnevich.libertex.service.AccountService;
import com.parakhnevich.libertex.service.TransactionService;
import com.parakhnevich.libertex.service.mapper.AccountMapper;
import com.parakhnevich.libertex.service.mapper.TransactionMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.parakhnevich.libertex.error"})
public class ControllerTestContext {

    @MockBean
    private AccountService accountService;
    @MockBean
    private TransactionService transactionService;
    @MockBean
    private AccountMapper accountMapper;
    @MockBean
    private TransactionMapper transactionMapper;

}
