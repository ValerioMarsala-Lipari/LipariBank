package com.lipari.bank.model;

import com.lipari.bank.exception.InsufficientFundsException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a checking account with an optional overdraft limit.
 *
 * <p>The account allows withdrawals and outgoing transfers beyond the
 * available balance up to the configured overdraft limit.</p>
 *
 * @author Valerio
 * @since 1.0
 */
public final class CheckingAccount extends Account {

  private final BigDecimal overdraftLimit;

  /**
   * Creates a checking account with the specified details and overdraft limit.
   *
   * @param id             unique product identifier
   * @param creationDate   account creation date
   * @param iban           account IBAN
   * @param balance        initial account balance
   * @param owner          account owner
   * @param overdraftLimit maximum amount allowed below the available balance
   * @throws IllegalArgumentException if the overdraft limit is null or negative
   */
  public CheckingAccount(String id, LocalDate creationDate, String iban, BigDecimal balance, Customer owner, BigDecimal overdraftLimit) {
    super(id, creationDate, iban, balance, owner);

    if (overdraftLimit == null || overdraftLimit.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Overdraft limit cannot be null or negative");
    }

    this.overdraftLimit = overdraftLimit;
  }

  /**
   * Returns the maximum overdraft allowed for this account.
   *
   * @return overdraft limit
   */
  public BigDecimal getOverdraftLimit() {
    return overdraftLimit;
  }

  /**
   * Withdraws an amount from the account, allowing the configured overdraft.
   *
   * @param amount amount to withdraw
   * @throws IllegalArgumentException   if the amount is null or not positive
   * @throws InsufficientFundsException if the amount exceeds the available
   *                                    balance including the overdraft limit
   */
  @Override
  public void withdraw(BigDecimal amount) {
    validateAmount(amount);

    BigDecimal maximumWithdrawal = getBalance().add(overdraftLimit);

    if (amount.compareTo(maximumWithdrawal) > 0) {
      throw new InsufficientFundsException(getIban(), amount, maximumWithdrawal);
    }

    setBalance(getBalance().subtract(amount));

    addTransaction(new Transaction(TransactionType.WITHDRAWAL, amount, "Withdrawal", LocalDateTime.now()));
  }

  /**
   * Validates an outgoing transfer against the available balance and
   * overdraft limit.
   *
   * @param amount amount to transfer
   * @throws IllegalArgumentException   if the amount is null or not positive
   * @throws InsufficientFundsException if the amount exceeds the available
   *                                    balance including the overdraft limit
   */
  @Override
  protected void validateTransferOut(BigDecimal amount) {
    validateAmount(amount);

    BigDecimal maximumWithdrawal = getBalance().add(overdraftLimit);

    if (amount.compareTo(maximumWithdrawal) > 0) {
      throw new InsufficientFundsException(getIban(), amount, maximumWithdrawal);
    }
  }

  /**
   * Calculates the tax applicable to the checking account.
   *
   * @return zero because checking accounts currently have no tax
   */
  @Override
  public BigDecimal calculateTaxAmount() {
    return BigDecimal.ZERO;
  }
}
