package org.interledger.ilp.core.ledger.model;

import org.interledger.ilp.core.InterledgerAddress;

public interface LedgerMessage {
      
  public InterledgerAddress getFrom();
  
  public InterledgerAddress getTo();
  
  public byte[] getData();

}
