package org.interledger.ilp.core.ledger.events;

import org.interledger.ilp.core.ledger.model.LedgerMessage;

public interface LedgerMessageEvent extends LedgerEvent {
  
  public LedgerMessage getMessage();

}
