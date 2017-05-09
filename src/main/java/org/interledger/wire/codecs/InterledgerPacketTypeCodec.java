package org.interledger.wire.codecs;

import org.interledger.wire.InterledgerPacketType;

/**
 * An implementation of {@link Codec} that reads and writes instances of {@link
 * InterledgerPacketType}.
 */
public interface InterledgerPacketTypeCodec extends Codec<InterledgerPacketType> {

}
