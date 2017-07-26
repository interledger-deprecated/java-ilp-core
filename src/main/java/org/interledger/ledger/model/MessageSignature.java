package org.interledger.ledger.model;

import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;

public interface MessageSignature {

  String getAlgorithm();

  AlgorithmParameterSpec getAlgorithmParameters();

  PublicKey getPublicKey();

  byte[] getSignature();

}
