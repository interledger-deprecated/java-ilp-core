package org.interledger.wire.codecs.oer.psk;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import org.interledger.InterledgerAddressBuilder;
import org.interledger.ilp.InterledgerPayment;
import org.interledger.psk.PskMessageBuilder;
import org.interledger.psk.PskMessageWriter;
import org.interledger.psk.PskReaderFactory;
import org.interledger.psk.PskWriterFactory;
import org.interledger.psk.model.BasicPskHeader;
import org.interledger.psk.model.PskMessage;
import org.interledger.wire.CodecContextFactory;
import org.interledger.wire.InterledgerPacket;
import org.interledger.wire.codecs.Codec;
import org.interledger.wire.codecs.CodecContext;
import org.interledger.wire.codecs.packets.AbstractVoidInterledgerPacketHandler.Template;
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
 * Unit tests to validate the {@link Codec} functionality for all {@link InterledgerPayment} packets
 * that include PSK payload data.  This test was assembled using the pseudocode instructions found
 * in IL-RFC-16.
 *
 * @see "https://github.com/interledger/rfcs/blob/master/0016-pre-shared-key/0016-pre-shared-key.md#pseudocode"
 */
@RunWith(Parameterized.class)
public class InterledgerPaymentWithPskCodecTests {

  // first data value (0) is default
  @Parameter
  public InterledgerPayment interledgerPayment;

  @Parameter(1)
  public PskMessage pskMessage;

  private static final byte[] sharedSecretKey = "icantsaybecauseitsasecretsosorry".getBytes();
  private static final BasicPskHeader publicHeader1
      = new BasicPskHeader("question", "What is the answer?");
  private static final BasicPskHeader publicHeader2
      = new BasicPskHeader("question", "What's the solution?");
  private static final BasicPskHeader privateHeader1
      = new BasicPskHeader("answer", "Choice, the problem is choice.");
  private static final BasicPskHeader privateHeader2
      = new BasicPskHeader("answer",
      "But we control these machines; they don't control us!");
  private static final byte[] applicationData
      = "{\"oracle\":\"candy\", \"forseen\":true}".getBytes();

  /**
   * The data for this test...
   */
  @Parameters
  public static Collection<Object[]> data() {

    /*
     * A sharedSecretKey is created in order to encrypt the private headers and the application
     * data. A nonce is used to generate the sharedSecretKey, so that the same private headers will
     * not produce the same ciphertext. Also note that the nonce's inclusion in the ILP payment
     * ensures that multiple payments using the same shared secret result in different hashes.
     * Note that the nonce is added automatically by the message builder unless a nonce header is 
     * specifically added.
     */

    final PskMessage pskMessage = new PskMessageBuilder()
        .addPrivateHeader(privateHeader1)
        .addPrivateHeader(privateHeader2)
        .addPublicHeader(publicHeader1)
        .addPublicHeader(publicHeader2)
        .setApplicationData(applicationData)
        .toMessage();

    final PskMessageWriter pskMessageWriter = PskWriterFactory.getEncryptedWriter(sharedSecretKey);
    final byte[] pskMessageBytes = pskMessageWriter.writeMessage(pskMessage);

    return Arrays.asList(new Object[][] {
        {new InterledgerPayment.Builder()
            .destinationAccount(InterledgerAddressBuilder.builder().value("test1.foo").build())
            .destinationAmount(100L)
            .data(pskMessageBytes)
            .build(), pskMessage},

        {new InterledgerPayment.Builder()
            .destinationAccount(InterledgerAddressBuilder.builder().value("test2.bar").build())
            .destinationAmount(1L)
            .data(pskMessageBytes)
            .build(), pskMessage},

        {new InterledgerPayment.Builder()
            .destinationAccount(InterledgerAddressBuilder.builder().value("test3.bar").build())
            .destinationAmount(0L)
            .data(pskMessageBytes)
            .build(), pskMessage},

    });
  }

  @Test
  public void testWriteRead() throws Exception {
    final CodecContext context = CodecContextFactory.interledger();

    // Write the payment to ASN.1...
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    context.write(interledgerPayment, outputStream);

    // Read the bytes using Codecs...
    final ByteArrayInputStream asn1OerPaymentBytes
        = new ByteArrayInputStream(outputStream.toByteArray());

    final InterledgerPacket decodedPacket = context.read(asn1OerPaymentBytes);
    assertThat(decodedPacket.getClass().getName(), is(interledgerPayment.getClass().getName()));
    assertThat(decodedPacket, is(interledgerPayment));

    // Validate the PSK Info....
    new Template() {
      @Override
      protected void handle(final InterledgerPayment decodedPayment) {
        assertThat(decodedPayment.getDestinationAccount(),
            is(interledgerPayment.getDestinationAccount()));
        assertThat(decodedPayment.getDestinationAmount(),
            is(interledgerPayment.getDestinationAmount()));

        final PskMessage decodedPskMessage = PskReaderFactory.getEncryptedReader(sharedSecretKey)
            .readMessage(decodedPayment.getData());

        assertThat(decodedPskMessage.getApplicationData(), is(pskMessage.getApplicationData()));
        decodedPskMessage.getPrivateHeaders().stream()
            .forEach(header ->
                assertTrue(pskMessage.getPrivateHeaders().contains(header))
            );
        decodedPskMessage.getPublicHeaders().stream()
            // TODO: See https://github.com/interledger/java-ilp-core/issues/32
            // The encryption header is not currently in the pre-encoded payload...
            .filter(header -> !header.getName().equalsIgnoreCase("Encryption"))
            .forEach(header -> assertTrue(String.format("Header was not found: %s", header),
                pskMessage.getPublicHeaders().contains(header))
            );
      }
    }.execute(decodedPacket);
  }

}
