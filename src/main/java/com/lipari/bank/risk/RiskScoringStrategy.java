package com.lipari.bank.risk;

import com.lipari.bank.model.Account;
import com.lipari.bank.model.Customer;
import com.lipari.bank.model.Transaction;

import java.util.List;

/**
 * Strategy for calculating a customer's risk score.
 *
 * @author Valerio
 * @since 1.0
 */
public interface RiskScoringStrategy {

  /**
   * Calculates the risk score for a customer.
   *
   * @param customer     customer to evaluate
   * @param accounts     customer's accounts
   * @param transactions customer's transactions
   * @return risk score between 0 and 100
   */
  int calculateScore(Customer customer, List<Account> accounts, List<Transaction> transactions);
}