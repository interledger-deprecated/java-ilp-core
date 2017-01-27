package org.interledger.ilp.ledger.events;

public interface LedgerExceptionEvent extends LedgerEvent {
 
  Exception getException();
  
}
