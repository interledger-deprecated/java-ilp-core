package org.interledger.psk.io;

import org.interledger.psk.io.PskUtils.GcmEncryptedData;
import org.interledger.psk.model.PskEncryptionHeader;
import org.interledger.psk.model.PskEncryptionType;
import org.interledger.psk.model.PskMessage;
import org.interledger.psk.model.PskMessageHeader;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * A writer for PSK messages that <b>encrypts</b> the private data portion of the PSK message using
 * AES-256-GCM.
 */
public class EncryptedPskMessageWriter extends UnencryptedPskMessageWriter {

  private SecretKey key;

  /**
   * Constructs a new instance of the writer, using the given clear pre-shared key.
   *
   * @param key The clear value of the pre-shared key.
   */
  public EncryptedPskMessageWriter(byte[] key) {
    if (key == null || key.length != PskUtils.AES_KEY_LEN_BYTES) {
      throw new IllegalArgumentException(
          "Invalid key - must be " + PskUtils.AES_KEY_LEN_BYTES + " bytes");
    }

    this.key = new SecretKeySpec(key, "AES");
  }

  @Override
  public void writeMessage(final PskMessage message, final OutputStream out) {
    Objects.requireNonNull(message, "Cannot write null message");
    Objects.requireNonNull(message, "Cannot write to null outputstream");
    validateMessage(message);

    try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

      /*
       * write out the private headers and application data. the format is the same as the overall
       * PSK message, without the status line
       */
      writeHeadersAndData(bos, false, message.getPrivateHeaders(), message.getApplicationData());

      byte[] privateDataBytes = bos.toByteArray();

      byte[] nonce = PskUtils.getNonce(message);

      /* now encrypt the private message and get the auth tag */
      GcmEncryptedData encrypted = PskUtils.encryptPskData(this.key, nonce, privateDataBytes);

      /*
       * Construct the appropriate encryption header for the public portion of the message. the
       * value of the header is the encryption type followed by a space followed by the base64url
       * encoded value of the auth tag.
       */

      List<PskMessageHeader> publicHeaders = message.getPublicHeaders();

      PskMessageHeader encryptionHeader =
          new PskEncryptionHeader(PskEncryptionType.AES_256_GCM, encrypted.getAuthenticationTag());

      publicHeaders.add(encryptionHeader);

      writeHeadersAndData(out, true, publicHeaders, encrypted.getEncryptedData());

    } catch (Exception ex) {
      throw new RuntimeException("Error writing PSK message to stream", ex);
    }
  }
}
