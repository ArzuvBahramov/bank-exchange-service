package com.exchange.bank.dao.repository;

import com.exchange.bank.dao.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    Set<ExchangeRate> findExchangerByRateDate(LocalDate rateDate);
    Set<ExchangeRate> findTop1ByOrderByRateDateDesc();
    Optional<ExchangeRate> findExchangerByCodeAndRateDate(String code, LocalDate rateDate);
    Optional<ExchangeRate> findExchangeRateByCodeOrderByRateDateDesc(String code);
}
