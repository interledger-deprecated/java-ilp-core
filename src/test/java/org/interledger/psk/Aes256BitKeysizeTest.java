package org.interledger.psk;

import org.junit.Test;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Test that locally installed provider is working as expected.
 * 
 * <p>To use 256-bit AES keys you must have Java Cryptography Extension (JCE) Unlimited Strength
 * Jurisdiction Policy Files installed.
 * 
 * <p>{@link http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html}
 */
public class AesEncryptionTest {

  @Test
  public final void test()
      throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
      InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

    SecureRandom sr = SecureRandom.getInstanceStrong();
    byte[] nonce = new byte[16];
    sr.nextBytes(nonce);

    // byte[] key = new byte[32];
    // sr.nextBytes(key);

    KeyGenerator keygen = KeyGenerator.getInstance("AES");
    keygen.init(256);
    byte[] key = keygen.generateKey().getEncoded();

    byte[] data = new byte[256];
    sr.nextBytes(data);

    Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5Padding");
    GCMParameterSpec paramSpec = new GCMParameterSpec(128, nonce);
    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), paramSpec);
    byte[] encrypted = cipher.doFinal(data);
    byte[] authBytes = Arrays.copyOfRange(encrypted, encrypted.length - 16, encrypted.length);
    encrypted = Arrays.copyOf(encrypted, encrypted.length - 16);

  }

}
