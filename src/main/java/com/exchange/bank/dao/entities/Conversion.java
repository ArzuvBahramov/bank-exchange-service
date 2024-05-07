package com.exchange.bank.dao.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table
@EntityListeners(AuditingEntityListener.class)
public class Conversion extends Auditor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "from_exchange_id")
    ExchangeRate fromExchange;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "to_exchange_id")
    ExchangeRate toExchange;

    LocalDate rateDate;

    @Column(precision = 19, scale = 3)
    BigDecimal fromValue;

    @Column(precision = 19, scale = 3)
    BigDecimal toValue;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id")
    User user;
}
