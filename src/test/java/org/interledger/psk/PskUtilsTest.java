package org.interledger.psk;

import org.interledger.mocks.DeterministicSecureRandomProvider;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;

public class PskUtilsTest {

  private byte[] receiverSecret = "secret".getBytes(Charset.forName("UTF8"));

  /**
   * Test using static values from JS impl.
   */
  @Test
  public final void testGetPskParams() {

    /* Known good test values */
    final byte[] token = Base64.getUrlDecoder().decode("PE7rnGiULIrfu655nwSYew");
    final byte[] receiverId = Base64.getUrlDecoder().decode("ebKWcAEB9_A");
    final byte[] sharedKey =
        Base64.getUrlDecoder().decode("66iH2jKo-lMSs55jU8fH3Tm-G_rf9aDi-Q3bu6gddGM");

    DeterministicSecureRandomProvider.setAsDefault(token);

    PskContext context = PskContext.seed(receiverSecret);

    Assert.assertArrayEquals("Invalid token", Arrays.copyOf(token, 16), context.getToken());

    Assert.assertArrayEquals("Invalid Receiver ID.", Arrays.copyOf(receiverId, 8),
        context.getReceiverId());

    Assert.assertArrayEquals("Invalid shared key", Arrays.copyOf(sharedKey, 16),
        context.getSharedKey());

    DeterministicSecureRandomProvider.remove();

  }

}
