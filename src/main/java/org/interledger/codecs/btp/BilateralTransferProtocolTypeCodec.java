package org.interledger.codecs.btp;

import org.interledger.codecs.Codec;
import org.interledger.codecs.btp.packettypes.BilateralTransferProtocolPacketType;
import org.interledger.codecs.packettypes.InterledgerPacketType;

/**
 * An implementation of {@link Codec} that reads and writes instances of
 * {@link InterledgerPacketType}.
 */
public interface BilateralTransferProtocolTypeCodec extends Codec<BilateralTransferProtocolPacketType> {

}
