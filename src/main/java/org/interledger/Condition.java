package org.interledger;

import java.net.URI;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The execution condition attached to all transfers in an Interledger payment.
 * 
 * <p>Interledger relies on conditional payments where each transfer that is part of a payment is
 * conditional upon the presentation of a fulfillment.
 * 
 * <p>The standard for conditions is to use the SHA-256 hash of a pre-image. The pre-image is
 * therefor the fulfillment of the condition.
 * 
 * @see Fulfillment
 */
public class Condition {

  public static final String CONDITION_REGEX_STRICT = "^ni://([A-Za-z0-9_-]?)/sha-256;([a-zA-Z0-9_-]{0,86})\\?(.+)$";


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

  /**
   * Parses a URI formatted crypto-condition
   *
   * @param uri
   *  The crypto-condition formatted as a uri.
   * @return
   *  The crypto condition
   */
  public static Condition parse(URI uri) {
    //based strongly on the five bells implementation at 
    //https://github.com/interledgerjs/five-bells-condition (7b6a97990cd3a51ee41b276c290e4ae65feb7882)
    
    if (!"ni".equals(uri.getScheme())) {
      throw new RuntimeException("Serialized condition must start with 'ni:'");
    }
    
    //the regex covers the entire uri format including the 'ni:' scheme
    Matcher m = Pattern.compile(CONDITION_REGEX_STRICT).matcher(uri.toString());
    
    if (!m.matches()) {
      throw new RuntimeException("Invalid condition format");
    }

    byte[] fingerprint = Base64.getUrlDecoder().decode(m.group(2));
    return new Condition(fingerprint);
  }
}
