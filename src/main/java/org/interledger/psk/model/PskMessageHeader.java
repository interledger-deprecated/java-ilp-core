package org.interledger.psk.model;

/**
 * Defines a public or private header in a PSK message.
 */
public interface PskMessageHeader {

  /**
   * Returns the name of the header.
   */
  String getName();

  /**
   * Returns the value associated with the header.
   */
  String getValue();


  /**
   * Defines standard PSK headers found in the public section.
   */
  class PublicHeaders {

    /**
     * The mandatory public Nonce header.
     */
    public static final String NONCE = "Nonce";


    /**
     * The mandatory public Encryption header.
     */
    public static final String ENCRYPTION = "Encryption";
  }
}
