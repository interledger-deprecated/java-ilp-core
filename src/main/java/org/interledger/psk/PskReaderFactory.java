package org.interledger.psk;

import org.interledger.psk.io.EncryptedPskMessageReader;
import org.interledger.psk.io.UnencryptedPskMessageReader;

/**
 * Convenience factory for constructing Pre-Shared Key Message readers.
 */
public class PskReaderFactory {
  
  /**
   * Constructs a reader that will read PSK messages but will <b>not</b> decrypt the private portion
   * of the message if it is encrypted.
   */
  public static PskMessageReader getUnencryptedReader() {
    return new UnencryptedPskMessageReader();
  }

  /**
   * Constructs a reader that will read PSK messages and <b>decrypts</b> the private portion of the
   * message if necessary.
   * 
   * @param secretKey The secret key used to decrypt the private portion.
   */
  public static PskMessageReader getEncryptedReader(byte[] secretKey) {
    return new EncryptedPskMessageReader(secretKey);
  }
}