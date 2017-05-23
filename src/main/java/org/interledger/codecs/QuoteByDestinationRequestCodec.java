package org.interledger.codecs;

import org.interledger.codecs.packettypes.InterledgerPacketType;
import org.interledger.codecs.packettypes.QuoteByDestinationRequestPacketType;
import org.interledger.ilqp.QuoteByDestinationRequest;

/**
 * An implementation of {@link Codec} that reads and writes instances of {@link
 * QuoteByDestinationRequest}.
 */
public interface QuoteByDestinationRequestCodec extends
    InterledgerPacketCodec<QuoteByDestinationRequest> {

  InterledgerPacketType TYPE = new QuoteByDestinationRequestPacketType();

  @Override
  default InterledgerPacketType getTypeId() {
    return TYPE;
  }

}
