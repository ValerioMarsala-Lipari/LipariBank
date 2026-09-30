package com.lipari.bank.exception;

/**
 * Thrown when an account cannot be found for the specified IBAN.
 *
 * @author Valerio
 * @since 1.0
 */
public class AccountNotFoundException extends BankException {

  /**
   * Creates an exception for a missing account.
   *
   * @param iban IBAN of the account that could not be found
   */
  public AccountNotFoundException(String iban) {
    super("Account not found for IBAN: " + iban, "ACCOUNT_NOT_FOUND");
  }
}