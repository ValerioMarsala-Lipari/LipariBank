package com.lipari.bank.pattern;

import com.lipari.bank.model.Account;

import java.math.BigDecimal;

public record TransferCommand(Account source, Account target, BigDecimal amount) implements BankCommand {

  @Override
  public void execute() {
    Account first = source.getIban().compareTo(target.getIban()) < 0 ? source : target;

    Account second = first == source ? target : source;

    synchronized (first) {
      synchronized (second) {
        source.withdraw(amount);
        target.deposit(amount);
      }
    }
  }

  @Override
  public void undo() {
    Account first = source.getIban().compareTo(target.getIban()) < 0 ? source : target;

    Account second = first == source ? target : source;

    synchronized (first) {
      synchronized (second) {
        target.withdraw(amount);
        source.deposit(amount);
      }
    }
  }
}