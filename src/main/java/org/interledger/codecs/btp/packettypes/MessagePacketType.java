package org.interledger.codecs.btp.packettypes;

import org.interledger.codecs.btp.packettypes.BilateralTransferProtocolPacketType
    .AbstractBilateralTransferProtocolPacketType;

public class MessagePacketType extends AbstractBilateralTransferProtocolPacketType
    implements BilateralTransferProtocolPacketType {

  /**
   * No-args Constructor.
   */
  public MessagePacketType() {
    super(BTP_MESSAGE_PACKET_TYPE);
  }
}
