package org.interledger.setup.spsp.model;

import java.net.URI;

import org.interledger.setup.model.ReceiverQuery;

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
