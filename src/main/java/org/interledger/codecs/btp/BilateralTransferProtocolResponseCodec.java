package org.interledger.codecs.btp;

import org.interledger.btp.BilateralTransferProtocolResponse;
import org.interledger.codecs.Codec;
import org.interledger.codecs.btp.packettypes.BilateralTransferProtocolPacketType;
import org.interledger.codecs.btp.packettypes.ResponsePacketType;

/**
 * An implementation of {@link Codec} that reads and writes instances of {@link BilateralTransferProtocolResponse}.
 */
public interface BilateralTransferProtocolResponseCodec extends
    BilateralTransferProtocolPacketCodec<BilateralTransferProtocolResponse> {

  BilateralTransferProtocolPacketType TYPE = new ResponsePacketType();

  @Override
  default BilateralTransferProtocolPacketType getTypeId() {
    return TYPE;
  }
}

