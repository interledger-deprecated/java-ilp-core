package org.interledger.ilp.core.ledger.model;

public interface LedgerMessage {
  
  //TODO - Strongly type the identifiers
  public String getLedger();
  
  public String getFromAccount();
  
  public String getToAccount();
  
  public Object getData();

}
