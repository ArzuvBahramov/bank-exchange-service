package com.exchange.bank.mapper;

import com.exchange.bank.dao.entity.ExchangeRate;
import com.exchange.bank.dto.ExchangeRateDto;
import com.exchange.bank.dto.centralbankapi.CurrencyCube;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface ExchangeRateMapper {
    ExchangeRateDto toDto(ExchangeRate exchangeRate);

    @Mapping(target = "code", source = "currency")
    @Mapping(target = "name", source = "currency")
    @Mapping(target = "rate", source = "rate")
    @Mapping(target = "rateDate", source = "rateDate")
    ExchangeRate currencyCubeToExchanger(CurrencyCube currencyCube);
}
