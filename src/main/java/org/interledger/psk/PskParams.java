package org.interledger.psk;

/**
 * The parameters derived from a receiver secret for use in the PSK protocol.
 */
public interface PskParams {
  
  /**
   * Part of a random nonce used in a PSK receiver address.
   * 
   * @return Base64Url encoded 16 byte nonce
   */
  String getToken();

  /**
   * Receiver ID generated from token and receiver secret per PSK spec.
   * 
   * @return Base64Url encoded Receiver ID
   */
  String getReceiverId();
 
  /**
   * Shared key derived from receiver secret to use for PSK crypto operations.
   * 
   * @return 16 byte key
   */
  byte[] getSharedKey();
}
