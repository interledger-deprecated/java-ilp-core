package org.interledger.ledger.events;

public interface LedgerExceptionEvent extends LedgerEvent {
 
  Exception getException();
  
}
