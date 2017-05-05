package org.interledger.encoding;

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * OER input stream reads OER encoded data from an underlying stream.
 * 
 * <p>
 * Limitations - INTEGER types are only supported up to 3 bytes (UNSIGNED).
 * </p>
 * 
 * @author adrianhopebailie
 *
 */
public class OerInputStream extends FilterInputStream {
  
  /**
   * Creates an <code>OerInputStream</code> instance.
   * 
   * @param stream the underlying input stream to read from.
   */
  public OerInputStream(InputStream stream) {
    super(stream);
  }

  /**
   * Reads an 8-bit unsigned integer value from the stream.
   * 
   * @return The 8-bit unsigned integer value in the range 0-255.
   */
  public int read8BitUInt() throws IOException {
    int value = read();
    verifyNotEof(value);
    return value;
  }

  /**
   * Reads a 16-bit unsigned integer value from the stream.
   * 
   * @return The 16-bit unsigned integer value in the range 0-65535.
   */
  public int read16BitUInt() throws IOException {
    int value = read();
    verifyNotEof(value);
    int next = read();
    verifyNotEof(next);

    return next + (value << 8);
  }

  /**
   * Reads a 32-bit unsigned integer value from the stream.
   * 
   * @return The 32-bit unsigned integer value in the range 0-4294967295.
   */
  public long read32BitUInt() throws IOException {
    int byte4 = read();
    verifyNotEof(byte4);
    int byte3 = read();
    verifyNotEof(byte3);
    int byte2 = read();
    verifyNotEof(byte2);
    int byte1 = read();
    verifyNotEof(byte1);
    
    return byte1 + (byte2 << 8) + (byte3 << 16) + ((long)byte4 << 24);
  }

  /**
   * Reads a variable length unsigned integer value from the stream.
   * 
   * <p>NOTE: integer values of greater than 16777215 (3 bytes) are not supported.
   * 
   * @return The value of the unsigned integer.
   */
  public int readVarUInt() throws IOException {

    /* We only support a 3 byte length indicator otherwise we go beyond Integer.MAX_SIZE */
    
    /* read the length indicator */
    int length = readLengthIndicator();
    
    /* read the first byte */
    int b1 = read();
    verifyNotEof(b1);
    
    int b2 = 0;
    int b3 = 0;

    /* read the second byte, if the length indicates it */
    if (length > 1) {
      b2 = read();
      verifyNotEof(b2);
      /* the leading byte is the most significant, so we shift it over */
      b1 = b1 << 8;
    }
    
    /* read the third byte, if the length indicates it */
    if (length > 2) {
      b3 = read();
      verifyNotEof(b3);
      /* the leading bytes are the most significant, so we shift them over */
      b1 = b1 << 8;
      b2 = b2 << 8;
    }
    
    /* we stop at 3 bytes */
    if (length > 3) {
      throw new IllegalArgumentException(
          "Integers of greater than 16777215 (3 bytes) are not supported.");
    }
    
    return b1 + b2 + b3;
  }

  /**
   * Reads an octet-string from the stream. The octet-string is a sequence of bytes preceded by a
   * length indicator.
   * 
   * @return The value of the octet-string. May be empty but will not be null.
   */
  public byte[] readOctetString() throws IOException {
    int length = readLengthIndicator();
    if (length == 0) {
      return new byte[] {};
    }
    byte[] value = new byte[length];
    int bytesRead = 0;
    bytesRead = read(value, 0, length);

    if (bytesRead < length) {
      throw new EOFException("Unexpected EOF when trying to decode OER data.");
    }
    return value;
  }

  /**
   * Reads a length indicator from the stream.
   * 
   * @return The value of the length indicator.
   */
  protected int readLengthIndicator() throws IOException {
    int length = read();

    verifyNotEof(length);

    if (length < 128) {
      return length;
    } else if (length > 128) {
      int lengthOfLength = length - 128;
      if (lengthOfLength > 3) {
        throw new RuntimeException(
            "This implementation only supports " + "variable length fields up to 16777215 bytes.");
      }
      length = 0;
      for (int i = lengthOfLength; i > 0; i--) {
        int next = read();
        verifyNotEof(next);
        length += (next << (8 * (i - 1)));
      }
      return length;
    } else {
      throw new RuntimeException("First byte of length indicator can't be 0x80.");
    }
  }

  /**
   * Convenience method to ensure data read from the stream is not the EOF marker.
   * 
   * @param data The data read from the stream.
   * @throws EOFException If the data corresponds to the stream EOF marker (-1).
   */
  protected void verifyNotEof(int data) throws EOFException {
    if (data == -1) {
      throw new EOFException("Unexpected EOF when trying to decode OER data.");
    }
  }
}
