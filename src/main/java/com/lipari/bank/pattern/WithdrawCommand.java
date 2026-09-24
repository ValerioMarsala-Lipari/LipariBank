package com.lipari.bank.pattern;

import com.lipari.bank.model.Account;

import java.math.BigDecimal;

public record WithdrawCommand(Account account, BigDecimal amount) implements BankCommand {

  @Override
  public void execute() {
    account.withdraw(amount);
  }

  @Override
  public void undo() {
    account.deposit(amount);
  }
}
