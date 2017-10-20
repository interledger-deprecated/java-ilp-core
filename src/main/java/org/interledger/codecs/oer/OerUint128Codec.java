package org.interledger.codecs.oer;

import org.interledger.codecs.Codec;
import org.interledger.codecs.CodecContext;
import org.interledger.codecs.oer.OerUint128Codec.OerUint128;
import org.interledger.codecs.oer.OerUint64Codec.OerUint64;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>An extension of {@link Codec} for reading and writing an ASN.1 OER 128-Bit unsigned integer
 * type as defined by the Interledger ASN.1 definitions.</p> <p>All Interledger ASN.1 integer types
 * are encoded as fixed-size, non-extensible numbers. Thus, for a UInt128 type, the integer value is
 * encoded as an unsigned binary integer in 16 octets, and supports values in the range
 * (0..2^16). </p>
 */
public class OerUint128Codec implements Codec<OerUint128> {

  /**
   * ASN.1 64BitUInt: If the lower bound of the value range constraint is not less than 0 and the
   * upper bound is not greater than 18446744073709551615 and the constraint is not extensible, the
   * integer value is encoded as an unsigned binary integer in eight octets.
   *
   * @param context     An instance of {@link CodecContext}.
   * @param inputStream An instance of @link InputStream}.
   *
   * @throws IOException              If there is a problem writing to the {@code stream}.
   * @throws IllegalArgumentException If the input has a value greater than 18446744073709551615.
   */
  @Override
  public OerUint128 read(final CodecContext context, final InputStream inputStream)
      throws IOException {
    Objects.requireNonNull(context);
    Objects.requireNonNull(inputStream);

    byte[] value = new byte[16];
    int read = inputStream.read(value);

    if (read != 16) {
      throw new IOException("unexpected end of stream. expected 16 bytes, read " + read);
    }

    return new OerUint128(value);
  }

  /**
   * ASN.1 64BitUInt: If the lower bound of the value range constraint is not less than 0 and the
   * upper bound is not greater than 18446744073709551615 and the constraint is not extensible, the
   * integer value is encoded as an unsigned binary integer in eight octets.
   *
   * @param context      An instance of {@link CodecContext}.
   * @param instance     An instance of {@link OerUint64}.
   * @param outputStream An instance of {@link OutputStream}.
   *
   * @throws IOException              If there is a problem writing to the {@code stream}.
   * @throws IllegalArgumentException If the input has a value greater than 18446744073709551615.
   */
  @Override
  public void write(final CodecContext context, final OerUint128 instance,
      final OutputStream outputStream) throws IOException, IllegalArgumentException {

    Objects.requireNonNull(context);
    Objects.requireNonNull(instance);
    Objects.requireNonNull(outputStream);

    outputStream.write(instance.getValue());
  }

  /**
   * Merely a typing mechanism for registering multiple codecs that operate on the same type.
   */
  public static class OerUint128 {

    private final byte[] value;

    /**
     * Constructs an OerUint128 instance.
     *
     * @param value The value to read or write as an OER 128-bit value.
     **/
    public OerUint128(final byte[] value) {
      if (value.length != 16) {
        throw new IllegalArgumentException("Expected 128 bits, got " + (value.length * 8));
      }
      this.value = value;
    }

    /**
     * Constructs an OerUint128 instance.
     *
     * @param value The value to read or write as a UUID.
     **/
    public OerUint128(final UUID value) {
      this.value = uuidAsBytes(value);
    }

    public byte[] getValue() {
      return value;
    }

    public UUID getUuid() {
      return bytesToUuid(value);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null || getClass() != obj.getClass()) {
        return false;
      }

      OerUint128 that = (OerUint128) obj;

      return Arrays.equals(value, that.value);
    }

    @Override
    public int hashCode() {
      return value.hashCode();
    }

    @Override
    public String toString() {
      return "OerUint64{"
          + "value=" + value
          + '}';
    }

    /**
     * Convert an array of bytes to a UUID.
     *
     * @param bytes an array of bytes representing 128-bits of data
     * @return the data converted to a UUID
     */
    public static UUID bytesToUuid(byte[] bytes) {
      if (bytes.length != 16) {
        throw new IllegalArgumentException("Expected 128 bits, got " + (bytes.length * 8));
      }

      ByteBuffer bb = ByteBuffer.wrap(bytes);
      long firstLong = bb.getLong();
      long secondLong = bb.getLong();
      return new UUID(firstLong, secondLong);
    }

    /**
     * Convert a UUID to an array of bytes.
     *
     * @param uuid A UUID
     * @return the UUID converted into raw bytes
     */
    public static byte[] uuidAsBytes(UUID uuid) {
      ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
      bb.putLong(uuid.getMostSignificantBits());
      bb.putLong(uuid.getLeastSignificantBits());
      return bb.array();
    }
  }
}
