package com.headless.ecommerce.controller.admin;

import com.headless.ecommerce.model.PaymentMethod;
import com.headless.ecommerce.repository.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/payment-methods")
@RequiredArgsConstructor
public class AdminPaymentController {

    private final PaymentMethodRepository paymentMethodRepository;

    @GetMapping
    public ResponseEntity<List<PaymentMethod>> list() {
        return ResponseEntity.ok(paymentMethodRepository.findAllByOrderBySortOrderAsc());
    }

    @PostMapping
    public ResponseEntity<PaymentMethod> create(@RequestBody PaymentMethod method) {
        return ResponseEntity.ok(paymentMethodRepository.save(method));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethod> update(@PathVariable Long id, @RequestBody PaymentMethod method) {
        method.setId(id);
        return ResponseEntity.ok(paymentMethodRepository.save(method));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        paymentMethodRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
