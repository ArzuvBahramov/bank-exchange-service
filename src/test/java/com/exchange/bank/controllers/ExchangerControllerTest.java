package com.exchange.bank.controllers;

import com.exchange.bank.dto.ExchangeRateDto;
import com.exchange.bank.services.ExchangeRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@SpringJUnitConfig(ExchangerController.class)
public class ExchangerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ExchangeRateService exchangeRateService;

    @BeforeEach
    void setUp() {
        Set<ExchangeRateDto> exchangeRates = Set.of(new ExchangeRateDto(1L, "USD", "Dollar", new BigDecimal("1.0"), LocalDate.now()));
        when(exchangeRateService.findAllExchangers()).thenReturn(exchangeRates);
    }

    @Test
    @WithMockUser(username = "testUser")
    public void getExchangersTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/exchanger")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].code").value("USD"))
                .andExpect(jsonPath("$[0].name").value("Dollar"))
                .andExpect(jsonPath("$[0].rate").value(new BigDecimal("1.0")))
                .andExpect(jsonPath("$[0].rateDate").value(LocalDate.now().toString()));
    }

    @Test
    public void getExchangersTestNoAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/exchanger")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }
}
