package org.interledger.codecs.oer.ilp;

import org.interledger.Condition;
import org.interledger.InterledgerAddress;
import org.interledger.codecs.Codec;
import org.interledger.codecs.CodecContext;
import org.interledger.codecs.ConditionCodec;
import org.interledger.codecs.oer.OerUint256Codec.OerUint256;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * An implementation of {@link Codec} that reads and writes instances of {@link InterledgerAddress}.
 */
public class ConditionOerCodec implements ConditionCodec {

  @Override
  public Condition read(final CodecContext context, final InputStream inputStream)
      throws IOException {
    Objects.requireNonNull(context);
    Objects.requireNonNull(inputStream);
    final byte[] value = context.read(OerUint256.class, inputStream).getValue();
    return new Condition(value);
  }

  @Override
  public void write(final CodecContext context, final Condition instance,
      final OutputStream outputStream) throws IOException {
    Objects.requireNonNull(context);
    Objects.requireNonNull(instance);
    Objects.requireNonNull(outputStream);

    context.write(OerUint256.class, new OerUint256(instance.getHash()), outputStream);
  }

}
