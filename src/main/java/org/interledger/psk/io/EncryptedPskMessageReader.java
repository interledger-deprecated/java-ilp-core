package org.interledger.psk.io;

import org.interledger.psk.io.PskUtils.GcmEncryptedData;
import org.interledger.psk.model.PskEncryptionHeader;
import org.interledger.psk.model.PskMessageImpl;

import java.io.ByteArrayInputStream;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * A PSK message reader that will decrypt the encrypted private data portion of the message using
 * the pre-shared key if required. Note that the reader will correctly read unencrypted PSK messages
 * too.
 */
public class EncryptedPskMessageReader extends UnencryptedPskMessageReader {

  private SecretKey key;

  /**
   * Constructs an {@link EncryptedPskMessageReader} instance with the key provided.
   * 
   * @param key The clear content of the pre-shared key.
   */
  public EncryptedPskMessageReader(byte[] key) {
    if (key == null || key.length != PskUtils.AES_KEY_LEN_BYTES) {
      throw new IllegalArgumentException(
          "Invalid key - must be " + PskUtils.AES_KEY_LEN_BYTES + " bytes");
    }

    this.key = new SecretKeySpec(key, "AES");
  }

  @Override
  protected void parsePrivatePortion(StreamReader reader, PskMessageImpl message,
      boolean parseHeaders) throws Exception {

    PskEncryptionHeader encryptionHeader = PskUtils.getEncryptionHeader(message);

    switch (encryptionHeader.getEncryptionType()) {
      case NONE:
        /* no encryption, let the unencrypted reader do its thing */
        super.parsePrivatePortion(reader, message, true);
        break;
        
      case AES_256_GCM:
        /* aha, something we can work with */
        decryptPrivateData(reader, message, encryptionHeader.getAuthenticationTagValue());
        break;
        
      default:
        throw new RuntimeException("Invalid PSK message - unsupported encryption header data '"
            + encryptionHeader.getValue() + "'");
    }
  }

  /**
   * Decrypts and parses the private data portion of the PSK message.
   * 
   * @param reader A reader providing access to the underlying PSK message data.
   * @param message A representation of the PSK message to populate with the decrypted data.
   * @param authenticationTag The authentication tag data extracted from the public encryption
   *        header.
   */
  protected void decryptPrivateData(StreamReader reader, PskMessageImpl message,
      byte[] authenticationTag) throws Exception {

    byte[] plainText = PskUtils.decryptPskData(this.key, PskUtils.getNonce(message),
        new GcmEncryptedData(reader.readRemainingBytes(), authenticationTag));

    try (ByteArrayInputStream bis = new ByteArrayInputStream(plainText)) {
      StreamReader plaintextReader = new StreamReader(bis);

      /* we've decrypted the data, so we can simply let the underlying reader do its thing */
      super.parsePrivatePortion(plaintextReader, message, true);
    }
  }
}
