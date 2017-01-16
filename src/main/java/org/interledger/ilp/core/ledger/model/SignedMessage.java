package org.interledger.ilp.core.ledger.model;

public interface SignedMessage<T> {

  public T getMessage();
  
  public MessageSignature getSignature();
  
}
