package com.lipari.bank.repository;

import com.lipari.bank.model.Account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory repository for bank accounts.
 *
 * @author Valerio
 * @since 1.0
 */
public class AccountRepository {

  private final Map<String, Account> accounts = new HashMap<>();

  /**
   * Saves an account using its IBAN as the key.
   *
   * @param account account to save
   * @return saved account
   */
  public Account save(Account account) {
    accounts.put(normalizeIban(account.getIban()), account);
    return account;

  }

  /**
   * Finds an account by IBAN.
   *
   * @param iban account IBAN
   * @return account if found, otherwise an empty optional
   */
  public Optional<Account> findByIban(String iban) {
    return Optional.ofNullable(accounts.get(normalizeIban(iban)));
  }

  /**
   * Returns all accounts currently stored in the repository.
   *
   * @return immutable list of accounts
   */
  public List<Account> findAll() {
    return List.copyOf(accounts.values());
  }

  /**
   * Checks whether an account with the specified IBAN exists.
   *
   * @param iban account IBAN
   * @return {@code true} if the account exists
   */
  public boolean existsByIban(String iban) {
    return accounts.containsKey(normalizeIban(iban));
  }

  /**
   * Deletes an account by IBAN.
   *
   * @param iban account IBAN
   * @return {@code true} if an account was deleted
   */
  public boolean deleteByIban(String iban) {
    return accounts.remove(normalizeIban(iban)) != null;
  }

  /**
   * Returns the number of accounts in the repository.
   *
   * @return account count
   */
  public int count() {
    return accounts.size();
  }

  private String normalizeIban(String iban) {
    return iban.trim().toUpperCase();
  }
}