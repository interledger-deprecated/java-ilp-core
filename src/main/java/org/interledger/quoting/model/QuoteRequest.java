package org.interledger.quoting.model;

import org.interledger.ilp.InterledgerAddress;

import java.time.Duration;

import javax.money.MonetaryAmount;


/**
 * Defines a request for a quote.
 */
public interface QuoteRequest {

  /**
   * Returns the address of the source's account in the source ledger.
   */
  public InterledgerAddress getSourceAddress();

  /**
   * Returns the fixed amount to debit from the source's account. (Required unless the destination
   * amount is specified.)
   */
  public MonetaryAmount getSourceAmount();

  /**
   * Returns the duration until the transfer in the source ledger expires. TODO: duration from when,
   * exactly?
   */
  public Duration getSourceExpiryDuration();

  /**
   * Returns the address of the destination's account in the destination ledger.
   */
  public InterledgerAddress getDestinationAddress();

  /**
   * Returns the fixed amount to credit to the destination's account. (Required unless the source
   * amount is specified.)
   */
  public MonetaryAmount getDestinationAmount();

  /**
   * Returns the duration until the transfer in the destination ledger expires. TODO: duration from
   * when, exactly?
   */
  public Duration getDestinationExpiryDuration();

}
