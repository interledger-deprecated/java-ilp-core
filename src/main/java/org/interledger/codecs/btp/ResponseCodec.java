package org.interledger.codecs.btp;

import org.interledger.btp.ResponsePacket;
import org.interledger.codecs.Codec;
import org.interledger.codecs.btp.packettypes.BtpPacketType;
import org.interledger.codecs.btp.packettypes.ResponsePacketType;

/**
 * An implementation of {@link Codec} that reads and writes instances of {@link ResponsePacket}.
 */
public interface ResponseCodec extends
    BtpPacketCodec<ResponsePacket> {

  BtpPacketType TYPE = new ResponsePacketType();

  @Override
  default BtpPacketType getTypeId() {
    return TYPE;
  }
}

