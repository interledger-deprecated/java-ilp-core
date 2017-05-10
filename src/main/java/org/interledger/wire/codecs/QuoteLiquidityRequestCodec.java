package org.interledger.wire.codecs;

import org.interledger.ilqp.QuoteLiquidityRequest;
import org.interledger.wire.InterledgerPacketType;
import org.interledger.wire.codecs.packets.InterledgerPacketCodec;
import org.interledger.wire.codecs.packets.QuoteLiquidityRequestPacketType;

/**
 * An implementation of {@link Codec} that reads and writes instances of {@link
 * QuoteLiquidityRequest}.
 */
public interface QuoteLiquidityRequestCodec extends InterledgerPacketCodec<QuoteLiquidityRequest> {

  InterledgerPacketType TYPE = new QuoteLiquidityRequestPacketType();

  @Override
  default InterledgerPacketType getTypeId() {
    return TYPE;
  }
}