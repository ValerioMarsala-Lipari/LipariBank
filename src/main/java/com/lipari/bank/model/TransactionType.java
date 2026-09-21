package com.lipari.bank.model;

public enum TransactionType {
  DEPOSIT("Deposito"),
  WITHDRAWAL("Prelievo"),
  TRANSFER("Trasferimento");

  private final String label;

  TransactionType(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  @Override
  public String toString() {
    return label;
  }
}