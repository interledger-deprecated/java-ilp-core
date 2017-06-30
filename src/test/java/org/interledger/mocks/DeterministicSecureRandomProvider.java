package org.interledger.mocks;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Provider;
import java.security.Security;

public class DeterministicSecureRandomProvider extends Provider {

  private static final long serialVersionUID = 5364295470454144673L;
  
  private static final String PROVIDER_NAME = "DeterministicSecureRandomProvider";
  private static final String ALGO_NAME = "DeterministicPRNG";
  
  
  public static byte[] SEED;
  public static String PREVIOUS_STRONG_ALGO;
  
  protected DeterministicSecureRandomProvider(byte[] seed) {
    super(PROVIDER_NAME, 1.0, null);
    SEED = seed;
    put("SecureRandom." + ALGO_NAME, DeterministicSecureRandomAlgorithm.class.getName());
  }
  
  public static void setAsDefault(byte[] seed) {
    
    PREVIOUS_STRONG_ALGO = AccessController.doPrivileged(
        new PrivilegedAction<String>() {
            @Override
            public String run() {
                return Security.getProperty(
                    "securerandom.strongAlgorithms");
            }
        });
        
    Security.insertProviderAt(new DeterministicSecureRandomProvider(seed), 1);
    
    AccessController.doPrivileged(
        new PrivilegedAction<String>() {
          @Override
          public String run() {
            
            final String property = ALGO_NAME + ":" + PROVIDER_NAME;
            
              Security.setProperty(
                  "securerandom.strongAlgorithms",
                  property);
              return property;
              
          }
      });
  }
  
  public static void remove() {
    
    AccessController.doPrivileged(
        new PrivilegedAction<String>() {
          @Override
          public String run() {
                        
              Security.setProperty(
                  "securerandom.strongAlgorithms",
                  PREVIOUS_STRONG_ALGO);
              return PREVIOUS_STRONG_ALGO;
              
          }
      });
    
    Security.removeProvider(PROVIDER_NAME);
  }

}
