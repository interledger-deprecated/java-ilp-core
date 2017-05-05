package org.interledger.encoding;

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
public class OerOutputStream extends OutputStream {

  protected final OutputStream stream;

  /**
   * Creates an <code>OerOutputStream</code> instance.
   * 
   * @param stream the underlying output stream to write to.
   */
  public OerOutputStream(OutputStream stream) {
    this.stream = stream;
  }

  @Override
  public void write(int value) throws IOException {
    stream.write(value);
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

    stream.write(value);
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

    stream.write((value >> 8));
    stream.write(value);
  }

  /**
   * Writes the value to the stream as a 32-bit unsigned integer value.
   * 
   * @param value The value to write to the stream. Must be in the range 0-4294967295.
   */
  public void write32BitUInt(long value) throws IOException {
    int max = ((1 << 31) - 1);
    if (value > max) {
      throw new IllegalArgumentException(Long.toString(value) + "is greater than 32 bits." + max);
    }
    stream.write((byte) (value >> 24 & 255));
    stream.write((byte) (value >> 16 & 255));
    stream.write((byte) (value >> 8 & 255));
    stream.write((byte) (value >> 0 & 255));
  }

  /**
   * Writes a variable length unsigned integer value to the stream.
   * 
   * @param value The value to write to the stream. Must be in the range 0-16777215 (3 bytes).
   */
  public void writeVarUInt(int value) throws IOException {
    // We only support a 3 byte length indicator otherwise we go beyond
    // Integer.MAX_SIZE

    // TODO add some safe checks
    if (value <= 255) {
      stream.write(1);
      stream.write(value);
    } else if (value <= 65535) {
      stream.write(2);
      stream.write((value >> 8));
      stream.write(value);
    } else if (value <= 16777215) {
      stream.write(3);
      stream.write((value >> 16));
      stream.write((value >> 8));
      stream.write(value);
    } else {
      throw new IllegalArgumentException("Integers of greater than 16777215 are not supported.");
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

    stream.write(bytes);
  }

  /**
   * Writes a length indicator to the stream.
   * 
   * @param length The value the length indicator should convey. Must be in the range 0-16777215.
   */
  protected void writeLengthIndicator(int length) throws IOException {

    if (length < 128) {
      stream.write(length);
    } else if (length <= 255) {
      // Write length of length byte "1000 0001"
      stream.write(128 + 1);
      stream.write(length);
    } else if (length <= 65535) {
      // Write length of length byte "1000 0010"
      stream.write(128 + 2);
      stream.write((length >> 8));
      stream.write(length);
    } else if (length <= 16777215) {
      // Write length of length byte "1000 0011"
      stream.write(128 + 3);
      stream.write((length >> 16));
      stream.write((length >> 8));
      stream.write(length);
    } else {
      throw new IllegalArgumentException(
          "Field lengths of greater than 16777215 are not supported.");
    }
  }

  /**
   * Flushes the stream. This will write any buffered output bytes and flush through to the
   * underlying stream.
   *
   * @throws IOException If an I/O error has occurred.
   */
  public void flush() throws IOException {
    stream.flush();
  }

  /**
   * Closes the stream. This method must be called to release any resources associated with the
   * stream.
   */
  public void close() {
    try {
      flush();
      stream.close();
    } catch (Exception e) {
      // TODO: Improvement. Inject Logger.
      System.out.println("WARN: Couldn't properly close the stream due to " + e.toString()
          + ", Some data could have not been flushed to disk");
    }
  }
}
