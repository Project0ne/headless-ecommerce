package com.headless.ecommerce.repository;

import com.headless.ecommerce.model.StoreConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreConfigRepository extends JpaRepository<StoreConfig, Long> {
    Optional<StoreConfig> findFirstByOrderByIdAsc();
}
