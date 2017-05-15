package org.interledger.wire.codecs.packets;

import org.interledger.wire.InterledgerPacketType;
import org.interledger.wire.InterledgerPacketType.AbstractInterledgerPacketType;

import java.net.URI;

/**
 * An implementation of {@link InterledgerPacketType} for ILQP Liquidity requests.
 */
public class QuoteLiquidityRequestPacketType extends AbstractInterledgerPacketType implements
    InterledgerPacketType {

  /**
   * No-args Constructor.
   */
  public QuoteLiquidityRequestPacketType() {
    super(ILQP_QUOTE_LIQUIDITY_REQUEST_TYPE,
        URI.create("https://interledger.org/ilqp/quote_liquidity_request"));
  }
}