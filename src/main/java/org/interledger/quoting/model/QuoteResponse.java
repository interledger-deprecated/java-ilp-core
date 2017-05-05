package org.interledger.quoting.model;

import org.interledger.ilp.InterledgerAddress;

import java.time.Duration;
import javax.money.MonetaryAmount;


/**
 * Defines a response to a quote request.
 */
public interface QuoteResponse {

  /**
   * Returns the address of the ILP-enabled ledger where the source account is located.
   */
  public InterledgerAddress getSourceLedger();

  /**
   * Returns the address of the ILP-enabled ledger where the destination account is located.
   */
  public InterledgerAddress getDestinationLedger();

  /**
   * Returns the address of the connector's account in the source ledger which should receive the
   * first transfer.
   */
  public InterledgerAddress getSourceConnectorAccount();

  /**
   * Returns the amount that the connector's account should receive in the source ledger.
   */
  public MonetaryAmount getSourceAmount();

  /**
   * Returns the amount that should be received by the destination account in the destination
   * ledger.
   */
  public MonetaryAmount getDestinationAmount();

  /**
   * Returns the duration between when the payment in the source ledger is prepared and when it must
   * be executed. TODO: isnt this a tad awkward, since we wont know when exactly the payment is
   * prepared? At millisecond precision, this could be important? Shouldn't we rather use a fixed
   * date-time?
   */
  public Duration getSourceExpiryDuration();

  /**
   * Returns the duration between when the payment in the destination ledger is prepared and when it
   * must be executed. TODO: isnt this a tad awkward, since we wont know when exactly the payment is
   * prepared? At millisecond precision, this could be important? Shouldn't we rather use a fixed
   * date-time?
   */
  public Duration getDestinationExpiryDuration();
}
