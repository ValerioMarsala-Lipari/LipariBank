package com.lipari.bank.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends BankException {

  public InsufficientFundsException(String iban, BigDecimal requested, BigDecimal available) {
    super("Insufficient funds for account " + iban + ": requested " + requested + ", available " + available, "INSUFFICIENT_FUNDS");
  }
}