package com.lipari.bank.exception;

public class InvalidTransactionException extends BankException {

  public InvalidTransactionException(String reason) {
    super("Invalid transaction: " + reason, "INVALID_TRANSACTION");
  }
}