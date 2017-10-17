package org.interledger;

import static org.junit.Assert.assertArrayEquals;

import org.interledger.cryptoconditions.InterledgerSha256Condition;
import org.junit.Test;

public class ConditionTest {

  @Test
  public final void test() {

    byte[] hash = new byte[32];
    assertArrayEquals("Hash is invalid.", hash, new InterledgerSha256Condition(hash).getFingerprint());
  }

}
