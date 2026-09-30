package com.lipari.bank.risk;

import java.time.LocalDateTime;

/**
 * Represents a risk score calculated for a customer.
 *
 * @param customerId   identifier of the customer
 * @param score        risk score between 0 and 100
 * @param level        risk level associated with the score
 * @param calculatedAt date and time when the score was calculated
 * @author Valerio
 * @since 1.0
 */
public record RiskScore(Long customerId, int score, RiskLevel level, LocalDateTime calculatedAt) {

  public RiskScore {
    if (customerId == null) {
      throw new IllegalArgumentException("Customer ID cannot be null");
    }

    if (score < 0 || score > 100) {
      throw new IllegalArgumentException("Risk score must be between 0 and 100");
    }

    if (level == null) {
      throw new IllegalArgumentException("Risk level cannot be null");
    }

    if (calculatedAt == null) {
      throw new IllegalArgumentException("Calculation date cannot be null");
    }
  }
}