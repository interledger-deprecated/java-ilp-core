package org.interledger.codecs.btp.packettypes;

import org.interledger.codecs.btp.packettypes.BtpPacketType.AbstractBtpPacketType;

public class MessagePacketType extends AbstractBtpPacketType
    implements BtpPacketType {

  /**
   * No-args Constructor.
   */
  public MessagePacketType() {
    super(BTP_MESSAGE_PACKET_TYPE);
  }
}
