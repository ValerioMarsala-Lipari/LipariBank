package com.lipari.bank.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PolicySummary(
    String policyId,
    String customerName,
    String policyType,
    BigDecimal premium,
    LocalDate expirationDate,
    boolean isActive
) {

  public boolean isExpiringSoon(int days) {
    if (days < 0) {
      throw new IllegalArgumentException("Days cannot be negative");
    }

    LocalDate today = LocalDate.now();
    LocalDate expirationThreshold = today.plusDays(days);

    return isActive
        && !expirationDate.isBefore(today)
        && !expirationDate.isAfter(expirationThreshold);
  }
}