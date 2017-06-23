package org.interledger;

import java.util.Arrays;

/**
 * The execution condition attached to all transfers in an Interledger payment.
 */
public class Condition {

  private byte[] hash;

  /**
   * Create a new {@code Condition} from a SHA-256 hash.
   * 
   * @param hash The SHA-256 hash of a preimage that is used as the fulfillment of this condition
   */
  public Condition(byte[] hash) {

    if (hash == null || hash.length != 32) {
      throw new IllegalArgumentException("Hash must be 32 bytes.");
    }

    this.hash = new byte[32];
    System.arraycopy(hash, 0, this.hash, 0, 32);
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
    byte[] hash = new byte[32];
    System.arraycopy(this.hash, 0, hash, 0, 32);
    return hash;
  }

}
