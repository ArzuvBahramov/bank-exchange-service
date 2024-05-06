package com.exchange.bank.controllers;

import com.exchange.bank.dto.ExchangeRateDto;
import com.exchange.bank.services.ExchangeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/v1/exchanger")
public class ExchangerController {
    final ExchangeService exchangeService;

    @GetMapping()
    public ResponseEntity<Set<ExchangeRateDto>> getExchangers() {

        return ResponseEntity.ok(exchangeService.findAllExchangers());
    }

}
