package org.interledger.codecs.btp;

import org.interledger.btp.BilateralTransferProtocolReject;
import org.interledger.codecs.Codec;
import org.interledger.codecs.btp.packettypes.BilateralTransferProtocolPacketType;
import org.interledger.codecs.btp.packettypes.RejectPacketType;

/**
 * An implementation of {@link Codec} that reads and writes instances of
 * {@link BilateralTransferProtocolReject}.
 */
public interface BilateralTransferProtocolRejectCodec extends
    BilateralTransferProtocolPacketCodec<BilateralTransferProtocolReject> {

  BilateralTransferProtocolPacketType TYPE = new RejectPacketType();

  @Override
  default BilateralTransferProtocolPacketType getTypeId() {
    return TYPE;
  }
}

