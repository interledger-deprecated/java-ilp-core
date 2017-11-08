package org.interledger.btp;

import org.interledger.codecs.CodecException;

/**
 * Represents the various BLT errors
 *
 * <pre>
 * T00  UnreachableError
 * Temporary error, indicating the connector cannot process this request. Try again later.
 * F00  NotAcceptedError
 * Data were symantically invalid.
 * F01  InvalidFieldsError
 * At least one field contained structurally invalid data, e.g. timestamp full of garbage chars.
 * F03  TransferNotFoundError
 * The transferId included in the packet does not reference an existing transfer.
 * F04  InvalidFulfillmentError
 * The fulfillment included in the packet does not match the transfer's condition.
 * F05  DuplicateIdError
 * The transferId and method match a previous request, but other data do not.
 * F06  AlreadyRolledBackError
 * The transfer cannot be fulfilled because it has already been rejected or expired.
 * F07  AlreadyFulfilledError
 * The transfer cannot be rejected because it has already been fulfilled.
 * F08  InsufficientBalanceError
 * The transfer cannot be prepared because there is not enough available liquidity.
 * </pre>
 */
public enum BtpErrorType {

  T00_UNREACHABLE("T00", "UnreachableError"),
  F00_NOT_ACCEPTED("F00", "NotAcceptedError"),
  F01_INVALID_FIELDS("F01", "InvalidFieldsError"),
  F03_TRANSFER_NOT_FOUND("F03", "TransferNotFoundError"),
  F04_INVALID_FULFILLMENT("F04", "InvalidFulfillmentError"),
  F05_DUPLICATE_ID("F05", "DuplicateIdError"),
  F06_ALREADY_ROLLED_BACK("F06", "AlreadyRolledBackError"),
  F07_ALREADY_FULFILLED("F07", "AlreadyFulfilledError"),
  F08_INSUFFICIENT_BALANCE("F08", "InsufficientBalanceError"),;

  private final String code;
  private final String name;

  BtpErrorType(String code, String name) {
    this.code = code;
    this.name = name;
  }

  /**
   * Get the BTP Error code.
   *
   * @return the error code as a String
   */
  public String getCode() {
    return code;
  }

  /**
   * Get the BTP Error name.
   *
   * @return the error name as a String
   */
  public String getName() {
    return name;
  }

  /**
   * Get the {@link BtpErrorType} from the code.
   *
   * @param code A BTP Error code as a String
   * @return the {@link BtpErrorType} or throws if unknown
   */
  public static BtpErrorType fromCode(String code) {
    if (T00_UNREACHABLE.code.equalsIgnoreCase(code)) {
      return T00_UNREACHABLE;
    }
    if (F00_NOT_ACCEPTED.code.equalsIgnoreCase(code)) {
      return F00_NOT_ACCEPTED;
    }
    if (F01_INVALID_FIELDS.code.equalsIgnoreCase(code)) {
      return F01_INVALID_FIELDS;
    }
    if (F03_TRANSFER_NOT_FOUND.code.equalsIgnoreCase(code)) {
      return F03_TRANSFER_NOT_FOUND;
    }
    if (F04_INVALID_FULFILLMENT.code.equalsIgnoreCase(code)) {
      return F04_INVALID_FULFILLMENT;
    }
    if (F05_DUPLICATE_ID.code.equalsIgnoreCase(code)) {
      return F05_DUPLICATE_ID;
    }
    if ( F06_ALREADY_ROLLED_BACK.code.equalsIgnoreCase(code)) {
      return F06_ALREADY_ROLLED_BACK;
    }
    if (F07_ALREADY_FULFILLED.code.equalsIgnoreCase(code)) {
      return F07_ALREADY_FULFILLED;
    }
    if (F08_INSUFFICIENT_BALANCE.code.equalsIgnoreCase(code)) {
      return F08_INSUFFICIENT_BALANCE;
    }
    throw new CodecException("Invalid Error Code: " + code);
  }

}
