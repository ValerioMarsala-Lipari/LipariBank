package com.lipari.bank.compliance.rules;

import com.lipari.bank.compliance.alert.Alert;
import com.lipari.bank.compliance.alert.AlertLevel;
import com.lipari.bank.model.Customer;
import com.lipari.bank.model.Transaction;
import com.lipari.bank.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Detects customers performing more than three high-value transfers
 * within a 24-hour period.
 *
 * @author Valerio
 * @since 1.0
 */
public class HighFrequencyTransferRule implements ComplianceRule {

  private static final BigDecimal TRANSFER_THRESHOLD = new BigDecimal("5000");
  private static final int MAX_TRANSFERS = 3;

  @Override
  public Optional<Alert> evaluate(Customer customer, List<Transaction> transactions) {
    LocalDateTime since = LocalDateTime.now().minusHours(24);

    long transferCount = transactions.stream().filter(transaction -> transaction.type() == TransactionType.TRANSFER).filter(transaction -> transaction.amount().compareTo(TRANSFER_THRESHOLD) > 0).filter(transaction -> !transaction.timestamp().isBefore(since)).count();

    if (transferCount > MAX_TRANSFERS) {
      return Optional.of(new Alert(null, customer.getId(), AlertLevel.HIGH, ruleName(), "More than three transfers above €5,000 detected in the last 24 hours", LocalDateTime.now()));
    }

    return Optional.empty();
  }

  @Override
  public String ruleName() {
    return "HighFrequencyTransferRule";
  }
}