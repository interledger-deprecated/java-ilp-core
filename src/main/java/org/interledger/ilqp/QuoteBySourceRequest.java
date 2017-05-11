package org.interledger.ilqp;

import java.time.Duration;
import java.util.UUID;
import javax.money.MonetaryAmount;
import org.interledger.InterledgerAddress;

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
   * @return
   */
  MonetaryAmount getSourceAmount();

}