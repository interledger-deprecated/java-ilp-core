package org.interledger.wire.codecs.oer.ilqp;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.interledger.wire.CodecContextFactory;
import org.interledger.wire.InterledgerPacket;
import org.interledger.wire.codecs.Codec;
import org.interledger.wire.codecs.CodecContext;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
        //        {new QuoteByDestinationAmountRequest.Builder()},
        //        {new QuoteByDestinationAmountResponse.Builder()},
        //        {new QuoteBySourceAmountRequest.Builder()},
        //        {new QuoteBySourceAmountResponse.Builder()},
        //        {new QuoteLiquidityRequest.Builder()},
        //        {new QuoteLiquidityResponse.Builder()},
    });
  }

  @Test
  @Ignore
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
