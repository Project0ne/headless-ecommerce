package com.headless.ecommerce.controller.admin;

import com.headless.ecommerce.model.Staff;
import com.headless.ecommerce.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/staff")
@RequiredArgsConstructor
public class AdminStaffController {

    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<Staff>> list() {
        return ResponseEntity.ok(staffRepository.findAllByOrderByCreatedAtDesc());
    }

    @PostMapping
    public ResponseEntity<Staff> create(@RequestBody Staff staff) {
        if (staffRepository.existsByUsername(staff.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        staff.setPassword(passwordEncoder.encode(staff.getPassword()));
        return ResponseEntity.ok(staffRepository.save(staff));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Staff> update(@PathVariable Long id, @RequestBody Staff staff) {
        Staff existing = staffRepository.findById(id).orElseThrow();
        existing.setDisplayName(staff.getDisplayName());
        existing.setEmail(staff.getEmail());
        existing.setPhone(staff.getPhone());
        existing.setRole(staff.getRole());
        existing.setIsActive(staff.getIsActive());
        if (staff.getPassword() != null && !staff.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(staff.getPassword()));
        }
        return ResponseEntity.ok(staffRepository.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        staffRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
