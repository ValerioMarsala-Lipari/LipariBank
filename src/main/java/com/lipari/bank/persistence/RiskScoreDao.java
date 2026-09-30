package com.lipari.bank.persistence;

import com.lipari.bank.risk.RiskLevel;
import com.lipari.bank.risk.RiskScore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access object for persisted risk scores.
 *
 * @author Valerio
 * @since 1.0
 */
public class RiskScoreDao {

  /**
   * Saves a risk score into the database.
   *
   * @param riskScore risk score to save
   * @throws SQLException if the database operation fails
   */
  public void save(RiskScore riskScore) throws SQLException {
    String sql = """
        INSERT INTO risk_scores (
            customer_id,
            score,
            risk_level,
            calculated_at
        )
        VALUES (?, ?, ?, ?)
        """;

    try (Connection connection = DatabaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, riskScore.customerId());
      statement.setInt(2, riskScore.score());
      statement.setString(3, riskScore.level().name());
      statement.setTimestamp(4, Timestamp.valueOf(riskScore.calculatedAt()));

      statement.executeUpdate();
    }
  }

  /**
   * Finds all persisted risk scores ordered by calculation time.
   *
   * @return persisted risk scores
   * @throws SQLException if the database operation fails
   */
  public List<RiskScore> findAll() throws SQLException {
    String sql = """
        SELECT customer_id, score, risk_level, calculated_at
        FROM risk_scores
        ORDER BY calculated_at
        """;

    List<RiskScore> riskScores = new ArrayList<>();

    try (Connection connection = DatabaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        riskScores.add(mapRow(resultSet));
      }
    }

    return riskScores;
  }

  /**
   * Maps a database row to a risk score.
   *
   * @param resultSet result set containing risk score data
   * @return mapped risk score
   * @throws SQLException if a database column cannot be read
   */
  private RiskScore mapRow(ResultSet resultSet) throws SQLException {
    return new RiskScore(resultSet.getLong("customer_id"), resultSet.getInt("score"), RiskLevel.valueOf(resultSet.getString("risk_level")), resultSet.getTimestamp("calculated_at").toLocalDateTime());
  }
}