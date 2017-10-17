package org.interledger.cryptoconditions;

import static org.interledger.cryptoconditions.CryptoConditionType.PREIMAGE_SHA256;
import static org.interledger.cryptoconditions.InterledgerSha256Fulfillment.INTERLEDGER_FULFILLMENT_LENGTH;

import java.util.Objects;

/**
 * This is an alternative implementation of a PREIMAGE SHA-256 condition which is the standard condition type
 * for ILP. This condition will always have a cost of 32.
 */
public final class InterledgerSha256Condition extends Sha256Condition implements SimpleCondition {

    /**
     * Constructs an instance of {@link InterledgerSha256Condition} using a fingerprint. Note
     * that this constructor variant does not include a preimage, and is thus intended to be used
     * to construct a condition that does not include a preimage (for example, if a condition is
     * supplied by a remote system).
     * <p/>
     *
     * @param fingerprint An instance of byte array that contains the calculated fingerprint
     */
    public InterledgerSha256Condition(final byte[] fingerprint) {
        super(PREIMAGE_SHA256, INTERLEDGER_FULFILLMENT_LENGTH, fingerprint);
    }

}