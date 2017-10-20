package org.interledger.btp;

import java.util.List;

public interface BtpPacketBase extends BilateralTransferProtocolPacket {

  /**
   * The sequence of SubProtocolData in the message.
   *
   * @return An list of {@link SubProtocolData}.
   */
  List<SubProtocolData> getSubProtocolData();

}
