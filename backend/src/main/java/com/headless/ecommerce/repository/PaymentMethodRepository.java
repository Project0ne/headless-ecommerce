package com.headless.ecommerce.repository;

import com.headless.ecommerce.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    List<PaymentMethod> findByIsEnabledTrueOrderBySortOrderAsc();
    List<PaymentMethod> findAllByOrderBySortOrderAsc();
}
