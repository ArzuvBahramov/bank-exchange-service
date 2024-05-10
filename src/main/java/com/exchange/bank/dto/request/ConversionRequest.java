package com.exchange.bank.dto.request;

import java.math.BigDecimal;

public record ConversionRequest(
    String currencyFrom,
    String currencyTo,
    BigDecimal fromValue
) {
}
