package org.interledger.codecs;

import org.interledger.codecs.packettypes.InterledgerPacketType;
import org.interledger.codecs.packettypes.QuoteBySourceResponsePacketType;
import org.interledger.ilqp.QuoteBySourceResponse;

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