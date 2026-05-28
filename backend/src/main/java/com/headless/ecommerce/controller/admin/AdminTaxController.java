package com.headless.ecommerce.controller.admin;

import com.headless.ecommerce.model.TaxRate;
import com.headless.ecommerce.repository.TaxRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/tax-rates")
@RequiredArgsConstructor
public class AdminTaxController {

    private final TaxRateRepository taxRateRepository;

    @GetMapping
    public ResponseEntity<List<TaxRate>> list() {
        return ResponseEntity.ok(taxRateRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<TaxRate> create(@RequestBody TaxRate taxRate) {
        return ResponseEntity.ok(taxRateRepository.save(taxRate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaxRate> update(@PathVariable Long id, @RequestBody TaxRate taxRate) {
        taxRate.setId(id);
        return ResponseEntity.ok(taxRateRepository.save(taxRate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taxRateRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
