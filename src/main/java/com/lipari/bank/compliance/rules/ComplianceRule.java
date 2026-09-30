package com.lipari.bank.compliance.rules;

import com.lipari.bank.compliance.alert.Alert;
import com.lipari.bank.model.Customer;
import com.lipari.bank.model.Transaction;

import java.util.List;
import java.util.Optional;

/**
 * Represents a compliance rule used to detect potentially suspicious activity.
 *
 * @author Valerio
 * @since 1.0
 */
public interface ComplianceRule {

  /**
   * Evaluates a customer's transactions against this compliance rule.
   *
   * @param customer     customer to evaluate
   * @param transactions customer's transactions
   * @return an alert when the rule is triggered, otherwise empty
   */
  Optional<Alert> evaluate(Customer customer, List<Transaction> transactions);

  /**
   * Returns the name of this compliance rule.
   *
   * @return rule name
   */
  String ruleName();
}