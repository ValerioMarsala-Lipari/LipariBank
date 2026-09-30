package com.lipari.bank.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a bank customer.
 *
 * <p>The fiscal code identifies the customer and is therefore used for
 * equality and hash code calculations.</p>
 *
 * @author Valerio
 * @since 1.0
 */
public class Customer {

  private Long id;
  private final String fiscalCode;
  private String firstName;
  private String lastName;
  private final CustomerType customerType;
  private final LocalDate createdAt;

  /**
   * Creates a customer with the specified details.
   *
   * @param fiscalCode   customer fiscal code
   * @param firstName    customer first name
   * @param lastName     customer last name
   * @param customerType customer type
   * @param createdAt    customer creation date
   * @throws IllegalArgumentException if a required value is null or blank
   */
  public Customer(String fiscalCode, String firstName, String lastName, CustomerType customerType, LocalDate createdAt) {
    if (fiscalCode == null || fiscalCode.isBlank()) {
      throw new IllegalArgumentException("Fiscal code cannot be null or blank");
    }

    if (firstName == null || firstName.isBlank()) {
      throw new IllegalArgumentException("First name cannot be null or blank");
    }

    if (lastName == null || lastName.isBlank()) {
      throw new IllegalArgumentException("Last name cannot be null or blank");
    }

    if (customerType == null) {
      throw new IllegalArgumentException("Customer type cannot be null");
    }

    if (createdAt == null) {
      throw new IllegalArgumentException("Created at cannot be null");
    }

    this.fiscalCode = fiscalCode;
    this.firstName = firstName;
    this.lastName = lastName;
    this.customerType = customerType;
    this.createdAt = createdAt;
  }

  /**
   * Returns the persistence identifier of the customer.
   *
   * @return customer identifier, or {@code null} if not persisted yet
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets the persistence identifier of the customer.
   *
   * @param id customer identifier
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Returns the customer's fiscal code.
   *
   * @return fiscal code
   */
  public String getFiscalCode() {
    return fiscalCode;
  }

  /**
   * Returns the customer's first name.
   *
   * @return first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Returns the customer's last name.
   *
   * @return last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Returns the customer's type.
   *
   * @return customer type
   */
  public CustomerType getCustomerType() {
    return customerType;
  }

  /**
   * Returns the date when the customer was created.
   *
   * @return customer creation date
   */
  public LocalDate getCreatedAt() {
    return createdAt;
  }

  /**
   * Updates the customer's first name.
   *
   * @param firstName new first name
   * @throws IllegalArgumentException if the name is null or blank
   */
  public void setFirstName(String firstName) {
    if (firstName == null || firstName.isBlank()) {
      throw new IllegalArgumentException("First name cannot be null or blank");
    }

    this.firstName = firstName;
  }

  /**
   * Updates the customer's last name.
   *
   * @param lastName new last name
   * @throws IllegalArgumentException if the name is null or blank
   */
  public void setLastName(String lastName) {
    if (lastName == null || lastName.isBlank()) {
      throw new IllegalArgumentException("Last name cannot be null or blank");
    }

    this.lastName = lastName;
  }

  /**
   * Compares this customer with another customer using the fiscal code.
   *
   * @param o object to compare
   * @return {@code true} if both objects represent the same customer
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Customer customer = (Customer) o;

    return Objects.equals(fiscalCode, customer.fiscalCode);
  }

  /**
   * Returns a hash code based on the customer's fiscal code.
   *
   * @return hash code for this customer
   */
  @Override
  public int hashCode() {
    return Objects.hash(fiscalCode);
  }
}