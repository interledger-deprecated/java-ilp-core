package org.interledger.codecs.btp;

import org.interledger.btp.BilateralTransferProtocolError;
import org.interledger.codecs.Codec;
import org.interledger.codecs.btp.packettypes.BilateralTransferProtocolPacketType;
import org.interledger.codecs.btp.packettypes.ErrorPacketType;

/**
 * An implementation of {@link Codec} that reads and writes instances of {@link
 * BilateralTransferProtocolError}.
 */
public interface BilateralTransferProtocolErrorCodec extends
    BilateralTransferProtocolPacketCodec<BilateralTransferProtocolError> {

  BilateralTransferProtocolPacketType TYPE = new ErrorPacketType();

  @Override
  default BilateralTransferProtocolPacketType getTypeId() {
    return TYPE;
  }
}

