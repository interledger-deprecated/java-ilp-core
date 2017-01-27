package org.interledger.ilp.ledger.events;

public interface LedgerEventHandler {
  
  void handleLedgerEvent(LedgerEvent event);
  
}
