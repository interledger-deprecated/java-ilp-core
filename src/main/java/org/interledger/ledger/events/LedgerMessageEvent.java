package org.interledger.ledger.events;

import org.interledger.ledger.model.LedgerMessage;

public interface LedgerMessageEvent extends LedgerEvent {

  LedgerMessage getMessage();

}
