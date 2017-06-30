package org.interledger.ipr;

import org.interledger.InterledgerAddress;
import org.interledger.InterledgerAddressBuilder;
import org.interledger.codecs.CodecContext;
import org.interledger.codecs.CodecContextFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.UUID;

public class InterledgerPaymentRequestBuilderTest {

  @Before
  public void setUp() throws Exception {}

  @After
  public void tearDown() throws Exception {}

  @Test
  public final void testSimpleInterledgerPaymentRequest() {

    InterledgerAddress destinationAddress =
        InterledgerAddressBuilder.builder().value("private.bob").build();
    long destinationAmount = 100L;
    ZonedDateTime expiresAt = ZonedDateTime.now().plusMinutes(1);
    byte[] receiverSecret = new byte[32];
    String paymentId = UUID.randomUUID().toString();

    InterledgerPaymentRequestBuilder builder = new InterledgerPaymentRequestBuilder(
        destinationAddress, destinationAmount, expiresAt, receiverSecret);

    builder.setEncrypted(false);
    builder.getPskMessageBuilder().addPublicHeader("Payment-Id", paymentId);

    CodecContext context = CodecContextFactory.interledger();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    try {
      context.write(InterledgerPaymentRequest.class, builder.build(), outputStream);
    } catch (IOException e) {
      throw new RuntimeException("Error encoding Interledger Packet.", e);
    }

    Base64.getUrlEncoder().encodeToString(outputStream.toByteArray());

  }

}
