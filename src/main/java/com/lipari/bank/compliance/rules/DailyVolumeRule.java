package com.lipari.bank.compliance.rules;

import com.lipari.bank.compliance.alert.Alert;
import com.lipari.bank.compliance.alert.AlertLevel;
import com.lipari.bank.model.Customer;
import com.lipari.bank.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Detects customers whose daily transaction volume exceeds the configured threshold.
 *
 * @author Valerio
 * @since 1.0
 */
public class DailyVolumeRule implements ComplianceRule {

  private static final BigDecimal DAILY_VOLUME_THRESHOLD = new BigDecimal("50000");

  @Override
  public Optional<Alert> evaluate(Customer customer, List<Transaction> transactions) {
    LocalDate today = LocalDate.now();

    BigDecimal dailyVolume = transactions.stream().filter(transaction -> transaction.timestamp().toLocalDate().equals(today)).map(Transaction::amount).reduce(BigDecimal.ZERO, BigDecimal::add);

    if (dailyVolume.compareTo(DAILY_VOLUME_THRESHOLD) > 0) {
      return Optional.of(new Alert(null, customer.getId(), AlertLevel.CRITICAL, ruleName(), "Daily transaction volume above €50,000 detected", LocalDateTime.now()));
    }

    return Optional.empty();
  }

  @Override
  public String ruleName() {
    return "DailyVolumeRule";
  }
}