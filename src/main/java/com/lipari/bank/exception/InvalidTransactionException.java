package com.lipari.bank.exception;

/**
 * Thrown when a transaction does not satisfy the required domain constraints.
 *
 * @author Valerio
 * @since 1.0
 */
public class InvalidTransactionException extends BankException {

  /**
   * Creates an exception for an invalid transaction.
   *
   * @param reason reason why the transaction is invalid
   */
  public InvalidTransactionException(String reason) {
    super("Invalid transaction: " + reason, "INVALID_TRANSACTION");
  }
}