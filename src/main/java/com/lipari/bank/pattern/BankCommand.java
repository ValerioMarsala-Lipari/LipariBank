package com.lipari.bank.pattern;

/**
 * Represents a bank operation that can be executed and undone.
 *
 * @author Valerio
 * @since 1.0
 */
public sealed interface BankCommand permits DepositCommand, WithdrawCommand, TransferCommand {

  /**
   * Executes the bank operation.
   */
  void execute();

  /**
   * Undoes the bank operation.
   */
  void undo();
}
