package org.interledger.ilp.packets;

import org.interledger.InterledgerPacket;

/**
 * <p>Interledger Packets are pieces of data attached to Interledger transfers to facilitate
 * Interledger payments.</p>
 *
 * <p>Interledger Payment Packets have three major consumers:
 *
 * <ul>
 * <li>Connectors utilize the Interledger Address contained in the packet to route the
 * payment.</li>
 * <li>The receiver of a packet uses it to identify the payment and which condition to
 * fulfill.</li>
 * <li>Interledger sub-protocols utilize custom data encoded in each packet to facilitate
 * sub-protocol operations.</li>
 * </ul>
 *
 * <h1>Life-Cycle of an Interledger Packet</h1>
 * <p>The exact use of the ILP Payment Packet depends on the transport protocol you use. In
 * <a href="https://github.com/interledger/rfcs/blob/master/0011-interledger-payment-request/0011-interledger-payment-request.md">IPR</a>,
 * the receiver generates and communicates a packet to the sender. In <a
 * href="https://github.com/interledger/rfcs/blob/master/0016-pre-shared-key/0016-pre-shared-key.md">PSK</a>,
 * the sender generates a packet according to agreed-upon rules with a pre-shared secret.</p>
 *
 * <p> When the sender prepares a transfer to start a payment, the sender attaches an ILP
 * Payment Packet to the transfer, in the memo field if possible. If a ledger does not support
 * attaching the entire ILP Payment Packet to a transfer as a memo, users of that ledger can
 * transmit the ILP Payment Packet using another authenticated messaging channel, but MUST be able
 * to correlate transfers and ILP Payment Packets.</p>
 *
 * <p> When a connector sees an incoming prepared transfer with an ILP Payment Packet, the
 * connector reads the ILP Payment Packet to get the ILP Address of the payment's receiver. If the
 * connector has a route to the receiver's account, the connector prepares a transfer to continue
 * the payment, and attaches the same ILP Payment Packet to the new transfer.</p>
 *
 * <p>  When the receiver sees the incoming prepared transfer, the receiver reads the ILP Payment
 * Packet to confirm the details of the packet. The receiver confirms that the amount from the ILP
 * Payment Packet matches the amount actually delivered by the transfer. The receiver decodes the
 * data of the ILP Payment Packet and matches the condition to the packet. The receiver MUST
 * confirm the integrity of the ILP Payment Packet, for example with a hash-based message
 * authentication code (HMAC). If the receiver finds the transfer acceptable, the receiver releases
 * the fulfillment for the transfer, which can be used to execute all prepared transfers that were
 * establlished prior to the receiver accepting the payment.</p>
 */
public interface InterledgerPaymentPacket<D> extends InterledgerPacket<IlpPaymentPacketType, D> {

  /**
   * Arbitrary data for the receiver that is set by the transport layer of a payment (for example,
   * this may contain PSK Headers.)
   *
   * @return
   */
  D getEncodedData();
}
