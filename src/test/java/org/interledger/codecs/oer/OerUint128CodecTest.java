package org.interledger.codecs.oer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.common.primitives.Longs;

import org.interledger.codecs.CodecContext;
import org.interledger.codecs.oer.OerUint128Codec.OerUint128;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;

/**
 * Parameterized unit tests for encoding an instance of {@link OerUint8Codec}.
 */
@RunWith(Parameterized.class)
public class OerUint128CodecTest {

  private CodecContext codecContext;
  private OerUint128Codec oerUint128Codec;

  /**
   * The data for this test...
   */
  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]
        {
            // Input Value as a BigInteger; Expected byte[] in ASN.1
            // 0
            {BigInteger.ZERO, resizeArray(Longs.toByteArray(0L))},
            // 1
            {BigInteger.ONE, resizeArray(Longs.toByteArray(1L))},
            // 2
            {new BigInteger("2"), resizeArray(Longs.toByteArray(2L))},
            // 3
            {new BigInteger("254"), resizeArray(Longs.toByteArray(254L))},
            // 4
            {new BigInteger("255"), resizeArray(Longs.toByteArray(255L))},

            // Two Bytes (16 bits)
            // 5
            {new BigInteger("256"), resizeArray(Longs.toByteArray(256L))},
            // 6
            {new BigInteger("257"), resizeArray(Longs.toByteArray(257L))},
            // 7
            {new BigInteger("65534"), resizeArray(Longs.toByteArray(65534L))},
            // 8
            {new BigInteger("65535"), resizeArray(Longs.toByteArray(65535L))},

            // Three Bytes (24 bits)
            // 9
            {new BigInteger("65536"), resizeArray(Longs.toByteArray(65536L))},
            // 10
            {new BigInteger("65537"), resizeArray(Longs.toByteArray(65537L))},
            // 11
            {new BigInteger("16777214"), resizeArray(Longs.toByteArray(16777214L))},
            // 12
            {new BigInteger("16777215"), resizeArray(Longs.toByteArray(16777215L))},

            // Four Bytes (32 bits)
            // 13
            {new BigInteger("16777216"), resizeArray(Longs.toByteArray(16777216L))},
            // 14
            {new BigInteger("16777217"), resizeArray(Longs.toByteArray(16777217L))},
            // 15
            {new BigInteger("4294967294"), resizeArray(Longs.toByteArray(4294967294L))},
            // 16
            {new BigInteger("4294967295"), resizeArray(Longs.toByteArray(4294967295L))},

            // Five Bytes (40 bits)
            // 17
            {new BigInteger("4294967296"), resizeArray(Longs.toByteArray(4294967296L))},
            // 18
            {new BigInteger("4294967297"), resizeArray(Longs.toByteArray(4294967297L))},
            // 19
            {new BigInteger("1099511627774"), resizeArray(Longs.toByteArray(1099511627774L))},
            // 20
            {new BigInteger("1099511627775"), resizeArray(Longs.toByteArray(1099511627775L))},

            // Six Bytes (48 bits)
            // 21
            {new BigInteger("1099511627776"), resizeArray(Longs.toByteArray(1099511627776L))},
            // 22
            {new BigInteger("1099511627777"), resizeArray(Longs.toByteArray(1099511627777L))},
            // 23
            {new BigInteger("281474976710654"), resizeArray(Longs.toByteArray(281474976710654L))},
            // 24
            {new BigInteger("281474976710655"), resizeArray(Longs.toByteArray(281474976710655L))},

            // Seven Bytes (56 bits)
            // 25
            {new BigInteger("281474976710656"), resizeArray(Longs.toByteArray(281474976710656L))},
            // 26
            {new BigInteger("281474976710657"), resizeArray(Longs.toByteArray(281474976710657L))},
            // 27
            {new BigInteger("72057594037927934"), 
                resizeArray(Longs.toByteArray(72057594037927934L))},            
            // 28 (max 7-bit long value)
            {new BigInteger("72057594037927935"), 
                resizeArray(Longs.toByteArray(72057594037927935L))},

            // Eight Bytes (64 bits)
            // 29
            {new BigInteger("72057594037927936"), 
                resizeArray(Longs.toByteArray(72057594037927936L))},
            // 30
            {new BigInteger("72057594037927937"), 
                resizeArray(Longs.toByteArray(72057594037927937L))},
            
            // 31
            {new BigInteger("9223372036854775807"), resizeArray(Longs.toByteArray(Long.MAX_VALUE))},
            // 32
            {new BigInteger("18446744073709551614"), resizeArray(createArray(8, 0xFF, 0xFF, 0xFE))},
            // 33
            {new BigInteger("18446744073709551615"), resizeArray(createArray(8, 0xFF, 0xFF, 0xFF))},
            
            // Nine Bytes (72 bits)
            // 34
            {new BigInteger("18446744073709551616"), resizeArray(createArray(9, 0x01, 0x00, 0x00))},
            // 35
            {new BigInteger("18446744073709551617"), resizeArray(createArray(9, 0x01, 0x00, 0x01))},
            // 36
            {new BigInteger("4722366482869645213694"), 
                  resizeArray(createArray(9, 0xFF, 0xFF, 0xFE))},
            // 37
            {new BigInteger("4722366482869645213695"), 
                  resizeArray(createArray(9, 0xFF, 0xFF, 0xFF))},
            
            // Ten Bytes (80 bits)
            // 38
            {new BigInteger("4722366482869645213696"), 
                  resizeArray(createArray(10, 0x01, 0x00, 0x00))},
            // 39
            {new BigInteger("4722366482869645213697"), 
                  resizeArray(createArray(10, 0x01, 0x00, 0x01))},            
            // 40
            {new BigInteger("1208925819614629174706174"), 
                  resizeArray(createArray(10, 0xFF, 0xFF, 0xFE))},
            // 41
            {new BigInteger("1208925819614629174706175"), 
                  resizeArray(createArray(10, 0xFF, 0xFF, 0xFF))},

            // Eleven Bytes (88 bits)
            // 42
            {new BigInteger("1208925819614629174706176"), 
                  resizeArray(createArray(11, 0x01, 0x00, 0x00))},
            // 43
            {new BigInteger("1208925819614629174706177"), 
                  resizeArray(createArray(11, 0x01, 0x00, 0x01))},
            // 44
            {new BigInteger("309485009821345068724781054"), 
                  resizeArray(createArray(11, 0xFF, 0xFF, 0xFE))},
            // 45
            {new BigInteger("309485009821345068724781055"), 
                  resizeArray(createArray(11, 0xFF, 0xFF, 0xFF))},

            // Twelve Bytes (96 bits)
            // 46
            {new BigInteger("309485009821345068724781056"), 
                  resizeArray(createArray(12, 0x01, 0x00, 0x00))},
            // 47
            {new BigInteger("309485009821345068724781057"), 
                  resizeArray(createArray(12, 0x01, 0x00, 0x01))},
            // 48
            {new BigInteger("79228162514264337593543950334"), 
                  resizeArray(createArray(12, 0xFF, 0xFF, 0xFE))},
            // 49
            {new BigInteger("79228162514264337593543950335"), 
                  resizeArray(createArray(12, 0xFF, 0xFF, 0xFF))},
            
            // Thirteen Bytes (104 bits)
            // 50
            {new BigInteger("79228162514264337593543950336"), 
                  resizeArray(createArray(13, 0x01, 0x00, 0x00))},
            // 51
            {new BigInteger("79228162514264337593543950337"), 
                  resizeArray(createArray(13, 0x01, 0x00, 0x01))},
            // 52
            {new BigInteger("20282409603651670423947251286014"), 
                  resizeArray(createArray(13, 0xFF, 0xFF, 0xFE))},
            // 53
            {new BigInteger("20282409603651670423947251286015"), 
                  resizeArray(createArray(13, 0xFF, 0xFF, 0xFF))},
            
            // Fourteen Bytes (112 bits)
            // 54
            {new BigInteger("20282409603651670423947251286016"), 
                  resizeArray(createArray(14, 0x01, 0x00, 0x00))},
            // 55
            {new BigInteger("20282409603651670423947251286017"), 
                  resizeArray(createArray(14, 0x01, 0x00, 0x01))},
            // 56
            {new BigInteger("5192296858534827628530496329220094"), 
                  resizeArray(createArray(14, 0xFF, 0xFF, 0xFE))},
            // 57
            {new BigInteger("5192296858534827628530496329220095"), 
                  resizeArray(createArray(14, 0xFF, 0xFF, 0xFF))},

            // Fifteen Bytes (120 bits)
            // 54
            {new BigInteger("5192296858534827628530496329220096"), 
                  resizeArray(createArray(15, 0x01, 0x00, 0x00))},
            // 55
            {new BigInteger("5192296858534827628530496329220097"), 
                  resizeArray(createArray(15, 0x01, 0x00, 0x01))},
            // 56
            {new BigInteger("1329227995784915872903807060280344574"), 
                  resizeArray(createArray(15, 0xFF, 0xFF, 0xFE))},
            // 57
            {new BigInteger("1329227995784915872903807060280344575"), 
                  resizeArray(createArray(15, 0xFF, 0xFF, 0xFF))},
            
            // Sixteen Bytes (128 bits)!
            // 58
            {new BigInteger("1329227995784915872903807060280344576"), 
                  createArray(16, 0x01, 0x00, 0x00)},
            // 59
            {new BigInteger("1329227995784915872903807060280344577"), 
                  createArray(16, 0x01, 0x00, 0x01)},
            // 60
            {new BigInteger("340282366920938463463374607431768211454"), 
                  createArray(16, 0xFF, 0xFF, 0xFE)},
            // 61
            {new BigInteger("340282366920938463463374607431768211455"), 
                  createArray(16, 0xFF, 0xFF, 0xFF)},
            
        }
    );
  }

  private BigInteger inputValue;

  private byte[] asn1OerBytes;

  /**
   * Construct an instance of this parameterized test with the supplied inputs.
   *
   * @param inputValue   A {@code int} representing the unsigned 8bit integer to write in OER
   *                     encoding.
   * @param asn1OerBytes The expected value, in binary, of the supplied {@code intValue}.
   */
  public OerUint128CodecTest(final BigInteger inputValue, final byte[] asn1OerBytes) {
    this.inputValue = inputValue;
    this.asn1OerBytes = asn1OerBytes;
  }

  /**
   * Test setup.
   */
  @Before
  public void setUp() throws Exception {
    // Register the codec to be tested...
    oerUint128Codec = new OerUint128Codec();
    codecContext = new CodecContext().register(OerUint128.class, oerUint128Codec);
  }

  @Test
  public void read() throws Exception {
    final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(asn1OerBytes);
    final BigInteger actualValue =
        oerUint128Codec.read(codecContext, byteArrayInputStream).getValue();
    
    assertThat(actualValue, is(inputValue));
  }

  @Test
  public void write() throws Exception {
    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    oerUint128Codec.write(codecContext, new OerUint128(inputValue), byteArrayOutputStream);
    assertThat(byteArrayOutputStream.toByteArray(), is(this.asn1OerBytes));
  }

  @Test
  public void writeThenRead() throws Exception {
    // Write...
    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    oerUint128Codec.write(codecContext, new OerUint128(inputValue), byteArrayOutputStream);
    assertThat(byteArrayOutputStream.toByteArray(), is(asn1OerBytes));

    // Read...
    final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
        byteArrayOutputStream.toByteArray());
    final OerUint128 decodedValue = oerUint128Codec.read(codecContext, byteArrayInputStream);

    // Write...
    final ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
    oerUint128Codec.write(codecContext, decodedValue, byteArrayOutputStream2);
    assertThat(byteArrayOutputStream2.toByteArray(), is(asn1OerBytes));
  }
  
  /**
   * Convenience method to resize the input array of 8-bytes to 16 bytes by *left-padding* with 
   * zeros, i.e. the output array will contain padding zeros on the left, followed by values from 
   * the input.
   * 
   * @param original An 8-byte array to resize.
   * @return    A 16-byte array containing the values from the original, left padded with zeros.
   */
  protected static byte[] resizeArray(byte[] original) {
    byte[] resized = new byte[16];
    
    System.arraycopy(original, 0, resized, 16 - original.length, original.length);
    return resized;
  }
  
  /**
   * Convenience method to construct an array of a given size, fill it with a byte value, and
   * set the first (most significant) and last (least significant) bytes. This makes it easy to
   * construct byte arrays for numeric testing, for example 0xFF, 0xFF, 0xFF, 0xFE
   * 
   * @param len The length of the desired byte array.
   * @param firstByte The value to place in the first element of the array.
   * @param fillByte The value to place in all elements other than the first and last.
   * @param lastByte The value to place in the last element of the array.
   * @return A byte array constructed with the values provided.
   */
  protected static byte[] createArray(int len, int firstByte, int fillByte, int lastByte) {
    byte[] arr = new byte[len];
    
    Arrays.fill(arr, (byte) fillByte);
    arr[0] = (byte) firstByte;
    arr[len - 1] = (byte) lastByte;
    
    return arr;
  }

}