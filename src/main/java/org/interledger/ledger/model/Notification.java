package org.interledger.ledger.model;

import java.util.Map;
import java.util.UUID;

public interface Notification<T>   {
  
  public UUID getId();
  
  public String getEvent();
  
  public T getResource();
  
  public Map<String, String> getRelatedResources();
  
    
}