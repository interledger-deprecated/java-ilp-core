package org.interledger.codecs.oer.btp;

import org.interledger.btp.BtpErrorType;
import org.interledger.codecs.Codec;
import org.interledger.codecs.CodecContext;
import org.interledger.codecs.CodecException;
import org.interledger.codecs.oer.OerLengthPrefixCodec.OerLengthPrefix;
import org.interledger.codecs.oer.btp.BtpErrorTypeOerCodec.OerBilateralTransferProtocolErrorTypes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class BtpErrorTypeOerCodec implements
    Codec<OerBilateralTransferProtocolErrorTypes> {

  @Override
  public OerBilateralTransferProtocolErrorTypes read(final CodecContext context,
      final InputStream inputStream)
      throws IOException {
    Objects.requireNonNull(context);
    Objects.requireNonNull(inputStream);

    /* beware the 0-length string */
    final String code = (this.toString(inputStream, 3));

    // Detect the length of the encoded IA5String, and move the buffer index to the correct spot.
    final int length = context.read(OerLengthPrefix.class, inputStream)
        .getLength();
    
    /* beware the 0-length string */
    final String name = (length == 0 ? "" : this.toString(inputStream, length));

    return new OerBilateralTransferProtocolErrorTypes(code, name);
  }

  @Override
  public void write(final CodecContext context,
      final OerBilateralTransferProtocolErrorTypes instance,
      final OutputStream outputStream) throws IOException {

    Objects.requireNonNull(context);
    Objects.requireNonNull(instance);
    Objects.requireNonNull(outputStream);

    final BtpErrorType errorType = instance.getValue();

    // Write the String bytes to the buffer.
    outputStream.write(errorType.getCode()
        .getBytes(StandardCharsets.US_ASCII));

    byte[] name = errorType.getName()
        .getBytes(StandardCharsets.US_ASCII);

    // Write the length-prefix, and move the buffer index to the correct spot.
    context.write(OerLengthPrefix.class, new OerLengthPrefix(name.length), outputStream);

    // Write the String bytes to the buffer.
    outputStream.write(name);
  }

  /**
   * Convert an {@link InputStream} into a {@link String}. Reference the SO below for an interesting
   * performance comparison of various InputStream to String methodologies.
   *
   * @param inputStream An instance of {@link InputStream}.
   *
   * @return A {@link String}
   *
   * @throws IOException If the {@code inputStream} is unable to be read properly.
   * @see "http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string"
   */
  private String toString(final InputStream inputStream, final int lengthToRead)
      throws IOException {
    Objects.requireNonNull(inputStream);
    ByteArrayOutputStream result = new ByteArrayOutputStream();

    // Read lengthToRead bytes from the inputStream into the buffer...
    byte[] buffer = new byte[lengthToRead];
    int read = inputStream.read(buffer);

    if (read != lengthToRead) {
      throw new IOException(
          "error reading " + lengthToRead + " bytes from stream, only read " + read);
    }

    result.write(buffer, 0, lengthToRead);
    return result.toString(StandardCharsets.US_ASCII.name());
  }


  /**
   * A typing mechanism for registering multiple codecs that operate on the same type (in this case,
   * {@link String}).
   */
  public static class OerBilateralTransferProtocolErrorTypes {

    private final BtpErrorType value;

    /**
     * Construct from {@link BtpErrorType}.
     *
     * @param value An instance of {@link BtpErrorType}
     */
    public OerBilateralTransferProtocolErrorTypes(final BtpErrorType value) {
      this.value = Objects.requireNonNull(value);
    }

    /**
     * Construct from a code and name.
     *
     * @param code the BTP Error code
     * @param name the BTP Error name
     */
    public OerBilateralTransferProtocolErrorTypes(final String code, final String name) {
      Objects.requireNonNull(code);
      Objects.requireNonNull(name);
      value = BtpErrorType.fromCode(code);

      if (!name.equals(value.getName())) {
        throw new CodecException("Invalid name provided for the error code: " + code);
      }

    }

    /**
     * Accessor for the value of this error type, as a {@link BtpErrorType}.
     *
     * @return An instance of {@link BtpErrorType}.
     */
    public BtpErrorType getValue() {
      return value;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null || getClass() != obj.getClass()) {
        return false;
      }

      OerBilateralTransferProtocolErrorTypes impl = (OerBilateralTransferProtocolErrorTypes) obj;

      return value.equals(impl.value);
    }

    @Override
    public int hashCode() {
      return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
      return "OerBilateralTransferProtocolErrorTypes{"
          + "value='" + value + '\''
          + '}';
    }
  }
}
