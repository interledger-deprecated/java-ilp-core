package org.interledger.codecs.oer.ilp;

import org.interledger.codecs.Codec;
import org.interledger.codecs.CodecContext;
import org.interledger.codecs.FulfillmentCodec;
import org.interledger.codecs.oer.OerUint256Codec.OerUint256;
import org.interledger.cryptoconditions.Fulfillment;
import org.interledger.cryptoconditions.PreimageSha256Fulfillment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
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
    return new PreimageSha256Fulfillment(value);
  }

  @Override
  public void write(final CodecContext context, final Fulfillment instance,
      final OutputStream outputStream) throws IOException {
    Objects.requireNonNull(context);
    Objects.requireNonNull(instance);
    Objects.requireNonNull(outputStream);

    if (instance instanceof PreimageSha256Fulfillment) {
      PreimageSha256Fulfillment fulfillment = (PreimageSha256Fulfillment) instance;
      byte[] preimage = Base64.getUrlDecoder().decode(fulfillment.getPreimage());
      context.write(OerUint256.class, new OerUint256(preimage), outputStream);
    } else {
      //TODO This is problematic because it's possible that there may be another
      // implementation with the same type. Design of Crypto-conditions is the problem
      throw new IllegalArgumentException("Only PreimageSha256Fulfillment instances can be encoded");
    }

  }

}
