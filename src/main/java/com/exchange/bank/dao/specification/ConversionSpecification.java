package com.exchange.bank.dao.specification;

import com.exchange.bank.dao.entity.Conversion;
import com.exchange.bank.dao.entity.ExchangeRate;
import com.exchange.bank.dao.entity.User;
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
        if (Objects.nonNull(from) && !from.trim().isEmpty()) {
            specification = specification.and(((root, query, criteriaBuilder) -> {
                Join<Conversion, ExchangeRate> conversionUserJoin = root.join("fromExchange");
                return criteriaBuilder.equal(conversionUserJoin.get("code"), from.toUpperCase());
            }));
        }

        if (Objects.nonNull(to) && !to.trim().isEmpty()) {
            specification = specification.and(((root, query, criteriaBuilder) -> {
                Join<Conversion, ExchangeRate> conversionExchangeRateJoin = root.join("toExchange");
                return criteriaBuilder.equal(conversionExchangeRateJoin.get("code"), to.toUpperCase());
            }));
        }

        if (Objects.nonNull(username) && !username.trim().isEmpty()) {
            specification = specification.and(((root, query, criteriaBuilder) -> {
                Join<Conversion, User> conversionUserJoin = root.join("user");
                return criteriaBuilder.equal(conversionUserJoin.get("username"), username);
            }));
        }

        if (Objects.nonNull(dateRequest)) {
            specification = specification.and(((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("rateDate"), dateRequest)));
        }

        return specification;
    }
}
