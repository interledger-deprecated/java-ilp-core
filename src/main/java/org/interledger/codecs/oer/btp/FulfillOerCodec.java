package org.interledger.codecs.oer.btp;

import org.interledger.btp.FulfillPacket;
import org.interledger.btp.ImmutableFulfillPacket;
import org.interledger.btp.ResponsePacket;
import org.interledger.btp.SubProtocolData;
import org.interledger.codecs.Codec;
import org.interledger.codecs.CodecContext;
import org.interledger.codecs.btp.FulfillCodec;
import org.interledger.codecs.btp.packettypes.BtpPacketType;
import org.interledger.codecs.oer.OerSequenceOfSubProtocolDataCodec.OerSequenceOfSubProtocolData;
import org.interledger.codecs.oer.OerUint128Codec.OerUint128;
import org.interledger.codecs.oer.OerUint32Codec.OerUint32;
import org.interledger.cryptoconditions.Fulfillment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>An implementation of {@link Codec} that reads and writes instances of {@link
 * ResponsePacket}.</p>
 *
 * @see "http://www.oss.com/asn1/resources/books-whitepapers-pubs/Overview%20of%20OER.pdf"
 */
public class FulfillOerCodec implements
    FulfillCodec {

  @Override
  public FulfillPacket read(final CodecContext context, final InputStream inputStream)
      throws IOException {
    Objects.requireNonNull(context);
    Objects.requireNonNull(inputStream);

    final long requestId = context.read(OerUint32.class, inputStream).getValue();

    final UUID transferId = context.read(OerUint128.class, inputStream).getUuid();

    final Fulfillment fulfillment = context.read(Fulfillment.class, inputStream);

    final List<SubProtocolData> data = context.read(OerSequenceOfSubProtocolData.class, inputStream)
        .getSubProtocols();

    return ImmutableFulfillPacket.builder()
        .requestId(requestId)
        .transferId(transferId)
        .fulfillment(fulfillment)
        .subProtocolData(data)
        .build();
  }

  @Override
  public void write(final CodecContext context, final FulfillPacket instance,
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

    // 4. Write the fulfillment portion of the packet.
    context.write(Fulfillment.class, instance.getFulfillment(), outputStream);

    // 5. Write the protocolData portion of the packet.
    context.write(OerSequenceOfSubProtocolData.class,
        new OerSequenceOfSubProtocolData(instance.getSubProtocolData()), outputStream);
  }


}
