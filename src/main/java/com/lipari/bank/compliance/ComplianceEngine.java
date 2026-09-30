package com.lipari.bank.compliance;

import com.lipari.bank.compliance.alert.Alert;
import com.lipari.bank.compliance.rules.ComplianceRule;
import com.lipari.bank.model.Customer;
import com.lipari.bank.model.Transaction;

import java.util.List;

/**
 * Engine responsible for evaluating compliance rules for a customer.
 *
 * @author Valerio
 * @since 1.0
 */
public class ComplianceEngine {

  private final List<ComplianceRule> rules;

  /**
   * Creates a compliance engine with the specified rules.
   *
   * @param rules compliance rules to evaluate
   */
  public ComplianceEngine(List<ComplianceRule> rules) {
    if (rules == null || rules.isEmpty()) {
      throw new IllegalArgumentException("Compliance rules cannot be null or empty");
    }

    this.rules = List.copyOf(rules);
  }

  /**
   * Evaluates all configured compliance rules for a customer.
   *
   * @param customer     customer to evaluate
   * @param transactions customer's transactions
   * @return generated compliance alerts
   */
  public List<Alert> evaluate(Customer customer, List<Transaction> transactions) {
    if (customer == null) {
      throw new IllegalArgumentException("Customer cannot be null");
    }

    if (transactions == null) {
      throw new IllegalArgumentException("Transactions cannot be null");
    }

    return rules.stream().map(rule -> rule.evaluate(customer, transactions)).flatMap(java.util.Optional::stream).toList();
  }
}