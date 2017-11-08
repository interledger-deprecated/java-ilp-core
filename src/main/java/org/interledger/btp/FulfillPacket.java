package org.interledger.btp;

import org.interledger.cryptoconditions.Fulfillment;

import org.immutables.value.Value.Immutable;

@Immutable
public interface FulfillPacket extends TransferPacket {

  /**
   * Accessor for the fulfillment of a bilateral transfer protocol.
   */
  Fulfillment getFulfillment();

}
