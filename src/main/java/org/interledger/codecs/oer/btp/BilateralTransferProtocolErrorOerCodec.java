package org.interledger.codecs.oer.btp;

import org.interledger.btp.BilateralTransferProtocolError;
import org.interledger.btp.BilateralTransferProtocolErrorTypes;
import org.interledger.btp.SubProtocolData;
import org.interledger.codecs.Codec;
import org.interledger.codecs.CodecContext;
import org.interledger.codecs.btp.BilateralTransferProtocolErrorCodec;
import org.interledger.codecs.btp.packettypes.BilateralTransferProtocolPacketType;
import org.interledger.codecs.oer.OerGeneralizedTimeCodec.OerGeneralizedTime;
import org.interledger.codecs.oer.OerOctetStringCodec.OerOctetString;
import org.interledger.codecs.oer.OerSequenceOfSubProtocolDataCodec.OerSequenceOfSubProtocolData;
import org.interledger.codecs.oer.OerUint32Codec.OerUint32;
import org.interledger.codecs.oer.btp.BilateralTransferProtocolErrorTypeOerCodec.OerBilateralTransferProtocolErrorTypes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>An implementation of {@link Codec} that reads and writes instances of {@link
 * BilateralTransferProtocolError}.</p>
 *
 * @see "http://www.oss.com/asn1/resources/books-whitepapers-pubs/Overview%20of%20OER.pdf"
 */
public class BilateralTransferProtocolErrorOerCodec implements BilateralTransferProtocolErrorCodec {

  @Override
  public BilateralTransferProtocolError read(final CodecContext context,
      final InputStream inputStream)
      throws IOException {
    Objects.requireNonNull(context);
    Objects.requireNonNull(inputStream);

    final long requestId = context.read(OerUint32.class, inputStream)
        .getValue();

    final BilateralTransferProtocolErrorTypes error = context.read(
        OerBilateralTransferProtocolErrorTypes.class, inputStream)
        .getValue();

    final ZonedDateTime triggeredAt = context.read(OerGeneralizedTime.class, inputStream)
        .getValue();

    final byte[] data = context.read(OerOctetString.class, inputStream)
        .getValue();

    final List<SubProtocolData> subProtocolData = context.read(OerSequenceOfSubProtocolData.class,
        inputStream)
        .getSubProtocols();

    return BilateralTransferProtocolError.builder()
        .requestId(requestId)
        .errorType(error)
        .triggeredAt(triggeredAt)
        .data(data)
        .setSubProtocolData(subProtocolData)
        .build();
  }

  @Override
  public void write(final CodecContext context, final BilateralTransferProtocolError instance,
      final OutputStream outputStream) throws IOException {
    Objects.requireNonNull(context);
    Objects.requireNonNull(instance);
    Objects.requireNonNull(outputStream);

    // 1. Write the packet type.
    context.write(BilateralTransferProtocolPacketType.class, this.getTypeId(), outputStream);

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
