package com.lipari.bank.repository;

import com.lipari.bank.model.Customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory repository for bank customers.
 *
 * @author Valerio
 * @since 1.0
 */
public class CustomerRepository {

  private final Map<String, Customer> customers = new HashMap<>();

  /**
   * Saves a customer using the normalized fiscal code as the key.
   *
   * @param customer customer to save
   * @return saved customer
   */
  public Customer save(Customer customer) {
    String key = normalizeFiscalCode(customer.getFiscalCode());

    customers.put(key, customer);

    return customer;
  }

  /**
   * Finds a customer by fiscal code.
   *
   * @param fiscalCode customer fiscal code
   * @return customer if found, otherwise an empty optional
   */
  public Optional<Customer> findByFiscalCode(String fiscalCode) {
    String key = normalizeFiscalCode(fiscalCode);

    return Optional.ofNullable(customers.get(key));
  }

  /**
   * Returns all customers currently stored in the repository.
   *
   * @return immutable list of customers
   */
  public List<Customer> findAll() {
    return List.copyOf(customers.values());
  }

  /**
   * Checks whether a customer with the specified fiscal code exists.
   *
   * @param fiscalCode customer fiscal code
   * @return {@code true} if the customer exists
   */
  public boolean existsByFiscalCode(String fiscalCode) {
    String key = normalizeFiscalCode(fiscalCode);

    return customers.containsKey(key);
  }

  /**
   * Deletes a customer by fiscal code.
   *
   * @param fiscalCode customer fiscal code
   * @return {@code true} if a customer was deleted
   */
  public boolean deleteByFiscalCode(String fiscalCode) {
    String key = normalizeFiscalCode(fiscalCode);

    return customers.remove(key) != null;
  }

  /**
   * Returns the number of customers in the repository.
   *
   * @return customer count
   */
  public int count() {
    return customers.size();
  }

  /**
   * Normalizes a fiscal code for consistent repository lookups.
   *
   * @param fiscalCode fiscal code to normalize
   * @return normalized fiscal code
   */
  private String normalizeFiscalCode(String fiscalCode) {
    return fiscalCode.trim().toUpperCase();
  }
}