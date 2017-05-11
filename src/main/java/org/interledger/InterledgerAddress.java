package org.interledger;

import java.util.Objects;

/**
 * Interledger Protocol (ILP) Addresses identify Ledger accounts (or groups of Ledger accounts) in
 * an ILP network, and provide a way to route a payment to its intended destination.
 *
 * <p>Interledger Addresses can be subdivided into two categories:</p>
 *
 * <p> <b>Destination Addresses</b> are complete addresses that can receive payments. A destination
 * address always maps to one account in a ledger, though it can also provide more specific
 * information, such as an invoice ID or a sub-account.  Destination addresses MUST NOT end in a
 * period (.) character. </p>
 *
 * <p> <b>Address Prefixes</b> are incomplete addresses representing a grouping of
 * destination addresses. Many depths of grouping are possible, for example: groups of accounts or
 * sub-accounts; an individual ledger or sub-ledger; or entire neighborhoods of ledgers.  Address
 * prefixes MUST end in a period (.) character. </p>
 *
 * <p> The formal specification for an Interledger Addresses is defined in <a
 * href="https://github.com/interledger/rfcs/tree/master/0015-ilp-addresses">Interledger RFC
 * #15</a>. </p>
 *
 * @see "https://github.com/interledger/rfcs/tree/master/0015-ilp-addresses"
 */
public interface InterledgerAddress {

  /**
   * Return this address's value as a non-null {@link String}.  For example:
   * <code>us.usd.bank.account</code>
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
   * Tests if this InterledgerAddress starts with the specified {@code addressSegment}.
   *
   * @param addressSegment An {@link String} prefix to compare against.
   *     @return {@code true} if this InterledgerAddress begins with the specified prefix, {@code
   *     false} otherwise.
   */
  default boolean startsWith(final String addressSegment) {
    Objects.requireNonNull(addressSegment, "addressSegment must not be null!");
    return this.getValue().startsWith(addressSegment);
  }

  /**
   * Tests if this InterledgerAddress starts with the specified {@code interledgerAddress}.
   *
   * @param interledgerAddress An {@link InterledgerAddress} prefix to compare against.
   * @return {@code true} if this InterledgerAddress begins with the specified prefix, {@code false}
   *     otherwise.
   */
  default boolean startsWith(final InterledgerAddress interledgerAddress) {
    Objects.requireNonNull(interledgerAddress, "interledgerAddress must not be null!");
    return this.startsWith(interledgerAddress.toString());
  }

  /**
   * Return a new InterledgerAddress by postfixing the supplied {@code segment} to this address.
   *
   * <p>This method can be used to construct both address prefixes and destination addresses. For
   * example, if the value of this address is '<code>us.usd.</code>', then calling this method with
   * an argument of '<code>bob</code>' would result in a new Interledger Address with a value of
   * '<code>us.usd.bob</code>', which is a destination address.</p>
   *
   * <p>Likewise, if the value of this address is '<code>us.usd.pacific.</code>', then calling this
   * method with an argument of '<code>creditunions.</code>' would result in a new Interledger
   * Address with a value of '<code>us.usd.pacific.creditunions.</code>', which is an address
   * prefix. </p>
   *
   * @param addressSegment A {@link String} to be appended to this address as an additional
   *     segment.
   * @return A new instance of InterledgerAddress representing the original address with a newly
   *     specified final segment.
   */
  InterledgerAddress with(String addressSegment);
}
