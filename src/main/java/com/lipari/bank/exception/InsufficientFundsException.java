package com.lipari.bank.exception;

import java.math.BigDecimal;

/**
 * Thrown when an account does not have sufficient funds for a requested operation.
 *
 * @author Valerio
 * @since 1.0
 */
public class InsufficientFundsException extends BankException {

  /**
   * Creates an exception for an operation that exceeds the available balance.
   *
   * @param iban      IBAN of the account
   * @param requested amount requested by the operation
   * @param available amount currently available in the account
   */
  public InsufficientFundsException(String iban, BigDecimal requested, BigDecimal available) {
    super("Insufficient funds for account " + iban + ": requested " + requested + ", available " + available, "INSUFFICIENT_FUNDS");
  }
}