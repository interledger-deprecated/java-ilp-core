package org.interledger.psk;

import org.interledger.psk.model.PskMessage;

import java.io.InputStream;

/**
 * Defines a basic reader for Pre-Shared-Key messages.
 */
public interface PskMessageReader {

  /**
   * Reads the PskMessage from a buffer.
   * 
   * @param message A buffer containing the encoded PSK message.
   * @return The message read from the buffer.
   */
  PskMessage readMessage(byte[] message);

  /**
   * Reads the PskMessage from the input stream. Note: it is the responsibility of the caller
   * to close the stream after calling this method.
   * 
   * @param in An input stream containing the encoded PSK message.
   * @return The message read from the stream.
   */
  PskMessage readMessage(InputStream in);
}
