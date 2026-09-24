package com.lipari.bank.pattern;

import java.math.BigDecimal;

public final class BankConfiguration {

  private static volatile BankConfiguration instance;

  private BigDecimal dailyTransferLimit;
  private BigDecimal maxOverdraftLimit;
  private int minAgeForLifeInsurance;
  private String bankName;

  private BankConfiguration(){
  }

  public static BankConfiguration getInstance(){
    if (instance == null){
      synchronized (BankConfiguration.class){
        if (instance == null){
          instance = new BankConfiguration();
        }
      }
    }

    return instance;
  }

  public BigDecimal getDailyTransferLimit() {
    return dailyTransferLimit;
  }

  public void setDailyTransferLimit(BigDecimal dailyTransferLimit) {
    this.dailyTransferLimit = dailyTransferLimit;
  }

  public BigDecimal getMaxOverdraftLimit() {
    return maxOverdraftLimit;
  }

  public void setMaxOverdraftLimit(BigDecimal maxOverdraftLimit) {
    this.maxOverdraftLimit = maxOverdraftLimit;
  }

  public int getMinAgeForLifeInsurance() {
    return minAgeForLifeInsurance;
  }

  public void setMinAgeForLifeInsurance(int minAgeForLifeInsurance) {
    this.minAgeForLifeInsurance = minAgeForLifeInsurance;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }
}