package com.lipari.bank.exception;

public class AccountNotFoundException extends BankException {

  public AccountNotFoundException(String iban) {
    super("Account not found for IBAN: " + iban, "ACCOUNT_NOT_FOUND");
  }
}