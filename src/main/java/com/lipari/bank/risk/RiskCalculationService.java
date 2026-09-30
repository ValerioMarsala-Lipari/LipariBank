package com.lipari.bank.risk;

import com.lipari.bank.model.Account;
import com.lipari.bank.model.Customer;
import com.lipari.bank.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Calculates customer risk scores using the appropriate scoring strategy.
 *
 * @author Valerio
 * @since 1.0
 */
public class RiskCalculationService {

  private final RiskScoringStrategy privateCustomerStrategy;
  private final RiskScoringStrategy businessCustomerStrategy;

  /**
   * Creates a risk calculation service with the provided strategies.
   *
   * @param privateCustomerStrategy  strategy for private customers
   * @param businessCustomerStrategy strategy for business customers
   */
  public RiskCalculationService(RiskScoringStrategy privateCustomerStrategy, RiskScoringStrategy businessCustomerStrategy) {
    if (privateCustomerStrategy == null) {
      throw new IllegalArgumentException("Private customer strategy cannot be null");
    }

    if (businessCustomerStrategy == null) {
      throw new IllegalArgumentException("Business customer strategy cannot be null");
    }

    this.privateCustomerStrategy = privateCustomerStrategy;
    this.businessCustomerStrategy = businessCustomerStrategy;
  }

  /**
   * Calculates the risk score for a customer.
   *
   * @param customer     customer to evaluate
   * @param accounts     customer's accounts
   * @param transactions customer's transactions
   * @return calculated risk score
   */
  public RiskScore calculateRisk(Customer customer, List<Account> accounts, List<Transaction> transactions) {
    if (customer == null) {
      throw new IllegalArgumentException("Customer cannot be null");
    }

    if (accounts == null) {
      throw new IllegalArgumentException("Accounts cannot be null");
    }

    if (transactions == null) {
      throw new IllegalArgumentException("Transactions cannot be null");
    }

    RiskScoringStrategy strategy = switch (customer.getCustomerType()) {
      case PRIVATE -> privateCustomerStrategy;
      case BUSINESS -> businessCustomerStrategy;
    };

    int score = strategy.calculateScore(customer, accounts, transactions);

    return new RiskScore(customer.getId(), score, determineRiskLevel(score), LocalDateTime.now());
  }

  private RiskLevel determineRiskLevel(int score) {
    if (score <= 25) {
      return RiskLevel.LOW;
    }

    if (score <= 50) {
      return RiskLevel.MEDIUM;
    }

    if (score <= 75) {
      return RiskLevel.HIGH;
    }

    return RiskLevel.CRITICAL;
  }
}