package com.lipari.bank.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class Policy extends FinancialProduct implements Taxable {

  private final String policyType;
  private final Customer holder;
  private final BigDecimal premium;
  private final BigDecimal coverageAmount;
  private final LocalDate startDate;
  private final LocalDate expirationDate;
  private final BigDecimal deductible;
  private PolicyStatus status;

  public Policy(String id, LocalDate creationDate, String policyType, Customer holder, BigDecimal premium, BigDecimal coverageAmount, LocalDate startDate, LocalDate expirationDate, BigDecimal deductible, PolicyStatus status) {
    super(id, creationDate);

    if (policyType == null || policyType.isBlank()) {
      throw new IllegalArgumentException("Policy type cannot be null or blank");
    }

    if (holder == null) {
      throw new IllegalArgumentException("Policy holder cannot be null");
    }

    if (premium == null || premium.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Premium must be greater than zero");
    }

    if (coverageAmount == null || coverageAmount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Coverage amount must be greater than zero");
    }

    if (startDate == null) {
      throw new IllegalArgumentException("Start date cannot be null");
    }

    if (expirationDate == null) {
      throw new IllegalArgumentException("Expiration date cannot be null");
    }

    if (!expirationDate.isAfter(startDate)) {
      throw new IllegalArgumentException("Expiration date must be after start date");
    }

    if (deductible == null || deductible.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Deductible cannot be null or negative");
    }

    if (status == null) {
      throw new IllegalArgumentException("Policy status cannot be null");
    }

    this.policyType = policyType;
    this.holder = holder;
    this.premium = premium;
    this.coverageAmount = coverageAmount;
    this.startDate = startDate;
    this.expirationDate = expirationDate;
    this.deductible = deductible;
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

  public BigDecimal getCoverageAmount() {
    return coverageAmount;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public LocalDate getExpirationDate() {
    return expirationDate;
  }

  public BigDecimal getDeductible() {
    return deductible;
  }

  public PolicyStatus getStatus() {
    return status;
  }

  public void setStatus(PolicyStatus status) {
    if (status == null) {
      throw new IllegalArgumentException("Policy status cannot be null");
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
    return premium.multiply(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_UP);
  }
}
