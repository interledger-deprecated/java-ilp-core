package org.interledger.ilqp;

import org.interledger.InterledgerAddress;

import java.time.Duration;
import java.util.UUID;

import javax.money.MonetaryAmount;

/**
 * A request for a quote that specifies the amount to deliver at the destination address.
 */
public interface QuoteBySourceRequest extends QuoteRequest {

  @Override
  UUID getRequestId();

  @Override
  InterledgerAddress getDestinationAccount();

  @Override
  Duration getDestinationHoldDuration();

  /**
   * The amount that will be taken from the sender.
   *
   * @return An instance of {@link MonetaryAmount}.
   */
  MonetaryAmount getSourceAmount();

}