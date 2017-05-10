package org.interledger.wire.codecs.packets;

import org.interledger.wire.InterledgerPacketType;
import org.interledger.wire.codecs.Codec;

/**
 * An implementation of {@link Codec} that reads and writes instances of {@link
 * InterledgerPacketType}.
 */
public interface InterledgerPacketTypeCodec extends Codec<InterledgerPacketType> {

}
