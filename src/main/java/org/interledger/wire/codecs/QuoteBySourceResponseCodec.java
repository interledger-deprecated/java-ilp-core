package org.interledger.wire.codecs;

import org.interledger.ilqp.QuoteBySourceResponse;
import org.interledger.wire.InterledgerPacketType;
import org.interledger.wire.codecs.packets.InterledgerPacketCodec;
import org.interledger.wire.codecs.packets.QuoteBySourceResponsePacketType;

/**
 * An implementation of {@link Codec} that reads and writes instances of {@link
 * QuoteBySourceResponse}.
 */
public interface QuoteBySourceResponseCodec extends InterledgerPacketCodec<QuoteBySourceResponse> {

  InterledgerPacketType TYPE = new QuoteBySourceResponsePacketType();

  @Override
  default InterledgerPacketType getTypeId() {
    return TYPE;
  }

}