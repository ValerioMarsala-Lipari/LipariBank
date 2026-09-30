package com.lipari.bank.compliance.rules;

import com.lipari.bank.compliance.alert.Alert;
import com.lipari.bank.compliance.alert.AlertLevel;
import com.lipari.bank.model.Customer;
import com.lipari.bank.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Detects new customers whose first transaction exceeds the configured threshold.
 *
 * @author Valerio
 * @since 1.0
 */
public class NewCustomerHighValueRule implements ComplianceRule {

  private static final BigDecimal FIRST_TRANSACTION_THRESHOLD = new BigDecimal("3000");

  @Override
  public Optional<Alert> evaluate(Customer customer, List<Transaction> transactions) {
    LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);

    if (customer.getCreatedAt().isBefore(sixMonthsAgo)) {
      return Optional.empty();
    }

    Optional<Transaction> firstTransaction = transactions.stream().min(Comparator.comparing(Transaction::timestamp));

    if (firstTransaction.isPresent() && firstTransaction.get().amount().compareTo(FIRST_TRANSACTION_THRESHOLD) > 0) {

      return Optional.of(new Alert(null, customer.getId(), AlertLevel.WARNING, ruleName(), "New customer with first transaction above €3,000 detected", LocalDateTime.now()));
    }

    return Optional.empty();
  }

  @Override
  public String ruleName() {
    return "NewCustomerHighValueRule";
  }
}