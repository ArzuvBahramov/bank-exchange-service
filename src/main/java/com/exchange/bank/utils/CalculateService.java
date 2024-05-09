package com.exchange.bank.utils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CalculateService {

    @Value("${DECIMAL_FORMAT}")
    String DECIMAL_FORMAT;

    public BigDecimal calculate(BigDecimal fromValue, BigDecimal fromRate, BigDecimal toRate) {
        BigDecimal result = fromValue.divide(fromRate, 10, RoundingMode.HALF_UP).multiply(toRate);
        DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_FORMAT);
        return new BigDecimal(decimalFormat.format(result));
    }
}
