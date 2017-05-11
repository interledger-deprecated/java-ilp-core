package org.interledger.wire.codecs;

import org.interledger.ilp.InterledgerPayment;
import org.interledger.wire.InterledgerPacketType;
import org.interledger.wire.codecs.packets.InterledgerPacketCodec;
import org.interledger.wire.codecs.packets.PaymentPacketType;

/**
 * An implementation of {@link Codec} that reads and writes instances of {@link InterledgerPayment}.
 */
public interface InterledgerPaymentCodec extends InterledgerPacketCodec<InterledgerPayment> {

  InterledgerPacketType TYPE = new PaymentPacketType();

  @Override
  default InterledgerPacketType getTypeId() {
    return TYPE;
  }
}
