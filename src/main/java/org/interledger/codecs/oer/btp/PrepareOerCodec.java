package org.interledger.codecs.oer.btp;

import org.interledger.btp.ImmutablePreparePacket;
import org.interledger.btp.PreparePacket;
import org.interledger.btp.SubProtocolData;
import org.interledger.codecs.Codec;
import org.interledger.codecs.CodecContext;
import org.interledger.codecs.btp.PrepareCodec;
import org.interledger.codecs.btp.packettypes.BtpPacketType;
import org.interledger.codecs.oer.OerGeneralizedTimeCodec.OerGeneralizedTime;
import org.interledger.codecs.oer.OerSequenceOfSubProtocolDataCodec.OerSequenceOfSubProtocolData;
import org.interledger.codecs.oer.OerUint128Codec.OerUint128;
import org.interledger.codecs.oer.OerUint256Codec.OerUint256;
import org.interledger.codecs.oer.OerUint32Codec.OerUint32;
import org.interledger.codecs.oer.OerUint64Codec.OerUint64;
import org.interledger.cryptoconditions.Condition;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>An implementation of {@link Codec} that reads and writes instances of {@link
 * PreparePacket}.</p>
 *
 * @see "http://www.oss.com/asn1/resources/books-whitepapers-pubs/Overview%20of%20OER.pdf"
 */
public class PrepareOerCodec implements
    PrepareCodec {

  @Override
  public PreparePacket read(final CodecContext context, final InputStream inputStream)
      throws IOException {
    Objects.requireNonNull(context);
    Objects.requireNonNull(inputStream);

    final long requestId = context.read(OerUint32.class, inputStream).getValue();

    final UUID transferId = context.read(OerUint128.class, inputStream).getUuid();

    final BigInteger amount = context.read(OerUint64.class, inputStream).getValue();

    final Condition condition = context.read(Condition.class, inputStream);

    final Instant expiresAt = context.read(OerGeneralizedTime.class, inputStream).getValue();

    final List<SubProtocolData> data = context.read(OerSequenceOfSubProtocolData.class, inputStream)
        .getSubProtocols();

    return ImmutablePreparePacket.builder()
        .requestId(requestId)
        .transferId(transferId)
        .amount(amount)
        .executionCondition(condition)
        .expiresAt(expiresAt)
        .subProtocolData(data)
        .build();
  }

  @Override
  public void write(final CodecContext context, final PreparePacket instance,
      final OutputStream outputStream) throws IOException {
    Objects.requireNonNull(context);
    Objects.requireNonNull(instance);
    Objects.requireNonNull(outputStream);

    // 1. Write the packet type.
    context.write(BtpPacketType.class, this.getTypeId(), outputStream);

    // 2. Write the requestId portion of the packet.
    context.write(OerUint32.class, new OerUint32(instance.getRequestId()), outputStream);

    // 3. Write the transferId portion of the packet.
    context.write(OerUint128.class, new OerUint128(instance.getTransferId()), outputStream);

    // 4. Write the amount portion of the packet.
    context.write(OerUint64.class, new OerUint64(instance.getAmount()), outputStream);

    // 5. Write the condition portion of the packet.
    context.write(Condition.class, instance.getExecutionCondition(), outputStream);

    // 4. Write the expiresAt portion of the packet.
    context.write(OerGeneralizedTime.class,
        new OerGeneralizedTime(instance.getExpiresAt()), outputStream);

    // 5. Write the protocolData portion of the packet.
    context.write(OerSequenceOfSubProtocolData.class,
        new OerSequenceOfSubProtocolData(instance.getSubProtocolData()), outputStream);
  }


}
