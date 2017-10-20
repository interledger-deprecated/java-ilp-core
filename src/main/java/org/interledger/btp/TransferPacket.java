package org.interledger.btp;

import java.util.UUID;

public interface TransferPacket extends BtpPacketBase {

  /**
   * The identity of the transfer.
   *
   * @return An instance of {@link UUID}
   */
  UUID getTransferId();

}
