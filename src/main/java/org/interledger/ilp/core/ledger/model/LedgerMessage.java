package org.interledger.ilp.core.ledger.model;

public interface LedgerMessage {
    
  public String getLedger();
  
  public String getFrom();
  
  public String getTo();
  
  public byte[] getData();

}
