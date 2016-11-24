package org.interledger.ilp.core.ledger.events;

public interface MessageEvent<T> {
  
  public T getMessage();

}
