package com.exchange.bank.controllers;

import com.exchange.bank.dto.ConversionDto;
import com.exchange.bank.dto.request.ConversionRequest;
import com.exchange.bank.services.ConversionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/conversion")
public class ConversationController {
    final ConversionService conversionService;

    @PostMapping("/convert")
    public ResponseEntity<ConversionDto> convert(@RequestBody ConversionRequest request) {
        return ResponseEntity.ok(conversionService.calculate(request));
    }
}
