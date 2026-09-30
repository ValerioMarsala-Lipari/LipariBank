package com.lipari.bank.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages database connections and schema initialization.
 *
 * @author Valerio
 * @since 1.0
 */
public final class DatabaseManager {

  private static final String URL = "jdbc:h2:./liparibank";

  private DatabaseManager() {
  }

  /**
   * Creates a new connection to the H2 database.
   *
   * @return database connection
   * @throws SQLException if a database connection cannot be established
   */
  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL);
  }

  /**
   * Initializes all database tables and indexes required by the application.
   *
   * @throws SQLException if schema initialization fails
   */
  public static void initializeSchema() throws SQLException {
    String customersTable = """
        CREATE TABLE IF NOT EXISTS customers (
            id            BIGINT AUTO_INCREMENT PRIMARY KEY,
            fiscal_code   VARCHAR(16) NOT NULL UNIQUE,
            first_name    VARCHAR(100) NOT NULL,
            last_name     VARCHAR(100) NOT NULL,
            customer_type VARCHAR(20) NOT NULL,
            created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        )
        """;

    String accountsTable = """
        CREATE TABLE IF NOT EXISTS accounts (
            id              BIGINT AUTO_INCREMENT PRIMARY KEY,
            iban            VARCHAR(34) NOT NULL UNIQUE,
            account_type    VARCHAR(20) NOT NULL,
            balance         DECIMAL(15,2) NOT NULL DEFAULT 0.00,
            customer_id     BIGINT NOT NULL,
            created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            overdraft_limit DECIMAL(15,2),
            interest_rate   DECIMAL(10,4),
            FOREIGN KEY (customer_id) REFERENCES customers(id)
        )
        """;

    String transactionsTable = """
        CREATE TABLE IF NOT EXISTS transactions (
            id          BIGINT AUTO_INCREMENT PRIMARY KEY,
            account_id  BIGINT NOT NULL,
            type        VARCHAR(20) NOT NULL,
            amount      DECIMAL(15,2) NOT NULL,
            description VARCHAR(500),
            created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY (account_id) REFERENCES accounts(id)
        )
        """;

    String policiesTable = """
        CREATE TABLE IF NOT EXISTS policies (
            id              BIGINT AUTO_INCREMENT PRIMARY KEY,
            policy_type     VARCHAR(50) NOT NULL,
            customer_id     BIGINT NOT NULL,
            premium         DECIMAL(15,2) NOT NULL,
            expiration_date DATE,
            status          VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
            FOREIGN KEY (customer_id) REFERENCES customers(id)
        )
        """;

    String accountsIndex = """
        CREATE INDEX IF NOT EXISTS idx_accounts_customer
        ON accounts(customer_id)
        """;

    String transactionsIndex = """
        CREATE INDEX IF NOT EXISTS idx_transactions_account
        ON transactions(account_id)
        """;

    String policiesIndex = """
        CREATE INDEX IF NOT EXISTS idx_policies_customer
        ON policies(customer_id)
        """;

    String riskScoresTable = """
        CREATE TABLE IF NOT EXISTS risk_scores (
            id              BIGINT AUTO_INCREMENT PRIMARY KEY,
            customer_id     BIGINT NOT NULL,
            score           INT NOT NULL,
            risk_level      VARCHAR(20) NOT NULL,
            calculated_at   TIMESTAMP NOT NULL,
            CONSTRAINT chk_risk_score CHECK (score BETWEEN 0 AND 100),
            FOREIGN KEY (customer_id) REFERENCES customers(id)
        )
        """;

    String alertsTable = """
        CREATE TABLE IF NOT EXISTS alerts (
            id           VARCHAR(36) PRIMARY KEY,
            customer_id  BIGINT NOT NULL,
            level        VARCHAR(20) NOT NULL,
            rule_name    VARCHAR(100) NOT NULL,
            message      VARCHAR(500) NOT NULL,
            created_at   TIMESTAMP NOT NULL,
            FOREIGN KEY (customer_id) REFERENCES customers(id)
        )
        """;

    try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
      statement.execute(customersTable);
      statement.execute(accountsTable);
      statement.execute(transactionsTable);
      statement.execute(policiesTable);

      statement.execute(accountsIndex);
      statement.execute(transactionsIndex);
      statement.execute(policiesIndex);
      statement.execute(riskScoresTable);
      statement.execute(alertsTable);
    }
  }
}