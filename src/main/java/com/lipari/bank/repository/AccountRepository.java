package com.lipari.bank.repository;

import com.lipari.bank.model.Account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AccountRepository {

  private final Map<String, Account> accounts = new HashMap<>();

  public Account save(Account account) {
    accounts.put(account.getIban(), account);

    return account;
  }

  public Optional<Account> findByIban(String iban) {
    return Optional.ofNullable(accounts.get(iban));
  }

  public List<Account> findAll() {
    return List.copyOf(accounts.values());
  }

  public boolean existsByIban(String iban) {
    return accounts.containsKey(iban);
  }

  public boolean deleteByIban(String iban) {
    return accounts.remove(iban) != null;
  }

  public int count() {
    return accounts.size();
  }
}