package com.parakhnevich.libertex.api;

import com.parakhnevich.libertex.api.dto.request.TransactionCreateRequest;
import com.parakhnevich.libertex.api.dto.response.TransactionResponse;
import com.parakhnevich.libertex.repository.entity.TransactionEntity;
import com.parakhnevich.libertex.service.TransactionService;
import com.parakhnevich.libertex.service.mapper.TransactionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    public TransactionController(TransactionService transactionService, TransactionMapper transactionMapper) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
    }

    /**
     * Initiate transaction
     *
     * @return transaction response dto
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(@RequestBody TransactionCreateRequest request) {
        log.debug("Initiate {} transaction for account: {}", request.getOperation(), request.getAccountId());
        TransactionEntity newTransaction = transactionMapper.mapRequestToEntity(request);
        TransactionEntity createdTransaction = transactionService.createTransaction(newTransaction);
        return transactionMapper.mapEntityToResponse(createdTransaction);
    }

    /**
     * Execute transaction that was already initiated
     *
     * @return transaction response dto
     */
    @PostMapping(value = "/{transactionId}/execute")
    public TransactionResponse executeTransaction(@PathVariable Long transactionId) {
        log.debug("Execute transaction with id: {}", transactionId);
        TransactionEntity transaction = transactionService.executeTransaction(transactionId);
        return transactionMapper.mapEntityToResponse(transaction);
    }

    /**
     * Retrieve transaction by id
     *
     * @return transaction response dto
     */
    @GetMapping(value = "/{transactionId}")
    public TransactionResponse getAccount(@PathVariable Long transactionId) {
        log.debug("Retrieve account by id: {}", transactionId);
        TransactionEntity transaction = transactionService.getTransaction(transactionId);
        return transactionMapper.mapEntityToResponse(transaction);
    }

}
