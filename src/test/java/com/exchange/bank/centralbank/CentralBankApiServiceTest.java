package com.exchange.bank.centralbank;

import com.exchange.bank.centralbankapi.CentralBankApiClient;
import com.exchange.bank.centralbankapi.CentralBankApiService;
import com.exchange.bank.dao.entities.ExchangeRate;
import com.exchange.bank.mapper.ExchangerXmlFileMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig({CentralBankApiService.class})
public class CentralBankApiServiceTest {
    @Autowired
    private CentralBankApiService centralBankApiService;

    @MockBean
    private CentralBankApiClient bankApiClient;

    @MockBean
    private ExchangerXmlFileMapper exchangerXmlFileMapper;

    @Test
    public void getRatesFromECBTest() {
        String xmlFile = "<xml>...</xml>";
        Set<ExchangeRate> expectedRates = Set.of(new ExchangeRate());

        when(bankApiClient.getFileWithExchangeRates()).thenReturn(xmlFile);
        when(exchangerXmlFileMapper.xmlFileToExchanger(xmlFile)).thenReturn(expectedRates);

        Set<ExchangeRate> actualRates = centralBankApiService.getRatesFromECB();

        assertNotNull(actualRates);
        assertEquals(expectedRates, actualRates);

        verify(bankApiClient).getFileWithExchangeRates();
        verify(exchangerXmlFileMapper).xmlFileToExchanger(xmlFile);
    }
}
