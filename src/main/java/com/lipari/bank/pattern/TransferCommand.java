package com.lipari.bank.pattern;

import com.lipari.bank.model.Account;

import java.math.BigDecimal;

public record TransferCommand(Account source, Account target, BigDecimal amount) implements BankCommand {

  @Override
  public void execute() {
    source.withdraw(amount);
    target.deposit(amount);
  }

  @Override
  public void undo() {
    target.withdraw(amount);
    source.deposit(amount);
  }
}
