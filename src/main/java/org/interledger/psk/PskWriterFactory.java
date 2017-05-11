package org.interledger.psk;

import org.interledger.psk.io.EncryptedPskMessageWriter;
import org.interledger.psk.io.UnencryptedPskMessageWriter;

/**
 * Convenience factory for constructing Pre-Shared Key Message writers.
 */
public class PskWriterFactory {

  /**
   * Constructs a writer that outputs PSK messages <b>without</b> encrypting the private portion of
   * the message.
   */
  public static PskMessageWriter getUnencryptedWriter() {
    return new UnencryptedPskMessageWriter();
  }

  /**
   * Constructs a writer that outputs PSK messages with the private portion of the message
   * <b>encrypted</b>
   *
   * @param secretKey The secret key used to encrypt the private portion.
   */
  public static PskMessageWriter getEncryptedWriter(final byte[] secretKey) {
    return new EncryptedPskMessageWriter(secretKey);
  }
}
