package org.interledger.ilqp.packets;

import org.interledger.InterledgerPacketType;

/**
 * An implementation of {@link InterledgerPacketType} for ILQP Liquidity responses.
 */
public interface IlqpQuoteByDestinationAmountResponsePacketType extends InterledgerPacketType {

  @Override
  default Integer getTypeIdentifier() {
    return ILQP_QUOTE_BY_DESTINATION_AMOUNT_RESPONSE_TYPE;
  }

  @Override
  default String getTypeUri() {
    return "https://interledger.org/ilqp/quote_by_destination_amount_response";
  }

}