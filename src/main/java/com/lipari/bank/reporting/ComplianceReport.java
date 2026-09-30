package com.lipari.bank.reporting;

import com.lipari.bank.compliance.alert.Alert;
import com.lipari.bank.risk.RiskLevel;
import com.lipari.bank.risk.RiskScore;

import java.util.List;
import java.util.Map;

/**
 * Represents a compliance report generated from risk scores and compliance alerts.
 *
 * @param riskDistribution          number of customers for each risk level
 * @param openAlerts                alerts currently detected, ordered by severity
 * @param averageRiskByCustomerType average risk score grouped by customer type
 * @param customersToFlag           customers with a risk score above 80
 * @author Valerio
 * @since 1.0
 */
public record ComplianceReport(Map<RiskLevel, Long> riskDistribution, List<Alert> openAlerts,
                               Map<String, Double> averageRiskByCustomerType, List<RiskScore> customersToFlag) {
  public ComplianceReport {
    if (riskDistribution == null) {
      throw new IllegalArgumentException("Risk distribution cannot be null");
    }

    if (openAlerts == null) {
      throw new IllegalArgumentException("Open alerts cannot be null");
    }

    if (averageRiskByCustomerType == null) {
      throw new IllegalArgumentException("Average risk by customer type cannot be null");
    }

    if (customersToFlag == null) {
      throw new IllegalArgumentException("Customers to flag cannot be null");
    }

    riskDistribution = Map.copyOf(riskDistribution);
    openAlerts = List.copyOf(openAlerts);
    averageRiskByCustomerType = Map.copyOf(averageRiskByCustomerType);
    customersToFlag = List.copyOf(customersToFlag);
  }
}