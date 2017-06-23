package org.interledger.ipr;

import org.interledger.Condition;
import org.interledger.ilp.InterledgerPayment;

/**
 * An Interledger Payment Request as defined in ILP RFC 11.
 *
 * @see "https://github.com/interledger/rfcs/blob/master/0011-interledger-payment-request/0011
 *      -interledger-payment-request.md"
 */

public class InterledgerPaymentRequest {

  private static int VERSION = 2;

  private InterledgerPayment packet;
  private Condition condition;

  public InterledgerPaymentRequest(InterledgerPayment packet, Condition condition) {
    this.packet = packet;
    this.condition = condition;
  }

  public int getVersion() {
    return VERSION;
  }

  public InterledgerPayment getPacket() {
    return packet;
  }

  public Condition getCondition() {
    return condition;
  }

  public void setPacket(InterledgerPayment packet) {
    this.packet = packet;
  }

  public void setCondition(Condition condition) {
    this.condition = condition;
  }

}

