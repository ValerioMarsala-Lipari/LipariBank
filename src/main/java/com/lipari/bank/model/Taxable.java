package com.lipari.bank.model;

import java.math.BigDecimal;

@FunctionalInterface
public interface Taxable {

  BigDecimal calculateTaxAmount();

  default String getTaxDescription() {
    return "Tax description";
  }
}