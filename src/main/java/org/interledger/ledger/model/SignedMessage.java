package org.interledger.ledger.model;

public interface SignedMessage<T> {

  public T getMessage();
  
  public MessageSignature getSignature();
  
}
