package org.interledger.ilqp;


import org.interledger.wire.InterledgerPacket;

import java.time.Duration;
import java.util.UUID;

/**
 * A parent interface for all quote responses in ILQP.
 */
public interface QuoteResponse extends InterledgerPacket {

  /**
   * A 128-bit unique identifer.
   */
  UUID getRequestId();

  /**
   * How long the sender should put money on hold.
   *
   * @return An instance of {@link Duration}.
   */
  Duration getSourceHoldDuration();

}