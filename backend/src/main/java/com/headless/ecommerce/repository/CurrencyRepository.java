package com.headless.ecommerce.repository;

import com.headless.ecommerce.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    List<Currency> findByIsEnabledTrueOrderByCodeAsc();
    Optional<Currency> findByIsDefaultTrue();
    Optional<Currency> findByCode(String code);
}
