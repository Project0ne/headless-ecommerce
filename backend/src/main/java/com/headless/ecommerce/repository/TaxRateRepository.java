package com.headless.ecommerce.repository;

import com.headless.ecommerce.model.TaxRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaxRateRepository extends JpaRepository<TaxRate, Long> {
    List<TaxRate> findByIsEnabledTrueOrderByPriorityAsc();
    Optional<TaxRate> findByCountryCodeAndStateCodeAndCityCode(String countryCode, String stateCode, String cityCode);
}
