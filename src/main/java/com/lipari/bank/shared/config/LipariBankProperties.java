package com.lipari.bank.shared.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Validated
@ConfigurationProperties(prefix = "liparibank")
public record LipariBankProperties(
        @NotBlank String bankCode,
        BigDecimal maxTransferAmount,
        Audit audit) {

    public record Audit(boolean enabled) {}
}
