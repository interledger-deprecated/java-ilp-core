package org.interledger.cryptoconditions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

/**
 * An implementation of {@link Fulfillment} for a crypto-condition fulfillment of type
 * "PREIMAGE-SHA-256" based upon a preimage and the SHA-256 hash function.
 *
 * @see "https://datatracker.ietf.org/doc/draft-thomas-crypto-conditions/"
 */
public class InterledgerSha256Fulfillment extends FulfillmentBase<InterledgerSha256Condition>
        implements Fulfillment<InterledgerSha256Condition> {

    public static final int INTERLEDGER_FULFILLMENT_LENGTH = 32;

    private final InterledgerSha256Condition condition;
    private final byte[] preimage;

    /**
     * Constructs an instance of the fulfillment.
     *
     * @param preimage The preimage associated with the fulfillment.
     */
    public InterledgerSha256Fulfillment(final byte[] preimage) {
        super(CryptoConditionType.PREIMAGE_SHA256);

        Objects.requireNonNull(preimage);
        if(preimage.length != INTERLEDGER_FULFILLMENT_LENGTH) {
            throw new IllegalArgumentException("Preimage must be exactly 32 bytes");
        }
        this.condition = new InterledgerSha256Condition(hashFingerprintContents(preimage));
        this.preimage = Arrays.copyOf(preimage, preimage.length);
    }

    @Override
    public final InterledgerSha256Condition getCondition() {
        return this.condition;
    }

    public final byte[] getPreimage() {
        return Arrays.copyOf(this.preimage, this.preimage.length);
    }

    @Override
    public final boolean verify(final InterledgerSha256Condition condition, final byte[] message) {
        Objects.requireNonNull(condition,
                "Can't verify a InterledgerSha256Fulfillment against a null condition.");
        Objects.requireNonNull(message, "Message must not be null!");

        return getCondition().equals(condition);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }

        InterledgerSha256Fulfillment that = (InterledgerSha256Fulfillment) object;

        if (!condition.equals(that.condition)) {
            return false;
        }
        return preimage.equals(that.preimage);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + condition.hashCode();
        result = 31 * result + preimage.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("InterledgerSha256Fulfillment{");
        sb.append("\ncondition=").append(condition);
        sb.append(", \n\tpreimage='").append(preimage).append('\'');
        sb.append(", \n\ttype=").append(getType());
        sb.append("\n}");
        return sb.toString();
    }

    /**
     * Constructs the fingerprint of this condition by taking the SHA-256 digest of the contents of
     * this condition, per the crypto-conditions RFC.
     *
     * @param fingerprintContents A byte array containing the unhashed contents of this condition
     *                            as assembled per the rules of the RFC.
     * @return A byte array containing the hashed fingerprint.
     */
    protected static final byte[] hashFingerprintContents(final byte[] fingerprintContents) {
        Objects.requireNonNull(fingerprintContents);
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            return messageDigest.digest(fingerprintContents);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
