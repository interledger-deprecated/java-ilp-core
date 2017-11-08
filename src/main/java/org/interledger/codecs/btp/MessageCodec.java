package org.interledger.codecs.btp;

import org.interledger.btp.MessagePacket;
import org.interledger.codecs.Codec;
import org.interledger.codecs.btp.packettypes.BtpPacketType;
import org.interledger.codecs.btp.packettypes.MessagePacketType;

/**
 * An implementation of {@link Codec} that reads and writes instances of {@link MessagePacket}.
 */
public interface MessageCodec extends
    BtpPacketCodec<MessagePacket> {

  BtpPacketType TYPE = new MessagePacketType();

  @Override
  default BtpPacketType getTypeId() {
    return TYPE;
  }
}

