package org.interledger.codecs.btp;

import org.interledger.codecs.Codec;
import org.interledger.codecs.btp.packettypes.BtpPacketType;
import org.interledger.codecs.packettypes.InterledgerPacketType;

/**
 * An implementation of {@link Codec} that reads and writes instances of
 * {@link BtpPacketType}.
 */
public interface BtpPacketTypeCodec extends Codec<BtpPacketType> {

}
