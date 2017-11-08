package org.interledger.codecs.btp;

import org.interledger.btp.RejectPacket;
import org.interledger.codecs.Codec;
import org.interledger.codecs.btp.packettypes.BtpPacketType;
import org.interledger.codecs.btp.packettypes.RejectPacketType;

/**
 * An implementation of {@link Codec} that reads and writes instances of
 * {@link RejectPacket}.
 */
public interface RejectCodec extends
    BtpPacketCodec<RejectPacket> {

  BtpPacketType TYPE = new RejectPacketType();

  @Override
  default BtpPacketType getTypeId() {
    return TYPE;
  }
}

