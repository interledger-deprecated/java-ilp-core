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

  //US-ASCII is basically IA5 and we're only using a limited set
  private static Charset IA5 = Charset.forName("US-ASCII");
  private static Pattern REGEX = Pattern.compile("^[a-zA-Z0-9._~-]+$");
  
  private String address;
  
  public InterledgerAddress(byte[] binaryData) {
    address = new String(binaryData, IA5);
    if(!isValid(address)) {
      throw new IllegalArgumentException("Invalid characters in address.");
    }
  }
  
  public InterledgerAddress(String address) {
    if(!isValid(address)) {
      throw new IllegalArgumentException("Invalid characters in address.");
    }
    this.address = address;
  }
  
  public static InterledgerAddress fromPrefixAndPath(InterledgerAddress ledgerPrefix, String path) {
    if(!isValid(path)) {
      throw new IllegalArgumentException("Invalid characters in path.");
    }
    
    String prefix = ledgerPrefix.toString();
    if(!isLedgerPrefix(prefix)) {
      throw new IllegalArgumentException("A ledger prefix should end in '.' (dot).");
    }
    
    if(path.startsWith(prefix)) {
      return new InterledgerAddress(path);
    }
    
    return new InterledgerAddress(prefix + path);
    
  }
    
  public InterledgerAddress trimPrefix(InterledgerAddress prefix) {
    String prefixStr = prefix.toString();
   
    if(!isLedgerPrefix(prefixStr)) {
      throw new IllegalArgumentException("A ledger prefix should end in '.' (dot).");
    }

    if(!address.startsWith(prefixStr)) {
      throw new IllegalArgumentException("Invalid ILP Address prefix [" + prefixStr + "] for address [" + address + "]");
    }
    
    return new InterledgerAddress(address.substring(prefixStr.length()));
    
  }
  
  public boolean startsWith(InterledgerAddress prefix) {
    return address.startsWith(prefix.toString());
  }
  
  public boolean isLedgerPrefix() {
    return InterledgerAddress.isLedgerPrefix(this.address);
  }
  
  public static boolean isLedgerPrefix(String address) {
    return address.endsWith(".");
  }

  public static boolean isValid(String address) {
    if(address == null) {
      return false;
    }
    return REGEX.matcher(address).matches();
  }
  
  public byte[] toByteArray(){
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
