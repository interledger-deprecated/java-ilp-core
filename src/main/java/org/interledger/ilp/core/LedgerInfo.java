package org.interledger.ilp.core;

/**
 * Metadata describing the ledger.
 *
 * This data is returned by the LedgerPlugin.getInfo() method.
 */
public interface LedgerInfo {

    /**
     * Get the precision supported by the ledger
     *
     * @return The total number of digits (base 10) of precision allowed by the
     * ledger.
     */
    int getPrecision();

    /**
     * Get the scale allowed by the ledger
     *
     * @return The number of digits allowed after the decimal point.
     *
     */
    int getScale();

    /**
     * Get the currency code of the ledger
     *
     * @return The ISO 4217 currency code (if any) used by the ledger.
     *
     */
    String getCurrencyCode();

    /**
     * Get the currency symbol of the ledger
     *
     * @return The currency symbol as one or more UTF8 characters.
     */
    String getCurrencySymbol();

}
