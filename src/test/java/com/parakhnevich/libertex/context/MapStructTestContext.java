package com.parakhnevich.libertex.context;

import com.parakhnevich.libertex.repository.AccountRepository;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * MapStruct test context that load all mapper beans
 */
@Configuration
@ComponentScan(basePackages = {"com.parakhnevich.libertex.service.mapper"})
@AutoConfigureJson
public class MapStructTestContext {

    @MockBean
    private AccountRepository accountRepository;

}
