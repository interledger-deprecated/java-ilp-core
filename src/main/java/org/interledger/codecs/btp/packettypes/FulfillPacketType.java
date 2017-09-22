package org.interledger.codecs.btp.packettypes;

import org.interledger.codecs.btp.packettypes.BilateralTransferProtocolPacketType
    .AbstractBilateralTransferProtocolPacketType;

public class FulfillPacketType extends AbstractBilateralTransferProtocolPacketType
    implements BilateralTransferProtocolPacketType {

  /**
   * No-args Constructor.
   */
  public FulfillPacketType() {
    super(BTP_FULFILL_PACKET_TYPE);
  }
}
