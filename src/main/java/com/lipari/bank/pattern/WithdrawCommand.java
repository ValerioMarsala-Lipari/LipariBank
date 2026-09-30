package com.lipari.bank.pattern;

import com.lipari.bank.model.Account;

import java.math.BigDecimal;

/**
 * Command that withdraws an amount from a bank account.
 *
 * @param account account on which the operation is performed
 * @param amount  amount to withdraw
 * @author Valerio
 * @since 1.0
 */
public record WithdrawCommand(Account account, BigDecimal amount) implements BankCommand {

  /**
   * Executes the withdrawal operation.
   */
  @Override
  public void execute() {
    account.withdraw(amount);
  }

  /**
   * Undoes the withdrawal by depositing the same amount.
   */
  @Override
  public void undo() {
    account.deposit(amount);
  }
}