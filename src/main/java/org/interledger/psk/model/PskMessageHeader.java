package org.interledger.psk.model;

/**
 * Defines a public or private header in a PSK message.
 */
public interface PskMessageHeader {
  
  /**
   * Returns the name of the header.
   */
  public String getName();
  
  /**
   * Returns the value associated with the header.
   */
  public String getValue();
  
  
  /**
   * Defines standard PSK headers found in the public section.
   */
  public static class PublicHeaders {
    
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
