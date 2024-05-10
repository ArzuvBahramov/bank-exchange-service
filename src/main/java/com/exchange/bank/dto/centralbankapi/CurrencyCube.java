package com.exchange.bank.dto.centralbankapi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurrencyCube {
    @JacksonXmlProperty(isAttribute = true, localName = "currency")
    String currency;
    @JacksonXmlProperty(isAttribute = true, localName = "rate")
    String rate;
    @JsonIgnore
    Date rateDate;
}
