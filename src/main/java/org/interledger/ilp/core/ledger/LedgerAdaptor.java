package org.interledger.ilp.core.ledger;

import org.interledger.ilp.core.InterledgerAddress;
import org.interledger.ilp.core.ledger.events.LedgerEvent;
import org.interledger.ilp.core.ledger.events.LedgerEventHandler;
import org.interledger.ilp.core.ledger.model.Account;
import org.interledger.ilp.core.ledger.model.LedgerInfo;
import org.interledger.ilp.core.ledger.model.LedgerMessage;
import org.interledger.ilp.core.ledger.model.LedgerTransfer;
import org.interledger.ilp.core.ledger.model.TransferRejectedReason;

public interface LedgerAdaptor {
  
  void connect();
  
  boolean isConnected();
  
  void disconnect();
  
  void sendMessage(LedgerMessage message);
  
  void setEventHandler(LedgerEventHandler eventHandler);
  
  LedgerInfo getLedgerInfo();
  
  InterledgerAddress getIlpAddressForAccount(Account account);
  
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
   * Get a transfer ID to use for a new transfer
   * @return
   */
  String getNextTransferId();
  
  /**
   * Get details of an account.
   * 
   * @param accountName local account name
   * @return 
   * @throws Exception
   */
  Account getAccount(String accountName);

  /**
   * Subscribe to notifications related to an account
   * 
   * Notifications that are received for this account should then be raised as a {@link LedgerEvent} 
   * and passed to the {@link LedgerEventHandler} for the adaptor.  
   * 
   * @param accountName
   * @throws Exception
   */
  void subscribeToAccountNotifications(Account account);
    
}
