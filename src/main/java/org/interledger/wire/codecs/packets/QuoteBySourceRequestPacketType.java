package org.interledger.wire.codecs.packets;

import org.interledger.wire.InterledgerPacketType;
import org.interledger.wire.InterledgerPacketType.AbstractInterledgerPacketType;

import java.net.URI;

/**
 * An implementation of {@link InterledgerPacketType} for ILQP quote requests.
 */
public class QuoteBySourceRequestPacketType extends AbstractInterledgerPacketType implements
    InterledgerPacketType {

  /**
   * No-args Constructor.
   */
  public QuoteBySourceRequestPacketType() {
    super(ILQP_QUOTE_BY_SOURCE_AMOUNT_REQUEST_TYPE,
        URI.create("https://interledger.org/ilqp/quote_by_source_amount_request"));
  }

}