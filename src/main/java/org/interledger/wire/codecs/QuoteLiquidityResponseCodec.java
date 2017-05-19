package org.interledger.wire.codecs;

import org.interledger.ilqp.QuoteLiquidityResponse;
import org.interledger.wire.InterledgerPacketType;
import org.interledger.wire.codecs.packets.InterledgerPacketCodec;
import org.interledger.wire.codecs.packets.QuoteLiquidityResponsePacketType;

/**
 * An implementation of {@link Codec} that reads and writes instances of {@link
 * QuoteLiquidityResponse}.
 */
public interface QuoteLiquidityResponseCodec extends
    InterledgerPacketCodec<QuoteLiquidityResponse> {

  InterledgerPacketType TYPE = new QuoteLiquidityResponsePacketType();

  @Override
  default InterledgerPacketType getTypeId() {
    return TYPE;
  }

}