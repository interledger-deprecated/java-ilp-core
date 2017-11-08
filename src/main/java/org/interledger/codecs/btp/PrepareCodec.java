package org.interledger.codecs.btp;

import org.interledger.btp.PreparePacket;
import org.interledger.codecs.Codec;
import org.interledger.codecs.btp.packettypes.BtpPacketType;
import org.interledger.codecs.btp.packettypes.PreparePacketType;

/**
 * An implementation of {@link Codec} that reads and writes instances of
 * {@link PreparePacket}.
 */
public interface PrepareCodec extends
    BtpPacketCodec<PreparePacket> {

  BtpPacketType TYPE = new PreparePacketType();

  @Override
  default BtpPacketType getTypeId() {
    return TYPE;
  }
}

