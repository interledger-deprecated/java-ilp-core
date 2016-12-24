package org.interledger.ilp.core.ledger.service;

import org.interledger.ilp.core.ledger.model.LedgerTransfer;
import org.interledger.ilp.core.ledger.model.TransferRejectedReason;

public interface LedgerTransferService {

  /**
   * Initiates a ledger-local transfer.
   *
   * @param transfer
   *          <code>LedgerTransfer</code>
   */
  void sendTransfer(LedgerTransfer transfer) throws Exception;
  
  /**
   * Reject a transfer
   *
   * This should only be allowed if the entity rejecting the transfer is the receiver
   *
   * @param transfer
   * @param reason
   * @throws Exception 
   */
  void rejectTransfer(LedgerTransfer transfer, TransferRejectedReason reason) throws Exception;

  /**
   * Get a transfer ID to use for a new transfer
   * @return
   */
  String getNextTransferId();

}
