package org.interledger.ilp.ledger.events;

import org.interledger.ilp.ledger.model.LedgerTransfer;

public interface LedgerTransferEvent extends LedgerEvent {

  public LedgerTransfer getTransfer();

}
