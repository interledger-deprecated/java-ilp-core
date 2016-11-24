package org.interledger.ilp.core.ledger.model;

public enum TransferRejectedReason {
    REJECTED_BY_RECEIVER,
    TIMEOUT,
    UNABLE_TO_VALIDATE_CONDITION
}
