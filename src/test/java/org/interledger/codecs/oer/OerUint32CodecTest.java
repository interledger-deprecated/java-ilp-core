package org.interledger.codecs.oer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import org.interledger.codecs.CodecContext;
import org.interledger.codecs.oer.OerUint32Codec.OerUint32;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Collection;

/**
 * Parameterized unit tests for encoding an instance of {@link OerUint32Codec}.
 */
@RunWith(Parameterized.class)
public class OerUint32CodecTest {

  private CodecContext codecContext;
  private OerUint32Codec oerUint32Codec;

  /**
   * The data for this test...
   */
  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]
        {
            // Input Value as a long; Expected byte[] in ASN.1
            // 0
            {0L, Ints.toByteArray(0)},
            // 1
            {1L, Ints.toByteArray(1)},
            // 2
            {2L, Ints.toByteArray(2)},
            // 3
            {254L, Ints.toByteArray(254)},
            // 4
            {255L, Ints.toByteArray(255)},

            // Two Bytes (16 bits)
            // 5
            {256L, Ints.toByteArray(256)},
            // 6
            {257L, Ints.toByteArray(257)},
            // 7
            {65534L, Ints.toByteArray(65534)},
            // 8
            {65535L, Ints.toByteArray(65535)},

            // Three Bytes (24 bits)
            // 9
            {65536L, Ints.toByteArray(65536)},
            // 10
            {65537L, Ints.toByteArray(65537)},
            // 11
            {16777214L, Ints.toByteArray(16777214)},
            // 12
            {16777215L, Ints.toByteArray(16777215)},

            // Four Bytes (32 bits)
            // 13
            {16777216L, Ints.toByteArray(16777216)},
            // 14
            {16777217L, Ints.toByteArray(16777217)},
            // 15 bits set. we use Longs to create a byte array, but resize from 8 to 4 bytes
            {4294967294L, Arrays.copyOfRange(Longs.toByteArray(4294967294L), 4, 8)},
            // 16 bits set. we use Longs to create a byte array, but resize from 8 to 4 bytes
            {4294967295L, Arrays.copyOfRange(Longs.toByteArray(4294967295L), 4, 8)}
        }
    );
  }

  private long inputValue;

  private byte[] asn1OerBytes;

  /**
   * Construct an instance of this parameterized test with the supplied inputs.
   *
   * @param inputValue   A {@code int} representing the unsigned 8bit integer to write in OER
   *                     encoding.
   * @param asn1OerBytes The expected value, in binary, of the supplied {@code intValue}.
   */
  public OerUint32CodecTest(final long inputValue, final byte[] asn1OerBytes) {
    this.inputValue = inputValue;
    this.asn1OerBytes = asn1OerBytes;
  }

  /**
   * Test setup.
   */
  @Before
  public void setUp() throws Exception {
    // Register the codec to be tested...
    oerUint32Codec = new OerUint32Codec();
    codecContext = new CodecContext().register(OerUint32.class, oerUint32Codec);
  }

  @Test
  public void read() throws Exception {
    final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(asn1OerBytes);
    final long actualValue = oerUint32Codec.read(codecContext, byteArrayInputStream).getValue();
    assertThat(actualValue, is(inputValue));
  }

  @Test
  public void write() throws Exception {
    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    oerUint32Codec.write(codecContext, new OerUint32(inputValue), byteArrayOutputStream);
    assertThat(byteArrayOutputStream.toByteArray(), is(this.asn1OerBytes));
  }

  @Test
  public void writeThenRead() throws Exception {
    // Write...
    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    oerUint32Codec.write(codecContext, new OerUint32(inputValue), byteArrayOutputStream);
    assertThat(byteArrayOutputStream.toByteArray(), is(asn1OerBytes));

    // Read...
    final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
        byteArrayOutputStream.toByteArray());
    final OerUint32 decodedValue = oerUint32Codec.read(codecContext, byteArrayInputStream);

    // Write...
    final ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
    oerUint32Codec.write(codecContext, decodedValue, byteArrayOutputStream2);
    assertThat(byteArrayOutputStream2.toByteArray(), is(asn1OerBytes));
  }
}