package com.exchange.bank.services;

import com.exchange.bank.dao.entities.Conversion;
import com.exchange.bank.dao.entities.ExchangeRate;
import com.exchange.bank.dao.entities.User;
import com.exchange.bank.dao.repositories.ConversionRepository;
import com.exchange.bank.dto.ConversionDto;
import com.exchange.bank.dto.request.ConversionRequest;
import com.exchange.bank.mapper.ConversionMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Transactional
@Service
public class ConversionService {
    final ConversionRepository conversionRepository;
    final ExchangeService exchangeService;
    final ConversionMapper conversionMapper;
    final UserService userService;

    @Value("${DECIMAL_FORMAT}")
    String DECIMAL_FORMAT;

    public Page<ConversionDto> getHistory(Long id, Pageable pageable) {
        if (Objects.isNull(id)) {
            return conversionRepository.findAll(pageable)
                    .map(conversionMapper::toDto);
        }

        return conversionRepository.findAllByUser(
                User.builder().id(id).build(), pageable)
                .map(conversionMapper::toDto);
    }

    public ConversionDto calculate(ConversionRequest conversionRequest) {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User: %s is not found".formatted(username)));

        ExchangeRate exchangeRateFrom = exchangeService.findExchangeRate(conversionRequest.currencyFro());
        ExchangeRate exchangeRateTo = exchangeService.findExchangeRate(conversionRequest.currencyTo());;

        LocalDate rateDate = exchangeRateFrom.getRateDate();

        BigDecimal fromValue = conversionRequest.fromValue();
        BigDecimal toValue = calculate(fromValue, exchangeRateFrom.getRate(), exchangeRateTo.getRate());
        Conversion conversion = Conversion.builder()
                .fromExchange(exchangeRateFrom)
                .toExchange(exchangeRateTo)
                .rateDate(rateDate)
                .fromValue(fromValue)
                .toValue(toValue)
                .user(user).build();
        conversionRepository.save(conversion);

        return conversionMapper.toDto(conversion);
    }

    private BigDecimal calculate(BigDecimal fromValue, BigDecimal fromRate, BigDecimal toRate) {
        BigDecimal result = fromValue.divide(fromRate, 10, RoundingMode.HALF_UP).multiply(toRate);
        DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_FORMAT);
        return new BigDecimal(decimalFormat.format(result));
    }
}
