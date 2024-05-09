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
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Transactional
@Service
public class ConversionService {
    final ConversionRepository conversionRepository;
    final ExchangeRateService exchangeRateService;
    final ConversionMapper conversionMapper;
    final UserService userService;
    final CalculateService calculateService;
    final ConversionSpecification conversionSpecification;

    public Page<ConversionDto> getHistory(Long id, Pageable pageable) {
        if (Objects.isNull(id)) {
            return conversionRepository.findAll(pageable)
                    .map(conversionMapper::toDto);
        }

        return conversionRepository.findAllByUser(
                User.builder().id(id).build(), pageable)
                .map(conversionMapper::toDto);
    }

    public ConversionDto convert(ConversionRequest conversionRequest) {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        User user = userService.getUserDetailsByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User: %s is not found".formatted(username)));

        ExchangeRate exchangeRateFrom = exchangeRateService.findExchangeRate(conversionRequest.currencyFro());
        ExchangeRate exchangeRateTo = exchangeRateService.findExchangeRate(conversionRequest.currencyTo());;

        LocalDate rateDate = exchangeRateFrom.getRateDate();

        BigDecimal fromValue = conversionRequest.fromValue();
        BigDecimal toValue = calculateService.calculate(fromValue, exchangeRateFrom.getRate(), exchangeRateTo.getRate());
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
}
