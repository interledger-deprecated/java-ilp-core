package org.interledger.encoding;

import java.io.EOFException;
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
public class OerInputStream extends InputStream {

  protected final InputStream stream;

  /**
   * Creates an <code>OerInputStream</code> instance.
   * 
   * @param stream the underlying input stream to read from.
   */
  public OerInputStream(InputStream stream) {
    this.stream = stream;
  }

  /**
   * Reads an 8-bit unsigned integer value from the stream.
   * 
   * @return The 8-bit unsigned integer value in the range 0-255.
   */
  public int read8BitUInt() throws IOException {
    int value = stream.read();
    verifyNotEof(value);
    return value;
  }

  /**
   * Reads a 16-bit unsigned integer value from the stream.
   * 
   * @return The 16-bit unsigned integer value in the range 0-65535.
   */
  public int read16BitUInt() throws IOException {
    int value = stream.read();
    verifyNotEof(value);
    int next = stream.read();
    verifyNotEof(next);

    return next + (value << 8);
  }

  /**
   * Reads a 32-bit unsigned integer value from the stream.
   * 
   * @return The 32-bit unsigned integer value in the range 0-4294967295.
   */
  public long read32BitUInt() throws IOException {
    // TODO: UnitTest read32BitUInt/write32BitUInt
    int byte4 = stream.read();
    verifyNotEof(byte4);
    int byte3 = stream.read();
    verifyNotEof(byte3);
    int byte2 = stream.read();
    verifyNotEof(byte2);
    int byte1 = stream.read();
    verifyNotEof(byte1);
    return byte1 + (byte2 << 8) + (byte3 << 16) + (byte4 << 24);
  }

  /**
   * Reads a variable length unsigned integer value from the stream.
   * 
   * <p>NOTE: integer values of greater than 16777215 (3 bytes) are not supported.
   * 
   * @return The value of the unsigned integer.
   */
  public int readVarUInt() throws IOException {

    // We only support a 3 byte length indicator otherwise we go beyond
    // Integer.MAX_SIZE
    int length = readLengthIndicator();
    int value = stream.read();
    verifyNotEof(value);

    if (length == 1) {
      return value;
    } else if (length == 2) {
      int next = stream.read();
      verifyNotEof(next);
      return value + (next << 8);
    } else if (length == 3) {
      int next = stream.read();
      verifyNotEof(next);
      value += (next << 8);
      next = stream.read();
      verifyNotEof(next);
      return value + (next << 16);
    } else {
      throw new IllegalArgumentException(
          "Integers of greater than 16777215 (3 bytes) are not supported.");
    }

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
    bytesRead = stream.read(value, 0, length);

    if (bytesRead < length) {
      throw new EOFException("Unexpected EOF when trying to decode OER data.");
    }
    return value;
  }

  @Override
  public int read() throws IOException {
    return this.stream.read();
  }

  /**
   * Reads a length indicator from the stream.
   * 
   * @return The value of the length indicator.
   */
  protected int readLengthIndicator() throws IOException {
    int length = stream.read();
    System.out.println("deteleme:  byte length = (byte) stream.read():" + length);

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
        int next = stream.read();
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

  /**
   * Closes the stream. This method must be called to release any resources associated with the
   * stream.
   */
  public void close() {
    try {
      stream.close();
    } catch (Exception e) {
      // TODO: Improvement. Inject Logger.
      System.out.println("WARN: Couldn't properly close the stream due to " + e.toString());
    }
  }
}
