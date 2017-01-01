package org.interledger.ilp.core;

import java.nio.charset.Charset;
import java.util.regex.Pattern;

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

  public static boolean isValid(String address) {
    return REGEX.matcher(address).matches();
  }
  
  public byte[] toByteArray(){
    return address.getBytes(IA5);
  }
  
  @Override
  public String toString() {
    return address;
  }  
  
}
