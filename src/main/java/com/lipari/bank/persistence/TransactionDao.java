package com.lipari.bank.persistence;

import com.lipari.bank.model.Transaction;
import com.lipari.bank.model.TransactionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access object for banking transactions.
 *
 * @author Valerio
 * @since 1.0
 */
public class TransactionDao {

  /**
   * Saves a transaction associated with an account.
   *
   * @param transaction transaction to save
   * @param accountId   identifier of the account
   * @throws SQLException if the database operation fails
   */
  public void save(Transaction transaction, Long accountId) throws SQLException {
    String sql = """
        INSERT INTO transactions (
            account_id,
            type,
            amount,
            description
        )
        VALUES (?, ?, ?, ?)
        """;

    try (Connection connection = DatabaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, accountId);
      statement.setString(2, transaction.type().name());
      statement.setBigDecimal(3, transaction.amount());
      statement.setString(4, transaction.description());

      statement.executeUpdate();
    }
  }

  /**
   * Finds all transactions associated with an account, ordered from newest
   * to oldest.
   *
   * @param accountId identifier of the account
   * @return list of transactions
   * @throws SQLException if the database operation fails
   */
  public List<Transaction> findByAccountId(Long accountId) throws SQLException {
    String sql = """
        SELECT type, amount, description, created_at
        FROM transactions
        WHERE account_id = ?
        ORDER BY created_at DESC
        """;

    List<Transaction> transactions = new ArrayList<>();

    try (Connection connection = DatabaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, accountId);

      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          Transaction transaction = new Transaction(TransactionType.valueOf(resultSet.getString("type")), resultSet.getBigDecimal("amount"), resultSet.getString("description"), resultSet.getTimestamp("created_at").toLocalDateTime());

          transactions.add(transaction);
        }
      }
    }

    return transactions;
  }
}