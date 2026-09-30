package com.lipari.bank.exception;

/**
 * Base runtime exception for application-specific banking errors.
 *
 * @author Valerio
 * @since 1.0
 */
public class BankException extends RuntimeException {

  private final String errorCode;

  /**
   * Creates a banking exception with a message and an application error code.
   *
   * @param message error message
   * @param errorCode application-specific error code
   */
  public BankException(String message, String errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  /**
   * Returns the application-specific error code.
   *
   * @return error code
   */
  public String getErrorCode() {
    return errorCode;
  }
}