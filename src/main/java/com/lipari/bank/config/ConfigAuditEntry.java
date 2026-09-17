package com.lipari.bank.config;

import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@Getter
@Scope("prototype")
public class ConfigAuditEntry {
    private final String id;
    private final Instant timestamp;

    public ConfigAuditEntry() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = Instant.now();
    }
}