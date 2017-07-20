package org.interledger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Base64;

public class FulfillmentTest {

  private static byte[] TEST_PREIMAGE = new byte[32];
  private static Condition TEST_CONDITION = Condition.builder()
      .hash(Base64.getUrlDecoder().decode("Zmh6rfhivXdsj8GLjp-OIAiXFIVu4jOzkCpZHQ1fKSU")).build();

  @Test(expected = IllegalArgumentException.class)
  public final void testSmallPreimage() {
    Fulfillment.builder().preimage(new byte[31]).build();
  }

  @Test(expected = IllegalArgumentException.class)
  public final void testBigPreimage() {
    Fulfillment.builder().preimage(new byte[33]).build();
  }

  @Test(expected = NullPointerException.class)
  public final void testNullPreimage() {
    Fulfillment.builder().preimage(null).build();
  }

  @Test()
  public final void testGetCondition() {

    Fulfillment fulfillment = Fulfillment.builder().preimage(TEST_PREIMAGE).build();
    assertEquals("Wrong condition", TEST_CONDITION, fulfillment.getCondition());

  }

  @Test()
  public final void testValidate() {

    Fulfillment fulfillment = Fulfillment.builder().preimage(TEST_PREIMAGE).build();
    assertTrue("Invalid condition", fulfillment.validate(TEST_CONDITION));

  }


}
