package org.interledger.psk.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Custom reader that reads both UTF-8 encoded strings and raw bytes from an input stream.
 * NOTE: this is a convenience class, it does *not* manage the state of the stream - closing it
 * remains the responsibility of the caller.
 */
public class StreamReader {

  /* Line feed '\n' character */
  private static final int LF = 10; 
  
  private InputStream in;

  /**
   * Constructs a new instance of the reader.
   * 
   * @param in  The input stream to read data from.
   */
  public StreamReader(InputStream in) {
    Objects.requireNonNull(in);
    this.in = in;
  }

  /**
   * Reads data from the input stream until a line feed character ('\n') is found or the input
   * stream is exhausted. Returns the data interpreted as a *UTF-8* string. Note that lines ending
   * with '\n' or '\r\n' are treated the same, the trailing '\r' will be removed if present.
   * 
   * @return A UTF-8 encoded string read from the input stream.
   */
  public String readLine() throws IOException {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream(256)) {
      int byteValue = 0;
      while ((byteValue = in.read()) != -1) {
        if (byteValue == LF) {
          break;
        }
        bos.write(byteValue);
      }

      String line = bos.toString(StandardCharsets.UTF_8.name());
      
      /*
       * we stopped reading at \n, which could have been by itself (*nix line endings) or part of
       * the windows '\r\n' line ending. check to see if that is the case and if so, strip the
       * trailing \r. NOTE: we do not trim the string, since we have no idea of the significance of
       * other whitespace characters
       */
      
      if (line.endsWith("\r")) {
        line = line.substring(0, line.length());
      }

      return line;
    }
  }

  /**
   * Consumes the remaining data in the stream, returning it as a byte array.
   */
  public byte[] readRemainingBytes() throws IOException {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream(256)) {
      readRemainingBytes(bos);

      return bos.toByteArray();
    }
  }
  
  /**
   * Consumes the remaining data in the input stream, writing it to the output stream.
   * @param out The output stream to write to.
   */
  public void readRemainingBytes(OutputStream out) throws IOException {
    Objects.requireNonNull(out, "cant write to null output stream");
    
    /* copy the remaining data from the input stream to the output stream in blocks of 256 bytes */
    byte[] buffer = new byte[256];
    int length;
    while ((length = in.read(buffer)) != -1) {
      out.write(buffer, 0, length);
    }
  }
}
