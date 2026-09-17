package com.lipari.bank.config;

import com.lipari.bank.shared.config.LipariBankProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/config")
public class ConfigController {

    private final LipariBankProperties properties;
    private final ObjectProvider<ConfigAuditEntry> auditEntryProvider;

    public ConfigController(LipariBankProperties properties, ObjectProvider<ConfigAuditEntry> auditEntryProvider) {
        this.properties = properties;
        this.auditEntryProvider = auditEntryProvider;
    }

    @GetMapping()
    public ResponseEntity<ConfigResponse> getConfig() {
        return ResponseEntity.accepted().body(
                new ConfigResponse(
                        properties,
                        auditEntryProvider.getObject()
                )
        );

    }

    public record ConfigResponse(
            LipariBankProperties properties,
            ConfigAuditEntry auditEntry) {
    }
}