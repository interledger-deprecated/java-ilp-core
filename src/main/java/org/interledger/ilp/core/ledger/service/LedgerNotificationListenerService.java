package org.interledger.ilp.core.ledger.service;

import org.interledger.ilp.core.ledger.events.MessageEvent;
import org.interledger.ilp.core.ledger.model.Notification;

public interface LedgerNotificationListenerService {
  
  public String getConnectionString();
  
  public void connect() throws Exception;
  
  public void disconnect() throws Exception;

  public void onNotificationMessageReceived(MessageEvent<Notification> event);
  

}
