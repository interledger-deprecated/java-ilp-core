package org.interledger.codecs.btp.packettypes;

import org.interledger.codecs.btp.packettypes.BilateralTransferProtocolPacketType
    .AbstractBilateralTransferProtocolPacketType;

public class ErrorPacketType extends AbstractBilateralTransferProtocolPacketType
    implements BilateralTransferProtocolPacketType {

  /**
   * No-args Constructor.
   */
  public ErrorPacketType() {
    super(BTP_ERROR_PACKET_TYPE);
  }

}
