package org.interledger.codecs.oer.btp;

import org.interledger.btp.BtpErrorType;
import org.interledger.btp.ErrorPacket;
import org.interledger.btp.ImmutableErrorPacket;
import org.interledger.btp.SubProtocolData;
import org.interledger.codecs.Codec;
import org.interledger.codecs.CodecContext;
import org.interledger.codecs.btp.ErrorCodec;
import org.interledger.codecs.btp.packettypes.BtpPacketType;
import org.interledger.codecs.oer.OerGeneralizedTimeCodec.OerGeneralizedTime;
import org.interledger.codecs.oer.OerOctetStringCodec.OerOctetString;
import org.interledger.codecs.oer.OerSequenceOfSubProtocolDataCodec.OerSequenceOfSubProtocolData;
import org.interledger.codecs.oer.OerUint32Codec.OerUint32;
import org.interledger.codecs.oer.btp.BtpErrorTypeOerCodec.OerBilateralTransferProtocolErrorTypes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * <p>An implementation of {@link Codec} that reads and writes instances of {@link
 * ErrorPacket}.</p>
 *
 * @see "http://www.oss.com/asn1/resources/books-whitepapers-pubs/Overview%20of%20OER.pdf"
 */
public class ErrorOerCodec implements ErrorCodec {

  @Override
  public ErrorPacket read(final CodecContext context,
      final InputStream inputStream)
      throws IOException {
    Objects.requireNonNull(context);
    Objects.requireNonNull(inputStream);

    final long requestId = context.read(OerUint32.class, inputStream)
        .getValue();

    final BtpErrorType error = context.read(
        OerBilateralTransferProtocolErrorTypes.class, inputStream)
        .getValue();

    final Instant triggeredAt = context.read(OerGeneralizedTime.class, inputStream)
        .getValue();

    final byte[] data = context.read(OerOctetString.class, inputStream)
        .getValue();

    final List<SubProtocolData> subProtocolData = context.read(OerSequenceOfSubProtocolData.class,
        inputStream)
        .getSubProtocols();

    return ImmutableErrorPacket.builder()
        .requestId(requestId)
        .error(error)
        .triggeredAt(triggeredAt)
        .data(data)
        .subProtocolData(subProtocolData)
        .build();
  }

  @Override
  public void write(final CodecContext context, final ErrorPacket instance,
      final OutputStream outputStream) throws IOException {
    Objects.requireNonNull(context);
    Objects.requireNonNull(instance);
    Objects.requireNonNull(outputStream);

    // 1. Write the packet type.
    context.write(BtpPacketType.class, this.getTypeId(), outputStream);

    // 2. Write the requestId portion of the packet.
    context.write(OerUint32.class, new OerUint32(instance.getRequestId()), outputStream);

    // 3. Write the error code and name portion of the packet.
    context.write(OerBilateralTransferProtocolErrorTypes.class,
        new OerBilateralTransferProtocolErrorTypes(instance.getError()), outputStream);

    // 4. Write the triggeredAt portion of the packet.
    context.write(OerGeneralizedTime.class, new OerGeneralizedTime(instance.getTriggeredAt()),
        outputStream);

    // 5. Write the data portion of the packet.
    context.write(OerOctetString.class, new OerOctetString(instance.getData()), outputStream);

    // 3. Write the protocolData portion of the packet.
    context.write(OerSequenceOfSubProtocolData.class,
        new OerSequenceOfSubProtocolData(instance.getSubProtocolData()), outputStream);
  }


}
