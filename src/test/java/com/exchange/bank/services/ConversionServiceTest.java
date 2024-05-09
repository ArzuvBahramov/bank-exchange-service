package com.exchange.bank.services;

import com.exchange.bank.dao.entities.Conversion;
import com.exchange.bank.dao.entities.ExchangeRate;
import com.exchange.bank.dao.entities.User;
import com.exchange.bank.dao.repositories.ConversionRepository;
import com.exchange.bank.dao.specification.ConversionSpecification;
import com.exchange.bank.dto.ConversionDto;
import com.exchange.bank.dto.request.ConversionRequest;
import com.exchange.bank.mapper.ConversionMapper;
import com.exchange.bank.utils.CalculateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
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
    @MockBean
    private ConversionSpecification conversionSpecification;

    @Test
    public void testGetHistory() {
        Pageable pageable = mock(Pageable.class);
        Page<ConversionDto> page = mock(Page.class);
        Specification<Conversion> specification = mock(Specification.class);
        when(page.map(any())).thenReturn(mock(Page.class));

        when(conversionSpecification.getAllHistorySpecification("USD",
                "AUD",
                "test",
                LocalDate.now())).thenReturn(specification);

        when(conversionRepository.findAll(specification, pageable)).thenReturn(mockCollectionOfConversions());

        assertNotNull(conversionService.getHistory("USD", "AUD", "test", LocalDate.now(), pageable));
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

    private static Conversion mockConversion() {
        return Conversion.builder()
                .id(1L)
                .fromExchange(null)
                .toExchange(null)
                .fromValue(new BigDecimal(100))
                .toValue(new BigDecimal(100))
                .rateDate(LocalDate.now()).build();
    }

    private static Page<Conversion> mockCollectionOfConversions() {
        return new PageImpl<>(List.of((mockConversion())));
    }
}
