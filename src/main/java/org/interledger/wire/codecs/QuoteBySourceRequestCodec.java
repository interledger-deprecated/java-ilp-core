package org.interledger.wire.codecs;

import org.interledger.ilqp.QuoteBySourceRequest;
import org.interledger.wire.InterledgerPacketType;
import org.interledger.wire.codecs.packets.InterledgerPacketCodec;
import org.interledger.wire.codecs.packets.QuoteBySourceRequestPacketType;

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
