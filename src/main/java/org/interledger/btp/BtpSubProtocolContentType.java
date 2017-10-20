package org.interledger.btp;

public enum BtpSubProtocolContentType {
  APPLICATION_OCTET_STREAM(0),
  TEXT_PLAIN_UTF8(1),
  APPLICATION_JSON(2);

  private final int code;

  BtpSubProtocolContentType(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }

  /**
   * Get a {@link BtpSubProtocolContentType} from the code.
   * @param code the BTP Subprotocol Content-Type code
   *
   * @return An instance of {@link BtpSubProtocolContentType}
   */
  public static BtpSubProtocolContentType fromCode(int code) {
    switch (code) {
      case 0 :
        return BtpSubProtocolContentType.APPLICATION_OCTET_STREAM;
      case 1 :
        return BtpSubProtocolContentType.TEXT_PLAIN_UTF8;
      case 2 :
        return BtpSubProtocolContentType.APPLICATION_JSON;
      default:
        throw new BtpRuntimeException("Unknown Content Type code: " + code);
    }
  }

}
