package com.parakhnevich.libertex.api.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AmountDto {

    private BigDecimal amount;
    private String currency;
}
