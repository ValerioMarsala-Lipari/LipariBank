package com.lipari.bank.service;

import com.lipari.bank.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Provides reporting operations over banking data.
 *
 * @author Valerio
 * @since 1.0
 */
public class ReportingService {

  /**
   * Calculates the total transaction amount grouped by transaction type.
   *
   * @param accounts accounts whose transactions should be analyzed
   * @return total transaction amounts grouped by type
   */
  public Map<TransactionType, BigDecimal> getTotalByTransactionType(List<Account> accounts) {
    return accounts.stream().flatMap(account -> account.getTransactions().stream()).collect(Collectors.groupingBy(Transaction::type, Collectors.reducing(BigDecimal.ZERO, Transaction::amount, BigDecimal::add)));
  }

  /**
   * Returns the top ten accounts ordered by balance in descending order.
   *
   * @param accounts accounts to analyze
   * @return account owners ordered by account balance
   */
  public List<String> getTop10CustomersByBalance(List<Account> accounts) {
    return accounts.stream().sorted(Comparator.comparing(Account::getBalance).reversed()).limit(10).map(account -> account.getOwner().toString()).toList();
  }

  /**
   * Finds active policies expiring within the specified number of days.
   *
   * @param policies policies to analyze
   * @param days     maximum number of days before expiration
   * @return summaries of policies expiring within the specified period
   * @throws IllegalArgumentException if days is negative
   */
  public List<PolicySummary> getExpiringPolicies(List<Policy> policies, int days) {
    if (days < 0) {
      throw new IllegalArgumentException("Days cannot be negative");
    }

    LocalDate today = LocalDate.now();
    LocalDate expirationLimit = today.plusDays(days);

    return policies.stream().filter(policy -> policy.getStatus() == PolicyStatus.ACTIVE).filter(policy -> !policy.getExpirationDate().isBefore(today) && !policy.getExpirationDate().isAfter(expirationLimit)).map(policy -> new PolicySummary(policy.getId(), policy.getHolder().toString(), policy.getPolicyType(), policy.getPremium(), policy.getExpirationDate(), policy.isActive())).toList();
  }

  /**
   * Counts customers grouped by customer type.
   *
   * @param customers customers to analyze
   * @return customer counts grouped by type
   */
  public Map<CustomerType, Long> getCustomerCountByType(List<Customer> customers) {
    return customers.stream().collect(Collectors.groupingBy(Customer::getCustomerType, Collectors.counting()));
  }

  /**
   * Calculates the average balance grouped by account type.
   *
   * @param accounts accounts to analyze
   * @return average balance grouped by account type
   */
  public Map<String, Double> getAverageBalanceByAccountType(List<Account> accounts) {
    return accounts.stream().collect(Collectors.groupingBy(account -> account.getClass().getSimpleName(), Collectors.averagingDouble(account -> account.getBalance().doubleValue())));
  }
}