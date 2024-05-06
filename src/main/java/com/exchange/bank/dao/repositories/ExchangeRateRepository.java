package com.exchange.bank.dao.repositories;

import com.exchange.bank.dao.entities.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Integer> {
    Set<ExchangeRate> findExchangerByRateDate(LocalDate rateDate);
    Set<ExchangeRate> findTop1ByOrderByRateDateDesc();
    Optional<ExchangeRate> findExchangerByCodeAndRateDate(String code, LocalDate rateDate);
    Optional<ExchangeRate> findExchangeRateByCodeOrderByRateDateDesc(String code);
}
