package org.interledger.codecs.btp.packettypes;


import org.interledger.codecs.btp.packettypes.BilateralTransferProtocolPacketType
    .AbstractBilateralTransferProtocolPacketType;

public class ResponsePacketType extends AbstractBilateralTransferProtocolPacketType
    implements BilateralTransferProtocolPacketType {

  /**
   * No-args Constructor.
   */
  public ResponsePacketType() {
    super(BTP_RESPONSE_PACKET_TYPE);
  }
}
