package org.interledger.psk;

import org.interledger.psk.model.PskMessage;
import org.interledger.psk.model.PskMessageHeader;
import org.interledger.psk.model.PskMessageImpl;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

/**
 * A builder for constructing concrete Pre-Shared Key message instances.
 */
public class PskMessageBuilder {

  private PskMessageImpl message;
  
  public PskMessageBuilder() {
    message = new PskMessageImpl();
  }

  /**
   * Adds a header to the <b>public</b> portion of the PSK message. Note that public headers are
   * visible to all parties transmitting the message.
   * 
   * @param header The header to add to the public header portion of the message.
   */
  public PskMessageBuilder addPublicHeader(PskMessageHeader header) {
    Objects.requireNonNull(header, "Cannot add null header");
    
    //TODO: not sure about this. the writers will set the header correctly, so it makes sense that
    //we prevent this happening. on the other hand, we are coupling this convenience builder to the
    //internal writer implementation.
    if (header.getName().equalsIgnoreCase(PskMessageHeader.PublicHeaders.ENCRYPTION)) {
      throw new IllegalArgumentException(
          "Encryption headers should not be added manually, "
          + "the message writer will set the appropriate header");
    }
    
    message.addPublicHeader(header);
    
    return this;
  }

  /**
   * Adds a header to the <b>public</b> portion of the PSK message. Note that public headers are
   * visible to all parties transmitting the message.
   * 
   * @param name The name of the header.
   * @param value The value associated with the header.
   */
  public PskMessageBuilder addPublicHeader(String name, String value) {
    Objects.requireNonNull(name, "Cannot add null header name");
    Objects.requireNonNull(value, "Cannot add null header value");
    
    //TODO: not sure about this. the writers will set the header correctly, so it makes sense that
    //we prevent this happening. on the other hand, we are coupling this convenience builder to the
    //internal writer implementation.
    if (name.equalsIgnoreCase(PskMessageHeader.PublicHeaders.ENCRYPTION)) {
      throw new IllegalArgumentException(
          "Encryption headers should not be added manually, "
          + "the message writer will set the appropriate header");
    }
    
    message.addPublicHeader(name, value);
    
    return this;
  }
  
  /**
   * Adds a header to the <b>private</b> portion of the PSK message. Note that public headers are
   * visible to all parties transmitting the message.
   * 
   * @param header The header to add to the public headers in the message.
   */
  public PskMessageBuilder addPrivateHeader(PskMessageHeader header) {
    message.addPrivateHeader(header);
    
    return this;
  }

  /**
   * Adds a header to the <b>public</b> portion of the PSK message. Note that public headers are
   * visible to all parties transmitting the message. The method will replace
   * 
   * @param name The name of the header.
   * @param value The value associated with the header.
   */
  public PskMessageBuilder addPrivateHeader(String name, String value) {
    message.addPrivateHeader(name, value);
    
    return this;
  }
  
  /**
   * Sets the application data contained in the <b>private</b> portion of the PSK message. Note 
   * that the content of the private data may be visible to all parties unless the message uses 
   * encryption.
   * 
   * @param applicationData The application data to store in the message.
   */
  public PskMessageBuilder setApplicationData(byte[] applicationData) {
    message.setApplicationData(applicationData);
    
    return this;
  }
  
  /**
   * Sets the Nonce that will be carried in the set of <b>public</b> headers in the PSK message.
   * 
   * @param nonce The value of the Nonce, expected to be 16 bytes.
   */
  public PskMessageBuilder withNonce(byte[] nonce) {
    if (nonce == null || nonce.length != 16) {
      throw new IllegalArgumentException("Invalid Nonce value - Nonce must be 16 bytes.");
    }

    message.addPublicHeader(PskMessageHeader.PublicHeaders.NONCE,
        Base64.getUrlEncoder().encodeToString(nonce));

    return this;
  }
  
  /**
   * Generates a cryptographically strong Nonce that will be carried in the set of <b>public</b> 
   * headers in the PSK message.
   */
  public PskMessageBuilder withNonce() {
    try {
      SecureRandom sr = SecureRandom.getInstanceStrong();
      byte[] nonce = new byte[16];
      sr.nextBytes(nonce);
      
      withNonce(nonce);
    } catch (NoSuchAlgorithmException nsa) {
      throw new RuntimeException("Could not generate secure nonce", nsa);
    }
    
    return this;
  }  
  
  /**
   * Builds the PSK message with the data provided.
   */
  public PskMessage toMessage() {
    return message;
  }
}


