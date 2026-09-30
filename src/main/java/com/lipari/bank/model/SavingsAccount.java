package com.lipari.bank.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a savings account with an interest rate.
 *
 * <p>Interest can be applied to the current account balance according to
 * the configured annual interest rate.</p>
 *
 * @author Valerio
 * @since 1.0
 */
public final class SavingsAccount extends Account {

  private final BigDecimal interestRate;

  /**
   * Creates a savings account with the specified details and interest rate.
   *
   * @param id           unique product identifier
   * @param creationDate account creation date
   * @param iban         account IBAN
   * @param balance      initial account balance
   * @param owner        account owner
   * @param interestRate interest rate expressed as a percentage
   * @throws IllegalArgumentException if the interest rate is null or negative
   */
  public SavingsAccount(String id, LocalDate creationDate, String iban, BigDecimal balance, Customer owner, BigDecimal interestRate) {
    super(id, creationDate, iban, balance, owner);

    if (interestRate == null || interestRate.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Interest rate cannot be null or negative");
    }

    this.interestRate = interestRate;
  }

  /**
   * Returns the interest rate applied to this account.
   *
   * @return interest rate as a percentage
   */
  public BigDecimal getInterestRate() {
    return interestRate;
  }

  /**
   * Applies the configured interest rate to the current account balance.
   *
   * <p>The calculated interest is rounded to two decimal places using
   * {@link RoundingMode#HALF_UP}. A transaction is recorded when the
   * calculated interest is greater than zero.</p>
   */
  public void applyInterest() {
    BigDecimal interest = getBalance().multiply(interestRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

    if (interest.compareTo(BigDecimal.ZERO) > 0) {
      setBalance(getBalance().add(interest));

      addTransaction(new Transaction(TransactionType.DEPOSIT, interest, "Interest applied", LocalDateTime.now()));
    }
  }

  /**
   * Calculates the tax applicable to the savings account.
   *
   * @return zero because savings accounts currently have no tax
   */
  @Override
  public BigDecimal calculateTaxAmount() {
    return BigDecimal.ZERO;
  }
}
