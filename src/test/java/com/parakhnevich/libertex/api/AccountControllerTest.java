package com.parakhnevich.libertex.api;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parakhnevich.libertex.api.dto.response.AccountResponse;
import com.parakhnevich.libertex.context.ControllerTestContext;
import com.parakhnevich.libertex.error.exception.AccountNotFoundException;
import com.parakhnevich.libertex.repository.entity.AccountEntity;
import com.parakhnevich.libertex.service.AccountService;
import com.parakhnevich.libertex.service.mapper.AccountMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.ResourceUtils;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(value = ControllerTestContext.class)
@ActiveProfiles("test")
public class AccountControllerTest {

    private static final String ACCOUNT_API_PATH = "/api/v1/accounts";

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    @Test
    void testGetAccount() throws Exception {
        //given
        AccountEntity accountEntity = new AccountEntity();
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setId(1L);
        accountResponse.setBalance(BigDecimal.TEN);
        accountResponse.setCurrency("USD");

        JsonNode node = readRequestResponse("getAccount.json");

        when(accountService.getAccount(1L)).thenReturn(accountEntity);
        when(accountMapper.mapEntityToResponse(accountEntity)).thenReturn(accountResponse);

        //when then
        mvc.perform(
                MockMvcRequestBuilders.get(ACCOUNT_API_PATH + "/{accountId}", 1)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(node.get("response").toString(), true));
    }

    @Test
    void testGetNonExistingAccount() throws Exception {
        //given
        JsonNode node = readRequestResponse("getAccountNonExisting.json");
        when(accountService.getAccount(1L))
                .thenThrow(new AccountNotFoundException(1L));

        //when then
        mvc.perform(
                MockMvcRequestBuilders
                        .get(ACCOUNT_API_PATH + "/{accountId}", 1)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(content().json(node.get("response").toString(), false));
    }

    private JsonNode readRequestResponse(String name) throws IOException {
        return objectMapper.readTree(ResourceUtils.getFile("classpath:api/" + name));
    }

}
