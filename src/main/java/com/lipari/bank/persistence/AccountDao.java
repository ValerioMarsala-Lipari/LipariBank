package com.lipari.bank.persistence;

import com.lipari.bank.model.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDao {

  public Account save(Account account, Long customerId) throws SQLException {

    String sql = """
        INSERT INTO accounts (
            iban,
            account_type,
            balance,
            customer_id
        )
        VALUES (?, ?, ?, ?)
        """;

    try (Connection connection = DatabaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setString(1, account.getIban());
      statement.setString(2, account.getClass().getSimpleName());
      statement.setBigDecimal(3, account.getBalance());
      statement.setLong(4, customerId);

      statement.executeUpdate();
    }

    return account;
  }

  public Optional<Account> findByIban(String iban) throws SQLException {

    String sql = """
        SELECT iban, account_type, balance, customer_id
        FROM accounts
        WHERE iban = ?
        """;

    try (Connection connection = DatabaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setString(1, iban);

      try (ResultSet resultSet = statement.executeQuery()) {

        if (!resultSet.next()) {
          return Optional.empty();
        }

        Long customerId = resultSet.getLong("customer_id");

        Customer customer = new CustomerDao().findById(customerId).orElseThrow(() -> new SQLException("Customer not found: " + customerId));

        String accountType = resultSet.getString("account_type");
        String accountIban = resultSet.getString("iban");
        BigDecimal balance = resultSet.getBigDecimal("balance");

        Account account;

        if ("CheckingAccount".equals(accountType)) {

          account = new CheckingAccount("DB-" + accountIban, LocalDate.now(), accountIban, balance, customer, BigDecimal.ZERO);

        } else if ("SavingsAccount".equals(accountType)) {

          account = new SavingsAccount("DB-" + accountIban, LocalDate.now(), accountIban, balance, customer, BigDecimal.ZERO);

        } else {
          throw new SQLException("Unknown account type: " + accountType);
        }

        return Optional.of(account);
      }
    }
  }

  public List<Account> findByCustomerId(Long customerId) throws SQLException {

    String sql = """
        SELECT iban, account_type, balance, customer_id
        FROM accounts
        WHERE customer_id = ?
        """;

    List<Account> accounts = new ArrayList<>();

    try (Connection connection = DatabaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setLong(1, customerId);

      try (ResultSet resultSet = statement.executeQuery()) {

        Customer customer = new CustomerDao().findById(customerId).orElseThrow(() -> new SQLException("Customer not found: " + customerId));

        while (resultSet.next()) {

          String accountType = resultSet.getString("account_type");
          String iban = resultSet.getString("iban");
          BigDecimal balance = resultSet.getBigDecimal("balance");

          Account account;

          if ("CheckingAccount".equals(accountType)) {

            account = new CheckingAccount("DB-" + iban, LocalDate.now(), iban, balance, customer, BigDecimal.ZERO);

          } else if ("SavingsAccount".equals(accountType)) {

            account = new SavingsAccount("DB-" + iban, LocalDate.now(), iban, balance, customer, BigDecimal.ZERO);

          } else {
            throw new SQLException("Unknown account type: " + accountType);
          }

          accounts.add(account);
        }
      }
    }

    return accounts;
  }

  public List<Customer> findCustomersWithTotalBalanceAbove(BigDecimal threshold) throws SQLException {

    String sql = """
        SELECT c.id, c.fiscal_code, c.first_name, c.last_name, c.customer_type, c.created_at
        FROM customers c
        INNER JOIN accounts a ON c.id = a.customer_id
        GROUP BY c.id, c.fiscal_code, c.first_name, c.last_name, c.customer_type
        HAVING SUM(a.balance) > ?
        ORDER BY SUM(a.balance) DESC
        """;

    List<Customer> customers = new ArrayList<>();

    try (Connection connection = DatabaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setBigDecimal(1, threshold);

      try (ResultSet resultSet = statement.executeQuery()) {

        while (resultSet.next()) {

          Customer customer = new Customer(resultSet.getString("fiscal_code"), resultSet.getString("first_name"), resultSet.getString("last_name"), CustomerType.valueOf(resultSet.getString("customer_type")), resultSet.getTimestamp("created_at").toLocalDateTime().toLocalDate());

          customer.setId(resultSet.getLong("id"));

          customers.add(customer);
        }
      }
    }

    return customers;
  }
}