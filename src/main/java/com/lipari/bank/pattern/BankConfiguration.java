package com.lipari.bank.pattern;

import java.math.BigDecimal;

/**
 * Singleton containing the global configuration of the bank.
 *
 * @author Valerio
 * @since 1.0
 */
public final class BankConfiguration {

  private static volatile BankConfiguration instance;

  private BigDecimal dailyTransferLimit;
  private BigDecimal maxOverdraftLimit;
  private int minAgeForLifeInsurance;
  private String bankName;

  private BankConfiguration() {
  }

  /**
   * Returns the singleton bank configuration instance.
   *
   * @return shared bank configuration instance
   */
  public static BankConfiguration getInstance() {
    if (instance == null) {
      synchronized (BankConfiguration.class) {
        if (instance == null) {
          instance = new BankConfiguration();
        }
      }
    }

    return instance;
  }

  /**
   * Returns the daily transfer limit.
   *
   * @return daily transfer limit
   */
  public BigDecimal getDailyTransferLimit() {
    return dailyTransferLimit;
  }

  /**
   * Sets the daily transfer limit.
   *
   * @param dailyTransferLimit daily transfer limit
   */
  public void setDailyTransferLimit(BigDecimal dailyTransferLimit) {
    this.dailyTransferLimit = dailyTransferLimit;
  }

  /**
   * Returns the maximum overdraft limit.
   *
   * @return maximum overdraft limit
   */
  public BigDecimal getMaxOverdraftLimit() {
    return maxOverdraftLimit;
  }

  /**
   * Sets the maximum overdraft limit.
   *
   * @param maxOverdraftLimit maximum overdraft limit
   */
  public void setMaxOverdraftLimit(BigDecimal maxOverdraftLimit) {
    this.maxOverdraftLimit = maxOverdraftLimit;
  }

  /**
   * Returns the minimum age required for life insurance.
   *
   * @return minimum age for life insurance
   */
  public int getMinAgeForLifeInsurance() {
    return minAgeForLifeInsurance;
  }

  /**
   * Sets the minimum age required for life insurance.
   *
   * @param minAgeForLifeInsurance minimum age for life insurance
   */
  public void setMinAgeForLifeInsurance(int minAgeForLifeInsurance) {
    this.minAgeForLifeInsurance = minAgeForLifeInsurance;
  }

  /**
   * Returns the bank name.
   *
   * @return bank name
   */
  public String getBankName() {
    return bankName;
  }

  /**
   * Sets the bank name.
   *
   * @param bankName bank name
   */
  public void setBankName(String bankName) {
    this.bankName = bankName;
  }
}
