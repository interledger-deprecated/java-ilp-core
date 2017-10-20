package org.interledger.codecs.oer.btp;

import org.interledger.btp.ImmutableResponsePacket;
import org.interledger.btp.ResponsePacket;
import org.interledger.btp.SubProtocolData;
import org.interledger.codecs.Codec;
import org.interledger.codecs.CodecContext;
import org.interledger.codecs.btp.ResponseCodec;
import org.interledger.codecs.btp.packettypes.BtpPacketType;
import org.interledger.codecs.oer.OerSequenceOfSubProtocolDataCodec.OerSequenceOfSubProtocolData;
import org.interledger.codecs.oer.OerUint32Codec.OerUint32;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

/**
 * <p>An implementation of {@link Codec} that reads and writes instances of {@link
 * ResponsePacket}.</p>
 *
 * @see "http://www.oss.com/asn1/resources/books-whitepapers-pubs/Overview%20of%20OER.pdf"
 */
public class ResponseOerCodec implements
    ResponseCodec {

  @Override
  public ResponsePacket read(final CodecContext context, final InputStream inputStream)
      throws IOException {
    Objects.requireNonNull(context);
    Objects.requireNonNull(inputStream);

    final long requestId = context.read(OerUint32.class, inputStream).getValue();

    final List<SubProtocolData> data = context.read(OerSequenceOfSubProtocolData.class, inputStream)
        .getSubProtocols();

    return ImmutableResponsePacket.builder()
        .requestId(requestId)
        .subProtocolData(data)
        .build();
  }

  @Override
  public void write(final CodecContext context, final ResponsePacket instance,
      final OutputStream outputStream) throws IOException {
    Objects.requireNonNull(context);
    Objects.requireNonNull(instance);
    Objects.requireNonNull(outputStream);

    // 1. Write the packet type.
    context.write(BtpPacketType.class, this.getTypeId(), outputStream);

    // 2. Write the requestId portion of the packet.
    context.write(OerUint32.class, new OerUint32(instance.getRequestId()), outputStream);

    // 3. Write the protocolData portion of the packet.
    context.write(OerSequenceOfSubProtocolData.class, new OerSequenceOfSubProtocolData(
        instance.getSubProtocolData()), outputStream);
  }


}
