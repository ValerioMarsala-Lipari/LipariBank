package com.lipari.bank.model;

import com.lipari.bank.exception.InsufficientFundsException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public sealed abstract class Account extends FinancialProduct implements Taxable permits CheckingAccount, SavingsAccount {

  private final String iban;
  private final Customer owner;
  private final List<Transaction> transactions;
  private BigDecimal balance;

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

  public String getIban() {
    return iban;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  protected void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public Customer getOwner() {
    return owner;
  }

  public void deposit(BigDecimal amount) {
    validateAmount(amount);

    balance = balance.add(amount);

    transactions.add(new Transaction(TransactionType.DEPOSIT, amount, "Deposit", java.time.LocalDateTime.now()));
  }

  public void withdraw(BigDecimal amount) {
    validateAmount(amount);

    if (amount.compareTo(balance) > 0) {
      throw new InsufficientFundsException(iban, amount, balance);
    }

    balance = balance.subtract(amount);

    transactions.add(new Transaction(TransactionType.WITHDRAWAL, amount, "Withdrawal", java.time.LocalDateTime.now()));
  }

  public List<Transaction> getTransactions() {
    return Collections.unmodifiableList(transactions);
  }

  protected void validateAmount(BigDecimal amount) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Amount must be greater than zero");
    }
  }

  protected void addTransaction(Transaction transaction) {
    if (transaction == null) {
      throw new IllegalArgumentException("Transaction cannot be null");
    }

    transactions.add(transaction);
  }

  @Override
  public BigDecimal calculateTax() {
    return calculateTaxAmount();
  }

  @Override
  public abstract BigDecimal calculateTaxAmount();

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

  @Override
  public int hashCode() {
    return Objects.hash(iban);
  }

  public void transferOut(BigDecimal amount, String targetIban) {
    validateTransferOut(amount);

    setBalance(getBalance().subtract(amount));

    addTransaction(new Transaction(TransactionType.TRANSFER, amount, "Transfer to " + targetIban, java.time.LocalDateTime.now()));
  }

  public void transferIn(BigDecimal amount, String sourceIban) {
    validateAmount(amount);

    setBalance(getBalance().add(amount));

    addTransaction(new Transaction(TransactionType.TRANSFER, amount, "Transfer from " + sourceIban, java.time.LocalDateTime.now()));
  }

  protected void validateTransferOut(BigDecimal amount) {
    validateAmount(amount);

    if (amount.compareTo(getBalance()) > 0) {
      throw new InsufficientFundsException(getIban(), amount, getBalance());
    }
  }

}