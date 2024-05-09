package com.exchange.bank.controllers;

import com.exchange.bank.dto.ConversionDto;
import com.exchange.bank.dto.request.ConversionRequest;
import com.exchange.bank.services.ConversionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@SpringJUnitConfig(ConversionController.class)
public class ConversionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ConversionService conversionService;

    @Test
    @WithMockUser(username = "testUser")
    public void testConvert() throws Exception {
        when(conversionService.convert(any(ConversionRequest.class))).thenReturn(mockConversion());

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/conversion/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mockConversionRequestJson())
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fromValue").value(100))
                .andExpect(jsonPath("$.toValue").value(50))
                .andExpect(jsonPath("$.rateDate").exists());
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testGetHistoryConversions() throws Exception {
        when(conversionService.getHistory(any(Long.class), any(Pageable.class))).thenReturn(mockCollectionOfConversions());
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/conversion/history/{id}", 1L)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].fromValue").exists())
                .andExpect(jsonPath("$.content[0].toValue").exists())
                .andExpect(jsonPath("$.content[0].rateDate").exists());
    }

    private static String mockConversionRequestJson() {
        return "{\"currencyFrom\":\"USD\",\"currencyTo\":\"EUR\",\"amount\":100}";
    }

    private static ConversionDto mockConversion() {
        return new ConversionDto(1L, null, null, new BigDecimal("100"), new BigDecimal("50"), LocalDate.now(), null, LocalDateTime.now());
    }

    private static Page<ConversionDto> mockCollectionOfConversions() {
        return new PageImpl<>(List.of((mockConversion())));
    }

}
