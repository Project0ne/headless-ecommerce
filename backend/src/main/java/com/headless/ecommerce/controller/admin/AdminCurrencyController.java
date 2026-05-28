package com.headless.ecommerce.controller.admin;

import com.headless.ecommerce.model.Currency;
import com.headless.ecommerce.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/currencies")
@RequiredArgsConstructor
public class AdminCurrencyController {

    private final CurrencyRepository currencyRepository;

    @GetMapping
    public ResponseEntity<List<Currency>> list() {
        return ResponseEntity.ok(currencyRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Currency> create(@RequestBody Currency currency) {
        return ResponseEntity.ok(currencyRepository.save(currency));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Currency> update(@PathVariable Long id, @RequestBody Currency currency) {
        currency.setId(id);
        return ResponseEntity.ok(currencyRepository.save(currency));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        currencyRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/set-default")
    public ResponseEntity<Void> setDefault(@PathVariable Long id) {
        currencyRepository.findAll().forEach(c -> {
            c.setIsDefault(false);
            currencyRepository.save(c);
        });
        Currency currency = currencyRepository.findById(id).orElseThrow();
        currency.setIsDefault(true);
        currencyRepository.save(currency);
        return ResponseEntity.ok().build();
    }
}
