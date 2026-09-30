package com.lipari.bank.compliance.rules;

import com.lipari.bank.compliance.alert.Alert;
import com.lipari.bank.compliance.alert.AlertLevel;
import com.lipari.bank.model.Customer;
import com.lipari.bank.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Detects transactions exceeding the high-value transaction threshold.
 *
 * @author Valerio
 * @since 1.0
 */
public class LargeTransactionRule implements ComplianceRule {

  private static final BigDecimal TRANSACTION_THRESHOLD = new BigDecimal("15000");

  @Override
  public Optional<Alert> evaluate(Customer customer, List<Transaction> transactions) {
    Optional<Transaction> largeTransaction = transactions.stream().filter(transaction -> transaction.amount().compareTo(TRANSACTION_THRESHOLD) > 0).findFirst();

    if (largeTransaction.isPresent()) {
      return Optional.of(new Alert(null, customer.getId(), AlertLevel.WARNING, ruleName(), "Transaction above €15,000 detected", LocalDateTime.now()));
    }

    return Optional.empty();
  }

  @Override
  public String ruleName() {
    return "LargeTransactionRule";
  }
}