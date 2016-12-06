package org.interledger.ilp.core.ledger.events;

public interface LedgerEventHandler {
  
  void handleLedgerEvent(LedgerEvent event);
  
}
