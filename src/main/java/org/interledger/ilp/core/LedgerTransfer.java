package org.interledger.ilp.core;

public interface LedgerTransfer {

    /**
     * Get the packet header for this transfer
     *
     * @return the Interledger Packet Header
     */
    InterledgerPacketHeader getHeader();

    /**
     * Get the local account that funds are being debited from.
     *
     * @return local account identifier
     */
    String getFromAccount();

    /**
     * Get the local account that funds are being debited from.
     *
     * @return local account identifier
     */
    String getToAccount();

    /**
     * Get the transfer amount.
     *
     * MUST be positive. The supported precision is defined by each ledger
     * plugin and can be queried by the host via getInfo. The ledger plugin MUST
     * throw an InsufficientPrecisionError if the given amount exceeds the
     * supported level of precision.
     *
     * @return a decimal amount, represented as a string
     */
    String getAmount();

    /**
     * Get the data to be sent.
     *
     * Ledger plugins SHOULD treat this data as opaque, however it will usually
     * start with an ILP header followed by a transport layer header, a quote
     * request or a custom user-provided data packet.
     *
     * If the data is too large, the ledger plugin MUST throw a
     * MaximumDataSizeExceededError. If the data is too large only because the
     * amount is insufficient, the ledger plugin MUST throw an
     * InsufficientAmountError.
     *
     * @return a buffer containing the data
     */
    String getData();

    /**
     * Get the host's internal memo
     *
     * An optional bytestring containing details the host needs to persist with
     * the transfer in order to be able to react to transfer events like
     * condition fulfillment later.
     *
     * Ledger plugins MAY attach the noteToSelf to the transfer and let the
     * ledger store it. Otherwise it MAY use the store in order to persist this
     * field. Regardless of the implementation, the ledger plugin MUST ensure
     * that all instances of the transfer carry the same noteToSelf, even across
     * different machines.
     *
     * Ledger plugins MUST ensure that the data in the noteToSelf either isn't
     * shared with any untrusted party or encrypted before it is shared.
     *
     * @return a buffer containing the data
     */
    String getNoteToSelf();

}
