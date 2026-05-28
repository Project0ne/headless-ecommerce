package com.headless.ecommerce.service;

import com.headless.ecommerce.model.StoreConfig;

public interface StoreConfigService {
    StoreConfig getConfig();
    StoreConfig updateConfig(StoreConfig config);
}
