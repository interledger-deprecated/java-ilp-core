package org.interledger.codecs.oer.ilqp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import org.interledger.codecs.Codec;
import org.interledger.codecs.CodecContext;
import org.interledger.codecs.QuoteByDestinationAmountResponseCodec;
import org.interledger.codecs.oer.OerUint32Codec.OerUint32;
import org.interledger.codecs.oer.OerUint64Codec.OerUint64;
import org.interledger.codecs.packettypes.InterledgerPacketType;
import org.interledger.ilqp.QuoteByDestinationAmountResponse;


/**
 * An implementation of {@link Codec} that reads and writes instances of
 * {@link QuoteByDestinationAmountResponse}. in OER format.
 * 
 * @see https://github.com/interledger/rfcs/blob/master/asn1/InterledgerQuotingProtocol.asn
 */
public class QuoteByDestinationAmountResponseOerCodec
    implements QuoteByDestinationAmountResponseCodec {

  @Override
  public QuoteByDestinationAmountResponse read(CodecContext context, InputStream inputStream)
      throws IOException {

    Objects.requireNonNull(context);
    Objects.requireNonNull(inputStream);

    /* read the source amount, which is a uint64 */
    /* NOTE: we don't expect amounts to exceed 2^63 - 1, so we risk the down-cast */
    // TODO: should we change this?
    long sourceAmount = context.read(OerUint64.class, inputStream).getValue().longValue();

    /* read the source hold duration which is a unit32 */
    long sourceHoldDuration = context.read(OerUint32.class, inputStream).getValue();

    return QuoteByDestinationAmountResponse.Builder.builder().sourceAmount(sourceAmount)
        .sourceHoldDuration(Duration.of(sourceHoldDuration, ChronoUnit.MILLIS)).build();
  }

  @Override
  public void write(CodecContext context, QuoteByDestinationAmountResponse instance,
      OutputStream outputStream) throws IOException {

    Objects.requireNonNull(context);
    Objects.requireNonNull(instance);
    Objects.requireNonNull(outputStream);

    /* Write the packet type. */
    context.write(InterledgerPacketType.class, this.getTypeId(), outputStream);

    /* source amount */
    context.write(OerUint64.class, new OerUint64(instance.getSourceAmount()), outputStream);

    /* source hold duration */
    context.write(OerUint32.class, new OerUint32(instance.getSourceHoldDuration().toMillis()),
        outputStream);
  }
}
