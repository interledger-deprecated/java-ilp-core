package org.interledger.codecs.btp.packettypes;

import org.interledger.codecs.btp.packettypes.BtpPacketType.AbstractBtpPacketType;

public class ErrorPacketType extends AbstractBtpPacketType
    implements BtpPacketType {

  /**
   * No-args Constructor.
   */
  public ErrorPacketType() {
    super(BTP_ERROR_PACKET_TYPE);
  }

}
