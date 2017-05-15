package org.interledger.psk.io;

import org.interledger.psk.PskMessageWriter;
import org.interledger.psk.model.PskEncryptionHeader;
import org.interledger.psk.model.PskEncryptionType;
import org.interledger.psk.model.PskMessage;
import org.interledger.psk.model.PskMessageHeader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * A writer for PSK messages that does <b>not</b> encrypt the private portion of the message. To
 * encrypt the private portion of the message, please see {@link EncryptedPskMessageWriter}.
 */
public class UnencryptedPskMessageWriter implements PskMessageWriter {

  @Override
  public byte[] writeMessage(PskMessage message) {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
      writeMessage(message, bos);
      return bos.toByteArray();
    } catch (IOException ioe) {
      throw new RuntimeException("Error writing message", ioe);
    }
  }

  @Override
  public void writeMessage(PskMessage message, OutputStream out) {
    Objects.requireNonNull(out, "Cannot write to null outputstream");
    validateMessage(message);

    try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
      
      /*
       * write out the private headers and application data. the format is the same as the overall
       * PSK message, without the status line
       */
      writeHeadersAndData(bos, false, message.getPrivateHeaders(), message.getApplicationData());

      byte[] privateDataBytes = bos.toByteArray();

      /*
       * this writer does not encrypt the private portion of the message, so we add the appropriate
       * encryption header. We have validated that the message does not already include an
       * encryption header.
       */

      List<PskMessageHeader> publicHeaders = message.getPublicHeaders();
      publicHeaders.add(new PskEncryptionHeader(PskEncryptionType.NONE));

      writeHeadersAndData(out, true, publicHeaders, privateDataBytes);
    } catch (Exception ex) {
      throw new RuntimeException("Error writing PSK message to stream", ex);
    }
  }

  /**
   * Validates that the message meets the minimum requirements to be written. At present this will
   * check that a nonce header is present, and that an encryption header is *not* present.
   *
   * @param message The message to validate.
   */
  protected void validateMessage(PskMessage message) {
    final List<PskMessageHeader> nonces
        = message.getPublicHeaders(PskMessageHeader.PublicHeaders.NONCE);

    if (nonces.isEmpty() || nonces.size() > 1) {
      throw new RuntimeException(
          "Invalid message content - PSK messages must have exactly one public Nonce header");
    }

    // TODO: this might be a bit confusing - we expect users to add a nonce header, but not the
    // encryption header. at the same time, it would probably be even more confusing if the writer
    // silently replaced the header with its own... Maybe carrying the nonce separately from the
    // headers in PskMessage would make it less confusing?

    List<PskMessageHeader> encryptionHeader =
        message.getPublicHeaders(PskMessageHeader.PublicHeaders.ENCRYPTION);

    if (!encryptionHeader.isEmpty()) {
      throw new RuntimeException(
          "Invalid message content - the public Encryption header should not be set, "
              + "it will be set by this writer.");
    }
  }

  /**
   * Convenience method to write a set of PSK headers and data to the stream, optionally preceded
   * by the PSK status line.
   *
   * @param out             The output stream to write the message to.
   * @param writeStatusLine Indicates whether the status line should be written (true) or not
   *                        (false).
   * @param headers         The headers to be written.
   * @param data            The application data to be written.
   */
  protected void writeHeadersAndData(OutputStream out, boolean writeStatusLine,
                                     List<PskMessageHeader> headers, byte[] data) throws Exception {
    if (writeStatusLine) {
      writeString(out, "PSK/1.0\n");
    }

    writeString(out, headersToString(headers));
    /* the separator between the headers and content */
    writeString(out, "\n");

    /* any data is written out simply as the 'raw' bytes, no Base64 encoding */
    if (data != null) {
      out.write(data);
    }
  }

  /**
   * Utility method to write the given string to the stream using UTF-8 encoding. Null strings are
   * simply ignored.
   *
   * @param out   The stream to write the string to.
   * @param value The string to write.
   */
  protected void writeString(OutputStream out, String value) throws Exception {
    if (value == null) {
      return;
    }

    out.write(value.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * Convenience method to return a single string representing all of the headers, *including* a
   * trailing '\n' if at least one header is present.
   *
   * @param headers The list of headers to write to the string.
   * @return A string containing all of the headers, formatted according to the PSK standard.
   */
  protected String headersToString(List<PskMessageHeader> headers) {
    if (headers == null || headers.isEmpty()) {
      return "";
    }

    StringBuilder sb = new StringBuilder(headers.size() * 50);

    for (PskMessageHeader header : headers) {
      sb.append(header.getName()).append(": ").append(header.getValue()).append("\n");
    }

    return sb.toString();
  }
}
