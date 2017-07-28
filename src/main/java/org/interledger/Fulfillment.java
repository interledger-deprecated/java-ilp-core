package org.interledger;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

/**
 * The fulfillment of a {@link Condition}.
 *
 * <p>The standard for Interledger payments is for the fulfillment to be the pre-image of a SHA-256
 * hash (the condition).
 *
 * <p>The fulfillment (pre-image) must be exactly 32 bytes.
 */
public interface Fulfillment {

  /**
   * Get the default builder.
   *
   * @return a {@link Builder} instance.
   */
  static Builder builder() {
    return new Builder();
  }

  /**
   * Build a new Fulfillment using the provided preimage.
   *
   * @param preimage The preimage representing the fulfillment
   *
   * @return a  {@link Fulfillment} instance
   */
  static Fulfillment of(final byte[] preimage) {
    return Fulfillment.builder()
        .preimage(preimage)
        .build();
  }

  /**
   * Get the raw pre-image (safe copy).
   *
   * @return 32 byte octet string
   */
  byte[] getPreimage();

  /**
   * Get the {@link Condition} that is fulfilled by this Fulfillment.
   *
   * @return a condition representing the SHA-256 hash of this preimage.
   */
  Condition getCondition();

  /**
   * Validate a given condition against this fulfillment.
   *
   * @param condition The condition to compare against.
   *
   * @return true if this fulfillment fulfills the given condition.
   */
  boolean validate(Condition condition);

  class Builder {

    private byte[] preimage;

    public static Builder builder() {
      return new Builder();
    }

    public Builder preimage(byte[] preimage) {

      this.preimage = Arrays.copyOf(preimage, preimage.length);
      return this;

    }

    public Fulfillment build() {
      return new Impl(this);
    }

    private static final class Impl implements Fulfillment {

      private final byte[] preimage;
      private final Condition condition;

      private Impl(Builder builder) {

        Objects.requireNonNull(builder.preimage, "Preimage cannot be null.");

        if (builder.preimage.length != 32) {
          throw new IllegalArgumentException("Preimage must be 32 bytes.");
        }

        this.preimage = Arrays.copyOf(builder.preimage, 32);

        try {
          MessageDigest digest = MessageDigest.getInstance("SHA-256");
          byte[] hash = digest.digest(builder.preimage);
          condition = Condition.builder()
              .hash(hash)
              .build();
        } catch (NoSuchAlgorithmException e) {
          throw new InterledgerRuntimeException(e);
        }
      }

      @Override
      public byte[] getPreimage() {
        return Arrays.copyOf(this.preimage, 32);
      }

      @Override
      public Condition getCondition() {
        return condition;
      }

      @Override
      public boolean validate(Condition condition) {
        return this.condition.equals(condition);
      }

      @Override
      public String toString() {
        return "Fulfillment.Impl{"
            + "preimage=" + Arrays.toString(this.preimage)
            + "}";
      }

      @Override
      public int hashCode() {
        return new BigInteger(this.preimage).intValue();
      }


      @Override
      public boolean equals(Object obj) {
        if (this == obj) {
          return true;
        }
        if (obj == null) {
          return false;
        }
        if (getClass() != obj.getClass()) {
          return false;
        }

        Fulfillment other = (Fulfillment) obj;
        if (condition == null) {
          if (other.getCondition() != null) {
            return false;
          }
        } else if (!condition.equals(other.getCondition())) {
          return false;
        }
        return Arrays.equals(preimage, other.getPreimage());
      }
    }

  }
}
