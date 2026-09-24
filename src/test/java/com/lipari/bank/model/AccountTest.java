package com.lipari.bank.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class AccountTest {

  public static void main(String[] args) {

    Customer customer = new Customer(
        "RSSMRA80A01H501X",
        "Mario",
        "Rossi",
        CustomerType.PRIVATE
    );

    Account account1 = new CheckingAccount(
        "ACC-001",
        LocalDate.now(),
        "IT60X0542811101000000123456",
        BigDecimal.valueOf(1000),
        customer,
        BigDecimal.valueOf(300)

    );

    Account account2 = new CheckingAccount(
        "ACC-002",
        LocalDate.now(),
        "IT60X0542811101000000123456",
        BigDecimal.valueOf(5000),
        customer,
        BigDecimal.valueOf(300)
    );

    System.out.println("equals: " + account1.equals(account2));
    System.out.println(
        "same hashCode: " + (account1.hashCode() == account2.hashCode())
    );

    Map<Account, String> accounts = new HashMap<>();

    accounts.put(account1, "Conto Mario");

    System.out.println("HashMap get: " + accounts.get(account2));
  }
}