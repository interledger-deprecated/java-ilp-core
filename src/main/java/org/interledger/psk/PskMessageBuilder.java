package org.interledger.psk;

import org.interledger.psk.model.PskMessage;
import org.interledger.psk.model.PskMessageHeader;
import org.interledger.psk.model.PskMessageImpl;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

/**
 * A builder for constructing concrete Pre-Shared Key message instances.
 */
public class PskMessageBuilder {

  private final PskMessageImpl message;

  public PskMessageBuilder() {
    message = new PskMessageImpl();
  }

  /**
   * Adds a header to the <b>public</b> portion of the PSK message. Note that public headers are
   * visible to all parties transmitting the message.
   *
   * @param header The header to add to the public header portion of the message.
   */
  public PskMessageBuilder addPublicHeader(final PskMessageHeader header) {
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
   * @param name  The name of the header.
   * @param value The value associated with the header.
   */
  public PskMessageBuilder addPublicHeader(final String name, final String value) {
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
  public PskMessageBuilder addPrivateHeader(final PskMessageHeader header) {
    message.addPrivateHeader(header);

    return this;
  }

  /**
   * Adds a header to the <b>public</b> portion of the PSK message. Note that public headers are
   * visible to all parties transmitting the message. The method will replace
   *
   * @param name  The name of the header.
   * @param value The value associated with the header.
   */
  public PskMessageBuilder addPrivateHeader(final String name, final String value) {
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
  public PskMessageBuilder setApplicationData(final byte[] applicationData) {
    message.setApplicationData(applicationData);

    return this;
  }

  /**
   * Builds the PSK message with the data provided, including adding a Nonce header if none is 
   * present.
   */
  public PskMessage toMessage() {
    addNonce();

    return this.message;
  }
  
  /**
   * Generates a secure nonce and adds it to the set of <b>public</b> headers in the PSK message
   * <i>if a nonce header is not already present</i>.
   */
  private void addNonce() {
    
    if (!message.getPublicHeaders(PskMessageHeader.PublicHeaders.NONCE).isEmpty()) {
      return;
    }
    
    try {
      SecureRandom sr = SecureRandom.getInstanceStrong();
      byte[] nonce = new byte[16];
      sr.nextBytes(nonce);

      message.addPublicHeader(PskMessageHeader.PublicHeaders.NONCE,
          Base64.getUrlEncoder().encodeToString(nonce));

    } catch (NoSuchAlgorithmException nsa) {
      throw new RuntimeException("Could not generate secure nonce", nsa);
    }
  }
}


