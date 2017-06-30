package org.interledger.psk;

import org.interledger.mocks.DeterministicSecureRandomProvider;
import org.interledger.psk.io.PskUtils;
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

    final String token = "PE7rnGiULIrfu655nwSYew";
    final String receiverId = "ebKWcAEB9_A";
    final String sharedKeyString = "66iH2jKo-lMSs55jU8fH3Tm-G_rf9aDi-Q3bu6gddGM";

    DeterministicSecureRandomProvider.setAsDefault(Base64.getUrlDecoder().decode(token));

    PskParams params = PskUtils.getPskParams(receiverSecret);

    Assert.assertEquals("Invalid token", token, params.getToken());

    Assert.assertEquals("Invalid Receiver ID.", receiverId, params.getReceiverId());

    Assert.assertArrayEquals("Invalid shared key",
        Arrays.copyOf(Base64.getUrlDecoder().decode(sharedKeyString), 16), params.getSharedKey());

    DeterministicSecureRandomProvider.remove();

  }

}
