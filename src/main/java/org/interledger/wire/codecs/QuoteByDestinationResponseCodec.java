package org.interledger.wire.codecs;

import org.interledger.ilqp.QuoteByDestinationResponse;
import org.interledger.wire.InterledgerPacketType;
import org.interledger.wire.codecs.packets.InterledgerPacketCodec;
import org.interledger.wire.codecs.packets.QuoteByDestinationResponsePacketType;

/**
 * An implementation of {@link Codec} that reads and writes instances of {@link
 * QuoteByDestinationResponse}.
 */
public interface QuoteByDestinationResponseCodec extends
    InterledgerPacketCodec<QuoteByDestinationResponse> {

  InterledgerPacketType TYPE = new QuoteByDestinationResponsePacketType();

  @Override
  default InterledgerPacketType getTypeId() {
    return TYPE;
  }

}
