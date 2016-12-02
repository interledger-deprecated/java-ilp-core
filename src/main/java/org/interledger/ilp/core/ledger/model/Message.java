package org.interledger.ilp.core.ledger.model;

public interface Message {
  
  //TODO - Strongly type the identifiers
  public String getFromAccount();
  
  public String getToAccount();
  
  public String getData();

}
