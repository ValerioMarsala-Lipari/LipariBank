package com.lipari.bank.risk;

/**
 * Represents a factor contributing to a customer's risk score.
 *
 * @param name name of the risk factor
 * @param points points assigned to the factor
 * @author Valerio
 * @since 1.0
 */
public record RiskFactor(
    String name,
    int points
) {

  public RiskFactor {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Risk factor name cannot be null or blank");
    }

    if (points < 0) {
      throw new IllegalArgumentException("Risk factor points cannot be negative");
    }
  }
}