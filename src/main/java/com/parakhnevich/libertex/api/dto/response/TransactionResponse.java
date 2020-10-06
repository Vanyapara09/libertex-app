package com.parakhnevich.libertex.api.dto.response;

import java.math.BigDecimal;

import com.parakhnevich.libertex.constant.OperationName;
import com.parakhnevich.libertex.constant.TransactionStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TransactionResponse {

    private Long id;
    private OperationName operation;
    private TransactionStatus status;
    private BigDecimal amount;
    private Long accountId;
    private String currency;

}
