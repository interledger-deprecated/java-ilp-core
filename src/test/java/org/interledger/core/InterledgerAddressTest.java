package org.interledger.core;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * JUnit tests to test {@link InterledgerAddress}.
 */
public class InterledgerAddressTest {

    private static final boolean IS_LEDGER_PREFIX = true;
    private static final boolean IS_NOT_LEDGER_PREFIX = false;
    private static final String EXPECTED_ERROR_MESSAGE = "Invalid characters in address.  Reference RFC 15 for proper format.";

    @Test
    public void testGetValue() throws Exception {
        assertThat(InterledgerAddressBuilder.builder().withValue("g.foo.bob").build().getValue(), is("g.foo.bob"));
    }

    @Test
    public void testIsLedgerPrefix() throws Exception {
        assertThat(
                InterledgerAddressBuilder.builder().withValue("g.foo.bob").build().isLedgerPrefix(),
                is(IS_NOT_LEDGER_PREFIX)
        );
        assertThat(
                InterledgerAddressBuilder.builder().withValue("g.foo.bob.").build().isLedgerPrefix(),
                is(IS_LEDGER_PREFIX)
        );
    }

    @Test
    public void test_startsWith_null() throws Exception {
        final InterledgerAddress address = InterledgerAddressBuilder.builder().withValue("g.foo.bob").build();
        assertThat(address.startsWith("g"), is(true));
        assertThat(address.startsWith("g."), is(true));
        assertThat(address.startsWith("g.foo"), is(true));
        assertThat(address.startsWith("g.foo."), is(true));
        assertThat(address.startsWith("g.foo.bob"), is(true));
        assertThat(address.startsWith("g.foo.bob."), is(false));
        assertThat(address.startsWith("test.foo.bob"), is(false));
    }

    @Test(expected = NullPointerException.class)
    public void test_address_with_null() {
        final InterledgerAddress addressPrefix = InterledgerAddressBuilder.builder().withValue("g.foo.").build();
        try {
            addressPrefix.with(null);
        } catch (NullPointerException e) {
            throw e;
        }
    }

    @Test
    public void test_address_with_empty() {
        final InterledgerAddress addressPrefix = InterledgerAddressBuilder.builder().withValue("g.foo.").build();
        assertThat(addressPrefix.with("").getValue(), is("g.foo."));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_address_with_blank() {
        final InterledgerAddress addressPrefix = InterledgerAddressBuilder.builder().withValue("g.foo.").build();
        try {
            addressPrefix.with("  ");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(EXPECTED_ERROR_MESSAGE));
            throw e;
        }
    }

    @Test
    public void test_address_prefix_with() {
        final InterledgerAddress addressPrefix = InterledgerAddressBuilder.builder().withValue("g.foo.").build();
        assertThat(addressPrefix.with("bob").getValue(), is("g.foo.bob"));
    }

    @Test
    public void test_destination_address_with() {
        final InterledgerAddress addressPrefix = InterledgerAddressBuilder.builder().withValue("g.foo").build();
        assertThat(addressPrefix.with("bob").getValue(), is("g.foo.bob"));
    }

    @Test
    public void test_address_hashcode() {
        final InterledgerAddress addressPrefix1 = InterledgerAddressBuilder.builder().withValue("g.foo").build();
        final InterledgerAddress addressPrefix2 = InterledgerAddressBuilder.builder().withValue("g.foo").build();
        final InterledgerAddress addressPrefix3 = InterledgerAddressBuilder.builder().withValue("g.foo.").build();

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