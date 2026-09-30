package com.lipari.bank.model;

import com.lipari.bank.exception.InsufficientFundsException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Base class representing a bank account.
 *
 * <p>An account has an owner, an IBAN, a balance and a transaction history.
 * Concrete account types define their own tax calculation.</p>
 *
 * @author Valerio
 * @since 1.0
 */
public sealed abstract class Account extends FinancialProduct implements Taxable permits CheckingAccount, SavingsAccount {

  private final String iban;
  private final Customer owner;
  private final List<Transaction> transactions;
  private BigDecimal balance;

  /**
   * Creates an account with the specified details.
   *
   * @param id           unique product identifier
   * @param creationDate account creation date
   * @param iban         account IBAN
   * @param balance      initial account balance
   * @param owner        account owner
   */
  protected Account(String id, LocalDate creationDate, String iban, BigDecimal balance, Customer owner) {
    super(id, creationDate);

    if (iban == null || iban.isBlank()) {
      throw new IllegalArgumentException("IBAN cannot be null or blank");
    }

    if (balance == null || balance.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Balance cannot be null or negative");
    }

    if (owner == null) {
      throw new IllegalArgumentException("Owner cannot be null");
    }

    this.iban = iban;
    this.balance = balance;
    this.owner = owner;
    this.transactions = new ArrayList<>();
  }

  /**
   * Returns the account IBAN.
   *
   * @return account IBAN
   */
  public String getIban() {
    return iban;
  }

  /**
   * Returns the current account balance.
   *
   * @return current account balance
   */
  public BigDecimal getBalance() {
    return balance;
  }

  /**
   * Updates the account balance.
   *
   * @param balance new account balance
   */
  protected void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  /**
   * Returns the customer who owns the account.
   *
   * @return account owner
   */
  public Customer getOwner() {
    return owner;
  }

  /**
   * Deposits an amount into the account.
   *
   * @param amount amount to deposit
   * @throws IllegalArgumentException if the amount is null or not positive
   */
  public void deposit(BigDecimal amount) {
    validateAmount(amount);

    balance = balance.add(amount);

    transactions.add(new Transaction(TransactionType.DEPOSIT, amount, "Deposit", LocalDateTime.now()));
  }

  /**
   * Withdraws an amount from the account.
   *
   * @param amount amount to withdraw
   * @throws IllegalArgumentException   if the amount is null or not positive
   * @throws InsufficientFundsException if the account balance is insufficient
   */
  public void withdraw(BigDecimal amount) {
    validateAmount(amount);

    if (amount.compareTo(balance) > 0) {
      throw new InsufficientFundsException(iban, amount, balance);
    }

    balance = balance.subtract(amount);

    transactions.add(new Transaction(TransactionType.WITHDRAWAL, amount, "Withdrawal", LocalDateTime.now()));
  }

  /**
   * Returns the account transaction history as an unmodifiable view.
   *
   * @return unmodifiable list of account transactions
   */
  public List<Transaction> getTransactions() {
    return Collections.unmodifiableList(transactions);
  }

  /**
   * Validates a monetary amount used in an account operation.
   *
   * @param amount amount to validate
   * @throws IllegalArgumentException if the amount is null or not positive
   */
  protected void validateAmount(BigDecimal amount) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Amount must be greater than zero");
    }
  }

  /**
   * Adds a transaction to the account history.
   *
   * @param transaction transaction to add
   * @throws IllegalArgumentException if the transaction is null
   */
  protected void addTransaction(Transaction transaction) {
    if (transaction == null) {
      throw new IllegalArgumentException("Transaction cannot be null");
    }

    transactions.add(transaction);
  }

  /**
   * Calculates the tax applicable to the account.
   *
   * @return calculated tax amount
   */
  @Override
  public BigDecimal calculateTax() {
    return calculateTaxAmount();
  }

  /**
   * Calculates the tax amount according to the concrete account type.
   *
   * @return calculated tax amount
   */
  @Override
  public abstract BigDecimal calculateTaxAmount();

  /**
   * Compares this account with another account using the IBAN and
   * concrete account type.
   *
   * @param o object to compare
   * @return {@code true} if both accounts have the same type and IBAN
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Account account = (Account) o;

    return Objects.equals(iban, account.iban);
  }

  /**
   * Returns a hash code based on the account IBAN.
   *
   * @return hash code for this account
   */
  @Override
  public int hashCode() {
    return Objects.hash(iban);
  }

  /**
   * Transfers an amount out of this account.
   *
   * @param amount     amount to transfer
   * @param targetIban IBAN of the target account
   * @throws IllegalArgumentException   if the amount is invalid
   * @throws InsufficientFundsException if the account balance is insufficient
   */
  public void transferOut(BigDecimal amount, String targetIban) {
    validateTransferOut(amount);

    setBalance(getBalance().subtract(amount));

    addTransaction(new Transaction(TransactionType.TRANSFER, amount, "Transfer to " + targetIban, LocalDateTime.now()));
  }

  /**
   * Receives an amount from another account.
   *
   * @param amount     amount to receive
   * @param sourceIban IBAN of the source account
   * @throws IllegalArgumentException if the amount is invalid
   */
  public void transferIn(BigDecimal amount, String sourceIban) {
    validateAmount(amount);

    setBalance(getBalance().add(amount));

    addTransaction(new Transaction(TransactionType.TRANSFER, amount, "Transfer from " + sourceIban, LocalDateTime.now()));
  }

  /**
   * Validates an outgoing transfer amount against the account balance.
   *
   * @param amount amount to transfer
   * @throws IllegalArgumentException   if the amount is invalid
   * @throws InsufficientFundsException if the account balance is insufficient
   */
  protected void validateTransferOut(BigDecimal amount) {
    validateAmount(amount);

    if (amount.compareTo(getBalance()) > 0) {
      throw new InsufficientFundsException(getIban(), amount, getBalance());
    }
  }
}