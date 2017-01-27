package org.interledger.ilp.ledger.model;

public enum TransferStatus {
    PROPOSED,
    PREPARED,
    EXECUTED,
    REJECTED;

    public static TransferStatus valueOf(int status) {
        switch (status) {
            case 0:
                return TransferStatus.PROPOSED;
            case 1:
                return TransferStatus.PREPARED;
            case 2:
                return TransferStatus.EXECUTED;
            case 3:
                return TransferStatus.REJECTED;
            default:
                throw new IllegalArgumentException("Invalid TransferStatus status " + status);
        }
    }

}
