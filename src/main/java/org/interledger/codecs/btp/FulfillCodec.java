package org.interledger.codecs.btp;

import org.interledger.btp.FulfillPacket;
import org.interledger.codecs.Codec;
import org.interledger.codecs.btp.packettypes.BtpPacketType;
import org.interledger.codecs.btp.packettypes.FulfillPacketType;

/**
 * An implementation of {@link Codec} that reads and writes instances of
 * {@link FulfillPacket}.
 */
public interface FulfillCodec extends
    BtpPacketCodec<FulfillPacket> {

  BtpPacketType TYPE = new FulfillPacketType();

  @Override
  default BtpPacketType getTypeId() {
    return TYPE;
  }
}

