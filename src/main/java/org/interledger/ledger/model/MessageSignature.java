package org.interledger.ledger.model;

import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;

public interface MessageSignature {
  
  public String getAlgorithm();
  
  public AlgorithmParameterSpec getAlgorithmParameters();
  
  public PublicKey getPublicKey();
  
  public byte[] getSignature();

}
