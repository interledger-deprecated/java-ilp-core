package org.interledger.ilqp;

import java.time.Duration;
import java.util.UUID;
import javax.money.MonetaryAmount;

/**
 * A quote sent in response to a request of type {@link QuoteByDestinationRequest}.
 */
public interface QuoteByDestinationResponse extends QuoteResponse {

  @Override
  UUID getRequestId();

  @Override
  Duration getSourceHoldDuration();

  /**
   * The amount the sender needs to send based on the requested destination amount.
   *
   * @return An instance of {@link MonetaryAmount}.
   */
  MonetaryAmount getSourceAmount();

}