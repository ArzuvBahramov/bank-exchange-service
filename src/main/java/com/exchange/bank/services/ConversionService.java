package com.exchange.bank.services;

import com.exchange.bank.dao.entities.Conversion;
import com.exchange.bank.dao.entities.ExchangeRate;
import com.exchange.bank.dao.repositories.ConversionRepository;
import com.exchange.bank.dto.ConversionDto;
import com.exchange.bank.dto.request.ConversionRequest;
import com.exchange.bank.mapper.ConversionMapper;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
public class ConversionService {
    final ConversionRepository conversionRepository;
    final ExchangeService exchangeService;
    final ConversionMapper conversionMapper;
    final String DECIMAL_FORMAT = "0.000";

    @Transactional
    public ConversionDto calculate(ConversionRequest conversionRequest) {
//        Long userId = AuthenticatedUser.getAuthenticatedUser().getId();

//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException(EXCEPTION_SERVICE, userId));

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
                .toValue(toValue).build();
//                .user(user).build();
        conversionRepository.save(conversion);

        return conversionMapper.toDto(conversion);
    }

    private BigDecimal calculate(BigDecimal fromValue, BigDecimal fromRate, BigDecimal toRate) {
        BigDecimal result = fromValue.divide(fromRate, 10, RoundingMode.HALF_UP).multiply(toRate);
        DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_FORMAT);
        return new BigDecimal(decimalFormat.format(result));
    }
}
