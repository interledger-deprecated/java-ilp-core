package org.interledger.ilp.exceptions;

public class InterledgerQuotingProtocolException extends Exception {

  /*
   *  TODO:(?) Must this Exception be encoded in the data field of the
   *  InterledgerException. According to RFC 03:
   *  """
   *     - Protocols built on top of ILP that define behavior for certain errors 
   *     SHOULD specify the encoding format of error data.
   *     - Is ILQP considered a protocol "on top" of ILP or parallel to ILP?
   *     note earizon: Looks to me that some standard info of InterledgerException
   *         like "triggeredBy", "triggeredAt" can be useful, as well as the patterns
   *         to classify in Final, Temporal, Relative, ... 
   *  """
   */
  
  private static final long serialVersionUID = 6272999499660262013L;

  public InterledgerQuotingProtocolException(String message) {
    super(message);
  }

  public InterledgerQuotingProtocolException(String message, Throwable cause) {
    super(message, cause);
  }

}
