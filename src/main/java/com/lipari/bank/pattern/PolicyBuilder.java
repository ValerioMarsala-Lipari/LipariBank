package com.lipari.bank.pattern;

import com.lipari.bank.model.Customer;
import com.lipari.bank.model.Policy;
import com.lipari.bank.model.PolicyStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Builder for creating insurance policies.
 *
 * @author Valerio
 * @since 1.0
 */
public class PolicyBuilder {

  private String policyId;
  private String type;
  private Customer beneficiary;
  private BigDecimal premium;
  private BigDecimal coverageAmount;
  private LocalDate startDate;
  private LocalDate endDate;
  private BigDecimal deductible;

  /**
   * Sets the policy identifier.
   *
   * @param policyId policy identifier
   * @return this builder
   */
  public PolicyBuilder policyId(String policyId) {
    this.policyId = policyId;
    return this;
  }

  /**
   * Sets the policy type.
   *
   * @param type policy type
   * @return this builder
   */
  public PolicyBuilder type(String type) {
    this.type = type;
    return this;
  }

  /**
   * Sets the policy beneficiary.
   *
   * @param beneficiary policy beneficiary
   * @return this builder
   */
  public PolicyBuilder beneficiary(Customer beneficiary) {
    this.beneficiary = beneficiary;
    return this;
  }

  /**
   * Sets the policy premium.
   *
   * @param premium policy premium
   * @return this builder
   */
  public PolicyBuilder premium(BigDecimal premium) {
    this.premium = premium;
    return this;
  }

  /**
   * Sets the policy coverage amount.
   *
   * @param coverageAmount policy coverage amount
   * @return this builder
   */
  public PolicyBuilder coverageAmount(BigDecimal coverageAmount) {
    this.coverageAmount = coverageAmount;
    return this;
  }

  /**
   * Sets the policy start date.
   *
   * @param startDate policy start date
   * @return this builder
   */
  public PolicyBuilder startDate(LocalDate startDate) {
    this.startDate = startDate;
    return this;
  }

  /**
   * Sets the policy end date.
   *
   * @param endDate policy end date
   * @return this builder
   */
  public PolicyBuilder endDate(LocalDate endDate) {
    this.endDate = endDate;
    return this;
  }

  /**
   * Sets the policy deductible.
   *
   * @param deductible policy deductible
   * @return this builder
   */
  public PolicyBuilder deductible(BigDecimal deductible) {
    this.deductible = deductible;
    return this;
  }

  /**
   * Builds an active insurance policy.
   *
   * @return newly created policy
   * @throws IllegalArgumentException if the configured values are invalid
   */
  public Policy build() {
    validate();

    return new Policy(policyId, LocalDate.now(), type, beneficiary, premium, coverageAmount, startDate, endDate, deductible, PolicyStatus.ACTIVE);
  }

  /**
   * Validates the values configured in the builder.
   *
   * @throws IllegalArgumentException if a required value is missing or
   *                                  the date range is invalid
   */
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