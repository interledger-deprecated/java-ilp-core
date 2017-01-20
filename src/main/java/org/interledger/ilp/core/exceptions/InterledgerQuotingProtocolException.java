package org.interledger.ilp.core.exceptions;

public class InterledgerQuotingProtocolException extends Exception {

  private static final long serialVersionUID = 6272999499660262013L;

  public InterledgerQuotingProtocolException(String message) {
    super(message);
  }
  
  public InterledgerQuotingProtocolException(String message, Throwable cause) {
    super(message, cause);
  }

}
