package org.interledger;

import org.interledger.ilp.InterledgerError;

import java.util.Objects;

/**
 * Base ILP exception, see RFC REF: https://interledger.org/rfcs/0003-interledger-protocol/#errors
 */
public class InterledgerProtocolException extends InterledgerRuntimeException {

  private static final long serialVersionUID = 1L;

  private final InterledgerError interledgerError;

  /**
   * Required-args constructor.
   *
   * @param interledgerError An instance of {@link InterledgerError} that is the underlying error
   *                         encapsulated by this exception.
   */
  public InterledgerProtocolException(final InterledgerError interledgerError) {
    super("Interledger protocol error.");
    this.interledgerError =
        Objects.requireNonNull(interledgerError, "interledgerError must not be null");
  }

  public InterledgerError getInterledgerError() {
    return interledgerError;
  }
}
