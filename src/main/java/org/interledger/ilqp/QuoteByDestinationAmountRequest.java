package org.interledger.ilqp;

import org.interledger.InterledgerAddress;

import java.time.Duration;

/**
 * A request for a quote that specifies the amount to deliver at the destination address.
 */
public interface QuoteByDestinationAmountRequest extends QuoteRequest {

  @Override
  InterledgerAddress getDestinationAccount();

  /**
   * Returns fixed the amount that will arrive at the receiver.
   */
  long getDestinationAmount();
  
  
  /**
   * Returns the amount of time the receiver needs to fulfill the payment.
   */
  Duration getDestinationHoldDuration();
}