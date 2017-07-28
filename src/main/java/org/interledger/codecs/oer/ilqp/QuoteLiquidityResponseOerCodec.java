package org.interledger.codecs.oer.ilqp;

import org.interledger.InterledgerAddress;
import org.interledger.codecs.Codec;
import org.interledger.codecs.CodecContext;
import org.interledger.codecs.QuoteLiquidityResponseCodec;
import org.interledger.codecs.oer.OerGeneralizedTimeCodec.OerGeneralizedTime;
import org.interledger.codecs.oer.OerUint32Codec.OerUint32;
import org.interledger.codecs.packettypes.InterledgerPacketType;
import org.interledger.ilqp.LiquidityCurve;
import org.interledger.ilqp.QuoteLiquidityResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * An implementation of {@link Codec} that reads and writes instances of
 * {@link QuoteLiquidityResponse}. in OER format.
 * 
 * @see https://github.com/interledger/rfcs/blob/master/asn1/InterledgerQuotingProtocol.asn
 */
public class QuoteLiquidityResponseOerCodec implements QuoteLiquidityResponseCodec {

  @Override
  public QuoteLiquidityResponse read(CodecContext context, InputStream inputStream)
      throws IOException {

    Objects.requireNonNull(context);
    Objects.requireNonNull(inputStream);

    /* read the Liquidity curve */
    final LiquidityCurve liquidityCurve = context.read(LiquidityCurve.class, inputStream);
    
    /* read the applies-to Address. */
    final InterledgerAddress appliesTo = context.read(InterledgerAddress.class, inputStream);

    /* read the source hold duration which is a unit32 */
    long sourceHoldDuration = context.read(OerUint32.class, inputStream).getValue();

    /* read the expires-at timestamp */
    ZonedDateTime expiresAt = context.read(OerGeneralizedTime.class, inputStream).getValue();
    
    return QuoteLiquidityResponse.Builder.builder()
        .liquidityCurve(liquidityCurve)
        .appliesTo(appliesTo)
        .sourceHoldDuration(Duration.of(sourceHoldDuration, ChronoUnit.MILLIS))
        .expiresAt(expiresAt)
        .build();
  }

  @Override
  public void write(CodecContext context, QuoteLiquidityResponse instance,
      OutputStream outputStream) throws IOException {
  
    Objects.requireNonNull(context);
    Objects.requireNonNull(instance);
    Objects.requireNonNull(outputStream);

    /* write the packet type. */
    context.write(InterledgerPacketType.class, this.getTypeId(), outputStream);

    /* liquidity curve */
    context.write(LiquidityCurve.class, instance.getLiquidityCurve(), outputStream);
    
    /* applies to prefix */
    context.write(InterledgerAddress.class, instance.getAppliesToPrefix(), outputStream);
    
    /* source hold duration, in milliseconds */
    context.write(OerUint32.class, new OerUint32(instance.getSourceHoldDuration().toMillis()),
        outputStream);
    
    /* expires at */
    context.write(OerGeneralizedTime.class, new OerGeneralizedTime(instance.getExpiresAt()),
        outputStream);
  }
}
