package com.lipari.bank.pattern;

import com.lipari.bank.model.Account;

import java.math.BigDecimal;

public record DepositCommand(Account account, BigDecimal amount) implements BankCommand {

  @Override
  public void execute() {
    account.deposit(amount);
  }

  @Override
  public void undo() {
    account.withdraw(amount);
  }
}
