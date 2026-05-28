package com.headless.ecommerce.controller.admin;

import com.headless.ecommerce.model.Policy;
import com.headless.ecommerce.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/policies")
@RequiredArgsConstructor
public class AdminPolicyController {

    private final PolicyRepository policyRepository;

    @GetMapping
    public ResponseEntity<List<Policy>> list() {
        return ResponseEntity.ok(policyRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Policy> get(@PathVariable Long id) {
        return policyRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Policy> create(@RequestBody Policy policy) {
        return ResponseEntity.ok(policyRepository.save(policy));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Policy> update(@PathVariable Long id, @RequestBody Policy policy) {
        policy.setId(id);
        return ResponseEntity.ok(policyRepository.save(policy));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        policyRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
