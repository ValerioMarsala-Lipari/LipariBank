package com.lipari.bank.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a summary of an insurance policy for reporting purposes.
 *
 * @param policyId       unique policy identifier
 * @param customerName   name of the policy holder
 * @param policyType     type of insurance policy
 * @param premium        policy premium
 * @param expirationDate policy expiration date
 * @param isActive       whether the policy is currently active
 * @author Valerio
 * @since 1.0
 */
public record PolicySummary(String policyId, String customerName, String policyType, BigDecimal premium,
                            LocalDate expirationDate, boolean isActive) {

  /**
   * Determines whether the policy is expiring within the specified number
   * of days.
   *
   * @param days maximum number of days before expiration
   * @return {@code true} if the active policy expires within the specified
   * period
   * @throws IllegalArgumentException if days is negative
   */
  public boolean isExpiringSoon(int days) {
    if (days < 0) {
      throw new IllegalArgumentException("Days cannot be negative");
    }

    LocalDate today = LocalDate.now();
    LocalDate expirationThreshold = today.plusDays(days);

    return isActive && !expirationDate.isBefore(today) && !expirationDate.isAfter(expirationThreshold);
  }
}