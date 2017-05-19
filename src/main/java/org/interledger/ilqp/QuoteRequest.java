package org.interledger.ilqp;


import org.interledger.InterledgerAddress;

import org.interledger.wire.InterledgerPacket;

import java.time.Duration;
import java.util.UUID;

/**
 * A parent interface for all quote requests in ILQP.
 */
public interface QuoteRequest extends InterledgerPacket {

  /**
   * A 128-bit unique identifer.
   */
  UUID getRequestId();

  /**
   * The account on the destination ledger that this quote applies to.
   *
   * @return An instance of {@link InterledgerAddress}.
   */
  InterledgerAddress getDestinationAccount();

  /**
   * How much time the receiver needs to fulfill the payment (in milliseconds)
   *
   * @return An instance of {@link Duration}.
   */
  Duration getDestinationHoldDuration();
}