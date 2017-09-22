package org.interledger.codecs.btp.packettypes;

import org.interledger.codecs.btp.packettypes.BilateralTransferProtocolPacketType
    .AbstractBilateralTransferProtocolPacketType;

public class RejectPacketType extends AbstractBilateralTransferProtocolPacketType
    implements BilateralTransferProtocolPacketType {

  /**
   * No-args Constructor.
   */
  public RejectPacketType() {
    super(BTP_REJECT_PACKET_TYPE);
  }
}
