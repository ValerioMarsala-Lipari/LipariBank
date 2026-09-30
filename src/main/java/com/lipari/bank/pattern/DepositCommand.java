package com.lipari.bank.pattern;

import com.lipari.bank.model.Account;

import java.math.BigDecimal;

/**
 * Command that deposits an amount into a bank account.
 *
 * @param account account on which the operation is performed
 * @param amount  amount to deposit
 * @author Valerio
 * @since 1.0
 */
public record DepositCommand(Account account, BigDecimal amount) implements BankCommand {

  /**
   * Executes the deposit operation.
   */
  @Override
  public void execute() {
    account.deposit(amount);
  }

  /**
   * Undoes the deposit by withdrawing the same amount.
   */
  @Override
  public void undo() {
    account.withdraw(amount);
  }
}