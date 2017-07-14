package org.interledger.setup.spsp.model;

import org.interledger.setup.model.ReceiverQuery;

import java.net.URI;

/**
 * Concrete implementation of a receiver query for the Simple Payment Setup Protocol.
 */
public final class SpspReceiverQuery implements ReceiverQuery {

  private URI endpoint;

  public SpspReceiverQuery(String receiverEndpoint) {
    this.endpoint = URI.create(receiverEndpoint);
  }

  public SpspReceiverQuery(URI receiverEndpoint) {
    this.endpoint = receiverEndpoint;
  }

  public URI getReceiverEndpoint() {
    return this.endpoint;
  }

}
