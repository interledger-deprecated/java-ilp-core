package org.interledger.setup.spsp.model;

import java.net.URI;

public interface Payee extends SpspReceiver {
  
  public String getName();

  public URI getImageUrl();
  
}

