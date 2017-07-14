package org.interledger.codecs;

import org.interledger.codecs.packettypes.InterledgerPacketType;
import org.interledger.codecs.packettypes.QuoteByDestinationResponsePacketType;
import org.interledger.ilqp.QuoteByDestinationResponse;

/**
 * An implementation of {@link Codec} that reads and writes instances of
 * {@link QuoteByDestinationResponse}.
 */
public interface QuoteByDestinationResponseCodec
    extends InterledgerPacketCodec<QuoteByDestinationResponse> {

  InterledgerPacketType TYPE = new QuoteByDestinationResponsePacketType();

  @Override
  default InterledgerPacketType getTypeId() {
    return TYPE;
  }

}
