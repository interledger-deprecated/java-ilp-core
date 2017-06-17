package org.interledger.ilqp;


import org.interledger.InterledgerAddress;

import org.interledger.InterledgerPacket;

/**
 * A parent interface for all quote requests in ILQP.
 */
public interface QuoteRequest extends InterledgerPacket {

  /**
   * The account on the destination ledger that this quote applies to.
   *
   * @return An instance of {@link InterledgerAddress}.
   */
  InterledgerAddress getDestinationAccount();
}