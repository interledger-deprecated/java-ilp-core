package org.interledger.wire.codecs.oer;

import org.interledger.wire.codecs.Codec;
import org.interledger.wire.codecs.CodecContext;
import org.interledger.wire.codecs.oer.OerUint64Codec.OerUint64;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * <p>An extension of {@link Codec} for reading and writing an ASN.1 OER 64-Bit integer type as
 * defined by the Interledger ASN.1 definitions.</p>
 * <p>All Interledger ASN.1 integer types are encoded as fixed-size, non-extensible numbers.  Thus,
 * for a UInt64 type, the integer value is encoded as an unsigned binary integer in 8 octets.</p>
 */
public class OerUint64Codec implements Codec<OerUint64> {

  /**
   * ASN.1 64BitUInt: If the lower bound of the value range constraint is not less than 0 and the
   * upper bound is not greater than 18446744073709551615 and the constraint is not extensible,
   * the integer value is encoded as an unsigned binary integer in eight octets.
   *
   * @param context     An instance of {@link CodecContext}.
   * @param inputStream An instance of @link InputStream}.
   * @throws IOException              If there is a problem writing to the {@code stream}.
   * @throws IllegalArgumentException If the input has a value greater than 18446744073709551615.
   */
  @Override
  public OerUint64 read(final CodecContext context, final InputStream inputStream)
      throws IOException {
    Objects.requireNonNull(context);
    Objects.requireNonNull(inputStream);

    long value = 0;
    for (int i = 0; i < 8; i++) {
      value <<= Byte.SIZE;
      value |= (inputStream.read() & 0xFF);
    }
    return new OerUint64(value);
  }

  /**
   * ASN.1 64BitUInt: If the lower bound of the value range constraint is not less than 0 and the
   * upper bound is not greater than 18446744073709551615 and the constraint is not extensible,
   * the integer value is encoded as an unsigned binary integer in eight octets.
   *
   * @param context      An instance of {@link CodecContext}.
   * @param instance     An instance of {@link OerUint64}.
   * @param outputStream An instance of {@link OutputStream}.
   * @throws IOException              If there is a problem writing to the {@code stream}.
   * @throws IllegalArgumentException If the input has a value greater than 18446744073709551615.
   */
  @Override
  public void write(
      final CodecContext context, final OerUint64 instance, final OutputStream outputStream
  ) throws IOException, IllegalArgumentException {

    Objects.requireNonNull(context);
    Objects.requireNonNull(instance);
    Objects.requireNonNull(outputStream);

    long value = instance.getValue();
    for (int i = 7; i >= 0; i--) {
      byte octet = ((byte) ((value >> (Byte.SIZE * i)) & 255));
      outputStream.write(octet);
    }
  }

  /**
   * Merely a typing mechanism for registering multiple codecs that operate on the same type.
   */
  public static class OerUint64 {

    private final long value;

    public OerUint64(final long value) {
      this.value = value;
    }

    public long getValue() {
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

      OerUint64 oerUint64 = (OerUint64) obj;

      return value == oerUint64.value;
    }

    @Override
    public int hashCode() {
      return (int) (value ^ (value >>> 32));
    }

    @Override
    public String toString() {
      final StringBuilder sb = new StringBuilder("OerUint64{");
      sb.append("value=").append(value);
      sb.append('}');
      return sb.toString();
    }
  }
}
