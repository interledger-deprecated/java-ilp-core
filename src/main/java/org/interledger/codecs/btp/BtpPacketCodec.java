package org.interledger.codecs.btp;

import org.interledger.btp.BilateralTransferProtocolPacket;
import org.interledger.codecs.Codec;
import org.interledger.codecs.btp.packettypes.BtpPacketType;

/**
 * An implementation of {@link Codec} that reads and writes instances of
 * {@link BilateralTransferProtocolPacket} having a discrete {@link BtpPacketType} and data payload.
 */
public interface BtpPacketCodec<T extends BilateralTransferProtocolPacket> extends Codec<T> {

  /**
   * Accessor for the {@link BtpPacketType} of this {@link Codec}.
   */
  BtpPacketType getTypeId();

}
