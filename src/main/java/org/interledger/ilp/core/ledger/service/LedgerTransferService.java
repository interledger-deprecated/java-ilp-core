package org.interledger.ilp.core.ledger.service;

import org.interledger.ilp.core.ledger.model.LedgerTransfer;

public interface LedgerTransferService {

  /**
   * Initiates a ledger-local transfer.
   *
   * @param transfer
   *          <code>LedgerTransfer</code>
   */
  public void send(LedgerTransfer transfer) throws Exception;

}
