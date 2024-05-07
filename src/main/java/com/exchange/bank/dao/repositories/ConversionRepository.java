package com.exchange.bank.dao.repositories;

import com.exchange.bank.dao.entities.Conversion;
import com.exchange.bank.dao.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversionRepository extends JpaRepository<Conversion, Long> {
    Page<Conversion> findAllByUser(User user, Pageable pageable);
}
