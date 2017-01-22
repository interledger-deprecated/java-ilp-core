package org.interledger.spsp.core.model;

import org.interledger.ilp.core.InterledgerAddress;

public interface Receiver {
  public ReceiverType getType();
  
  public InterledgerAddress getAccount();
  
  public String getCurrencyCode();
  
  public String getCurrencySymbol();  
}

