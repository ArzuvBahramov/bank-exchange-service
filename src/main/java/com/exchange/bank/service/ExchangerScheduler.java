package com.exchange.bank.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ExchangerScheduler {
    final ExchangeRateService exchangeRateService;

    @Scheduled(cron = "${exchange.cron}")
    public void exchangeScheduler() {
        exchangeRateService.retrieveData();
    }

}
