package org.interledger.codecs.oer.ilqp;

import org.interledger.InterledgerAddress;
import org.interledger.codecs.Codec;
import org.interledger.codecs.CodecContext;
import org.interledger.codecs.QuoteByDestinationAmountRequestCodec;
import org.interledger.codecs.oer.OerUint32Codec.OerUint32;
import org.interledger.codecs.oer.OerUint64Codec.OerUint64;
import org.interledger.ilqp.QuoteByDestinationAmountRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * An implementation of {@link Codec} that reads and writes instances of
 * {@link QuoteByDestinationAmountRequest}. in OER format.
 * 
 * @see https://github.com/interledger/rfcs/blob/master/asn1/InterledgerQuotingProtocol.asn
 */
public class QuoteByDestinationRequestOerCodec implements QuoteByDestinationAmountRequestCodec {

  @Override
  public QuoteByDestinationAmountRequest read(CodecContext context, InputStream inputStream)
      throws IOException {

    Objects.requireNonNull(context);
    Objects.requireNonNull(inputStream);

    /* read the request id, which is a uint128 */
    //long requestId = context.read(OerUint128.class, inputStream).getValue();
    
    /* read the Interledger Address. */
    final InterledgerAddress destinationAccount =
        context.read(InterledgerAddress.class, inputStream);
    
    /* read the destination amount, which is a uint64 */
    /* NOTE: we dont expect amounts to exceed 2^63 - 1, so we risk the down-cast */
    long destinationAmount = context.read(OerUint64.class, inputStream).getValue().longValue();

    /* read the destination hold duration which is a unit32 */
    long destinationHoldDuration = context.read(OerUint32.class, inputStream).getValue();

    return null; //TODO:
  }

  @Override
  public void write(CodecContext context, QuoteByDestinationAmountRequest instance,
      OutputStream outputStream) throws IOException {
    // TODO Auto-generated method stub

  }


}
