package org.interledger.core;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link InterledgerAddressBuilder}.
 */
public class InterledgerAddressBuilderTest {

    private static final String EXPECTED_ERROR_MESSAGE = "Invalid characters in address.  Reference RFC 15 for proper format.";
    private static final String TEST1_US_USD_BOB = "test1.us.usd.bob";
    private static final String TEST1_US_USD = "test1.us.usd.";

    @Test(expected = NullPointerException.class)
    public void test_constructor_wit_uninitialized_build() throws Exception {
        try {
            new InterledgerAddressBuilder().build();
        } catch (NullPointerException e) {
            assertThat(e.getMessage(), is("InterledgerAddress must not be null!"));
            throw e;
        }
    }

    @Test(expected = NullPointerException.class)
    public void test_wither_with_null_value() throws Exception {
        try {
            new InterledgerAddressBuilder().withValue(null);
        } catch (NullPointerException e) {
            assertThat(e.getMessage(), is("value must not be null!"));
            throw e;
        }
    }

    @Test
    public void test_builder_copy() throws Exception {
        final InterledgerAddress address = new InterledgerAddressBuilder().withValue(TEST1_US_USD_BOB).build();
        assertThat(new InterledgerAddressBuilder(address).build(), is(address));
    }

    @Test
    public void testConstruction_DeliverableAddress() throws Exception {
        final InterledgerAddress address = new InterledgerAddressBuilder().withValue(TEST1_US_USD_BOB).build();
        assertThat(address.getValue(), is(TEST1_US_USD_BOB));
        assertThat(address.isLedgerPrefix(), is(not(true)));
    }

    @Test
    public void testConstruction_LedgerPrefix() throws Exception {
        final InterledgerAddress address = InterledgerAddressBuilder.builder().withValue(TEST1_US_USD).build();
        assertThat(address.getValue(), is(TEST1_US_USD));
        assertThat(address.isLedgerPrefix(), is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_empty_address() throws Exception {
        try {
            InterledgerAddressBuilder.builder().withValue("").build();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(EXPECTED_ERROR_MESSAGE));
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_blank_address() throws Exception {
        try {
            InterledgerAddressBuilder.builder().withValue("  ").build();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(EXPECTED_ERROR_MESSAGE));
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_address_with_space() throws Exception {
        try {
            InterledgerAddressBuilder.builder().withValue(TEST1_US_USD_BOB + " space").build();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(EXPECTED_ERROR_MESSAGE));
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_address_too_long() throws Exception {

        final String TOO_LONG = "g.012345678901234567890123456789012345678901234567890123456789012345678901234567890123456" +
                "789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +
                "901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890" +
                "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123" +
                "4567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456" +
                "7890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012" +
                "3456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345" +
                "6789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +
                "90123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789";

        try {
            InterledgerAddressBuilder.builder().withValue(TOO_LONG).build();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(EXPECTED_ERROR_MESSAGE));
            throw e;
        }
    }

    @Test
    public void test_address_just_right_length() throws Exception {
        // This is 1023 characters long...
        final String JUST_RIGHT = "g.012345678901234567890123456789012345678901234567890123456789012345678901234567890123456" +
                "789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +
                "901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890" +
                "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123" +
                "4567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456" +
                "7890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012" +
                "3456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345" +
                "6789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +
                "9012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678912312349393";

        final InterledgerAddress address = InterledgerAddressBuilder.builder().withValue(JUST_RIGHT).build();
        assertThat(address.getValue(), is(JUST_RIGHT));
        assertThat(address.isLedgerPrefix(), is(false));
    }

    @Test
    public void test_address_all_valid_characters() throws Exception {
        final String allValues = "g.0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_~-.";
        final InterledgerAddress address = InterledgerAddressBuilder.builder().withValue(allValues).build();
        assertThat(address.getValue(), is(allValues));
        assertThat(address.isLedgerPrefix(), is(true));
    }
}