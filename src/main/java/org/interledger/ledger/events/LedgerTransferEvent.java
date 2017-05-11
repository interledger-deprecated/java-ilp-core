package org.interledger.ledger.events;

import org.interledger.ledger.model.LedgerTransfer;

public interface LedgerTransferEvent extends LedgerEvent {

  public LedgerTransfer getTransfer();

}
