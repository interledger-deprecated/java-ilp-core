package org.interledger.codecs.btp;

import org.interledger.btp.BilateralTransferProtocolPrepare;
import org.interledger.codecs.Codec;
import org.interledger.codecs.btp.packettypes.BilateralTransferProtocolPacketType;
import org.interledger.codecs.btp.packettypes.PreparePacketType;

/**
 * An implementation of {@link Codec} that reads and writes instances of
 * {@link BilateralTransferProtocolPrepare}.
 */
public interface BilateralTransferProtocolPrepareCodec extends
    BilateralTransferProtocolPacketCodec<BilateralTransferProtocolPrepare> {

  BilateralTransferProtocolPacketType TYPE = new PreparePacketType();

  @Override
  default BilateralTransferProtocolPacketType getTypeId() {
    return TYPE;
  }
}

