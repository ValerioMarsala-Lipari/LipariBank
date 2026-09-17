package com.lipari.bank.lifecycle;

import com.lipari.bank.shared.config.LipariBankProperties;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BankSystemLifecycle {

    private static final Logger log = LoggerFactory.getLogger(BankSystemLifecycle.class);

    private final LipariBankProperties properties;

    public BankSystemLifecycle(LipariBankProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void onStartup() {
        log.info("=================================================");
        log.info("  LipariBank system initialized");
        log.info("  Bank code   : {}", properties.bankCode());
        log.info("  Max transfer: {}", properties.maxTransferAmount());
        log.info("  Audit active: {}", properties.audit().enabled());
        log.info("=================================================");
    }
}
