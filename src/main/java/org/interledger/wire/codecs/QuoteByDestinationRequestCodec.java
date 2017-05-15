package org.interledger.wire.codecs;

import org.interledger.ilqp.QuoteByDestinationRequest;
import org.interledger.wire.InterledgerPacketType;
import org.interledger.wire.codecs.packets.InterledgerPacketCodec;
import org.interledger.wire.codecs.packets.QuoteByDestinationRequestPacketType;

/**
 * An implementation of {@link Codec} that reads and writes instances of {@link
 * QuoteByDestinationRequest}.
 */
public interface QuoteByDestinationRequestCodec
    extends InterledgerPacketCodec<QuoteByDestinationRequest> {

  InterledgerPacketType TYPE = new QuoteByDestinationRequestPacketType();

  @Override
  default InterledgerPacketType getTypeId() {
    return TYPE;
  }

}
