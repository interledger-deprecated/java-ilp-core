package org.interledger.codecs;

import org.interledger.codecs.packettypes.InterledgerPacketType;
import org.interledger.codecs.packettypes.QuoteBySourceRequestPacketType;
import org.interledger.ilqp.QuoteBySourceRequest;

/**
 * An implementation of {@link Codec} that reads and writes instances of {@link
 * QuoteBySourceRequest}.
 */
public interface QuoteBySourceRequestCodec extends InterledgerPacketCodec<QuoteBySourceRequest> {

  InterledgerPacketType TYPE = new QuoteBySourceRequestPacketType();

  @Override
  default InterledgerPacketType getTypeId() {
    return TYPE;
  }

}
