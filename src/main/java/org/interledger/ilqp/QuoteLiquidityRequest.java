package org.interledger.ilqp;


import java.time.Duration;
import java.util.UUID;
import org.interledger.InterledgerAddress;

/**
 * A request to receive liquidity information between the current ledger and the destination
 * account. This information is sufficient to locally quote any amount until the curve expires.
 */
public interface QuoteLiquidityRequest extends QuoteRequest {

  @Override
  UUID getRequestId();

  @Override
  InterledgerAddress getDestinationAccount();

  @Override
  Duration getDestinationHoldDuration();

}