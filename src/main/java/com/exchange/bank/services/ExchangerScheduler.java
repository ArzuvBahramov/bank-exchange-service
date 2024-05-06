package com.exchange.bank.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ExchangerScheduler {
    final ExchangeService exchangeService;

    @Scheduled(cron = "${exchange.cron}")
    public void exchangeScheduler() {
        exchangeService.retrieveData();
    }

}
