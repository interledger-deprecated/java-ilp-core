package org.interledger.ilp.core.ledger.model;

public interface LedgerMessage {
    
  public LedgerInfo getLedger();
  
  public Account getFrom();
  
  public Account getTo();
  
  public byte[] getData();

}
