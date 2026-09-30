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

/**
 * Data access object for bank accounts.
 *
 * @author Valerio
 * @since 1.0
 */
public class AccountDao {

  /**
   * Saves an account associated with a customer.
   *
   * @param account    account to save
   * @param customerId identifier of the account owner
   * @return the saved account
   * @throws SQLException if a database error occurs
   */
  public Account save(Account account, Long customerId) throws SQLException {
    String sql = """
        INSERT INTO accounts (
            iban,
            account_type,
            balance,
            customer_id,
            created_at,
            overdraft_limit,
            interest_rate
        )
        VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

    try (Connection connection = DatabaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, account.getIban());
      statement.setString(2, account.getClass().getSimpleName());
      statement.setBigDecimal(3, account.getBalance());
      statement.setLong(4, customerId);
      statement.setObject(5, account.getCreationDate());

      if (account instanceof CheckingAccount checkingAccount) {
        statement.setBigDecimal(6, checkingAccount.getOverdraftLimit());
        statement.setBigDecimal(7, null);
      } else if (account instanceof SavingsAccount savingsAccount) {
        statement.setBigDecimal(6, null);
        statement.setBigDecimal(7, savingsAccount.getInterestRate());
      } else {
        statement.setBigDecimal(6, null);
        statement.setBigDecimal(7, null);
      }

      statement.executeUpdate();
    }

    return account;
  }

  /**
   * Finds an account by its IBAN.
   *
   * @param iban account IBAN
   * @return the account if found, otherwise an empty optional
   * @throws SQLException if a database error occurs
   */
  public Optional<Account> findByIban(String iban) throws SQLException {
    String sql = """
        SELECT iban,
               account_type,
               balance,
               customer_id,
               created_at,
               overdraft_limit,
               interest_rate
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

        return Optional.of(mapAccount(resultSet, customer));
      }
    }
  }

  /**
   * Finds all accounts belonging to a customer.
   *
   * @param customerId identifier of the customer
   * @return list of the customer's accounts
   * @throws SQLException if a database error occurs
   */
  public List<Account> findByCustomerId(Long customerId) throws SQLException {
    String sql = """
        SELECT iban,
               account_type,
               balance,
               customer_id,
               created_at,
               overdraft_limit,
               interest_rate
        FROM accounts
        WHERE customer_id = ?
        """;

    List<Account> accounts = new ArrayList<>();

    try (Connection connection = DatabaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, customerId);

      try (ResultSet resultSet = statement.executeQuery()) {
        Customer customer = new CustomerDao().findById(customerId).orElseThrow(() -> new SQLException("Customer not found: " + customerId));

        while (resultSet.next()) {
          accounts.add(mapAccount(resultSet, customer));
        }
      }
    }

    return accounts;
  }

  /**
   * Finds customers whose total account balance exceeds the specified threshold.
   *
   * @param threshold minimum total balance
   * @return customers whose total balance exceeds the threshold
   * @throws SQLException if a database error occurs
   */
  public List<Customer> findCustomersWithTotalBalanceAbove(BigDecimal threshold) throws SQLException {
    String sql = """
        SELECT c.id,
               c.fiscal_code,
               c.first_name,
               c.last_name,
               c.customer_type,
               c.created_at
        FROM customers c
        INNER JOIN accounts a ON c.id = a.customer_id
        GROUP BY c.id,
                 c.fiscal_code,
                 c.first_name,
                 c.last_name,
                 c.customer_type,
                 c.created_at
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

  /**
   * Maps a database row to the corresponding account subtype.
   *
   * @param resultSet result set containing account data
   * @param customer  account owner
   * @return mapped account
   * @throws SQLException if the account type is unknown or a column cannot be read
   */
  private Account mapAccount(ResultSet resultSet, Customer customer) throws SQLException {
    String accountType = resultSet.getString("account_type");
    String iban = resultSet.getString("iban");
    BigDecimal balance = resultSet.getBigDecimal("balance");
    LocalDate creationDate = resultSet.getTimestamp("created_at").toLocalDateTime().toLocalDate();

    return switch (accountType) {
      case "CheckingAccount" ->
          new CheckingAccount("DB-" + iban, creationDate, iban, balance, customer, resultSet.getBigDecimal("overdraft_limit"));
      case "SavingsAccount" ->
          new SavingsAccount("DB-" + iban, creationDate, iban, balance, customer, resultSet.getBigDecimal("interest_rate"));
      default -> throw new SQLException("Unknown account type: " + accountType);
    };
  }
}