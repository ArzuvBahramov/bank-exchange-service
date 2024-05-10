package com.exchange.bank.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig({CalculateService.class})
@TestPropertySource(properties = {"DECIMAL_FORMAT=0.000"})
public class CalculateServiceTest {
    @Autowired
    private CalculateService calculateService;

    @Test
    public void testCalculate() {
        BigDecimal fromValue = new BigDecimal("100");
        BigDecimal fromRate = new BigDecimal("1.5");
        BigDecimal toRate = new BigDecimal("2");

        BigDecimal expectedResult = new BigDecimal("133.333");

        BigDecimal result = calculateService.calculate(fromValue, fromRate, toRate);

        assertEquals(expectedResult, result);
    }

}
