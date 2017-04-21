package org.interledger.psk.io;

import org.interledger.psk.PskMessageReader;
import org.interledger.psk.model.PskEncryptionHeader;
import org.interledger.psk.model.PskEncryptionType;
import org.interledger.psk.model.PskMessage;
import org.interledger.psk.model.PskMessageImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;


/**
 * Concrete implementation of a PSK message reader that will read unencrypted messages, or the
 * public portion of encrypted messages.
 */
public class UnencryptedPskMessageReader implements PskMessageReader {

  @Override
  public PskMessage readMessage(byte[] message) {
    Objects.requireNonNull(message, "Cannot read null message");

    try (ByteArrayInputStream bis = new ByteArrayInputStream(message)) {
      return readMessage(bis);
    } catch (IOException ioe) {
      throw new RuntimeException("Error reading message data", ioe);
    }
  }

  @Override
  public PskMessage readMessage(InputStream in) {
    Objects.requireNonNull(in, "Cannot read from null inputstream");

    PskMessageImpl message = new PskMessageImpl();

    try {
      StreamReader reader = new StreamReader(in);
      parseMessage(reader, true, message);

      return message;
    } catch (Exception ex) {
      throw new RuntimeException("Error parsing message", ex);
    } finally {
      try {
        in.close();
      } catch (IOException ioe) {
        throw new RuntimeException("Error closing inputstream", ioe);
      }
    }
  }

  /**
   * Parses a PSK message based on the underlying data.
   * 
   * @param reader The reader providing access to the PSK data, either as strings or raw bytes
   * @param readStatusLine Set to true to read the status line, false otherwise.
   * @param message A message to populate with the content read from the input.
   */
  protected void parseMessage(StreamReader reader, boolean readStatusLine, PskMessageImpl message)
      throws Exception {
    
    /* read and validate the PSK status line, if requested */
    if (readStatusLine) {
      String statusLine = reader.readLine();

      if (statusLine == null || !statusLine.matches("^PSK/1\\.\\d+$")) {
        throw new RuntimeException(
            "Invalid message content, status line '" + statusLine + "' not valid");
      }
    }

    /* read and populate the public headers */
    parseHeaders(reader, message, true);

    PskEncryptionHeader encryptionHeader = PskUtils.getEncryptionHeader(message);
    
    /* the remainder of the data *might* be unencrypted, so we should try read it if possible */
    
    parsePrivatePortion(reader, message,
        PskEncryptionType.NONE == encryptionHeader.getEncryptionType());
  }

  /**
   * Parses the private section of the PSK message, which may be encrypted. If so, we simply treat
   * it as opaque application data.
   * 
   * @param reader The reader providing access to the underlying message content.
   * @param message A message to populate with the content read from the input.
   * @param parseHeaders Indicates if the reader should attempt to parse the private headers or not
   */
  protected void parsePrivatePortion(StreamReader reader, PskMessageImpl message,
      boolean parseHeaders) throws Exception {

    if (parseHeaders) {
      parseHeaders(reader, message, false);
    }

    /*
     * whatever remains (after the private headers, or if we cant read the encrypted data) is simply
     * considered opaque application data. we do this so that intermediaries who dont have the
     * message key can read the public headers but nothing else.
     */

    message.setApplicationData(reader.readRemainingBytes());
  }

  /**
   * Reads the headers from the PSK message data.
   * 
   * @param reader Provides access to the 'raw' PSK message data.
   * @param message A PSK message representation to populate with any headers read.
   * @param publicHeaders Set to true indicating that the headers read are part of the PSK public
   *        header section, false to indicate they are part of the private header section.
   */
  protected void parseHeaders(StreamReader reader, PskMessageImpl message, boolean publicHeaders)
      throws IOException {

    /*
     * each header ends in a '\n' (or maybe '\r\n'), and the header block ends with an empty line
     * ('\n'), so we simply read lines until we find an empty one.
     */

    String headerLine = null;
    while (true) {
      headerLine = reader.readLine();

      /* we have found the empty line separating headers from the body, stop. */
      if (headerLine.isEmpty()) {
        break;
      }

      /*
       * the PSK RFC specifies that headers follow RFC 7230 (HTTP message), which says Each header
       * field consists of a case-insensitive field name followed by a colon (":"), optional leading
       * whitespace, the field value, and optional trailing whitespace.
       */

      int headerSplit = headerLine.indexOf(":");
      if (headerSplit == -1) {
        throw new RuntimeException(
            "Invalid message content, '" + headerLine + "' is not a valid header");
      }

      String headerName = headerLine.substring(0, headerSplit);
      String headerValue = headerLine.substring(headerSplit + 1);

      /*
       * we always populate the public headers, private headers are opaque and will be managed by a
       * reader capable of decrypting the application data, if the message indicates it. Note that
       * we trim the value, since whitespace is optional and not significant
       */

      if (publicHeaders) {
        message.addPublicHeader(headerName, headerValue.trim());
      } else {
        message.addPrivateHeader(headerName, headerValue.trim());
      }
    }
  }
}
