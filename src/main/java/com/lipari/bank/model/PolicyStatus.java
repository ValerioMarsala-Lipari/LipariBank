package com.lipari.bank.model;

/**
 * Represents the possible statuses of an insurance policy.
 *
 * @author Valerio
 * @since 1.0
 */
public enum PolicyStatus {

  ACTIVE("Attivo"), EXPIRED("Scaduto"), SUSPENDED("Sospeso"), CANCELLED("Cancellato");

  private final String label;

  /**
   * Creates a policy status with the specified display label.
   *
   * @param label display label
   */
  PolicyStatus(String label) {
    this.label = label;
  }

  /**
   * Returns the display label of this status.
   *
   * @return status label
   */
  public String getLabel() {
    return label;
  }

  /**
   * Returns the display label of this status.
   *
   * @return status label
   */
  @Override
  public String toString() {
    return label;
  }
}