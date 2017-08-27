package org.interledger.codecs.packettypes;

import java.net.URI;

import org.interledger.codecs.packettypes.InterledgerPacketType.AbstractInterledgerPacketType;

/**
 * An implementation of {@link InterledgerPacketType} for ILP Payment packets.
 */
public class PaymentPacketType extends AbstractInterledgerPacketType
    implements InterledgerPacketType {

  /**
   * No-args Constructor.
   */
  public PaymentPacketType() {
    super(ILP_PAYMENT_TYPE, URI.create("https://interledger.org/payment_packet"));
  }
}
