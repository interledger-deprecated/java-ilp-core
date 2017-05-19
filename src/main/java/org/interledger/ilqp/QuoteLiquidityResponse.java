package org.interledger.ilqp;


import org.interledger.InterledgerAddress;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import javax.money.convert.ExchangeRate;

/**
 * A response to a quote request with liquidity information regarding transfers between the current
 * ledger and the destination account. This information is sufficient to locally quote any amount
 * until the curve expires.
 */
public interface QuoteLiquidityResponse extends QuoteResponse {

  @Override
  UUID getRequestId();

  @Override
  Duration getSourceHoldDuration();

  /**
   * <p>A series of exchange rates that can be plotted to assemble a "curve" of liquidity
   * representing the amount that one currency can be exchanged for another.</p>
   * <p>For example, if a liquidity curve contains the rate [0,0] and [10,20], then there is a
   * linear path of rates for which one currency can be exchange for another.  To illustate, it can
   * be assumed that [5,10] exists on this curve.</p>
   *
   * @return A {@link List} of type {link ExchangeRate}.
   */
  List<ExchangeRate> getLiquidityCurve();

  /**
   * <p>A common address prefix of all addresses for which the above liquidity curve applies.  If
   * the curve only applies to the destination account (see
   * {@link QuoteLiquidityRequest#getDestinationAccount()}) of the corresponding quote request,
   * then this value will be equal to that address. If the curve applies to other accounts with a
   * certain prefix, then this value will be set to that prefix.</p> <p>For more on ILP Address
   * Prefixes, see {@link InterledgerAddress}.</p>
   *
   * @return An instance of {@link InterledgerAddress}.
   */
  InterledgerAddress getAppliesToPrefix();

  /**
   * Maximum time where the connector (and any connectors after it) expects to be able to honor
   * this liquidity curve. Note that a quote in ILP is non-committal, meaning that the liquidity is
   * only likely to be available -- but not reserved -- and therefore not guaranteed.
   *
   * @return An instance of {@link ZonedDateTime}.
   */
  ZonedDateTime getExpiresAt();

}