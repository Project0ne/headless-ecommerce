package com.headless.ecommerce.repository;

import com.headless.ecommerce.model.ShippingMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShippingMethodRepository extends JpaRepository<ShippingMethod, Long> {
    List<ShippingMethod> findByIsEnabledTrueOrderBySortOrderAsc();
    List<ShippingMethod> findAllByOrderBySortOrderAsc();
}
