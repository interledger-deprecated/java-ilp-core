package org.interledger.spsp.core.model;

import java.net.URL;

public interface Payee extends Receiver {
  public String getName();
  
  public URL getImageUrl();
}

