package com.exchange.bank.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExchangeRateDto (
        Long id,
        String code,
        String name,
        BigDecimal rate,
        LocalDate rateDate
){
}
