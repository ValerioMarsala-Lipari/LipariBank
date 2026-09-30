package com.lipari.bank.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Base class for financial products offered by the bank.
 *
 * <p>Each financial product has a unique identifier, a creation date
 * and a tax calculation defined by its concrete implementation.</p>
 *
 * @author Valerio
 * @since 1.0
 */
public abstract class FinancialProduct {

  private final String id;
  private final LocalDate creationDate;

  /**
   * Creates a financial product with the specified details.
   *
   * @param id           unique product identifier
   * @param creationDate product creation date
   * @throws IllegalArgumentException if the identifier is null or blank,
   *                                  or if the creation date is null
   */
  protected FinancialProduct(String id, LocalDate creationDate) {
    if (id == null || id.isBlank()) {
      throw new IllegalArgumentException("Id cannot be null or blank");
    }

    if (creationDate == null) {
      throw new IllegalArgumentException("Creation date cannot be null");
    }

    this.id = id;
    this.creationDate = creationDate;
  }

  /**
   * Returns the unique identifier of this financial product.
   *
   * @return product identifier
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the date when this financial product was created.
   *
   * @return product creation date
   */
  public LocalDate getCreationDate() {
    return creationDate;
  }

  /**
   * Calculates the tax applicable to this financial product.
   *
   * @return calculated tax amount
   */
  public abstract BigDecimal calculateTax();
}