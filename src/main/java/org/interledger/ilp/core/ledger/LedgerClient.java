package org.interledger.ilp.core.ledger;

import org.interledger.ilp.core.ledger.model.LedgerInfo;
import org.interledger.ilp.core.ledger.model.Message;
import org.interledger.ilp.core.ledger.service.LedgerAccountService;
import org.interledger.ilp.core.ledger.service.LedgerNotificationListenerService;
import org.interledger.ilp.core.ledger.service.LedgerTransferService;

public interface LedgerClient {
  
  public void connect() throws Exception;
  
  public boolean isConnected();
  
  public void disconnect() throws Exception;
  
  public void sendMessage(Message message) throws Exception;
  
  public LedgerInfo getLedgerInfo() throws Exception;
  
  public LedgerAccountService getAccountService() throws Exception;

  public LedgerTransferService getTransferService() throws Exception;
  
  public LedgerNotificationListenerService getLedgerNotificationListenerService() throws Exception;
  
}
