package org.interledger.psk.model;

import java.util.List;

/**
 * This interface represents the message format as defined in the Pre-Shared Key Transport Protocol.
 */
public interface PskMessage {

  /**
   * Returns a list of all public headers in the message. Note that all parties may view the public
   * headers.
   */
  List<PskMessageHeader> getPublicHeaders();

  /**
   * Returns the all the <b>public</b> headers with the specified name.
   *
   * @param headerName The name of the header(s) to return
   * @return the <b>public</b> headers whose name matches the parameter, or an empty list.
   */
  List<PskMessageHeader> getPublicHeaders(String headerName);

  /**
   * Returns a list of all <b>private</b> headers in the message, provided that either the message
   * was <b>not</b> encrypted, or the private portion of the message has been decrypted by the
   * receiver.
   */
  List<PskMessageHeader> getPrivateHeaders();

  /**
   * Returns a list of all <b>private</b> headers in the message with the specified name. Note that
   * this depends on either the message <b>not</b> being encrypted, or that the private portion of
   * the message has already been decrypted by the receiver.
   *
   * @param headerName The name of the header(s) to return
   * @return the <b>private</b> headers whose name matches the parameter, or an empty list.
   */
  List<PskMessageHeader> getPrivateHeader(String headerName);

  /**
   * Returns the application data included in the private portion of the message, provided that
   * either the message is not encrypted, or has already been decrypted by the receiver.
   */
  byte[] getApplicationData();
}
