package com.lipari.bank.exception;

/**
 * Thrown when compliance alerts cannot be generated.
 *
 * @author Valerio
 * @since 1.0
 */
public class AlertGenerationException extends RuntimeException {

  /**
   * Creates an exception with the specified message.
   *
   * @param message error message
   */
  public AlertGenerationException(String message) {
    super(message);
  }

  /**
   * Creates an exception with the specified message and cause.
   *
   * @param message error message
   * @param cause   underlying cause
   */
  public AlertGenerationException(String message, Throwable cause) {
    super(message, cause);
  }
}