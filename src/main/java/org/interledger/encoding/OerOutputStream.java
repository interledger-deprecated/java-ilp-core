package org.interledger.encoding;

import java.io.FilterOutputStream;
import java.io.IOException;

import java.io.OutputStream;

/**
 * OER input stream writes OER encoded data to an underlying stream.
 * 
 * <p>
 * Limitations - INTEGER types are only supported up to 3 bytes (UNSIGNED)
 * </p>
 * 
 * @author adrianhopebailie
 *
 */
public class OerOutputStream extends FilterOutputStream {
  
  public static final long MAX_UINT_32_VALUE = 4294967295L;
  public static final int MAX_VAR_UINT_VALUE = 16777215;

  /**
   * Creates an <code>OerOutputStream</code> instance.
   * 
   * @param stream the underlying output stream to write to.
   */
  public OerOutputStream(OutputStream stream) {
    super(stream);
  }

  /**
   * Writes the value to the stream as an 8-bit unsigned integer value.
   * 
   * @param value The value to write to the stream. Must be in the range 0-255.
   */
  public void write8BitUInt(int value) throws IOException {

    if (value > 255) {
      throw new IllegalArgumentException(value + " exceeds 8 bit representation limit.");
    }

    write(value);
  }

  /**
   * Writes the value to the stream as a 16-bit unsigned integer value.
   * 
   * @param value The value to write to the stream. Must be in the range 0-65535.
   */
  public void write16BitUInt(int value) throws IOException {

    if (value > 65535) {
      throw new IllegalArgumentException(value + "exceeds 16 bit representation limit.");
    }

    write((value >> 8));
    write(value);
  }

  /**
   * Writes the value to the stream as a 32-bit unsigned integer value.
   * 
   * @param value The value to write to the stream. Must be in the range 0-4294967295.
   */
  public void write32BitUInt(long value) throws IOException {
    if (value > MAX_UINT_32_VALUE) {
      throw new IllegalArgumentException(
          value + " is greater than 32 bit unsigned int maximum value (" + MAX_UINT_32_VALUE + ")");
    }

    write((byte) (value >> 24 & 255));
    write((byte) (value >> 16 & 255));
    write((byte) (value >> 8 & 255));
    write((byte) (value >> 0 & 255));
  }

  /**
   * Writes a variable length unsigned integer value to the stream.
   * 
   * @param value The value to write to the stream. Must be in the range 0-16777215 (3 bytes).
   */
  public void writeVarUInt(int value) throws IOException {
    if (value > MAX_VAR_UINT_VALUE) {
      throw new IllegalArgumentException(
          "Integers of greater than " + MAX_VAR_UINT_VALUE + " are not supported.");
    }
    
    /* We only support a 3 byte length indicator otherwise we go beyond Integer.MAX_SIZE */

    // TODO add some safe checks
    if (value <= 255) {
      write(1);
      write(value);
    } else if (value <= 65535) {
      write(2);
      write((value >> 8));
      write(value);
    } else {
      write(3);
      write((value >> 16));
      write((value >> 8));
      write(value);
    }
  }

  /**
   * Writes an octet-string to the stream.
   * 
   * @param bytes The content of the octet string to write. Must not be null.
   */
  public void writeOctetString(byte[] bytes) throws IOException {
    if (bytes == null) {
      throw new IllegalArgumentException("Cannot write null byte array to the stream.");
    }

    writeLengthIndicator(bytes.length);

    write(bytes);
  }

  /**
   * Writes a length indicator to the stream.
   * 
   * @param length The value the length indicator should convey. Must be in the range 0-16777215.
   */
  protected void writeLengthIndicator(int length) throws IOException {

    if (length < 128) {
      write(length);
    } else if (length <= 255) {
      // Write length of length byte "1000 0001"
      write(128 + 1);
      write(length);
    } else if (length <= 65535) {
      // Write length of length byte "1000 0010"
      write(128 + 2);
      write((length >> 8));
      write(length);
    } else if (length <= 16777215) {
      // Write length of length byte "1000 0011"
      write(128 + 3);
      write((length >> 16));
      write((length >> 8));
      write(length);
    } else {
      throw new IllegalArgumentException(
          "Field lengths of greater than 16777215 are not supported.");
    }
  }
}
