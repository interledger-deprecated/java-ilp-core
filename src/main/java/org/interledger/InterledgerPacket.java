package org.interledger;

import org.interledger.ilp.InterledgerAddress;

/**
 * <p>Interledger Packets are attached to Interledger transfers to facilitate Interledger
 * payments.</p>
 *
 * @param <T> The generic type of this packet, as defined by implementations.
 * @param <D> The generic type of the data payload, as defined by implementations.
 */
public interface InterledgerPacket<T extends InterledgerPacketType, D> {

  /**
   * The type of this header as a well-known URI.  Not encapsulated in an enum because the type of
   * this packet might be unspecified.
   *
   * @return A {@link String} representing the type of this header.
   */
  T getType();

  /**
   * The amount to deliver, in discrete units of the destination ledger's asset type. The scale of
   * the units is determined by the destination ledger's smallest indivisible unit.
   */
  Long getDestinationAmount();

  /**
   * The Interledger address of the account where the receiver should ultimately receive the
   * payment.
   */
  InterledgerAddress getDestinationAccount();

  /**
   * Arbitrary data for the receiver that is set by the transport layer of a payment (for example,
   * this may contain PSK Headers).
   *
   * @return An instance of type {@link D}.
   */
  D getEncodedData();
}
