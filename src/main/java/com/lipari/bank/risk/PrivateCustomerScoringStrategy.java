package com.lipari.bank.risk;

import com.lipari.bank.model.Account;
import com.lipari.bank.model.Customer;
import com.lipari.bank.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

/**
 * Calculates the risk score for private customers.
 *
 * @author Valerio
 * @since 1.0
 */
public class PrivateCustomerScoringStrategy implements RiskScoringStrategy {

  private static final BigDecimal HIGH_BALANCE_THRESHOLD = new BigDecimal("50000");
  private static final BigDecimal HIGH_VALUE_TRANSACTION_THRESHOLD = new BigDecimal("10000");

  @Override
  public int calculateScore(Customer customer, List<Account> accounts, List<Transaction> transactions) {
    int transactionCountScore = Math.min(transactions.size() * 2, 30);

    long highValueCount = transactions.stream().filter(transaction -> transaction.amount().compareTo(HIGH_VALUE_TRANSACTION_THRESHOLD) > 0).count();

    int highValueScore = (int) Math.min(highValueCount * 10, 25);

    BigDecimal totalBalance = accounts.stream().map(Account::getBalance).reduce(BigDecimal.ZERO, BigDecimal::add);

    int balanceScore = totalBalance.compareTo(HIGH_BALANCE_THRESHOLD) > 0 ? 20 : 0;

    int accountScore = Math.min(accounts.size() * 5, 10);

    return Math.min(transactionCountScore + highValueScore + balanceScore + accountScore, 100);
  }
}