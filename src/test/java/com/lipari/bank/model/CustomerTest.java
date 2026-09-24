package com.lipari.bank.model;

import java.util.HashMap;
import java.util.Map;

public class CustomerTest {

  public static void main(String[] args) {

    Customer customer1 = new Customer("RSSMRA80A01H501X", "Mario", "Rossi", CustomerType.PRIVATE);

    Customer customer2 = new Customer("RSSMRA80A01H501X", "Marco", "Rossi", CustomerType.PRIVATE);

    System.out.println("equals: " + customer1.equals(customer2));
    System.out.println("same hashCode: " + (customer1.hashCode() == customer2.hashCode()));

    Map<Customer, String> customers = new HashMap<>();

    customers.put(customer1, "Conto principale");

    System.out.println("HashMap get: " + customers.get(customer2));
  }
}