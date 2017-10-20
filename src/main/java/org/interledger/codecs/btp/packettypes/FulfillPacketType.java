package org.interledger.codecs.btp.packettypes;

import org.interledger.codecs.btp.packettypes.BtpPacketType.AbstractBtpPacketType;

public class FulfillPacketType extends AbstractBtpPacketType
    implements BtpPacketType {

  /**
   * No-args Constructor.
   */
  public FulfillPacketType() {
    super(BTP_FULFILL_PACKET_TYPE);
  }
}
