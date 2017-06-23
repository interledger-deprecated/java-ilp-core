package org.interledger;

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
public class Fulfillment {

  private final byte[] preimage;
  private final Condition condition;

  /**
   * Create a new Fulfillment from the 32 byte pre-image that is hashed to get the condition.
   * 
   * @param preimage A 32 byte octet string
   */
  public Fulfillment(byte[] preimage) {

    Objects.requireNonNull(preimage, "Pre-image must not be null!");
    if (preimage.length != 32) {
      throw new IllegalArgumentException("Pre-image must be 32 bytes.");
    }

    this.preimage = Arrays.copyOf(preimage, 32);

    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(preimage);
      condition = new Condition(hash);

    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Get the raw pre-image (safe copy).
   * 
   * @return 32 byte octet string
   */
  public byte[] getPreimage() {
    return Arrays.copyOf(this.preimage, 32);
  }

  /**
   * Get the {@link Condition} that is fulfilled by this Fulfillment.
   * 
   * @return a condition representing the SHA-256 hash of this preimage.
   */
  public Condition getCondition() {
    return condition;
  }

  /**
   * Validate a given condition against this fulfillment.
   * 
   * @param condition The condition to compare against.
   * 
   * @return true if this fulfillment fulfills the given condition.
   */
  public boolean validate(Condition condition) {
    return this.condition.equals(condition);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((condition == null) ? 0 : condition.hashCode());
    result = prime * result + Arrays.hashCode(preimage);
    return result;
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
      if (other.condition != null) {
        return false;
      }
    } else if (!condition.equals(other.condition)) {
      return false;
    }
    if (!Arrays.equals(preimage, other.preimage)) {
      return false;
    }

    return true;
  }

}
