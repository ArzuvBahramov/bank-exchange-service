package com.exchange.bank.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ConversionDto (
        Long id,
        ExchangeRateDto from,
        ExchangeRateDto to,
        BigDecimal fromValue,
        BigDecimal toValue,
        LocalDate rateDate,
        UserDto user,
        LocalDateTime createdAt
) {
}
