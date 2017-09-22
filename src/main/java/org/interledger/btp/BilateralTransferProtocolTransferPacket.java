package org.interledger.btp;

import java.util.UUID;

public interface BilateralTransferProtocolTransferPacket extends BilateralTransferProtocolPacket {

  UUID getTransferId();

}
