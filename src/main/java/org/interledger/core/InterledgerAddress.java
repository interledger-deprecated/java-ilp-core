package org.interledger.core;

import java.util.Objects;

/**
 * Interledger Protocol (ILP) Addresses identify Ledger accounts (or groups of Ledger accounts) in an ILP network, and
 * provide a way to route a payment to its intended destination.
 * <p>
 * Interledger Addresses can be subdivided into two categories:
 * <p>
 * <b>Destination Addresses</b> are complete addresses that can receive payments. A destination address always maps to
 * one account in a ledger, though it can also provide more specific information, such as an invoice ID or a
 * sub-account.
 * <p>
 * Destination addresses MUST NOT end in a period (.) character.
 * <p>
 * <b>Address Prefixes</b> are incomplete addresses representing a grouping of destination addresses. Many depths of
 * grouping are possible, for example: groups of accounts or sub-accounts; an individual ledger or subledger; or entire
 * neighborhoods of ledgers.
 * <p>
 * Address prefixes MUST end in a period (.) character.
 * <p>
 * The formal specification for an Interledger Addresses is defined in
 * <a href="https://github.com/interledger/rfcs/tree/master/0015-ilp-addresses">Interledger RFC #15</a>.
 *
 * @see "https://github.com/interledger/rfcs/tree/master/0015-ilp-addresses"
 */
public interface InterledgerAddress {

    /**
     * Return this address's value as a non-null {@link String}.
     * <p>
     * For example: <code>us.usd.bank.account</code>
     *
     * @return A {@link String} representation of this Interledger address.
     */
    String getValue();

    /**
     * Tests if this Interledger address represents a ledger prefix.
     *
     * @return True if the address is a ledger prefix, false otherwise.
     */
    default boolean isLedgerPrefix() {
        return getValue().endsWith(".");
    }

    /**
     * Tests if this Interledger address starts with the specified {@code prefix}.
     *
     * @param addressPrefix The prefix.
     * @return True if the address begins with the specified prefix, false otherwise.
     */
    default boolean startsWith(final String addressPrefix) {
        Objects.requireNonNull(addressPrefix, "addressPrefix must not be null!");
        return this.getValue().startsWith(addressPrefix);
    }

    /**
     * Return a new {@link InterledgerAddress} by postfixing {@code segment} to this address.
     * <p>
     * For example, if this address is '<code>us.usd</code>', then calling this method with an argument of
     * '<code>bob</code>' would result in a new Interledger Address with a value of '<code>us.usd.bob</code>'.
     *
     * @param segment A {@link String} to be appended to this address as an additional, yet final, segment.
     * @return A new instance of {@link InterledgerAddress} representing the original address with a newly specified
     * final segment.
     */
    InterledgerAddress with(String segment);
}
