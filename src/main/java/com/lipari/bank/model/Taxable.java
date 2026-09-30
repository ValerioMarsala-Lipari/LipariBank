package com.lipari.bank.model;

import java.math.BigDecimal;

/**
 * Represents an entity for which a tax amount can be calculated.
 *
 * @author Valerio
 * @since 1.0
 */
@FunctionalInterface
public interface Taxable {

  /**
   * Calculates the applicable tax amount.
   *
   * @return calculated tax amount
   */
  BigDecimal calculateTaxAmount();

  /**
   * Returns a description of the applicable tax.
   *
   * @return tax description
   */
  default String getTaxDescription() {
    return "Tax description";
  }
}