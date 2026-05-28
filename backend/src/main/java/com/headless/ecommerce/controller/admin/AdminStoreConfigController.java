package com.headless.ecommerce.controller.admin;

import com.headless.ecommerce.model.StoreConfig;
import com.headless.ecommerce.service.StoreConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/store")
@RequiredArgsConstructor
public class AdminStoreConfigController {

    private final StoreConfigService storeConfigService;

    @GetMapping("/config")
    public ResponseEntity<StoreConfig> getConfig() {
        return ResponseEntity.ok(storeConfigService.getConfig());
    }

    @PutMapping("/config")
    public ResponseEntity<StoreConfig> updateConfig(@RequestBody StoreConfig config) {
        return ResponseEntity.ok(storeConfigService.updateConfig(config));
    }
}
