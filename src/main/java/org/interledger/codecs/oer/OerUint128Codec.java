package org.interledger.codecs.oer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Objects;

import org.interledger.codecs.Codec;
import org.interledger.codecs.CodecContext;
import org.interledger.codecs.oer.OerUint128Codec.OerUint128;
import org.interledger.codecs.oer.OerUint64Codec.OerUint64;

/**
 * <p>An extension of {@link Codec} for reading and writing an ASN.1 OER 128-Bit unsigned integer 
 * type as defined by the Interledger ASN.1 definitions.</p>
 * <p>All Interledger ASN.1 integer types are encoded as fixed-size, non-extensible numbers.  Thus,
 * for a UInt128 type, the integer value is encoded as an unsigned binary integer in sixteen octets,
 * supporting values in the range (0..18446744073709551615). </p>
 */
public class OerUint128Codec implements Codec<OerUint128> {

  public static final BigInteger MAX_VALUE = new BigInteger("18446744073709551615");
  
  /**
   * ASN.1 128BitUInt: If the lower bound of the value range constraint is not less than 0 and the
   * upper bound is not greater than 18446744073709551615 and the constraint is not extensible,
   * the integer value is encoded as an unsigned binary integer in sixteen octets.
   *
   * @param context     An instance of {@link CodecContext}.
   * @param inputStream An instance of @link InputStream}.
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
    
    return new OerUint128(new BigInteger(1, value));
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
      final CodecContext context, final OerUint128 instance, final OutputStream outputStream
  ) throws IOException, IllegalArgumentException {

    Objects.requireNonNull(context);
    Objects.requireNonNull(instance);
    Objects.requireNonNull(outputStream);

    if (instance.getValue().compareTo(BigInteger.ZERO) < 0
        || instance.getValue().compareTo(MAX_VALUE) > 1) {
      throw new IllegalArgumentException(
          "Interledger Uint128 only supports values from 0 to 18446744073709551615, value "
              + instance.getValue() + " is out of range.");
    }
   
    byte[] value = instance.getValue().toByteArray();
    
    /* BigInteger's toByteArray writes data in two's complement, so positive values requiring 128
     * bits will include a leading byte set to 0 which we don't want. */
    if (value.length > 16) {
      outputStream.write(value, value.length - 16, 16);
      return;
    }
    
    /* BigInteger.toByteArray will return the smallest byte array possible. We are committed
     * to a fixed number of bytes, so we might need to pad the value out. */
    for (int i = 0; i < 16 - value.length; i++) {
      outputStream.write(0);
    }
    outputStream.write(value);
  }

  /**
   * Merely a typing mechanism for registering multiple codecs that operate on the same type.
   */
  public static class OerUint128 {

    private final BigInteger value;

    public OerUint128(final BigInteger value) {
      this.value = value;
    }

    public BigInteger getValue() {
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

      OerUint128 oerUint128 = (OerUint128) obj;

      return value.equals(oerUint128.value);
    }

    @Override
    public int hashCode() {
      return value.hashCode();
    }

    @Override
    public String toString() {
      final StringBuilder sb = new StringBuilder("OerUint128{");
      sb.append("value=").append(value);
      sb.append('}');
      return sb.toString();
    }
  }
}
