package org.interledger.ilqp.packets;

import org.interledger.InterledgerPacketType;

/**
 * An implementation of {@link InterledgerPacketType} for ILQP Liquidity requests.
 */
public interface IlqpQuoteLiquidityRequestPacketType extends InterledgerPacketType {

  default Integer getTypeIdentifier() {
    return ILQP_QUOTE_LIQUIDITY_REQUEST_TYPE;
  }

  default String getTypeUri() {
    return "https://interledger.org/ilqp/quote_liquidity_request";
  }

}