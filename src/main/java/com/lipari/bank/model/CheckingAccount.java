package com.lipari.bank.model;

import com.lipari.bank.exception.InsufficientFundsException;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class CheckingAccount extends Account {

  private final BigDecimal overdraftLimit;

  public CheckingAccount(String id, LocalDate creationDate, String iban, BigDecimal balance, Customer owner, BigDecimal overdraftLimit) {
    super(id, creationDate, iban, balance, owner);

    if (overdraftLimit == null || overdraftLimit.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Overdraft limit cannot be null or negative");
    }

    this.overdraftLimit = overdraftLimit;
  }

  public BigDecimal getOverdraftLimit() {
    return overdraftLimit;
  }

  @Override
  public void withdraw(BigDecimal amount) {
    validateAmount(amount);

    BigDecimal maximumWithdrawal = getBalance().add(overdraftLimit);

    if (amount.compareTo(maximumWithdrawal) > 0) {
      throw new InsufficientFundsException(getIban(), amount, maximumWithdrawal);
    }

    setBalance(getBalance().subtract(amount));

    addTransaction(new Transaction(TransactionType.WITHDRAWAL, amount, "Withdrawal", java.time.LocalDateTime.now()));
  }

  @Override
  protected void validateTransferOut(BigDecimal amount) {
    validateAmount(amount);

    BigDecimal maximumWithdrawal = getBalance().add(overdraftLimit);

    if (amount.compareTo(maximumWithdrawal) > 0) {
      throw new InsufficientFundsException(getIban(), amount, maximumWithdrawal);
    }
  }

  @Override
  public BigDecimal calculateTaxAmount() {
    return BigDecimal.ZERO;
  }
}