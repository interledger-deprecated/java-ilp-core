package org.interledger;

import java.util.Arrays;
import java.util.Objects;

/**
 * The execution condition attached to all transfers in an Interledger payment.
 * 
 * <p>Interledger relies on conditional payments where each transfer that is part of a payment is
 * conditional upon the presentation of a fulfillment.
 * 
 * <p>The standard for conditions is to use the SHA-256 hash of a pre-image. The pre-image is therefor
 * the fulfillment of the condition.
 * 
 * @see Fulfillment
 */
public class Condition {

  private final byte[] hash;

  /**
   * Create a new {@code Condition} from a SHA-256 hash.
   * 
   * @param hash The SHA-256 hash of a pre-image that is used as the fulfillment of this condition
   */
  public Condition(byte[] hash) {

    Objects.requireNonNull(hash, "Hash must not be null!");
    if (hash.length != 32) {
      throw new IllegalArgumentException("Hash must be 32 bytes.");
    }

    this.hash = Arrays.copyOf(hash, 32);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(hash);
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

    Condition other = (Condition) obj;
    if (!Arrays.equals(hash, other.hash)) {
      return false;
    }

    return true;
  }

  /**
   * Get the SHA-256 hash of this condition.
   * 
   * @return a {@code byte[]} of exactly 32 bytes
   */
  public byte[] getHash() {
    return Arrays.copyOf(this.hash, 32);
  }

}
