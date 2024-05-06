package com.exchange.bank.dto.request;

import java.math.BigDecimal;

public record ConversionRequest(
    String currencyFro,
    String currencyTo,
    BigDecimal fromValue
) {
}
