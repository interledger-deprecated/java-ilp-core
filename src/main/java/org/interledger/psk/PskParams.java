package org.interledger.psk;

public interface PskParams {
  String getToken();
  String getReceiverId();
  byte[] getSharedKey();
}
