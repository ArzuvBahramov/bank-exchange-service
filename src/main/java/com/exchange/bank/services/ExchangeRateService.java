package com.exchange.bank.services;

import com.exchange.bank.centralbankapi.CentralBankApiService;
import com.exchange.bank.dao.entities.ExchangeRate;
import com.exchange.bank.dao.repositories.ExchangeRateRepository;
import com.exchange.bank.dto.ExchangeRateDto;
import com.exchange.bank.mapper.ExchangeRateMapper;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Transactional
@RequiredArgsConstructor
public class ExchangeRateService {
    final ExchangeRateRepository exchangeRateRepository;
    final CentralBankApiService centralBankApiService;
    final ExchangeRateMapper exchangeRateMapper;

    public Set<ExchangeRateDto> findAllExchangers() {
        Set<ExchangeRate> exchangeRates = exchangeRateRepository.findTop1ByOrderByRateDateDesc();
        if (exchangeRates.isEmpty()) {
            exchangeRates = new HashSet<>(retrieveData());
        }

        LocalDate localDate = exchangeRates.stream().findFirst().orElseThrow().getRateDate();
        exchangeRates = exchangeRateRepository.findExchangerByRateDate(localDate);

        return exchangeRates
                .stream()
                .map(exchangeRateMapper::toDto)
                .collect(Collectors.toSet());
    }

    public ExchangeRate findExchangeRate(String code) {
        return exchangeRateRepository.findExchangerByCodeAndRateDate(code.toUpperCase(), currentDate())
                .orElse(exchangeRateRepository.findExchangeRateByCodeOrderByRateDateDesc(code.toUpperCase()).orElseThrow());
    }

    public List<ExchangeRate> retrieveData() {
        Set<ExchangeRate> exchangeRates = centralBankApiService.getRatesFromECB();
        return exchangeRateRepository.saveAll(exchangeRates);
    }

    private LocalDate currentDate() {
        return LocalDate.now();
    }
}
