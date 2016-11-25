package org.interledger.ilp.core.ledger.service;

import org.interledger.ilp.core.ledger.model.LedgerTransfer;

public interface LedgerTransferService {

  public void send(LedgerTransfer transfer) throws Exception;

}
