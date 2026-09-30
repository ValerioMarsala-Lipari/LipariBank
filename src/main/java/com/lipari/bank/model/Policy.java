package com.lipari.bank.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Represents an insurance policy associated with a customer.
 *
 * <p>A policy has a premium, coverage amount, validity period, deductible
 * and status. The applicable tax is calculated as a percentage of the
 * policy premium.</p>
 *
 * @author Valerio
 * @since 1.0
 */
public class Policy extends FinancialProduct implements Taxable {

  private final String policyType;
  private final Customer holder;
  private final BigDecimal premium;
  private final BigDecimal coverageAmount;
  private final LocalDate startDate;
  private final LocalDate expirationDate;
  private final BigDecimal deductible;
  private PolicyStatus status;

  /**
   * Creates a policy with the specified details.
   *
   * @param id             unique product identifier
   * @param creationDate   policy creation date
   * @param policyType     type of insurance policy
   * @param holder         customer who holds the policy
   * @param premium        policy premium
   * @param coverageAmount maximum coverage amount
   * @param startDate      policy start date
   * @param expirationDate policy expiration date
   * @param deductible     policy deductible amount
   * @param status         current policy status
   * @throws IllegalArgumentException if a required value is null, blank,
   *                                  non-positive or otherwise invalid
   */
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

  /**
   * Returns the type of this policy.
   *
   * @return policy type
   */
  public String getPolicyType() {
    return policyType;
  }

  /**
   * Returns the customer who holds this policy.
   *
   * @return policy holder
   */
  public Customer getHolder() {
    return holder;
  }

  /**
   * Returns the policy premium.
   *
   * @return policy premium
   */
  public BigDecimal getPremium() {
    return premium;
  }

  /**
   * Returns the maximum amount covered by the policy.
   *
   * @return coverage amount
   */
  public BigDecimal getCoverageAmount() {
    return coverageAmount;
  }

  /**
   * Returns the policy start date.
   *
   * @return policy start date
   */
  public LocalDate getStartDate() {
    return startDate;
  }

  /**
   * Returns the policy expiration date.
   *
   * @return policy expiration date
   */
  public LocalDate getExpirationDate() {
    return expirationDate;
  }

  /**
   * Returns the policy deductible.
   *
   * @return deductible amount
   */
  public BigDecimal getDeductible() {
    return deductible;
  }

  /**
   * Returns the current policy status.
   *
   * @return policy status
   */
  public PolicyStatus getStatus() {
    return status;
  }

  /**
   * Updates the policy status.
   *
   * @param status new policy status
   * @throws IllegalArgumentException if the status is null
   */
  public void setStatus(PolicyStatus status) {
    if (status == null) {
      throw new IllegalArgumentException("Policy status cannot be null");
    }

    this.status = status;
  }

  /**
   * Calculates the tax applicable to this policy.
   *
   * @return calculated tax amount
   */
  @Override
  public BigDecimal calculateTax() {
    return calculateTaxAmount();
  }

  /**
   * Calculates the tax amount as 10% of the policy premium.
   *
   * @return calculated tax amount
   */
  @Override
  public BigDecimal calculateTaxAmount() {
    return premium.multiply(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_UP);
  }

  /**
   * Determines whether the policy is currently active.
   *
   * <p>A policy is active when its status is {@link PolicyStatus#ACTIVE},
   * its start date has been reached and its expiration date has not yet
   * been reached.</p>
   *
   * @return {@code true} if the policy is currently active
   */
  public boolean isActive() {
    LocalDate today = LocalDate.now();

    return status == PolicyStatus.ACTIVE && !startDate.isAfter(today) && expirationDate.isAfter(today);
  }
}
