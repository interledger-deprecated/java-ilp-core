package org.interledger.ilqp.packets;

import org.interledger.InterledgerPacketType;

/**
 * An implementation of {@link InterledgerPacketType} for ILQP Liquidity requests.
 */
public interface IlqpQuoteBySourceAmountResponsePacketType extends InterledgerPacketType {

  @Override
  default Integer getTypeIdentifier() {
    return ILQP_QUOTE_BY_SOURCE_AMOUNT_RESPONSE_TYPE;
  }

  @Override
  default String getTypeUri() {
    return "https://interledger.org/ilqp/quote_by_source_amount_response";
  }

}