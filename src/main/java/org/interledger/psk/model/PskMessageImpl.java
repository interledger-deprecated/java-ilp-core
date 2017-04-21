package org.interledger.psk.model;

import org.interledger.psk.model.PskMessage;
import org.interledger.psk.model.PskMessageHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A concrete implementation of a PSK Message as defined in RFC 16 - Pre-Shared Key Transport
 * Protocol.
 */
public class PskMessageImpl implements PskMessage {

  private List<PskMessageHeader> publicHeaders;
  private List<PskMessageHeader> privateHeaders;

  private byte[] applicationData;

  /**
   * Default constructor for instances of {@link PskMessageImpl}. 
   */
  public PskMessageImpl() {
    /*
     * we choose array lists to maintain insertion order, and to allow duplicates. the PSK RFC is
     * not explicit, but since its inspired by HTTP which allows duplicates, we presume the same.
     */
    publicHeaders = new ArrayList<>();
    privateHeaders = new ArrayList<>();
  }

  /**
   * Adds a header to the <b>public</b> portion of the PSK message. Note: the content of the public
   * headers can be seen by all parties.
   * 
   * @param header The header to add to the public header section of the message.
   */
  public void addPublicHeader(PskMessageHeader header) {
    if (header == null) {
      throw new IllegalArgumentException("Invalid public header - must not be null");
    }

    publicHeaders.add(header);
  }

  /**
   * Adds a header to the <b>public</b> portion of the PSK message. Note: the content of the public
   * headers can be seen by all parties.
   * 
   * @param name The name of the header.
   * @param value The value associated with the header.
   */
  public void addPublicHeader(String name, String value) {
    addPublicHeader(new BasicPskHeader(name, value));
  }

  /**
   * Adds a header to the <b>private</b> portion of the PSK message. Note: the content of the
   * private headers may be seen by all parties unless the public encryption header is set
   * accordingly.
   * 
   * @param header The header to add to the private headers in the message.
   */
  public void addPrivateHeader(PskMessageHeader header) {
    if (header == null) {
      throw new IllegalArgumentException("Invalid private header - must not be null");
    }

    privateHeaders.add(header);
  }

  /**
   * Adds a header to the <b>private</b> portion of the PSK message. Note: the content of the
   * private headers may be seen by all parties unless the public encryption header is set
   * accordingly.
   * 
   * @param name The name of the header.
   * @param value The value associated with the header.
   */
  public void addPrivateHeader(String name, String value) {
    addPrivateHeader(new BasicPskHeader(name, value));
  }

  /**
   * Sets the application data contained in the <b>private</b> portion of the PSK message. Note that
   * the content of the private data may be visible to all parties unless the message uses
   * encryption.
   * 
   * @param applicationData The application data to store in the message.
   */
  public void setApplicationData(byte[] applicationData) {
    if (applicationData == null) {
      throw new IllegalArgumentException("Invalid application data - must not be null");
    }

    this.applicationData = copyBytes(applicationData);
  }

  @Override
  public List<PskMessageHeader> getPublicHeaders() {
    return new ArrayList<>(publicHeaders);
  }

  @Override
  public List<PskMessageHeader> getPublicHeaders(String headerName) {
    return publicHeaders.parallelStream().filter(h -> h.getName().equalsIgnoreCase(headerName))
        .collect(Collectors.toList());
  }

  @Override
  public List<PskMessageHeader> getPrivateHeaders() {
    return new ArrayList<>(privateHeaders);
  }

  @Override
  public List<PskMessageHeader> getPrivateHeader(String headerName) {
    return privateHeaders.parallelStream().filter(h -> h.getName().equalsIgnoreCase(headerName))
        .collect(Collectors.toList());
  }

  @Override
  public byte[] getApplicationData() {
    if (applicationData == null) {
      return null;
    }

    // TODO: yuck, super expensive operation just to be immutable. maybe we should use a
    // collection of some kind instead?
    return copyBytes(applicationData);
  }

  /**
   * Convenience method to produce a copy of the given byte array.
   *  
   * @param original A byte array to copy. must not be null.
   * @return    A copy of the byte array parameter.
   */
  protected byte[] copyBytes(byte[] original) {
    byte[] copy = new byte[original.length];
    System.arraycopy(original, 0, copy, 0, original.length);
    return copy;
  }
}
