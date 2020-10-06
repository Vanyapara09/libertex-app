package com.parakhnevich.libertex.service.mapper;

import java.math.BigDecimal;

import com.parakhnevich.libertex.api.dto.request.TransactionCreateRequest;
import com.parakhnevich.libertex.api.dto.response.TransactionResponse;
import com.parakhnevich.libertex.config.MapperConfiguration;
import com.parakhnevich.libertex.error.exception.AccountNotFoundException;
import com.parakhnevich.libertex.repository.AccountRepository;
import com.parakhnevich.libertex.repository.entity.AccountEntity;
import com.parakhnevich.libertex.repository.entity.TransactionEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(config = MapperConfiguration.class)
public abstract class TransactionMapper {

    @Autowired
    private AccountRepository accountRepository;

    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "amount", ignore = true)
    public abstract TransactionResponse mapEntityToResponse(TransactionEntity transactionEntity);

    @Mapping(target = "account", source = "accountId")
    @Mapping(target = "status", constant = "INITIATED")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "amount", ignore = true)
    public abstract TransactionEntity mapRequestToEntity(TransactionCreateRequest transactionCreateRequest);

    protected AccountEntity getExistingAccountEntity(Long id) {
        AccountEntity result = null;
        if (id != null) {
            result = accountRepository.findById(id)
                    .orElseThrow(() -> new AccountNotFoundException(id));
        }
        return result;
    }

    @AfterMapping
    public void mapAmount(TransactionEntity transactionEntity, @MappingTarget TransactionResponse transactionResponse) {
        transactionResponse.setAmount(transactionEntity.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    @AfterMapping
    public void mapAmount(TransactionCreateRequest request, @MappingTarget TransactionEntity transactionEntity) {
        transactionEntity.setAmount(request.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
    }

}
