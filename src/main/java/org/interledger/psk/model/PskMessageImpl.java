package org.interledger.psk.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A concrete implementation of a PSK Message as defined in RFC 16 - Pre-Shared Key Transport
 * Protocol.
 */
public class PskMessageImpl implements PskMessage {

  private final List<PskMessageHeader> publicHeaders;
  private final List<PskMessageHeader> privateHeaders;

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
  public void addPublicHeader(final PskMessageHeader header) {
    Objects.requireNonNull(header, "Invalid public header - must not be null");
    publicHeaders.add(header);
  }

  /**
   * Adds a header to the <b>public</b> portion of the PSK message. Note: the content of the public
   * headers can be seen by all parties.
   *
   * @param name The name of the header.
   * @param value The value associated with the header.
   */
  public void addPublicHeader(final String name, final String value) {
    addPublicHeader(new BasicPskHeader(name, value));
  }

  /**
   * Adds a header to the <b>private</b> portion of the PSK message. Note: the content of the
   * private headers may be seen by all parties unless the public encryption header is set
   * accordingly.
   *
   * @param header The header to add to the private headers in the message.
   */
  public void addPrivateHeader(final PskMessageHeader header) {
    Objects.requireNonNull("Invalid private header - must not be null");
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
  public void addPrivateHeader(final String name, final String value) {
    addPrivateHeader(new BasicPskHeader(name, value));
  }

  /**
   * Sets the application data contained in the <b>private</b> portion of the PSK message. Note that
   * the content of the private data may be visible to all parties unless the message uses
   * encryption.
   *
   * @param applicationData The application data to store in the message.
   */
  public void setApplicationData(final byte[] applicationData) {
    Objects.requireNonNull(applicationData, "Invalid application data - must not be null");
    this.applicationData = copyBytes(applicationData);
  }

  @Override
  public List<PskMessageHeader> getPublicHeaders() {
    return new ArrayList<>(publicHeaders);
  }

  @Override
  public List<PskMessageHeader> getPublicHeaders(final String headerName) {
    return publicHeaders.stream()
      .filter(h -> h.getName().equalsIgnoreCase(headerName))
      .collect(Collectors.toList());
  }

  @Override
  public List<PskMessageHeader> getPrivateHeaders() {
    return new ArrayList<>(privateHeaders);
  }

  @Override
  public List<PskMessageHeader> getPrivateHeader(String headerName) {
    return privateHeaders.stream()
      .filter(h -> h.getName().equalsIgnoreCase(headerName))
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
   * Removes all public headers with the given header name.
   * 
   * @param headerName The name of the header to remove.
   */
  public void removePublicHeaders(String headerName) {
    Iterator<PskMessageHeader> itr = publicHeaders.iterator();
    while (itr.hasNext()) {
      PskMessageHeader header = (PskMessageHeader) itr.next();

      if (header.getName().equals(headerName)) {
        itr.remove();
      }
    }
  }

  /**
   * Removes all private headers with the given header name.
   * 
   * @param headerName The name of the header to remove.
   */
  public void removePrivateHeaders(String headerName) {
    Iterator<PskMessageHeader> itr = privateHeaders.iterator();
    while (itr.hasNext()) {
      PskMessageHeader header = (PskMessageHeader) itr.next();

      if (header.getName().equals(headerName)) {
        itr.remove();
      }
    }
  }
  
  /**
   * Convenience method to produce a copy of the given byte array.
   *
   * @param original A byte array to copy. must not be null.
   * @return A copy of the byte array parameter.
   */
  protected byte[] copyBytes(final byte[] original) {
    byte[] copy = new byte[original.length];
    System.arraycopy(original, 0, copy, 0, original.length);
    return copy;
  }
}
