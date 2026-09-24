package com.lipari.bank.pattern;

import com.lipari.bank.model.Customer;
import com.lipari.bank.model.Policy;
import com.lipari.bank.model.PolicyStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PolicyBuilder {

  private String policyId;
  private String type;
  private Customer beneficiary;
  private BigDecimal premium;
  private BigDecimal coverageAmount;
  private LocalDate startDate;
  private LocalDate endDate;
  private BigDecimal deductible;

  public PolicyBuilder policyId(String policyId) {
    this.policyId = policyId;
    return this;
  }

  public PolicyBuilder type(String type) {
    this.type = type;
    return this;
  }

  public PolicyBuilder beneficiary(Customer beneficiary) {
    this.beneficiary = beneficiary;
    return this;
  }

  public PolicyBuilder premium(BigDecimal premium) {
    this.premium = premium;
    return this;
  }

  public PolicyBuilder coverageAmount(BigDecimal coverageAmount) {
    this.coverageAmount = coverageAmount;
    return this;
  }

  public PolicyBuilder startDate(LocalDate startDate) {
    this.startDate = startDate;
    return this;
  }

  public PolicyBuilder endDate(LocalDate endDate) {
    this.endDate = endDate;
    return this;
  }

  public PolicyBuilder deductible(BigDecimal deductible) {
    this.deductible = deductible;
    return this;
  }

  public Policy build() {
    validate();

    return new Policy(policyId, startDate, type, beneficiary, premium, coverageAmount, startDate, endDate, deductible, PolicyStatus.ACTIVE);
  }

  private void validate() {
    if (policyId == null) {
      throw new IllegalArgumentException("Policy ID cannot be null");
    }

    if (type == null) {
      throw new IllegalArgumentException("Policy type cannot be null");
    }

    if (beneficiary == null) {
      throw new IllegalArgumentException("Beneficiary cannot be null");
    }

    if (premium == null) {
      throw new IllegalArgumentException("Premium cannot be null");
    }

    if (coverageAmount == null) {
      throw new IllegalArgumentException("Coverage amount cannot be null");
    }

    if (startDate == null) {
      throw new IllegalArgumentException("Start date cannot be null");
    }

    if (endDate == null) {
      throw new IllegalArgumentException("End date cannot be null");
    }

    if (deductible == null) {
      throw new IllegalArgumentException("Deductible cannot be null");
    }

    if (!endDate.isAfter(startDate)) {
      throw new IllegalArgumentException("End date must be after start date");
    }
  }
}
