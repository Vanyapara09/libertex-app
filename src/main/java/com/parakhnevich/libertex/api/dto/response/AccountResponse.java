package com.parakhnevich.libertex.api.dto.response;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AccountResponse {

    private Long id;
    private BigDecimal balance;
    private String currency;

}
