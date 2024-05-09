package com.exchange.bank.services;

import com.exchange.bank.dao.entities.Conversion;
import com.exchange.bank.dao.entities.ExchangeRate;
import com.exchange.bank.dao.entities.User;
import com.exchange.bank.dao.repositories.ConversionRepository;
import com.exchange.bank.dto.ConversionDto;
import com.exchange.bank.dto.request.ConversionRequest;
import com.exchange.bank.mapper.ConversionMapper;
import com.exchange.bank.utils.CalculateUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Transactional
@Service
public class ConversionService {
    final ConversionRepository conversionRepository;
    final ExchangeRateService exchangeRateService;
    final ConversionMapper conversionMapper;
    final UserService userService;
    final CalculateUtil calculateUtil;

    public Page<ConversionDto> getHistory(String from,
                                          String to,
                                          String username,
                                          LocalDate dateRequest,
                                          Pageable pageable) {

        Specification<Conversion> specification = Specification.where(((root, query, criteriaBuilder) -> {
            Join<Conversion, User> conversionUserJoin = root.join("user");
            return criteriaBuilder.equal(conversionUserJoin.get("username"), username);
        }));

        specification = specification.and(((root, query, criteriaBuilder) -> {
            Join<Conversion, ExchangeRate> conversionUserJoin = root.join("fromExchange");
            return criteriaBuilder.equal(conversionUserJoin.get("code"), from);
        }));

        specification = specification.and(((root, query, criteriaBuilder) -> {
            Join<Conversion, ExchangeRate> conversionExchangeRateJoin = root.join("toExchange");
            return criteriaBuilder.equal(conversionExchangeRateJoin.get("code"), to);
        }));

        specification = specification.and(((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("rateDate"), dateRequest);
        }));

//        Long userId = userService.getUserByUsername(username).id();
//        Long fromId = exchangeRateService.findExchangeRate(from).getId();
//        Long toId = exchangeRateService.findExchangeRate(from).getId();

        return new PageImpl(List.of(conversionRepository.findAll(specification)
                .stream().map(conversionMapper::toDto).collect(Collectors.toSet())));
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
        BigDecimal toValue = calculateUtil.calculate(fromValue, exchangeRateFrom.getRate(), exchangeRateTo.getRate());
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
