package com.lipari.bank.model;

public class Customer {

  private final String fiscalCode;
  private String firstName;
  private String lastName;
  private final CustomerType customerType;

  public Customer(
      String fiscalCode,
      String firstName,
      String lastName,
      CustomerType customerType
  ) {
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

    this.fiscalCode = fiscalCode;
    this.firstName = firstName;
    this.lastName = lastName;
    this.customerType = customerType;
  }

  public String getFiscalCode() {
    return fiscalCode;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public CustomerType getCustomerType() {
    return customerType;
  }

  public void setFirstName(String firstName) {
    if (firstName == null || firstName.isBlank()) {
      throw new IllegalArgumentException("First name cannot be null or blank");
    }

    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    if (lastName == null || lastName.isBlank()) {
      throw new IllegalArgumentException("Last name cannot be null or blank");
    }

    this.lastName = lastName;
  }
}