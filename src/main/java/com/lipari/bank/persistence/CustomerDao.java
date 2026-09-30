package com.lipari.bank.persistence;

import com.lipari.bank.model.Customer;
import com.lipari.bank.model.CustomerType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDao {

  public Customer save(Customer customer) throws SQLException {

    String sql = """
        INSERT INTO customers (
            fiscal_code,
            first_name,
            last_name,
            customer_type,
            created_at
        )
        VALUES (?, ?, ?, ?, ?)
        """;

    try (Connection connection = DatabaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      statement.setString(1, customer.getFiscalCode());
      statement.setString(2, customer.getFirstName());
      statement.setString(3, customer.getLastName());
      statement.setString(4, customer.getCustomerType().name());
      statement.setObject(5, customer.getCreatedAt());

      statement.executeUpdate();

      try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

        if (!generatedKeys.next()) {
          throw new SQLException("Failed to retrieve generated customer ID");
        }

        customer.setId(generatedKeys.getLong(1));
      }
    }

    return customer;
  }

  public Optional<Customer> findById(Long id) throws SQLException {

    String sql = """
        SELECT id, fiscal_code, first_name, last_name, customer_type, created_at
        FROM customers
        WHERE id = ?
        """;

    try (Connection connection = DatabaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setLong(1, id);

      try (ResultSet resultSet = statement.executeQuery()) {

        if (resultSet.next()) {
          return Optional.of(mapCustomer(resultSet));
        }

        return Optional.empty();
      }
    }
  }

  public Optional<Customer> findByFiscalCode(String fiscalCode) throws SQLException {

    String sql = """
        SELECT id, fiscal_code, first_name, last_name, customer_type, created_at
        FROM customers
        WHERE fiscal_code = ?
        """;

    try (Connection connection = DatabaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setString(1, fiscalCode.trim().toUpperCase());

      try (ResultSet resultSet = statement.executeQuery()) {

        if (resultSet.next()) {
          return Optional.of(mapCustomer(resultSet));
        }

        return Optional.empty();
      }
    }
  }

  public List<Customer> findAll() throws SQLException {

    String sql = """
        SELECT id, fiscal_code, first_name, last_name, customer_type,  created_at
        FROM customers
        ORDER BY last_name
        """;

    List<Customer> customers = new ArrayList<>();

    try (Connection connection = DatabaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {

      while (resultSet.next()) {
        customers.add(mapCustomer(resultSet));
      }
    }

    return customers;
  }

  public boolean delete(Long id) throws SQLException {

    String sql = """
        DELETE FROM customers
        WHERE id = ?
        """;

    try (Connection connection = DatabaseManager.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setLong(1, id);

      int rowsAffected = statement.executeUpdate();

      return rowsAffected > 0;
    }
  }

  private Customer mapCustomer(ResultSet resultSet) throws SQLException {
    Customer customer = new Customer(
        resultSet.getString("fiscal_code"),
        resultSet.getString("first_name"),
        resultSet.getString("last_name"),
        CustomerType.valueOf(resultSet.getString("customer_type")),
        resultSet.getTimestamp("created_at").toLocalDateTime().toLocalDate()
    );

    customer.setId(resultSet.getLong("id"));

    return customer;
  }
}