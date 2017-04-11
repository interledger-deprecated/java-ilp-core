package org.interledger.ilp.packets;

import org.interledger.InterledgerPacketType;

/**
 * An implementation of {@link InterledgerPacketType} for ILP Payment packets.
 */
interface IlpPaymentPacketType extends InterledgerPacketType {

  default Integer getTypeIdentifier() {
    return ILP_PAYMENT_TYPE;
  }

  default String getTypeUri() {
    return "https://interledger.org/payment_packet";
  }

}