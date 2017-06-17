package org.interledger.codecs.oer.ilqp;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.interledger.InterledgerAddressBuilder;
import org.interledger.InterledgerPacket;
import org.interledger.codecs.Codec;
import org.interledger.codecs.CodecContext;
import org.interledger.codecs.CodecContextFactory;
import org.interledger.ilqp.QuoteBySourceAmountRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;

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
  public static Collection<Object[]> data() {

    return Arrays.asList(new Object[][] {
      {new QuoteBySourceAmountRequest.Builder()
        .destinationAccount(InterledgerAddressBuilder.builder().value("test3.foo").build())
        .sourceAmount(100).destinationHoldDuration(Duration.ofSeconds(30)).build()},
        // {new QuoteByDestinationAmountRequest.Builder()},
        // {new QuoteByDestinationAmountResponse.Builder()},      
        // {new QuoteBySourceAmountResponse.Builder()},
        // {new QuoteLiquidityRequest.Builder()},
        // {new QuoteLiquidityResponse.Builder()},
    });
  }

  @Test
  public void testName() throws Exception {
    final CodecContext context = CodecContextFactory.interledger();

    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    context.write(packet, outputStream);

    final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
        outputStream.toByteArray());

    final InterledgerPacket decodedPacket = context.read(byteArrayInputStream);
    assertThat(decodedPacket.getClass().getName(), is(packet.getClass().getName()));
    assertThat(decodedPacket, is(packet));
  }
}
