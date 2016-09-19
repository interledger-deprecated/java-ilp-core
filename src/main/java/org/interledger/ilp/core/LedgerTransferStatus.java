package org.interledger.ilp.core;

/**
 * The legit ledger transfer statuses.
 */
public enum LedgerTransferStatus {
    PROPOSED,
    PREPARED,
    EXECUTED,
    REJECTED;

    public static LedgerTransferStatus valueOf(int status) {
        switch (status) {
            case 0:
                return LedgerTransferStatus.PROPOSED;
            case 1:
                return LedgerTransferStatus.PREPARED;
            case 2:
                return LedgerTransferStatus.EXECUTED;
            case 3:
                return LedgerTransferStatus.REJECTED;
            default:
                throw new IllegalArgumentException("Invalid LedgerTransferStatus status " + status);
        }
    }

}
