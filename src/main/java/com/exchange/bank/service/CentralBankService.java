package com.exchange.bank.service;

import com.exchange.bank.centralbankapi.CentralBankApiClient;
import com.exchange.bank.dao.entity.ExchangeRate;
import com.exchange.bank.mapper.ExchangerXmlFileMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CentralBankService {
    final CentralBankApiClient bankApiClient;
    final ExchangerXmlFileMapper mapper;

    @SneakyThrows
    public Set<ExchangeRate> getRatesFromECB() {
        String xmlFile = bankApiClient.getFileWithExchangeRates();
        return mapper.xmlFileToExchanger(xmlFile);
    }
}
