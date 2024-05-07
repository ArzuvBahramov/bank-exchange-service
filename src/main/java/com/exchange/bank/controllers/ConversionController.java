package com.exchange.bank.controllers;

import com.exchange.bank.dto.ConversionDto;
import com.exchange.bank.dto.request.ConversionRequest;
import com.exchange.bank.services.ConversionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/conversion")
public class ConversionController {
    final ConversionService conversionService;

    @PostMapping("/convert")
    public ResponseEntity<ConversionDto> convert(@RequestBody ConversionRequest request) {
        return ResponseEntity.ok(conversionService.convert(request));
    }

    @GetMapping("/history/{id}")
    public ResponseEntity<Page<ConversionDto>> getHistoryConversions(@PathVariable Long id,
                                                                     Pageable pageable) {
        return ResponseEntity.ok(conversionService.getHistory(id, pageable));
    }
}
