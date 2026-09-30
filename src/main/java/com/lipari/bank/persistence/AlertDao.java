package com.lipari.bank.persistence;

import com.lipari.bank.compliance.alert.Alert;
import com.lipari.bank.compliance.alert.AlertLevel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access object for persisted compliance alerts.
 *
 * @author Valerio
 * @since 1.0
 */
public class AlertDao {

  /**
   * Saves a compliance alert into the database.
   *
   * @param alert compliance alert to save
   * @throws SQLException if the database operation fails
   */
  public void save(Alert alert) throws SQLException {
    String sql = """
        INSERT INTO alerts (
            id,
            customer_id,
            level,
            rule_name,
            message,
            created_at
        )
        VALUES (?, ?, ?, ?, ?, ?)
        """;

    try (Connection connection = DatabaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, alert.id());
      statement.setLong(2, alert.customerId());
      statement.setString(3, alert.level().name());
      statement.setString(4, alert.ruleName());
      statement.setString(5, alert.message());
      statement.setTimestamp(6, Timestamp.valueOf(alert.timestamp()));

      statement.executeUpdate();
    }
  }

  /**
   * Finds all persisted compliance alerts.
   *
   * @return persisted compliance alerts
   * @throws SQLException if the database operation fails
   */
  public List<Alert> findAll() throws SQLException {
    String sql = """
        SELECT id, customer_id, level, rule_name, message, created_at
        FROM alerts
        ORDER BY created_at
        """;

    List<Alert> alerts = new ArrayList<>();

    try (Connection connection = DatabaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        alerts.add(mapRow(resultSet));
      }
    }

    return alerts;
  }

  private Alert mapRow(ResultSet resultSet) throws SQLException {
    return new Alert(resultSet.getString("id"), resultSet.getLong("customer_id"), AlertLevel.valueOf(resultSet.getString("level")), resultSet.getString("rule_name"), resultSet.getString("message"), resultSet.getTimestamp("created_at").toLocalDateTime());
  }
}