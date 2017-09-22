package org.interledger.codecs.btp;

import org.interledger.btp.BilateralTransferProtocolMessage;
import org.interledger.codecs.Codec;
import org.interledger.codecs.btp.packettypes.BilateralTransferProtocolPacketType;
import org.interledger.codecs.btp.packettypes.MessagePacketType;

/**
 * An implementation of {@link Codec} that reads and writes instances of {@link BilateralTransferProtocolMessage}.
 */
public interface BilateralTransferProtocolMessageCodec extends
    BilateralTransferProtocolPacketCodec<BilateralTransferProtocolMessage> {

  BilateralTransferProtocolPacketType TYPE = new MessagePacketType();

  @Override
  default BilateralTransferProtocolPacketType getTypeId() {
    return TYPE;
  }
}

