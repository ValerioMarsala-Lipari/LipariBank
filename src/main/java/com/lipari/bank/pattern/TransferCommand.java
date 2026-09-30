package com.lipari.bank.pattern;

import com.lipari.bank.model.Account;

import java.math.BigDecimal;

/**
 * Command that transfers an amount between two bank accounts.
 *
 * @param source source account
 * @param target target account
 * @param amount amount to transfer
 * @author Valerio
 * @since 1.0
 */
public record TransferCommand(Account source, Account target, BigDecimal amount) implements BankCommand {

  /**
   * Executes the transfer from the source account to the target account.
   *
   * <p>Accounts are locked in a deterministic order based on their IBANs
   * to avoid deadlocks when concurrent transfers involve the same accounts.</p>
   */
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

  /**
   * Undoes the transfer by moving the amount back to the source account.
   */
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