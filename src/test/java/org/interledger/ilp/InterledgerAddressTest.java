package org.interledger.ilp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * JUnit tests to test the {@link InterledgerAddress} class.
 */
public class InterledgerAddressTest {

  @Test
  public void test_to_from_binary() {
    InterledgerAddress address = new InterledgerAddress("ilpdemo.red.bob");

    byte[] binary = address.toByteArray();

    InterledgerAddress binaryAddress = new InterledgerAddress(binary);

    assertEquals(address, binaryAddress);
  }

  @Test
  public void test_fromPrefixAndPath() {
    InterledgerAddress address =
        InterledgerAddress.fromPrefixAndPath(new InterledgerAddress("ilpdemo.red."), "bob");

    assertEquals("ilpdemo.red.bob", address.toString());

    InterledgerAddress existingPrefix = InterledgerAddress
        .fromPrefixAndPath(new InterledgerAddress("ilpdemo.red."), "ilpdemo.red.bob");
    assertEquals("ilpdemo.red.bob", existingPrefix.toString());
  }
  
  @Test
  public void test_TrimPrefix() {
    InterledgerAddress address = new InterledgerAddress("ilpdemo.red.bob");
    InterledgerAddress trimmed = address.trimPrefix(new InterledgerAddress("ilpdemo.red."));
    
    assertEquals("bob", trimmed.toString());
  }

}
