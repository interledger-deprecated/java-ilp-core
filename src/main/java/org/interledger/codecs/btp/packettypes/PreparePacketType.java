package org.interledger.codecs.btp.packettypes;

import org.interledger.codecs.btp.packettypes.BilateralTransferProtocolPacketType
    .AbstractBilateralTransferProtocolPacketType;

public class PreparePacketType extends AbstractBilateralTransferProtocolPacketType
    implements BilateralTransferProtocolPacketType {

  /**
   * No-args Constructor.
   */
  public PreparePacketType() {
    super(BTP_PREPARE_PACKET_TYPE);
  }

}
