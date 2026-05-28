package com.headless.ecommerce.controller.admin;

import com.headless.ecommerce.model.ShippingMethod;
import com.headless.ecommerce.repository.ShippingMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/shipping-methods")
@RequiredArgsConstructor
public class AdminShippingController {

    private final ShippingMethodRepository shippingMethodRepository;

    @GetMapping
    public ResponseEntity<List<ShippingMethod>> list() {
        return ResponseEntity.ok(shippingMethodRepository.findAllByOrderBySortOrderAsc());
    }

    @PostMapping
    public ResponseEntity<ShippingMethod> create(@RequestBody ShippingMethod method) {
        return ResponseEntity.ok(shippingMethodRepository.save(method));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShippingMethod> update(@PathVariable Long id, @RequestBody ShippingMethod method) {
        method.setId(id);
        return ResponseEntity.ok(shippingMethodRepository.save(method));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        shippingMethodRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
