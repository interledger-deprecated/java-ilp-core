package org.interledger.ilp.ledger;

import java.util.Set;
import java.util.UUID;

import org.interledger.cryptoconditions.Fulfillment;
import org.interledger.ilp.InterledgerAddress;
import org.interledger.ilp.ledger.events.LedgerConnectEvent;
import org.interledger.ilp.ledger.events.LedgerEvent;
import org.interledger.ilp.ledger.events.LedgerEventHandler;
import org.interledger.ilp.ledger.model.AccountInfo;
import org.interledger.ilp.ledger.model.LedgerInfo;
import org.interledger.ilp.ledger.model.LedgerMessage;
import org.interledger.ilp.ledger.model.LedgerTransfer;
import org.interledger.ilp.ledger.model.TransferRejectedReason;

public interface LedgerAdaptor {
  
  /**
   * Async (non-blocking) connect request.
   * 
   * The adaptor should raise an {@link LedgerConnectEvent} when it has 
   * connected to the ledger.
   * 
   */
  void connect();
  
  boolean isConnected();
  
  void disconnect();
  
  void sendMessage(LedgerMessage message);
  
  void setEventHandler(LedgerEventHandler eventHandler);
  
  LedgerInfo getLedgerInfo();
    
  /**
   * Initiates a ledger-local transfer.
   *
   * @param transfer
   *          <code>LedgerTransfer</code>
   */
  void sendTransfer(LedgerTransfer transfer);
  
  /**
   * Reject a transfer
   *
   * This should only be allowed if the entity rejecting the transfer is the receiver
   *
   * @param transfer
   * @param reason
   * @throws Exception 
   */
  void rejectTransfer(LedgerTransfer transfer, TransferRejectedReason reason);
  
  /**
   * Get basic details of an account.
   * 
   * @param accountId local account identifier
   * @return 
   * @throws Exception
   */
  AccountInfo getAccountInfo(InterledgerAddress account);

  /**
   * Subscribe to notifications related to an account
   * 
   * Notifications that are received for this account should then be raised as a {@link LedgerEvent} 
   * and passed to the {@link LedgerEventHandler} for the adaptor.  
   * 
   * @param accountName
   * @throws Exception
   */
  void subscribeToAccountNotifications(InterledgerAddress account);

  /**
   * Get the set of ledger accounts that are known to be owned by connectors.
   * 
   * @return
   */
  Set<InterledgerAddress> getConnectors();

  /**
   * Fulfill a condition for a transfer that has been prepared on a ledger.
   * 
   * @param transferId
   * @param fulfillment
   */
  void fulfillTransfer(UUID transferId, Fulfillment fulfillment);
  
      
}
