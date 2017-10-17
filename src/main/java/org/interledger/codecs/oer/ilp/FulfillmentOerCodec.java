package org.interledger.codecs.oer.ilp;

import org.interledger.codecs.Codec;
import org.interledger.codecs.CodecContext;
import org.interledger.codecs.FulfillmentCodec;
import org.interledger.codecs.oer.OerUint256Codec.OerUint256;
import org.interledger.cryptoconditions.Fulfillment;
import org.interledger.cryptoconditions.InterledgerSha256Fulfillment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * An implementation of {@link Codec} that reads and writes instances of {@link Fulfillment}.
 */
public class FulfillmentOerCodec implements FulfillmentCodec {

  @Override
  public Fulfillment read(final CodecContext context, final InputStream inputStream)
      throws IOException {
    Objects.requireNonNull(context);
    Objects.requireNonNull(inputStream);
    final byte[] value = context.read(OerUint256.class, inputStream)
        .getValue();
    return new InterledgerSha256Fulfillment(value);
  }

  @Override
  public void write(final CodecContext context, final Fulfillment instance,
      final OutputStream outputStream) throws IOException {
    Objects.requireNonNull(context);
    Objects.requireNonNull(instance);
    Objects.requireNonNull(outputStream);

    if(instance instanceof InterledgerSha256Fulfillment){
      InterledgerSha256Fulfillment fulfillment = (InterledgerSha256Fulfillment) instance;
      context.write(OerUint256.class, new OerUint256(fulfillment.getPreimage()), outputStream);
    }
    else
    {
      throw new IllegalArgumentException("Only InterledgerSha256Fulfillment instances can be encoded");
    }

  }

}
