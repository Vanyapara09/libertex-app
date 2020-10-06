package com.parakhnevich.libertex.api;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parakhnevich.libertex.api.dto.request.TransactionCreateRequest;
import com.parakhnevich.libertex.api.dto.response.TransactionResponse;
import com.parakhnevich.libertex.constant.OperationName;
import com.parakhnevich.libertex.constant.TransactionStatus;
import com.parakhnevich.libertex.context.ControllerTestContext;
import com.parakhnevich.libertex.error.exception.TransactionNotFoundException;
import com.parakhnevich.libertex.repository.entity.TransactionEntity;
import com.parakhnevich.libertex.service.TransactionService;
import com.parakhnevich.libertex.service.mapper.TransactionMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.ResourceUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(value = ControllerTestContext.class)
@ActiveProfiles("test")
public class TransactionControllerTest {

    private static final String TRANSACTION_API_PATH = "/api/v1/transactions";

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    @Test
    void testGetTransaction() throws Exception {
        //given
        TransactionEntity transactionEntity = new TransactionEntity();
        TransactionResponse transactionResponse = createTransactionResponse(TransactionStatus.SUCCESS);

        JsonNode node = readRequestResponse("getTransaction.json");

        when(transactionService.getTransaction(1L)).thenReturn(transactionEntity);
        when(transactionMapper.mapEntityToResponse(transactionEntity)).thenReturn(transactionResponse);

        //when then
        mvc.perform(
                MockMvcRequestBuilders.get(TRANSACTION_API_PATH + "/{transactionId}", 1)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(node.get("response").toString(), true));
    }

    @Test
    void testGetNonExistingTransaction() throws Exception {
        //given
        JsonNode node = readRequestResponse("getTransactionNonExisting.json");
        when(transactionService.getTransaction(1L))
                .thenThrow(new TransactionNotFoundException(1L));

        //when then
        mvc.perform(
                MockMvcRequestBuilders
                        .get(TRANSACTION_API_PATH + "/{transactionId}", 1)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(content().json(node.get("response").toString(), false));
    }

    @Test
    void testCreateTransaction() throws Exception {
        //given
        JsonNode data = readRequestResponse("createTransaction.json");
        //when
        TransactionEntity transactionEntity = new TransactionEntity();
        when(transactionMapper.mapRequestToEntity(any(TransactionCreateRequest.class))).thenReturn(transactionEntity);
        when(transactionService.createTransaction(any(TransactionEntity.class))).thenReturn(transactionEntity);
        when(transactionMapper.mapEntityToResponse(any(TransactionEntity.class))).thenReturn(createTransactionResponse(TransactionStatus.INITIATED));

        mvc.perform(
                MockMvcRequestBuilders.post(TRANSACTION_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(data.get("request").toString()))
                .andExpect(status().isCreated())
                .andExpect(content().json(data.get("response").toString(), true));
        //then
        verify(transactionService).createTransaction(any(TransactionEntity.class));
    }

    @Test
    void testExecuteTransaction() throws Exception {
        //given
        TransactionEntity transactionEntity = new TransactionEntity();
        TransactionResponse transactionResponse = createTransactionResponse(TransactionStatus.SUCCESS);

        JsonNode node = readRequestResponse("executeTransaction.json");

        when(transactionService.executeTransaction(1L)).thenReturn(transactionEntity);
        when(transactionMapper.mapEntityToResponse(transactionEntity)).thenReturn(transactionResponse);

        //when then
        mvc.perform(
                MockMvcRequestBuilders.post(TRANSACTION_API_PATH + "/{transactionId}/execute", 1)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(node.get("response").toString(), true));
    }

    private TransactionResponse createTransactionResponse(TransactionStatus transactionStatus) {
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setAccountId(1L);
        transactionResponse.setAmount(BigDecimal.TEN);
        transactionResponse.setId(1L);
        transactionResponse.setOperation(OperationName.CREDIT);
        transactionResponse.setStatus(transactionStatus);
        transactionResponse.setCurrency("USD");
        return transactionResponse;
    }

    private JsonNode readRequestResponse(String name) throws IOException {
        return objectMapper.readTree(ResourceUtils.getFile("classpath:api/" + name));
    }

}
