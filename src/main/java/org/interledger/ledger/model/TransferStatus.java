package org.interledger.ledger.model;

/**
 * Enumerates the status of a transfer.
 */
public enum TransferStatus {
  PROPOSED, PREPARED, EXECUTED, REJECTED;

  /**
   * Returns the {@link TransferStatus} associated with the given integer representation as listed
   * below.
   * 
   * <ol>
   * <li>Proposed</li>
   * <li>Prepared</li>
   * <li>Executed</li>
   * <li>Rejected</li>
   * </ol>
   */
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

  /**
   * Parses a status string and returns the corresponding TransferStatus enum value.
   * 
   * @param status A status string to parse. must not be null.
   * @return The corresponding {@link TransferStatus}.
   */
  public static TransferStatus parse(String status) {
    status = status.toLowerCase();
    if ("proposed".equals(status) || "0".equals(status)) {
      return PROPOSED;
    }
    if ("prepared".equals(status) || "1".equals(status)) {
      return PREPARED;
    }
    if ("executed".equals(status) || "2".equals(status)) {
      return EXECUTED;
    }
    if ("rejected".equals(status) || "3".equals(status)) {
      return REJECTED;
    }
    throw new RuntimeException("Can not parse String " + status + " as TransferStatus");
  }


}
