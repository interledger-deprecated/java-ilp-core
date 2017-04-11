package org.interledger.ilqp.packets;

import org.interledger.InterledgerPacket;
import org.interledger.InterledgerPacketType;

/**
 * <p>Interledger Packets are pieces of data attached to Interledger transfers to facilitate
 * Interledger payments.</p>
 *
 * <p>Interledger Quoting Protocol Packets are used to send and receive quoting information for
 * potential Interledger payments.
 */
public interface InterledgerQuotingProtocolPacket<T extends InterledgerPacketType, D>
    extends InterledgerPacket<T, D> {

  // TODO: Finish this...
}
