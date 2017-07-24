package org.interledger;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class ConditionTest {

  @Test
  public final void test() {

    byte[] hash = new byte[32];
    assertArrayEquals("Hash is invalid.", hash, Condition.builder().hash(hash).build().getHash());
  }

}
