package com.lipari.bank.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Policy extends FinancialProduct implements Taxable {

    private final String policyType;
    private final Customer holder;
    private final BigDecimal premium;
    private final LocalDate expirationDate;
    private PolicyStatus status;

    public Policy(
            String id,
            LocalDate creationDate,
            String policyType,
            Customer holder,
            BigDecimal premium,
            LocalDate expirationDate,
            PolicyStatus status
    ) {
        super(id, creationDate);

        if (policyType == null || policyType.isBlank()) {
            throw new IllegalArgumentException(
                    "Policy type cannot be null or blank"
            );
        }

        if (holder == null) {
            throw new IllegalArgumentException(
                    "Policy holder cannot be null"
            );
        }

        if (premium == null || premium.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Premium must be greater than zero"
            );
        }

        if (expirationDate == null) {
            throw new IllegalArgumentException(
                    "Expiration date cannot be null"
            );
        }

        if (status == null) {
            throw new IllegalArgumentException(
                    "Policy status cannot be null"
            );
        }

        this.policyType = policyType;
        this.holder = holder;
        this.premium = premium;
        this.expirationDate = expirationDate;
        this.status = status;
    }

    public String getPolicyType() {
        return policyType;
    }

    public Customer getHolder() {
        return holder;
    }

    public BigDecimal getPremium() {
        return premium;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public PolicyStatus getStatus() {
        return status;
    }

    public void setStatus(PolicyStatus status) {
        if (status == null) {
            throw new IllegalArgumentException(
                    "Policy status cannot be null"
            );
        }

        this.status = status;
    }

    @Override
    public BigDecimal calculateTax() {
        return calculateTaxAmount();
    }

    @Override
    public BigDecimal calculateTaxAmount() {
        // Esempio: 10% del premio
        return premium
                .multiply(new BigDecimal("0.10"))
                .setScale(2, java.math.RoundingMode.HALF_UP);
    }
}