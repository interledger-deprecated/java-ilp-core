package org.interledger.ilqp.packets;

import org.interledger.InterledgerPacketType;

/**
 * An implementation of {@link InterledgerPacketType} for ILQP Liquidity responses.
 */
public interface IlqpQuoteBySourceAmountRequestPacketType extends InterledgerPacketType {

  @Override
  default Integer getTypeIdentifier() {
    return ILQP_QUOTE_BY_SOURCE_AMOUNT_REQUEST_TYPE;
  }

  @Override
  default String getTypeUri() {
    return "https://interledger.org/ilqp/quote_by_source_amount_request";
  }

}