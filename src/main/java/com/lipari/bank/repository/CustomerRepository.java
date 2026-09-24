package com.lipari.bank.repository;

import com.lipari.bank.model.Customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CustomerRepository {

  private final Map<String, Customer> customers = new HashMap<>();

  public Customer save(Customer customer) {
    String key = customer.getFiscalCode().toUpperCase().trim();

    customers.put(key, customer);

    return customer;
  }

  public Optional<Customer> findByFiscalCode(String fiscalCode) {
    String key = fiscalCode.toUpperCase().trim();

    return Optional.ofNullable(customers.get(key));
  }

  public List<Customer> findAll() {
    return List.copyOf(customers.values());
  }

  public boolean existsByFiscalCode(String fiscalCode) {
    String key = fiscalCode.toUpperCase().trim();

    return customers.containsKey(key);
  }

  public boolean deleteByFiscalCode(String fiscalCode) {
    String key = fiscalCode.toUpperCase().trim();

    return customers.remove(key) != null;
  }

  public int count() {
    return customers.size();
  }
}