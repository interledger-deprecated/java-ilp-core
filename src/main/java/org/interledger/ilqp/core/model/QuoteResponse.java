package org.interledger.ilqp.core.model;

import java.time.Duration;

import javax.money.MonetaryAmount;

import org.interledger.ilp.core.InterledgerAddress;

public interface QuoteResponse {
  
  public InterledgerAddress getSourceLedger();
  
  public InterledgerAddress getDestinationLedger();
  
  public InterledgerAddress getSourceConnectorAccount();
  
  public MonetaryAmount getSourceAmount();
  
  public MonetaryAmount getDestinationAmount();
  
  public Duration getSourceExpiryDuration();
  
  public Duration getDestinationExpiryDuration();
}
