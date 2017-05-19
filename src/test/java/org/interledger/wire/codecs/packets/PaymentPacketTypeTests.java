package org.interledger.wire.codecs.packets;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.interledger.wire.InterledgerPacketType;
import org.interledger.wire.codecs.Codec;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

/**
 * Unit tests to validate the {@link Codec} functionality for all Interledger packets.
 */
@RunWith(Parameterized.class)
public class PaymentPacketTypeTests {

  // first data value (0) is default
  @Parameter
  public InterledgerPacketType firstPacket;

  @Parameter(1)
  public InterledgerPacketType secondPacket;

  /**
   * The data for this test...
   */
  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
        {new PaymentPacketType(), new PaymentPacketType()},
        {new QuoteByDestinationRequestPacketType(), new QuoteByDestinationRequestPacketType()},
        {new QuoteByDestinationResponsePacketType(), new QuoteByDestinationResponsePacketType()},
        {new QuoteBySourceRequestPacketType(), new QuoteBySourceRequestPacketType()},
        {new QuoteBySourceResponsePacketType(), new QuoteBySourceResponsePacketType()},
        {new QuoteLiquidityRequestPacketType(), new QuoteLiquidityRequestPacketType()},
        {new QuoteLiquidityResponsePacketType(), new QuoteLiquidityResponsePacketType()},

        {new PaymentPacketType(),
            InterledgerPacketType.fromTypeId(InterledgerPacketType.ILP_PAYMENT_TYPE)},
        {new QuoteByDestinationRequestPacketType(),
            InterledgerPacketType.fromTypeId(
                InterledgerPacketType.ILQP_QUOTE_BY_DESTINATION_AMOUNT_REQUEST_TYPE)},
        {new QuoteByDestinationResponsePacketType(),
            InterledgerPacketType.fromTypeId(
                InterledgerPacketType.ILQP_QUOTE_BY_DESTINATION_AMOUNT_RESPONSE_TYPE)},
        {new QuoteBySourceRequestPacketType(),
            InterledgerPacketType.fromTypeId(
                InterledgerPacketType.ILQP_QUOTE_BY_SOURCE_AMOUNT_REQUEST_TYPE)},
        {new QuoteBySourceResponsePacketType(),
            InterledgerPacketType.fromTypeId(
                InterledgerPacketType.ILQP_QUOTE_BY_SOURCE_AMOUNT_RESPONSE_TYPE)},
        {new QuoteLiquidityRequestPacketType(),
            InterledgerPacketType.fromTypeId(
                InterledgerPacketType.ILQP_QUOTE_LIQUIDITY_REQUEST_TYPE)},
        {new QuoteLiquidityResponsePacketType(),
            InterledgerPacketType.fromTypeId(
                InterledgerPacketType.ILQP_QUOTE_LIQUIDITY_RESPONSE_TYPE)},
    });
  }

  @Test
  public void testEqualsHashcode() throws Exception {
    assertThat(firstPacket, is(secondPacket));
    assertThat(secondPacket, is(firstPacket));

    assertTrue(firstPacket.equals(secondPacket));
    assertTrue(secondPacket.equals(firstPacket));

    assertThat(firstPacket.hashCode(), is(secondPacket.hashCode()));
    assertThat(firstPacket, is(secondPacket));
  }
}
