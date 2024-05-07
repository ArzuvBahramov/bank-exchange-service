package com.exchange.bank.mapper;

import com.exchange.bank.dao.entities.Conversion;
import com.exchange.bank.dao.entities.ExchangeRate;
import com.exchange.bank.dao.entities.User;
import com.exchange.bank.dto.ConversionDto;
import com.exchange.bank.dto.ExchangeRateDto;
import com.exchange.bank.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig({ConversionMapperImpl.class, ExchangeRateMapperImpl.class, UserMapperImpl.class})
public class ConversionMapperTest {
    @Autowired
    private ConversionMapper conversionMapper;

    @MockBean
    private ExchangeRateMapper exchangeRateMapper;

    @MockBean
    private UserMapper userMapper;

    @Test
    void toDto() {
        Conversion conversion = mockConversion();

        ExchangeRateDto fromExchangeRateDto = mockFromExchangeRateDto();
        ExchangeRateDto toExchangeRateDto = mockToExchangeRateDto();
        UserDto userDto = mockUserDto();

        when(exchangeRateMapper.toDto(any(ExchangeRate.class))).thenReturn(fromExchangeRateDto).thenReturn(toExchangeRateDto);
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);

        ConversionDto conversionDto = conversionMapper.toDto(conversion);

        assertNotNull(conversionDto);
        assertEquals(conversion.getId(), conversionDto.id());
        assertEquals(fromExchangeRateDto, conversionDto.from());
        assertEquals(toExchangeRateDto, conversionDto.to());
        assertEquals(conversion.getFromValue(), conversionDto.fromValue());
        assertEquals(conversion.getToValue(), conversionDto.toValue());
        assertEquals(conversion.getRateDate(), conversionDto.rateDate());
        assertEquals(userDto, conversionDto.user());

        verify(exchangeRateMapper, times(2)).toDto(any(ExchangeRate.class));
        verify(userMapper).toDto(any(User.class));
    }

    private static ExchangeRateDto mockToExchangeRateDto() {
        return new ExchangeRateDto(2L, "EUR", "Euro", new BigDecimal("0.9"), LocalDate.now());
    }
    private static ExchangeRateDto mockFromExchangeRateDto() {
        return new ExchangeRateDto(1L, "USD", "Dollar", new BigDecimal("1.0"), LocalDate.now());
    }
    private static UserDto mockUserDto() {
        return new UserDto(1L,
                "John", "Doe",
                "johndoe", "john.doe@example.com");
    }

    private static Conversion mockConversion() {
        return Conversion.builder()
                .id(1L)
                .fromExchange(new ExchangeRate())
                .toExchange(new ExchangeRate())
                .rateDate(LocalDate.now())
                .fromValue(new BigDecimal("100"))
                .toValue(new BigDecimal("200"))
                .user(new User())
                .build();
    }
}
