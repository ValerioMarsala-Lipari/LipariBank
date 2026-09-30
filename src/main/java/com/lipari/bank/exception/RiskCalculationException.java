package com.lipari.bank.exception;

/**
 * Thrown when a risk calculation cannot be completed.
 *
 * @author Valerio
 * @since 1.0
 */
public class RiskCalculationException extends RuntimeException {

  /**
   * Creates an exception with the specified message.
   *
   * @param message error message
   */
  public RiskCalculationException(String message) {
    super(message);
  }

  /**
   * Creates an exception with the specified message and cause.
   *
   * @param message error message
   * @param cause   underlying cause
   */
  public RiskCalculationException(String message, Throwable cause) {
    super(message, cause);
  }
}