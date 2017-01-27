package org.interledger.quoting.model;

import java.time.Duration;

import javax.money.MonetaryAmount;

import org.interledger.ilp.InterledgerAddress;

public interface QuoteResponse {
  
  public InterledgerAddress getSourceLedger();
  
  public InterledgerAddress getDestinationLedger();
  
  public InterledgerAddress getSourceConnectorAccount();
  
  public MonetaryAmount getSourceAmount();
  
  public MonetaryAmount getDestinationAmount();
  
  public Duration getSourceExpiryDuration();
  
  public Duration getDestinationExpiryDuration();
}
