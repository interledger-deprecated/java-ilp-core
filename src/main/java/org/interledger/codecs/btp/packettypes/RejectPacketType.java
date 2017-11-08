package org.interledger.codecs.btp.packettypes;

import org.interledger.codecs.btp.packettypes.BtpPacketType.AbstractBtpPacketType;

public class RejectPacketType extends AbstractBtpPacketType
    implements BtpPacketType {

  /**
   * No-args Constructor.
   */
  public RejectPacketType() {
    super(BTP_REJECT_PACKET_TYPE);
  }
}
