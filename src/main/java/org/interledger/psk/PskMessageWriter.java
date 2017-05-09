package org.interledger.psk;

import org.interledger.psk.model.PskMessage;

import java.io.OutputStream;

/**
 * Defines a basic writer for Pre-Shared-Key messages.
 */
public interface PskMessageWriter {
  
  /**
   * Writes the PskMessage to a buffer.
   * 
   * @param message The message to write
   * @return    a buffer containing the encoded PSK message.
   */
  byte[] writeMessage(PskMessage message);
  
  /**
   * Writes the PskMessage to the given output stream.
   * 
   * @param message The message to write.
   * @param out The output stream to write the message to.
   */
  void writeMessage(PskMessage message, OutputStream out);
}
