package org.interledger.ilqp.core.model;

import java.time.Duration;

import javax.money.MonetaryAmount;

import org.interledger.ilp.core.InterledgerAddress;

public interface QuoteRequest {

  public InterledgerAddress getSourceAddress();
  
  public MonetaryAmount getSourceAmount();
  
  public Duration getSourceExpiryDuration();

  public InterledgerAddress getDestinationAddress();
  
  public MonetaryAmount getDestinationAmount();
  
  public Duration getDestinationExpiryDuration();
      
}
