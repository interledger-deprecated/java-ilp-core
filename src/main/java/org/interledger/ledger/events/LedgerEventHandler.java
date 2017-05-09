package org.interledger.ledger.events;

public interface LedgerEventHandler {
  
  void handleLedgerEvent(LedgerEvent event);
  
}
