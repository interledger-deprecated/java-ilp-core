package org.interledger.wire.codecs.oer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.common.primitives.Longs;

import org.interledger.wire.codecs.CodecContext;
import org.interledger.wire.codecs.oer.OerUint64Codec.OerUint64;
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
 * Parameterized unit tests for encoding an instance of {@link OerUint8Codec}.
 */
@RunWith(Parameterized.class)
public class OerUint64CodecTest {

  private CodecContext codecContext;
  private OerUint64Codec oerUint64Codec;

  /**
   * The data for this test...
   */
  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]
        {
            // Input Value as a long; Expected byte[] in ASN.1
            // 0
            {0L, Longs.toByteArray(0L)},
            // 1
            {1L, Longs.toByteArray(1L)},
            // 2
            {2L, Longs.toByteArray(2L)},
            // 3
            {254L, Longs.toByteArray(254L)},
            // 4
            {255L, Longs.toByteArray(255L)},

            // Two Bytes (16 bits)
            // 5
            {256L, Longs.toByteArray(256L)},
            // 6
            {257L, Longs.toByteArray(257L)},
            // 7
            {65534L, Longs.toByteArray(65534L)},
            // 8
            {65535L, Longs.toByteArray(65535L)},

            // Three Bytes (24 bits)
            // 9
            {65536L, Longs.toByteArray(65536L)},
            // 10
            {65537L, Longs.toByteArray(65537L)},
            // 11
            {16777214L, Longs.toByteArray(16777214L)},
            // 12
            {16777215L, Longs.toByteArray(16777215L)},

            // Four Bytes (32 bits)
            // 13
            {16777216L, Longs.toByteArray(16777216L)},
            // 14
            {16777217L, Longs.toByteArray(16777217L)},
            // 15
            {4294967294L, Longs.toByteArray(4294967294L)},
            // 16
            {4294967295L, Longs.toByteArray(4294967295L)},

            // Five Bytes (40 bits)
            // 17
            {4294967296L, Longs.toByteArray(4294967296L)},
            // 18
            {4294967297L, Longs.toByteArray(4294967297L)},
            // 19
            {1099511627774L, Longs.toByteArray(1099511627774L)},
            // 20
            {1099511627775L, Longs.toByteArray(1099511627775L)},

            // Size Bytes (48 bits)
            // 21
            {1099511627776L, Longs.toByteArray(1099511627776L)},
            // 22
            {1099511627777L, Longs.toByteArray(1099511627777L)},
            // 23
            {281474976710654L, Longs.toByteArray(281474976710654L)},
            // 24
            {281474976710655L, Longs.toByteArray(281474976710655L)},

            // Seven Bytes (56 bits)
            // 25
            {281474976710656L, Longs.toByteArray(281474976710656L)},
            // 26
            {281474976710657L, Longs.toByteArray(281474976710657L)},
            // 27
            {72057594037927934L, Longs.toByteArray(72057594037927934L)},
            // 28 (max 7-bit long value)
            {72057594037927935L, Longs.toByteArray(72057594037927935L)},

            {Long.MAX_VALUE - 2L, Longs.toByteArray(Long.MAX_VALUE - 2L)},
            {Long.MAX_VALUE - 1L, Longs.toByteArray(Long.MAX_VALUE - 1L)},
            {Long.MAX_VALUE, Longs.toByteArray(Long.MAX_VALUE)},
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
  public OerUint64CodecTest(final long inputValue, final byte[] asn1OerBytes) {
    this.inputValue = inputValue;
    this.asn1OerBytes = asn1OerBytes;
  }

  /**
   * Test setup.
   */
  @Before
  public void setUp() throws Exception {
    // Register the codec to be tested...
    oerUint64Codec = new OerUint64Codec();
    codecContext = new CodecContext().register(OerUint64.class, oerUint64Codec);
  }

  @Test
  public void read() throws Exception {
    final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(asn1OerBytes);
    final long actualValue = oerUint64Codec.read(codecContext, byteArrayInputStream).getValue();
    assertThat(actualValue, is(inputValue));
  }

  @Test
  public void write() throws Exception {
    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    oerUint64Codec.write(codecContext, new OerUint64(inputValue), byteArrayOutputStream);
    assertThat(byteArrayOutputStream.toByteArray(), is(this.asn1OerBytes));
  }

  @Test
  public void writeThenRead() throws Exception {
    // Write...
    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    oerUint64Codec.write(codecContext, new OerUint64(inputValue), byteArrayOutputStream);
    assertThat(byteArrayOutputStream.toByteArray(), is(asn1OerBytes));

    // Read...
    final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
        byteArrayOutputStream.toByteArray());
    final OerUint64 decodedValue = oerUint64Codec.read(codecContext, byteArrayInputStream);

    // Write...
    final ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
    oerUint64Codec.write(codecContext, decodedValue, byteArrayOutputStream2);
    assertThat(byteArrayOutputStream2.toByteArray(), is(asn1OerBytes));
  }
}