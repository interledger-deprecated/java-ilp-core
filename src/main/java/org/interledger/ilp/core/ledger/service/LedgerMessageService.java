package org.interledger.ilp.core.ledger.service;

import org.interledger.ilp.core.ledger.events.MessageEvent;
import org.interledger.ilp.core.ledger.model.Notification;

public interface LedgerMessageService {
  
  public String getConnectionString();
  
  public void connect() throws Exception;
  
  public boolean sendNotification(Notification message);
  
  public void onNotificationMessageReceived(MessageEvent<Notification> event);

}
