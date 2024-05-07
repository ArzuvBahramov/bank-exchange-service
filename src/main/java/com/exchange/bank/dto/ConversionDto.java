package com.exchange.bank.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ConversionDto (
        Long id,
        ExchangeRateDto from,
        ExchangeRateDto to,
        BigDecimal fromValue,
        BigDecimal toValue,
        LocalDate rateDate,
        UserDto user
) {
}
