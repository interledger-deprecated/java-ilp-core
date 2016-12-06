package org.interledger.ilp.core.ledger.events;

public interface LedgerExceptionEvent extends LedgerEvent {
 
  Exception getException();
  
}
