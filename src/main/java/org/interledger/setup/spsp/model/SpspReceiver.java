package org.interledger.setup.spsp.model;

import org.interledger.setup.model.Receiver;

import java.net.URI;

/**
 * Defines a receiver for the Simple Payment Setup Protocol.
 */
public interface SpspReceiver extends Receiver {

  /**
   * Returns the endpoint of the receiver.
   */
  URI getEndpoint();

  /**
   * Returns the type of receiver.
   */
  ReceiverType getType();

}
