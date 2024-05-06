package com.exchange.bank.dto.centralbankapi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurrencyCube {
    @JacksonXmlProperty(isAttribute = true, localName = "currency")
    String currency;
    @JacksonXmlProperty(isAttribute = true, localName = "rate")
    String rate;
    @JsonIgnore
    Date rateDate;
}
