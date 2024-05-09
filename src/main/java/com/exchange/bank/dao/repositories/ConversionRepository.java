package com.exchange.bank.dao.repositories;

import com.exchange.bank.dao.entities.Conversion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversionRepository extends JpaRepository<Conversion, Long>, JpaSpecificationExecutor<Conversion> {

//    @Query("select * from conversion where user_id = :id and from_exchange_id = :from and to_exchange_id = :to and created_at = :date_request")
//    Page<Conversion> findAll(Long from,
//                             Long to,
//                             Long id,
//                             LocalDate dateRequest,
//                             Pageable pageable);
}
