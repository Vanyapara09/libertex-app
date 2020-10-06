package com.parakhnevich.libertex.api.dto.request;

import java.math.BigDecimal;
import java.util.Currency;

import com.parakhnevich.libertex.constant.OperationName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TransactionCreateRequest {

    private OperationName operation;
    private Long accountId;
    private BigDecimal amount;
    private String currency;

}
