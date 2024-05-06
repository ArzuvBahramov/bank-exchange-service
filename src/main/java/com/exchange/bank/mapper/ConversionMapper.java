package com.exchange.bank.mapper;

import com.exchange.bank.dao.entities.Conversion;
import com.exchange.bank.dto.ConversionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ExchangeRateMapper.class})
public interface ConversionMapper {

    @Mapping(source = "fromExchange", target = "from")
    @Mapping(source = "toExchange", target = "to")
    ConversionDto toDto(Conversion conversion);
}
