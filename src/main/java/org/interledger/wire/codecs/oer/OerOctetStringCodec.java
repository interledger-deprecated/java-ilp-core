package org.interledger.wire.codecs.oer;

import org.interledger.wire.codecs.Codec;
import org.interledger.wire.codecs.CodecContext;
import org.interledger.wire.codecs.oer.OerLengthPrefixCodec.OerLengthPrefix;
import org.interledger.wire.codecs.oer.OerOctetStringCodec.OerOctetString;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Objects;

/**
 * <p>An extension of {@link Codec} for reading and writing an ASN.1 OER OctetString.</p>
 * <p>The encoding of OctetString types depends on the size constraint present in the type, if any.
 * Interledger's usage of OctetString always uses a dynamic size constraint, so the encoding of the
 * string value consists of a length prefix followed by the encodings of each octet.</p>
 * <p>After encoding a length-prefix using an instance of {@link OerLengthPrefixCodec}, each byte
 * in the supplied byte array will be encoded in one octet with the highest-order bit set to
 * zero.</p>
 */
public class OerOctetStringCodec implements Codec<OerOctetString> {

  @Override
  public OerOctetString read(final CodecContext context, final InputStream inputStream)
      throws IOException {
    Objects.requireNonNull(context);
    Objects.requireNonNull(inputStream);

    // Detect the length of the encoded OctetString...
    final int lengthPrefix = context.read(OerLengthPrefix.class, inputStream).getLength();

    final byte[] returnable = new byte[lengthPrefix];
    inputStream.read(returnable);
    return new OerOctetString(returnable);
  }

  @Override
  public void write(
      final CodecContext context, final OerOctetString instance, final OutputStream outputStream
  ) throws IOException {
    Objects.requireNonNull(context);
    Objects.requireNonNull(instance);
    Objects.requireNonNull(outputStream);

    // Write the length-prefix, and move the buffer index to the correct spot.
    final int numOctets = instance.getValue().length;
    context.write(OerLengthPrefix.class, new OerLengthPrefix(numOctets), outputStream);

    // Write the OctetString bytes to the buffer.
    if (numOctets > 0) {
      outputStream.write(instance.getValue());
    }
  }

  /**
   * A typing mechanism for registering multiple codecs that operate on the same type (in this case,
   * byet[]).
   */
  public static class OerOctetString {

    private final byte[] value;

    public OerOctetString(final byte[] value) {
      this.value = Objects.requireNonNull(value);
    }

    public byte[] getValue() {
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

      OerOctetString that = (OerOctetString) obj;

      return Arrays.equals(value, that.value);
    }

    @Override
    public int hashCode() {
      return Arrays.hashCode(value);
    }

    @Override
    public String toString() {
      final StringBuilder sb = new StringBuilder("OctetString{");
      sb.append("value=").append(Arrays.toString(value));
      sb.append('}');
      return sb.toString();
    }
  }
}
