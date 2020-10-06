package com.parakhnevich.libertex.service.mapper;

import java.math.BigDecimal;

import com.parakhnevich.libertex.api.dto.response.AccountResponse;
import com.parakhnevich.libertex.api.dto.response.TransactionResponse;
import com.parakhnevich.libertex.config.MapperConfiguration;
import com.parakhnevich.libertex.repository.entity.AccountEntity;
import com.parakhnevich.libertex.repository.entity.TransactionEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfiguration.class)
public abstract class AccountMapper {

    @Mapping(target = "balance", ignore = true)
    public abstract AccountResponse mapEntityToResponse(AccountEntity accountEntity);

    @AfterMapping
    public void mapBalance(AccountEntity accountEntity, @MappingTarget AccountResponse accountResponse) {
        accountResponse.setBalance(accountEntity.getBalance().setScale(2, BigDecimal.ROUND_HALF_UP));
    }
}
