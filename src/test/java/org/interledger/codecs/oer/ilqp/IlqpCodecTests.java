package org.interledger.codecs.oer.ilqp;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.interledger.InterledgerAddressBuilder;
import org.interledger.InterledgerPacket;
import org.interledger.codecs.Codec;
import org.interledger.codecs.CodecContext;
import org.interledger.codecs.CodecContextFactory;
import org.interledger.ilqp.QuoteByDestinationAmountRequest;
import org.interledger.ilqp.QuoteByDestinationAmountResponse;
import org.interledger.ilqp.QuoteBySourceAmountRequest;
import org.interledger.ilqp.QuoteBySourceAmountResponse;
import org.interledger.ilqp.QuoteLiquidityRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.Duration;

/**
 * Unit tests to validate the {@link Codec} functionality for all Interledger packets.
 */
@RunWith(Parameterized.class)
public class IlqpCodecTests {

  // first data value (0) is default
  @Parameter
  public InterledgerPacket packet;

  /**
   * The data for this test...
   */
  @Parameters
  public static Object[] data() {
    return new Object[] {
        new QuoteBySourceAmountRequest.Builder()
            .destinationAccount(InterledgerAddressBuilder.builder().value("test1.foo").build())
            .sourceAmount(100)
            .destinationHoldDuration(Duration.ofSeconds(30)).build(),
        new QuoteBySourceAmountResponse.Builder()
            .destinationAmount(95)
            .sourceHoldDuration(Duration.ofSeconds(30)).build(),
        new QuoteByDestinationAmountRequest.Builder()
            .destinationAccount(InterledgerAddressBuilder.builder().value("test2.foo").build())
            .destinationAmount(100)
            .destinationHoldDuration(Duration.ofSeconds(35)).build(),
        new QuoteByDestinationAmountResponse.Builder()
            .sourceAmount(105)
            .sourceHoldDuration(Duration.ofMinutes(1)).build(),
        new QuoteLiquidityRequest.Builder()
        .destinationAccount(InterledgerAddressBuilder.builder().value("test3.foo").build())
        .destinationHoldDuration(Duration.ofMinutes(5)).build()};
  }

  @Test
  public void testName() throws Exception {
    final CodecContext context = CodecContextFactory.interledger();

    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    context.write(packet, outputStream);

    final ByteArrayInputStream byteArrayInputStream =
        new ByteArrayInputStream(outputStream.toByteArray());

    final InterledgerPacket decodedPacket = context.read(byteArrayInputStream);
    assertThat(decodedPacket.getClass().getName(), is(packet.getClass().getName()));
    assertThat(decodedPacket, is(packet));
  }
}
