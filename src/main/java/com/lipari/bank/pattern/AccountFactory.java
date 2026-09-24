package com.lipari.bank.pattern;

import com.lipari.bank.model.Account;
import com.lipari.bank.model.CheckingAccount;
import com.lipari.bank.model.Customer;
import com.lipari.bank.model.SavingsAccount;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public final class AccountFactory {

  private AccountFactory() {}

  public enum AccountType {
    CHECKING,
    SAVINGS
  }

  public static Account create(
      AccountType type,
      String iban,
      Customer owner
  ) {
    String id = UUID.randomUUID().toString();
    LocalDate creationDate = LocalDate.now();
    BigDecimal initialBalance = BigDecimal.ZERO;

    return switch (type) {
      case CHECKING -> new CheckingAccount(
          id,
          creationDate,
          iban,
          initialBalance,
          owner,
          BigDecimal.ZERO
      );

      case SAVINGS -> new SavingsAccount(
          id,
          creationDate,
          iban,
          initialBalance,
          owner,
          BigDecimal.ZERO
      );
    };
  }
}