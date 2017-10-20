package org.interledger.codecs.btp;

import org.interledger.btp.ErrorPacket;
import org.interledger.codecs.Codec;
import org.interledger.codecs.btp.packettypes.BtpPacketType;
import org.interledger.codecs.btp.packettypes.ErrorPacketType;

/**
 * An implementation of {@link Codec} that reads and writes instances of {@link
 * ErrorPacket}.
 */
public interface ErrorCodec extends
    BtpPacketCodec<ErrorPacket> {

  BtpPacketType TYPE = new ErrorPacketType();

  @Override
  default BtpPacketType getTypeId() {
    return TYPE;
  }
}

