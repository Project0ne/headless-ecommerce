package com.headless.ecommerce.repository;

import com.headless.ecommerce.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long> {
    List<Policy> findByIsEnabledTrueOrderBySortOrderAsc();
    Optional<Policy> findByPolicyType(String policyType);
}
