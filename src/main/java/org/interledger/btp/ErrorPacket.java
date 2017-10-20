package org.interledger.btp;

import org.immutables.value.Value.Immutable;

import java.time.Instant;

@Immutable
public interface ErrorPacket extends BtpPacketBase {

  BtpErrorType getError();

  Instant getTriggeredAt();

  byte[] getData();

}
