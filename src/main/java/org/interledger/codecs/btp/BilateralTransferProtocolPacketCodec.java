package org.interledger.codecs.btp;

import org.interledger.InterledgerPacket;
import org.interledger.btp.BilateralTransferProtocolPacket;
import org.interledger.codecs.Codec;
import org.interledger.codecs.btp.packettypes.BilateralTransferProtocolPacketType;
import org.interledger.codecs.packettypes.InterledgerPacketType;

/**
 * An implementation of {@link Codec} that reads and writes instances of {@link BilateralTransferProtocolPacket}
 * having a discrete {@link BilateralTransferProtocolPacketType} and data payload.
 */
public interface BilateralTransferProtocolPacketCodec<T extends BilateralTransferProtocolPacket> extends Codec<T> {

  /**
   * Accessor for the {@link BilateralTransferProtocolPacketType} of this {@link Codec}.
   */
  BilateralTransferProtocolPacketType getTypeId();

}
