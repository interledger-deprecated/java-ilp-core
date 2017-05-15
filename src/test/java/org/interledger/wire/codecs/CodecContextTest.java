package org.interledger.wire.codecs;

import static junit.framework.TestCase.assertTrue;

import org.interledger.InterledgerAddress;
import org.interledger.ilp.InterledgerPayment;
import org.interledger.wire.CodecContextFactory;
import org.interledger.wire.InterledgerPacketType;
import org.interledger.wire.codecs.oer.OerIA5StringCodec.OerIA5String;
import org.interledger.wire.codecs.oer.OerLengthPrefixCodec.OerLengthPrefix;
import org.interledger.wire.codecs.oer.OerOctetStringCodec.OerOctetString;
import org.interledger.wire.codecs.oer.OerUint64Codec.OerUint64;
import org.interledger.wire.codecs.oer.OerUint8Codec.OerUint8;
import org.junit.Test;

/**
 * Unit tests for {@link CodecContext}.
 */
public class CodecContextTest {

  @Test
  public void hasRegisteredCodec() throws Exception {
    final CodecContext context = CodecContextFactory.interledger();

    assertTrue(context.hasRegisteredCodec(OerUint8.class));
    assertTrue(context.hasRegisteredCodec(OerUint64.class));
    assertTrue(context.hasRegisteredCodec(OerOctetString.class));
    assertTrue(context.hasRegisteredCodec(OerLengthPrefix.class));
    assertTrue(context.hasRegisteredCodec(OerIA5String.class));

    assertTrue(context.hasRegisteredCodec(InterledgerAddress.class));
    assertTrue(context.hasRegisteredCodec(InterledgerPacketType.class));
    assertTrue(context.hasRegisteredCodec(InterledgerPayment.class));

    // TODO: Add ILQP!
    //    assertTrue(context.readable(InterledgerPacketType.ILQP_QUOTE_LIQUIDITY_REQUEST_TYPE));
    //    assertTrue(context.readable(InterledgerPacketType.ILQP_QUOTE_LIQUIDITY_RESPONSE_TYPE));
    //    assertTrue(context.readable(InterledgerPacketType.ILP_PAYMENT_TYPE));
    //    assertTrue(
    // context.readable(InterledgerPacketType.ILQP_QUOTE_BY_DESTINATION_AMOUNT_REQUEST_TYPE));
    //    assertTrue(
    // context.readable(InterledgerPacketType.ILQP_QUOTE_BY_DESTINATION_AMOUNT_RESPONSE_TYPE));
    // assertTrue(context
    // .readable(InterledgerPacketType.ILQP_QUOTE_BY_SOURCE_AMOUNT_REQUEST_TYPE));
    // assertTrue(context
    // .readable(InterledgerPacketType.ILQP_QUOTE_BY_SOURCE_AMOUNT_RESPONSE_TYPE));
  }

}