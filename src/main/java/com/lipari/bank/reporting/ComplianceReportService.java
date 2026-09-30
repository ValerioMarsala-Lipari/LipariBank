package com.lipari.bank.reporting;

import com.lipari.bank.compliance.alert.Alert;
import com.lipari.bank.compliance.alert.AlertLevel;
import com.lipari.bank.model.CustomerType;
import com.lipari.bank.risk.RiskLevel;
import com.lipari.bank.risk.RiskScore;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Generates compliance reports from risk scores and compliance alerts.
 *
 * @author Valerio
 * @since 1.0
 */
public class ComplianceReportService {

  private static final int FLAG_THRESHOLD = 80;

  /**
   * Generates a compliance report.
   *
   * @param riskScores    calculated risk scores
   * @param alerts        generated compliance alerts
   * @param customerTypes customer type associated with each customer ID
   * @return generated compliance report
   */
  public ComplianceReport generateReport(List<RiskScore> riskScores, List<Alert> alerts, Map<Long, CustomerType> customerTypes) {
    if (riskScores == null) {
      throw new IllegalArgumentException("Risk scores cannot be null");
    }

    if (alerts == null) {
      throw new IllegalArgumentException("Alerts cannot be null");
    }

    if (customerTypes == null) {
      throw new IllegalArgumentException("Customer types cannot be null");
    }

    Map<RiskLevel, Long> riskDistribution = riskScores.stream().collect(Collectors.groupingBy(RiskScore::level, Collectors.counting()));

    List<Alert> sortedAlerts = alerts.stream().sorted(Comparator.comparing(Alert::level, Comparator.comparingInt(AlertLevel::ordinal).reversed())).toList();

    Map<String, Double> averageRiskByCustomerType = riskScores.stream().filter(riskScore -> customerTypes.containsKey(riskScore.customerId())).collect(Collectors.groupingBy(riskScore -> customerTypes.get(riskScore.customerId()).name(), Collectors.averagingInt(RiskScore::score)));

    List<RiskScore> customersToFlag = riskScores.stream().filter(riskScore -> riskScore.score() > FLAG_THRESHOLD).toList();

    return new ComplianceReport(riskDistribution, sortedAlerts, averageRiskByCustomerType, customersToFlag);
  }
}