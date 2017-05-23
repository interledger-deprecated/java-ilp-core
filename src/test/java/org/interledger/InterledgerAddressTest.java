package org.interledger.ilp;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.interledger.InterledgerAddress;
import org.interledger.InterledgerAddressBuilder;
import org.junit.Test;

/**
 * JUnit tests to test {@link InterledgerAddress}.
 */
public class InterledgerAddressTest {

  private static final boolean IS_LEDGER_PREFIX = true;
  private static final boolean IS_NOT_LEDGER_PREFIX = false;
  private static final String EXPECTED_ERROR_MESSAGE
      = "Invalid characters in address.  Reference Interledger RFC-15 for proper format.";

  @Test
  public void testGetValue() throws Exception {
    assertThat(InterledgerAddressBuilder.builder().value("g.foo.bob").build().getValue(),
        is("g.foo.bob"));
  }

  @Test
  public void testIsLedgerPrefix() throws Exception {
    assertThat(
        InterledgerAddressBuilder.builder().value("g.foo.bob").build().isLedgerPrefix(),
        is(IS_NOT_LEDGER_PREFIX)
    );
    assertThat(
        InterledgerAddressBuilder.builder().value("g.foo.bob.").build().isLedgerPrefix(),
        is(IS_LEDGER_PREFIX)
    );
  }

  @Test
  public void test_startsWith_null() throws Exception {
    final InterledgerAddress address = InterledgerAddressBuilder.builder().value("g.foo.bob")
        .build();
    assertThat(address.startsWith("g"), is(true));
    assertThat(address.startsWith("g."), is(true));
    assertThat(address.startsWith("g.foo"), is(true));
    assertThat(address.startsWith("g.foo."), is(true));
    assertThat(address.startsWith("g.foo.bob"), is(true));
    assertThat(address.startsWith("g.foo.bob."), is(false));
    assertThat(address.startsWith("test.foo.bob"), is(false));
  }

  @Test(expected = NullPointerException.class)
  public void test_address_null_value_string() {
    final InterledgerAddress addressPrefix = InterledgerAddressBuilder.builder().value("g.foo.")
        .build();
    try {
      addressPrefix.with(null);
    } catch (NullPointerException e) {
      assertThat(e.getMessage(), is("Segment String must not be null!"));
      throw e;
    }
  }

  @Test
  public void test_address_with_empty() {
    final InterledgerAddress addressPrefix = InterledgerAddressBuilder.builder().value("g.foo.")
        .build();
    assertThat(addressPrefix.with("").getValue(), is("g.foo."));
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_address_with_blank() {
    final InterledgerAddress addressPrefix = InterledgerAddressBuilder.builder().value("g.foo.")
        .build();
    try {
      addressPrefix.with("  ");
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is(EXPECTED_ERROR_MESSAGE));
      throw e;
    }
  }

  /**
   * Validates adding an address prefix (as an InterledgerAddress) to an address prefix.
   */
  @Test
  public void test_address_prefix_with_address_prefix() {
    final InterledgerAddress destinationAddress = InterledgerAddressBuilder.builder()
        .value("g.foo")
        .build();
    final String additionalDestinationAddress = "bob.";
    assertThat(destinationAddress.with(additionalDestinationAddress).getValue(), is("g.foo.bob."));
  }

  /**
   * Validates adding a destination address (as an InterledgerAddress) to an address prefix.
   */
  @Test
  public void test_address_prefix_with_destination_address() {
    final InterledgerAddress addressPrefix = InterledgerAddressBuilder.builder().value("g.foo.")
        .build();
    final String additionalDestinationAddress = "bob";
    assertThat(addressPrefix.with(additionalDestinationAddress).getValue(), is("g.foo.bob"));
  }

  /**
   * Validates adding a destination address (as an InterledgerAddress) to a destination address.
   */
  @Test
  public void test_destination_address_with_destination_address() {
    final InterledgerAddress destinationAddress = InterledgerAddressBuilder.builder()
        .value("g.foo")
        .build();
    final String additionalDestinationAddress = "bob";
    assertThat(destinationAddress.with(additionalDestinationAddress).getValue(), is("g.foo.bob"));
  }

  /**
   * Validates adding a destination address (as an InterledgerAddress) to a destination address.
   */
  @Test
  public void test_destination_address_with_address_prefix() {
    final InterledgerAddress destinationAddress = InterledgerAddressBuilder.builder()
        .value("g.foo")
        .build();
    final String additionalDestinationAddress = "bob.";
    assertThat(destinationAddress.with(additionalDestinationAddress).getValue(), is("g.foo.bob."));
  }

  @Test
  public void test_address_hashcode() {
    final InterledgerAddress addressPrefix1 = InterledgerAddressBuilder.builder().value("g.foo")
        .build();
    final InterledgerAddress addressPrefix2 = InterledgerAddressBuilder.builder().value("g.foo")
        .build();
    final InterledgerAddress addressPrefix3 = InterledgerAddressBuilder.builder()
        .value("g.foo.").build();

    assertThat(addressPrefix1.hashCode() == addressPrefix2.hashCode(), is(true));
    assertThat(addressPrefix1.equals(addressPrefix2), is(true));
    assertThat(addressPrefix2.equals(addressPrefix1), is(true));
    assertThat(addressPrefix1.toString().equals(addressPrefix2.toString()), is(true));

    assertThat(addressPrefix1.hashCode() == addressPrefix3.hashCode(), is(false));
    assertThat(addressPrefix1.equals(addressPrefix3), is(false));
    assertThat(addressPrefix3.equals(addressPrefix1), is(false));
    assertThat(addressPrefix1.toString().equals(addressPrefix3.toString()), is(false));
  }
}