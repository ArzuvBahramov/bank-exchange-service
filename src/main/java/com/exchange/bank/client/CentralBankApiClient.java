package com.exchange.bank.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "central-bank-api")
public interface CentralBankApiClient {

    @GetMapping(value = "/stats/eurofxref/eurofxref-daily.xml", consumes = "application/xml")
    String getFileWithExchangeRates();

}
