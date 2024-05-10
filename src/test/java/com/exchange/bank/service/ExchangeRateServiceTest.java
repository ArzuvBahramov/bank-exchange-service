package com.exchange.bank.service;

import com.exchange.bank.centralbankapi.CentralBankService;
import com.exchange.bank.dao.entity.ExchangeRate;
import com.exchange.bank.dao.repository.ExchangeRateRepository;
import com.exchange.bank.dto.ExchangeRateDto;
import com.exchange.bank.mapper.ExchangeRateMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig({ExchangeRateService.class})
public class ExchangeRateServiceTest {
    @Autowired
    private ExchangeRateService exchangeRateService;

    @MockBean
    private ExchangeRateRepository exchangeRateRepository;
    @MockBean
    private CentralBankService centralBankService;
    @MockBean
    private ExchangeRateMapper exchangeRateMapper;

    @Test
    public void testFindAllExchangers() {
        Set<ExchangeRate> exchangeRates = new HashSet<>();
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setRateDate(LocalDate.now());
        exchangeRates.add(exchangeRate);

        when(exchangeRateRepository.findTop1ByOrderByRateDateDesc()).thenReturn(exchangeRates);
        when(exchangeRateRepository.findExchangerByRateDate(any(LocalDate.class))).thenReturn(exchangeRates);
        when(exchangeRateMapper.toDto(any(ExchangeRate.class))).thenAnswer(invocation -> {
            ExchangeRate rate = invocation.getArgument(0);
            return new ExchangeRateDto(rate.getId(), rate.getCode(), rate.getName(), rate.getRate(), rate.getRateDate());
        });

        Set<ExchangeRateDto> result = exchangeRateService.findAllExchangers();

        assertFalse(result.isEmpty());
        verify(exchangeRateRepository).findTop1ByOrderByRateDateDesc();
        verify(exchangeRateRepository).findExchangerByRateDate(any(LocalDate.class));
        verify(exchangeRateMapper, times(exchangeRates.size())).toDto(any(ExchangeRate.class));
    }

    @Test
    public void testFindExchangeRate() {
        String code = "USD";
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setCode(code);
        exchangeRate.setRateDate(LocalDate.now());

        when(exchangeRateRepository.findExchangerByCodeAndRateDate(eq(code), any(LocalDate.class))).thenReturn(Optional.of(exchangeRate));
        when(exchangeRateRepository.findExchangeRateByCodeOrderByRateDateDesc(eq(code))).thenReturn(Optional.of(exchangeRate));

        ExchangeRate result = exchangeRateService.findExchangeRate(code);

        assertNotNull(result);
        assertEquals(code, result.getCode());
        verify(exchangeRateRepository).findExchangerByCodeAndRateDate(eq(code), any(LocalDate.class));
    }

    @Test
    public void testRetrieveData() {
        Set<ExchangeRate> exchangeRates = new HashSet<>();
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRates.add(exchangeRate);

        when(centralBankService.getRatesFromECB()).thenReturn(exchangeRates);
        when(exchangeRateRepository.saveAll(any(Set.class))).thenReturn(new ArrayList<>(exchangeRates));

        List<ExchangeRate> result = exchangeRateService.retrieveData();

        assertFalse(result.isEmpty());
        verify(centralBankService).getRatesFromECB();
        verify(exchangeRateRepository).saveAll(any(Set.class));
    }
}
