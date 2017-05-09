package org.interledger.ilqp;

public class InterledgerQuotingException extends RuntimeException {

  private static final long serialVersionUID = 6272999499660262013L;

  public InterledgerQuotingException(String message) {
    super(message);
  }

  public InterledgerQuotingException(String message, Throwable cause) {
    super(message, cause);
  }

}
