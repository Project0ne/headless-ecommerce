package com.headless.ecommerce.service.impl;

import com.headless.ecommerce.model.StoreConfig;
import com.headless.ecommerce.repository.StoreConfigRepository;
import com.headless.ecommerce.service.StoreConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreConfigServiceImpl implements StoreConfigService {

    private final StoreConfigRepository storeConfigRepository;

    @Override
    public StoreConfig getConfig() {
        return storeConfigRepository.findFirstByOrderByIdAsc()
                .orElseGet(() -> storeConfigRepository.save(StoreConfig.builder().build()));
    }

    @Override
    @Transactional
    public StoreConfig updateConfig(StoreConfig config) {
        StoreConfig existing = getConfig();
        existing.setStoreName(config.getStoreName());
        existing.setStoreLogo(config.getStoreLogo());
        existing.setStoreDescription(config.getStoreDescription());
        existing.setContactEmail(config.getContactEmail());
        existing.setContactPhone(config.getContactPhone());
        existing.setContactAddress(config.getContactAddress());
        existing.setTimezone(config.getTimezone());
        existing.setCurrencyCode(config.getCurrencyCode());
        existing.setCurrencySymbol(config.getCurrencySymbol());
        existing.setLanguage(config.getLanguage());
        existing.setCustomDomain(config.getCustomDomain());
        existing.setFaviconUrl(config.getFaviconUrl());
        existing.setFooterText(config.getFooterText());
        return storeConfigRepository.save(existing);
    }
}
