package org.interledger.ilp.ledger.events;

import org.interledger.ilp.ledger.model.LedgerMessage;

public interface LedgerMessageEvent extends LedgerEvent {
  
  public LedgerMessage getMessage();

}
