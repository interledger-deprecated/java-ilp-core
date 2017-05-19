package org.interledger.wire.codecs.packets;

import org.interledger.wire.InterledgerPacketType;
import org.interledger.wire.InterledgerPacketType.AbstractInterledgerPacketType;

import java.net.URI;

/**
 * An implementation of {@link InterledgerPacketType} for ILQP Liquidity responses.
 */
public class QuoteByDestinationRequestPacketType extends
    AbstractInterledgerPacketType implements InterledgerPacketType {

  /**
   * No-args Constructor.
   */
  public QuoteByDestinationRequestPacketType() {
    super(ILQP_QUOTE_BY_DESTINATION_AMOUNT_REQUEST_TYPE,
        URI.create("https://interledger.org/ilqp/quote_by_destination_amount_request"));
  }

}