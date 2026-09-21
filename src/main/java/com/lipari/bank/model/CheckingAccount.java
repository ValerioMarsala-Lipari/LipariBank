package com.lipari.bank.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class CheckingAccount extends Account {

  private final BigDecimal overdraftLimit;

  public CheckingAccount(
      String id,
      LocalDate creationDate,
      String iban,
      BigDecimal balance,
      Customer owner,
      BigDecimal overdraftLimit
  ) {
    super(id, creationDate, iban, balance, owner);

    if (overdraftLimit == null || overdraftLimit.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException(
          "Overdraft limit cannot be null or negative"
      );
    }

    this.overdraftLimit = overdraftLimit;
  }

  public BigDecimal getOverdraftLimit() {
    return overdraftLimit;
  }

  @Override
  public void withdraw(BigDecimal amount) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException(
          "Amount must be greater than zero"
      );
    }

    BigDecimal maximumWithdrawal = getBalance().add(overdraftLimit);

    if (amount.compareTo(maximumWithdrawal) > 0) {
      throw new IllegalStateException(
          "Withdrawal exceeds overdraft limit"
      );
    }

    setBalance(getBalance().subtract(amount));

    addTransaction(
        new Transaction(
            TransactionType.WITHDRAWAL,
            amount,
            "Withdrawal",
            java.time.LocalDateTime.now()
        )
    );
  }

  @Override
  public BigDecimal calculateTaxAmount() {
    return BigDecimal.ZERO;
  }
}