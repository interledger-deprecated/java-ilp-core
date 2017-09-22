package org.interledger.codecs.btp;

import org.interledger.btp.BilateralTransferProtocolFulfill;
import org.interledger.codecs.Codec;
import org.interledger.codecs.btp.packettypes.BilateralTransferProtocolPacketType;
import org.interledger.codecs.btp.packettypes.FulfillPacketType;

/**
 * An implementation of {@link Codec} that reads and writes instances of
 * {@link BilateralTransferProtocolFulfill}.
 */
public interface BilateralTransferProtocolFulfillCodec extends
    BilateralTransferProtocolPacketCodec<BilateralTransferProtocolFulfill> {

  BilateralTransferProtocolPacketType TYPE = new FulfillPacketType();

  @Override
  default BilateralTransferProtocolPacketType getTypeId() {
    return TYPE;
  }
}

