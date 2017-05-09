package org.interledger.ilp.exceptions;

import org.interledger.ilp.InterledgerError;

import java.util.Objects;

/**
 * Base ILP exception, see RFC REF: https://interledger.org/rfcs/0003-interledger-protocol/#errors
 */
public class InterledgerException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  private final InterledgerError interledgerError;

  /**
   * Constructs an immutable instance of <code>InterledgerException</code> Used by connectors
   * forwarding the exception. Check the RFC
   * https://interledger.org/rfcs/0003-interledger-protocol/#errors for the newest updated doc. The
   * helper static method InterledgerAddress[]
   * InterledgerException.addSelfToForwardedBy(forwardedBy, selfAddress) can be used to create an
   * updated forwardedBy list right before calling the constructor.
   * 
   * @param interledgerError The underlying error encapsulated in this exception.
   */
  public InterledgerException(final InterledgerError interledgerError) {
    super();
    this.interledgerError =
        Objects.requireNonNull(interledgerError, "interledgerError must not be null");
  }

  public InterledgerError getInterledgerError() {
    return interledgerError;
  }
}