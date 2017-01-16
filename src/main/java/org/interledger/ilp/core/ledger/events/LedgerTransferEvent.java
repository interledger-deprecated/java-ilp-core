package org.interledger.ilp.core.ledger.events;

import org.interledger.ilp.core.ledger.model.LedgerTransfer;

public interface LedgerTransferEvent extends LedgerEvent {

    public LedgerTransfer getTransfer();

}
