package org.interledger.setup.spsp.model;

import java.net.URI;

/**
 * Defines a Payee type receiver in the Simple Payment Setup Protocol. A payee is a general 
 * receiving account for peer-to-peer payments.
 */
public interface Payee extends SpspReceiver {
  
  /**
   * Returns the name of the payee.
   */
  public String getName();

  /**
   * Returns the URI of an image representing the payee.
   */
  public URI getImageUrl();
  
}

