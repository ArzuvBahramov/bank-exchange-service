package com.exchange.bank.controllers;

import com.exchange.bank.dto.ExchangeRateDto;
import com.exchange.bank.services.ExchangeRateService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/v1/exchanger")
public class ExchangerController {
    final ExchangeRateService exchangeRateService;

    @GetMapping()
    public ResponseEntity<Set<ExchangeRateDto>> getExchangers() {

        return ResponseEntity.ok(exchangeRateService.findAllExchangers());
    }

}
