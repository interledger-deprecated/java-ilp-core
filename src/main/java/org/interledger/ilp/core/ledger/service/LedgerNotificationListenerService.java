package org.interledger.ilp.core.ledger.service;

import org.interledger.ilp.core.ledger.events.MessageEvent;
import org.interledger.ilp.core.ledger.model.Notification;

public interface LedgerNotificationListenerService {
  
  public void connect() throws Exception;
  
  public void disconnect() throws Exception;

  public void subscribeToAccountNotifications(String account) throws Exception;
  
  //TODO: we might want to remove this - the listener service should be used to subscribe to
  //events, not be the callback for the notification?
  public void onNotificationMessageReceived(MessageEvent<Notification> event);
  

}
