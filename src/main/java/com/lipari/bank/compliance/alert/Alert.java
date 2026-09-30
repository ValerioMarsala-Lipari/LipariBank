package com.lipari.bank.compliance.alert;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a compliance alert generated for a customer.
 *
 * @param id         unique identifier of the alert
 * @param customerId identifier of the customer
 * @param level      severity of the alert
 * @param ruleName   name of the compliance rule that generated the alert
 * @param message    descriptive message of the alert
 * @param timestamp  date and time when the alert was generated
 * @author Valerio
 * @since 1.0
 */
public record Alert(String id, Long customerId, AlertLevel level, String ruleName, String message,
                    LocalDateTime timestamp) {

  public Alert {
    if (id == null || id.isBlank()) {
      id = UUID.randomUUID().toString();
    }

    if (customerId == null) {
      throw new IllegalArgumentException("Customer ID cannot be null");
    }

    if (level == null) {
      throw new IllegalArgumentException("Alert level cannot be null");
    }

    if (ruleName == null || ruleName.isBlank()) {
      throw new IllegalArgumentException("Rule name cannot be null or blank");
    }

    if (message == null || message.isBlank()) {
      throw new IllegalArgumentException("Message cannot be null or blank");
    }

    if (timestamp == null) {
      timestamp = LocalDateTime.now();
    }
  }
}