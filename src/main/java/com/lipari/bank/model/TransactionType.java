package com.lipari.bank.model;

/**
 * Represents the possible types of banking transactions.
 *
 * @author Valerio
 * @since 1.0
 */
public enum TransactionType {

  DEPOSIT("Deposito"), WITHDRAWAL("Prelievo"), TRANSFER("Trasferimento");

  private final String label;

  /**
   * Creates a transaction type with the specified display label.
   *
   * @param label display label
   */
  TransactionType(String label) {
    this.label = label;
  }

  /**
   * Returns the display label of this transaction type.
   *
   * @return transaction type label
   */
  public String getLabel() {
    return label;
  }

  /**
   * Returns the display label of this transaction type.
   *
   * @return transaction type label
   */
  @Override
  public String toString() {
    return label;
  }
}
