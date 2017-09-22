package org.interledger.btp;

public enum SubProtocolContentType {
  APPLICATION_OCTET_STREAM  (0),
  TEXT_PLAIN_UTF8           (1),
  APPLICATION_JSON          (2);

  private final int code;

  SubProtocolContentType(int i) {
    code = i;
  }

  public int getCode() {
    return code;
  }

  public static SubProtocolContentType fromCode(int code) {
    switch (code) {
      case 0 :
        return SubProtocolContentType.APPLICATION_OCTET_STREAM;
      case 1 :
        return SubProtocolContentType.TEXT_PLAIN_UTF8;
      case 2 :
        return SubProtocolContentType.APPLICATION_JSON;
      default:
        throw new BilateralTransferProtocolRuntimeException("Unknown Content Type code: " + code);
    }
  }

}
