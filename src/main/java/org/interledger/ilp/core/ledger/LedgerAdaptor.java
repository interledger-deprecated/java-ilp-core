package org.interledger.ilp.core.ledger;

import org.interledger.ilp.core.ledger.events.LedgerEventHandler;
import org.interledger.ilp.core.ledger.model.LedgerInfo;
import org.interledger.ilp.core.ledger.model.LedgerMessage;
import org.interledger.ilp.core.ledger.service.LedgerAccountService;
import org.interledger.ilp.core.ledger.service.LedgerTransferService;

public interface LedgerAdaptor {
  
  void connect() throws Exception;
  
  boolean isConnected();
  
  void disconnect() throws Exception;
  
  void sendMessage(LedgerMessage message) throws Exception;
  
  void setEventHandler(LedgerEventHandler eventHandler);
  
  LedgerInfo getLedgerInfo() throws Exception;
  
  LedgerAccountService getAccountService() throws Exception;

  LedgerTransferService getTransferService() throws Exception;
  
}
