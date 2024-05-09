package com.exchange.bank.dao.specification;

import com.exchange.bank.dao.entities.Conversion;
import com.exchange.bank.dao.entities.ExchangeRate;
import com.exchange.bank.dao.entities.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

@Component
public class ConversionSpecification {
    public Specification<Conversion> getAllHistorySpecification(String from,
                                                                String to,
                                                                String username,
                                                                LocalDate dateRequest) {
        Specification<Conversion> specification = Specification.where(null);
        if (Objects.nonNull(from) && !from.isEmpty()) {
            specification = specification.and(((root, query, criteriaBuilder) -> {
                Join<Conversion, User> conversionUserJoin = root.join("user");
                return criteriaBuilder.equal(conversionUserJoin.get("username"), username);
            }));
        }

        if (Objects.nonNull(to) && !to.isEmpty()) {
            specification = specification.and(((root, query, criteriaBuilder) -> {
                Join<Conversion, ExchangeRate> conversionUserJoin = root.join("fromExchange");
                return criteriaBuilder.equal(conversionUserJoin.get("code"), from);
            }));
        }

        if (Objects.nonNull(username) && !username.isEmpty()) {
            specification = specification.and(((root, query, criteriaBuilder) -> {
                Join<Conversion, ExchangeRate> conversionExchangeRateJoin = root.join("toExchange");
                return criteriaBuilder.equal(conversionExchangeRateJoin.get("code"), to);
            }));
        }

        if (Objects.nonNull(dateRequest)) {
            specification = specification.and(((root, query, criteriaBuilder) -> {
                return criteriaBuilder.equal(root.get("rateDate"), dateRequest);
            }));
        }

        return specification;
    }
}
