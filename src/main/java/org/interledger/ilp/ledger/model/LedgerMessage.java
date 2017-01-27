package org.interledger.ilp.ledger.model;

import java.util.UUID;

import org.interledger.ilp.InterledgerAddress;

public interface LedgerMessage {
  
  public UUID getId();
      
  public String getType();
  
  public InterledgerAddress getFrom();
  
  public InterledgerAddress getTo();
  
  public Object getData();
  
}
