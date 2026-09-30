package com.lipari.bank.pattern;

import com.lipari.bank.model.Account;
import com.lipari.bank.model.CheckingAccount;
import com.lipari.bank.model.Customer;
import com.lipari.bank.model.SavingsAccount;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Factory for creating bank accounts.
 *
 * @author Valerio
 * @since 1.0
 */
public final class AccountFactory {

  private AccountFactory() {
  }

  /**
   * Represents the supported account types.
   */
  public enum AccountType {
    CHECKING, SAVINGS
  }

  /**
   * Creates a new account of the specified type.
   *
   * @param type  type of account to create
   * @param iban  account IBAN
   * @param owner account owner
   * @return newly created account
   * @throws IllegalArgumentException if the account type is null
   */
  public static Account create(AccountType type, String iban, Customer owner) {
    if (type == null) {
      throw new IllegalArgumentException("Account type cannot be null");
    }

    String id = UUID.randomUUID().toString();
    LocalDate creationDate = LocalDate.now();
    BigDecimal initialBalance = BigDecimal.ZERO;

    return switch (type) {
      case CHECKING -> new CheckingAccount(id, creationDate, iban, initialBalance, owner, BigDecimal.ZERO);

      case SAVINGS -> new SavingsAccount(id, creationDate, iban, initialBalance, owner, BigDecimal.ZERO);
    };
  }
}
