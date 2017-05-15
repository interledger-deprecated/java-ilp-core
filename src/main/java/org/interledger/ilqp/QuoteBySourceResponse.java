package org.interledger.ilqp;

import java.time.Duration;
import java.util.UUID;

import javax.money.MonetaryAmount;

/**
 * A quote sent in response to a request of type {@link QuoteBySourceRequest}.
 */
public interface QuoteBySourceResponse extends QuoteResponse {

  @Override
  UUID getRequestId();

  @Override
  Duration getSourceHoldDuration();

  /**
   * The amount that will arrive at the receiver.
   *
   * @return An instance of {@link MonetaryAmount}.
   */
  MonetaryAmount getDestinationAmount();

}