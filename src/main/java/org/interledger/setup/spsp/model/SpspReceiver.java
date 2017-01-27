package org.interledger.setup.spsp.model;

import java.net.URI;

import org.interledger.setup.model.Receiver;
import org.interledger.setup.spsp.model.ReceiverType;

public interface SpspReceiver extends Receiver {

  URI getEndpoint();
  
  ReceiverType getType();
  
}
