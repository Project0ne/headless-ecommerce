package com.headless.ecommerce.repository;

import com.headless.ecommerce.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByUsername(String username);
    Optional<Staff> findByEmail(String email);
    List<Staff> findAllByOrderByCreatedAtDesc();
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
