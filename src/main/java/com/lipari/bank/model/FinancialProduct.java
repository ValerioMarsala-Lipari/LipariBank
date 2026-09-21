package com.lipari.bank.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class FinancialProduct {

  private final String id;
  private final LocalDate creationDate;

  protected FinancialProduct(String id, LocalDate creationDate) {
    if (id == null || id.isBlank()) {
      throw new IllegalArgumentException("Id cannot be null or blank");
    }

    if (creationDate == null) {
      throw new IllegalArgumentException("Creation Date cannot be null");
    }

    this.id = id;
    this.creationDate = creationDate;
  }

  public String getId() {
    return id;
  }

  public LocalDate getCreationDate() {
    return creationDate;
  }

  public abstract BigDecimal calculateTax();
}