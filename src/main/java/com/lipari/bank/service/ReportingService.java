package com.lipari.bank.service;

import com.lipari.bank.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportingService {

  public Map<TransactionType, BigDecimal> getTotalByTransactionType(
      List<Account> accounts
  ) {
    return accounts.stream()
        .flatMap(account -> account.getTransactions().stream())
        .collect(Collectors.groupingBy(
            Transaction::type,
            Collectors.reducing(
                BigDecimal.ZERO,
                Transaction::amount,
                BigDecimal::add
            )
        ));
  }

  public List<String> getTop10CustomersByBalance(
      List<Account> accounts
  ) {
    return accounts.stream()
        .sorted(
            Comparator.comparing(Account::getBalance)
                .reversed()
        )
        .limit(10)
        .map(account -> account.getOwner().toString())
        .toList();
  }

  public List<PolicySummary> getExpiringPolicies(
      List<Policy> policies,
      int days
  ) {
    LocalDate today = LocalDate.now();
    LocalDate expirationLimit = today.plusDays(days);

    return policies.stream()
        .filter(policy -> policy.getStatus() == PolicyStatus.ACTIVE)
        .filter(policy ->
            !policy.getExpirationDate().isBefore(today)
                && !policy.getExpirationDate().isAfter(expirationLimit)
        )
        .map(policy -> new PolicySummary(
            policy.getId(),
            policy.getPolicyType(),
            policy.getHolder().toString(),
            policy.getExpirationDate()
        ))
        .toList();
  }

  public Map<CustomerType, Long> getCustomerCountByType(
      List<Customer> customers
  ) {
    return customers.stream()
        .collect(Collectors.groupingBy(
            Customer::getCustomerType,
            Collectors.counting()
        ));
  }

  public Map<String, Double> getAverageBalanceByAccountType(
      List<Account> accounts
  ) {
    return accounts.stream()
        .collect(Collectors.groupingBy(
            account -> account.getClass().getSimpleName(),
            Collectors.averagingDouble(
                account -> account.getBalance().doubleValue()
            )
        ));
  }
}