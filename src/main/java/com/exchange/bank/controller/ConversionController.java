package com.exchange.bank.controller;

import com.exchange.bank.dto.ConversionDto;
import com.exchange.bank.dto.request.ConversionRequest;
import com.exchange.bank.service.ConversionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/v1/conversion")
public class ConversionController {
    final ConversionService conversionService;

    @PostMapping("/convert")
    public ResponseEntity<ConversionDto> convert(@RequestBody ConversionRequest request) {
        ConversionDto conversion = conversionService.convert(request);
        return ResponseEntity.ok(conversion);
    }

    @GetMapping("/history")
    public ResponseEntity<Page<ConversionDto>> getHistoryConversions(@RequestParam(required = false) String from,
                                                                     @RequestParam(required = false) String to,
                                                                     @RequestParam(required = false) String username,
                                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateRequest,
                                                                     Pageable pageable) {
        Page<ConversionDto> conversions = conversionService.getHistory(from, to, username, dateRequest, pageable);
        return ResponseEntity.ok(conversions);
    }
}
