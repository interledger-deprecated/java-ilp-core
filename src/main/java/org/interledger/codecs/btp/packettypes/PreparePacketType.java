package org.interledger.codecs.btp.packettypes;

import org.interledger.codecs.btp.packettypes.BtpPacketType.AbstractBtpPacketType;

public class PreparePacketType extends AbstractBtpPacketType
    implements BtpPacketType {

  /**
   * No-args Constructor.
   */
  public PreparePacketType() {
    super(BTP_PREPARE_PACKET_TYPE);
  }

}
