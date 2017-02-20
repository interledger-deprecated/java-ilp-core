package org.interledger.ilp;

import java.nio.charset.Charset;
import java.util.regex.Pattern;

/**
 * An interledger account address.
 * 
 * @author adrianhopebailie
 *
 */
public class InterledgerAddress {

  // US-ASCII is basically IA5 and we're only using a limited set
  private static Charset IA5 = Charset.forName("US-ASCII");
  private static Pattern REGEX = Pattern.compile("^[a-zA-Z0-9._~-]+$");

  private String address;

  /**
   * Constructs an Interledger Address from the binary data assumed to be encoded in the 
   * IA5 character set (currently treated as US-ASCII).
   * @param binaryData
   *    The raw data representing a IA5/US-ASCII encoded string Interledger address.
   */
  public InterledgerAddress(byte[] binaryData) {
    address = new String(binaryData, IA5);
    if (!isValid(address)) {
      throw new IllegalArgumentException("Invalid characters in address.");
    }
  }

  /**
   * Constructs an Interledger Address from the given string.
   * @param address
   *    A string representing an Interledger address
   */
  public InterledgerAddress(String address) {
    if (!isValid(address)) {
      throw new IllegalArgumentException("Invalid characters in address.");
    }
    this.address = address;
  }

  /**
   * Forms an Interledger Address based on a ledger prefix and a path. For example, given
   * the prefix 'ilpdemo.red.' and path 'bob', the method will return the address 'ilpdemo.red.bob'
   * 
   * @param ledgerPrefix
   *    The ledger prefix that should form part of the final Interledger address
   * @param path
   *    A path to add to the prefix to form a final Interledger address.
   * @return
   *    An Interledger address that is the combination of the ledger prefix and the given path.
   */
  public static InterledgerAddress fromPrefixAndPath(InterledgerAddress ledgerPrefix, String path) {
    if (!isValid(path)) {
      throw new IllegalArgumentException("Invalid characters in path.");
    }

    //TODO: why this restriction - cant we infer that it should end in a '.' if it  doesnt already?
    String prefix = ledgerPrefix.toString();
    if (!isLedgerPrefix(prefix)) {
      throw new IllegalArgumentException("A ledger prefix should end in '.' (dot).");
    }

    if (path.startsWith(prefix)) {
      return new InterledgerAddress(path);
    }

    return new InterledgerAddress(prefix + path);
  }

  /**
   * Forms an Interledger Address by removing the given prefix from the current address. 
   * For example, given the address 'ilpdemo.red.bob' and prefix 'ilpdemo.red.', the method will 
   * return the address 'bob'
   * 
   * @param prefix
   *    The prefix to remove from the current Interledger address.
   * @return
   *    A new Interledger address with the given prefix removed.
   */
  public InterledgerAddress trimPrefix(InterledgerAddress prefix) {
    String prefixStr = prefix.toString();

    //TODO: why this restriction - cant we infer that it should end in a '.' if it  doesnt already?
    if (!isLedgerPrefix(prefixStr)) {
      throw new IllegalArgumentException("A ledger prefix should end in '.' (dot).");
    }

    if (!address.startsWith(prefixStr)) {
      throw new IllegalArgumentException(
          "Invalid ILP Address prefix [" + prefixStr + "] for address [" + address + "]");
    }

    return new InterledgerAddress(address.substring(prefixStr.length()));
  }

  /**
   * Tests if this Interledger address starts with the specified prefix.
   * 
   * @param prefix
   *    The prefix.
   * @return
   *    True if the address begins with the specified prefix, false otherwise.
   */
  public boolean startsWith(InterledgerAddress prefix) {
    return address.startsWith(prefix.toString());
  }

  /**
   * Tests if this Interledger address represents a ledger prefix.
   * 
   * @return
   *    True if the address is a ledger prefix, false otherwise.
   */
  public boolean isLedgerPrefix() {
    return InterledgerAddress.isLedgerPrefix(this.address);
  }

  /**
   * Tests if the given address represents a ledger prefix.
   * 
   * @return
   *    True if the address is a ledger prefix, false otherwise.
   */
  public static boolean isLedgerPrefix(String address) {
    return address.endsWith(".");
  }

  /**
   * Tests if the given string represents a valid Interledger address. A valid address is a non 
   * empty sequence of upper and lower case US-ASCII characters and numbers including the special 
   * characters '.', '~', '_', and '-'. 
   * 
   * @param address
   *    The address to test.
   * @return
   *    True if the address is valid, false otherwise.
   */
  public static boolean isValid(String address) {
    if (address == null) {
      return false;
    }
    return REGEX.matcher(address).matches();
  }

  /**
   * Returns the address as a sequence of bytes using the IA5 character encoding scheme.
   */
  public byte[] toByteArray() {
    return address.getBytes(IA5);
  }

  @Override
  public String toString() {
    return address;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((address == null) ? 0 : address.hashCode());
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
    InterledgerAddress other = (InterledgerAddress) obj;
    if (address == null) {
      if (other.address != null) {
        return false;
      }
    } else if (!address.equals(other.address)) {
      return false;
    }
    return true;
  }
}
