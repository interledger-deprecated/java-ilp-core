package org.interledger.wire.codecs.packets;

import org.interledger.wire.InterledgerPacket;
import org.interledger.wire.InterledgerPacketType;
import org.interledger.wire.codecs.Codec;

/**
 * An implementation of {@link Codec} that reads and writes instances of {@link InterledgerPacket}
 * having a discrete {@link InterledgerPacketType} and data payload.
 */
public interface InterledgerPacketCodec<T> extends Codec<T> {

  /**
   * Accessor for the {@link InterledgerPacketType} of this {@link Codec}.
   */
  InterledgerPacketType getTypeId();
}
