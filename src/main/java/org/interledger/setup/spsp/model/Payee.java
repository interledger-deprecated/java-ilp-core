package org.interledger.setup.spsp.model;

import java.net.URI;

public interface Payee extends SpspReceiver {
  
  public String getName();

  //TODO Should we return a java.awt.Image?
  public URI getImageUrl();
  
}

