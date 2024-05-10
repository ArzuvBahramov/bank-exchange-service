package com.exchange.bank.mapper;

import com.exchange.bank.dao.entity.ExchangeRate;
import com.exchange.bank.dto.centralbankapi.Envelope;
import com.exchange.bank.dto.centralbankapi.TimeCube;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExchangerXmlFileMapper {
    final ExchangeRateMapper exchangeRateMapper;
    @SneakyThrows
    public Set<ExchangeRate> xmlFileToExchanger(String xmlFile) {
        XmlMapper xmlMapper = new XmlMapper();
        Envelope envelope = xmlMapper.readValue(xmlFile, Envelope.class);
        TimeCube timeCube = envelope.getCube().getTimeCube()
                .stream().findFirst().orElseThrow();
        return timeCube.getCurrencyCube().stream()
                .map(cubeItem -> {
                    cubeItem.setRateDate(timeCube.getTime());
                    return exchangeRateMapper.currencyCubeToExchanger(cubeItem);
                }).collect(Collectors.toSet());
    }
}
