package com.exchange.bank.services;

import com.exchange.bank.dao.entities.Conversion;
import com.exchange.bank.dao.entities.ExchangeRate;
import com.exchange.bank.dao.entities.User;
import com.exchange.bank.dao.repositories.ConversionRepository;
import com.exchange.bank.dto.ConversionDto;
import com.exchange.bank.dto.request.ConversionRequest;
import com.exchange.bank.mapper.ConversionMapper;
import com.exchange.bank.utils.CalculateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig({ConversionService.class})
public class ConversionServiceTest {
    @Autowired
    private ConversionService conversionService;

    @MockBean
    private ConversionRepository conversionRepository;
    @MockBean
    private ExchangeRateService exchangeRateService;
    @MockBean
    private ConversionMapper conversionMapper;
    @MockBean
    private UserService userService;
    @MockBean
    private CalculateUtil calculateUtil;

    @Test
    public void testGetHistory() {
        Pageable pageable = mock(Pageable.class);
        Page<Conversion> page = mock(Page.class);
        when(conversionRepository.findAll(pageable)).thenReturn(page);
        when(page.map(any())).thenReturn(mock(Page.class));

        assertNotNull(conversionService.getHistory(null, pageable));
        verify(conversionRepository).findAll(pageable);
    }

    @Test
    public void testCalculate() {
        String username = "testUser";
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(username, null, Collections.singletonList(new SimpleGrantedAuthority("USER")))
        );

        User user = new User();
        user.setUsername(username);
        ExchangeRate exchangeRateFrom = new ExchangeRate();
        exchangeRateFrom.setRate(new BigDecimal("1.0"));
        ExchangeRate exchangeRateTo = new ExchangeRate();
        exchangeRateTo.setRate(new BigDecimal("0.5"));
        ConversionRequest conversionRequest = new ConversionRequest("USD", "EUR", new BigDecimal("100"));

        when(userService.getUserDetailsByUsername(username)).thenReturn(Optional.of(user));
        when(exchangeRateService.findExchangeRate("USD")).thenReturn(exchangeRateFrom);
        when(exchangeRateService.findExchangeRate("EUR")).thenReturn(exchangeRateTo);
        when(conversionMapper.toDto(any(Conversion.class))).thenReturn(new ConversionDto(1L, null, null, new BigDecimal("100"), new BigDecimal("50"), LocalDate.now(), null, LocalDateTime.now()));

        ConversionDto result = conversionService.convert(conversionRequest);

        assertNotNull(result);
        assertEquals(new BigDecimal("50"), result.toValue());
        verify(userService).getUserDetailsByUsername(username);
        verify(exchangeRateService).findExchangeRate("USD");
        verify(exchangeRateService).findExchangeRate("EUR");
        verify(conversionRepository).save(any(Conversion.class));
    }
}
