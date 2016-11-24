package org.interledger.ilp.core.ledger.model;

public interface Notification {
  
  public String getId();
  
  public String getEvent();
  
  public Resource getResource();
  
  public RelatedResources getRelatedResources();
  
  public interface Resource {
    
    public String getLedger();

    public String getFrom();

    public String getTo();

    public String getData();
    
  }
  
  public interface RelatedResources {
    
    public String getCancellationConditionFulfillment();
    
    public String getExecutionConditionFulfillment();
    
  }
  
}