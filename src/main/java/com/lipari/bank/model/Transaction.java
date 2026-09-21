package com.lipari.bank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transaction(
    TransactionType type,
    BigDecimal amount,
    String description,
    LocalDateTime timestamp
) {

  public Transaction {
    if (type == null) {
      throw new IllegalArgumentException("Transaction type cannot be null");
    }

    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Transaction amount must be greater than zero");
    }

    if (description == null || description.isBlank()) {
      throw new IllegalArgumentException("Transaction description cannot be null or blank");
    }

    if (timestamp == null) {
      throw new IllegalArgumentException("Transaction timestamp cannot be null");
    }
  }
}