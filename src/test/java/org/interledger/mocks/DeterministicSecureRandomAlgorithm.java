package org.interledger.mocks;

import java.security.SecureRandomSpi;

public class DeterministicSecureRandomAlgorithm extends SecureRandomSpi {

  private static final long serialVersionUID = 8914286719772377141L;

  @Override
  protected void engineSetSeed(byte[] seed) {
    for (int i = 0; i < seed.length; i++) {
      int j = i % DeterministicSecureRandomProvider.SEED.length;
      seed[i] = DeterministicSecureRandomProvider.SEED[j];
    }
  }

  @Override
  protected void engineNextBytes(byte[] bytes) {
    for (int i = 0; i < bytes.length; i++) {
      int j = i % DeterministicSecureRandomProvider.SEED.length;
      bytes[i] = DeterministicSecureRandomProvider.SEED[j];
    }
  }

  @Override
  protected byte[] engineGenerateSeed(int numBytes) {
    byte[] bytes = new byte[numBytes];
    for (int i = 0; i < numBytes; i++) {
      int j = i % DeterministicSecureRandomProvider.SEED.length;
      bytes[i] = DeterministicSecureRandomProvider.SEED[j];
    }
    return bytes;
  }

}
