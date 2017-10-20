package org.interledger.btp;

import org.interledger.cryptoconditions.Condition;

import org.immutables.value.Value.Immutable;

import java.math.BigInteger;
import java.time.Instant;
import java.time.ZonedDateTime;

@Immutable
public interface PreparePacket extends TransferPacket {

  /**
   * The amount to deliver, in discrete units of the destination ledger's asset type. The scale of
   * the units is determined by the destination ledger's smallest indivisible unit.
   *
   * @return An instance of {@link BigInteger}.
   */
  BigInteger getAmount();

  /**
   * Execution condition for the transfer.
   *
   * @return An instance of {@link Condition}.
   */
  Condition getExecutionCondition();

  /**
   * Expiry of the transfer.
   *
   * @return An instance of {@link ZonedDateTime}.
   */
  Instant getExpiresAt();


}
