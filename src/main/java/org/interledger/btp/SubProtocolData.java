package org.interledger.btp;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

public interface SubProtocolData {
  /**
   * Get the default builder for this sub protocol.
   *
   * @return a {@link Builder} instance.
   */
  static Builder builder(String protocolIdentifier) {
    return new Builder(protocolIdentifier);
  }

  /**
   * The protocol identifier of this sub protocol.
   *
   * @return A String representing the protocol identifier.
   */
  String getProtocol();

  /**
   * The content type of the sub protocol data
   *
   * @return An instance of {@link BtpSubProtocolContentType}.
   */
  BtpSubProtocolContentType getContentType();

  /**
   * Raw sub-protocol data.
   *
   * @return A byte array.
   */
  byte[] getData();

  /**
   * A builder for instances of {@link SubProtocolData}.
   */
  class Builder {

    private final String protocolIdentifier;
    private BtpSubProtocolContentType contentType;
    private byte[] data;

    /**
     * Initialize builder.
     *
     * @param protocolIdentifier The protocol identifier.
     */
    public Builder(final String protocolIdentifier) {
      this.protocolIdentifier = Objects.requireNonNull(protocolIdentifier);
    }

    /**
     * Set the data payload for this sub protocol (decodes String as UTF8).
     *
     * @param data A UTF8 encoded String.
     */
    public Builder textData(final String data) {
      Objects.requireNonNull(data);
      this.contentType = BtpSubProtocolContentType.TEXT_PLAIN_UTF8;
      this.data = data.getBytes(StandardCharsets.UTF_8);
      return this;
    }

    /**
     * Set the data payload for this sub protocol (decodes String as UTF8).
     *
     * <p>TODO: Validate the JSON data
     *
     * @param data A UTF8 encoded String.
     */
    public Builder jsonData(final String data) {
      Objects.requireNonNull(data);
      this.contentType = BtpSubProtocolContentType.APPLICATION_JSON;
      this.data = data.getBytes(StandardCharsets.UTF_8);
      return this;
    }

    /**
     * Set the data payload for this sub protocol.
     *
     * @param data An instance of {@link byte[]}. May be empty but may not be null.
     */
    public Builder binaryData(final byte[] data) {
      this.data = Objects.requireNonNull(data);
      this.contentType = BtpSubProtocolContentType.APPLICATION_OCTET_STREAM;
      return this;
    }

    /**
     * The method that actually constructs a payment.
     *
     * @return An instance of {@link SubProtocolData}.
     */
    public SubProtocolData build() {
      return new Impl(this);
    }

    /**
     * A private, immutable implementation of {@link SubProtocolData}.
     */
    private static final class Impl implements SubProtocolData {

      private final String protocolIdentifier;
      private final BtpSubProtocolContentType contentType;
      private final byte[] data;

      /**
       * No-args Constructor.
       */
      private Impl(final Builder builder) {
        Objects.requireNonNull(builder);
        this.protocolIdentifier = Objects.requireNonNull(builder.protocolIdentifier,
            "protocolIdentifier must not be null!");
        this.contentType = Objects.requireNonNull(builder.contentType,
            "contentType must not be null!");
        this.data = Objects.requireNonNull(builder.data, "data must not be null!");
      }

      @Override
      public String getProtocol() {
        return this.protocolIdentifier;
      }

      @Override
      public BtpSubProtocolContentType getContentType() {
        return this.contentType;
      }

      @Override
      public byte[] getData() {
        return Arrays.copyOf(this.data, this.data.length);
      }

      @Override
      public boolean equals(Object obj) {
        if (this == obj) {
          return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
          return false;
        }

        Impl impl = (Impl) obj;

        return protocolIdentifier.equals(impl.protocolIdentifier)
            && contentType.equals(impl.contentType)
            && Arrays.equals(data, impl.data);
      }

      @Override
      public int hashCode() {
        int result = protocolIdentifier.hashCode();
        result = 31 * result + contentType.hashCode();
        result = 31 * result + Arrays.hashCode(data);
        return result;
      }

      @Override
      public String toString() {
        return "SubProtocolData.Impl{"
            + "protocol=" + protocolIdentifier
            + ", contentType=" + contentType
            + ", data=" + Arrays.toString(data)
            + '}';
      }
    }
  }
}
