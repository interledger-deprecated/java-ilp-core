package org.interledger.wire;

import org.interledger.InterledgerAddress;
import org.interledger.ilp.InterledgerPayment;
import org.interledger.wire.codecs.CodecContext;
import org.interledger.wire.codecs.oer.OerIA5StringCodec;
import org.interledger.wire.codecs.oer.OerIA5StringCodec.OerIA5String;
import org.interledger.wire.codecs.oer.OerLengthPrefixCodec;
import org.interledger.wire.codecs.oer.OerLengthPrefixCodec.OerLengthPrefix;
import org.interledger.wire.codecs.oer.OerOctetStringCodec;
import org.interledger.wire.codecs.oer.OerOctetStringCodec.OerOctetString;
import org.interledger.wire.codecs.oer.OerUint64Codec;
import org.interledger.wire.codecs.oer.OerUint64Codec.OerUint64;
import org.interledger.wire.codecs.oer.OerUint8Codec;
import org.interledger.wire.codecs.oer.OerUint8Codec.OerUint8;
import org.interledger.wire.codecs.oer.ilp.InterledgerAddressOerCodec;
import org.interledger.wire.codecs.oer.ilp.InterledgerPacketTypeOerCodec;
import org.interledger.wire.codecs.oer.ilp.InterledgerPaymentOerCodec;

/**
 * A factory class for constructing a CodecContext that can read and write Interledger objects using
 * ASN.1 OER encoding.
 */
public class CodecContextFactory {

  /**
   * Create an instance of {@link CodecContext} that encodes and decodes Interledger packets using
   * ASN.1 OER encoding.
   */
  public static CodecContext interledger() {

    // OER Base...
    return new CodecContext()
        .register(OerUint8.class, new OerUint8Codec())
        .register(OerUint64.class, new OerUint64Codec())
        .register(OerLengthPrefix.class, new OerLengthPrefixCodec())
        .register(OerIA5String.class, new OerIA5StringCodec())
        .register(OerOctetString.class, new OerOctetStringCodec())

        // ILP
        .register(InterledgerAddress.class, new InterledgerAddressOerCodec())
        .register(InterledgerPacketType.class, new InterledgerPacketTypeOerCodec())
        .register(InterledgerPayment.class, new InterledgerPaymentOerCodec())

        // ILQP
        //.register(QuoteByDestinationRequest.class, new QuoteByDestinationRequestOerCodec())
        //.register(QuoteByDestinationResponse.class, new QuoteByDestinationResponseOerCodec())
        //.register(QuoteBySourceRequest.class, new QuoteBySourceRequestOerCodec())
        //.register(QuoteBySourceResponse.class, new QuoteBySourceResponseOerCodec())
        //.register(QuoteLiquidityRequest.class, new QuoteLiquidityRequestOerCodec())
        //.register(QuoteLiquidityResponse.class, new QuoteLiquidityResponseOerCodec())

        ;
  }

}
