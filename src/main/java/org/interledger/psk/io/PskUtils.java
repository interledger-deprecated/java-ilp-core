package org.interledger.psk.io;

import org.interledger.psk.model.PskEncryptionHeader;
import org.interledger.psk.model.PskMessage;
import org.interledger.psk.model.PskMessageHeader;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/**
 * Convenience methods for PSK messages.
 */
public class PskUtils {
  
  /* the expected length of the auth tag, in bytes */
  public static final int AUTH_TAG_LEN_BYTES = 16;
  /* the expected AES key length (256 bits), in bytes */
  public static final int AES_KEY_LEN_BYTES = 256 / 8;
  /* the expected length of the nonce, in bytes */
  public static final int NONCE_LEN_BYTES = 16;
  
  /* the cipher spec used for encryption and decryption. Note that the RFC calls for PKCS-7,
   * which wikipedia claims is equivalent to PKCS-5 */
  public static final String CIPHER_SPEC = "AES/GCM/PKCS5Padding";
  
  /**
   * Convenience method to extract and validate the Nonce value from the public message headers of 
   * a PSK message. The nonce value is validated to be 16 bytes in length.
   * 
   * @param message The message containing a Nonce value.
   * @return    The Nonce value from the public header.
   */
  public static byte[] getNonce(PskMessage message) {
    Objects.requireNonNull(message, "PSK message must not be null");

    List<PskMessageHeader> nonceHeaders =
        message.getPublicHeaders(PskMessageHeader.PublicHeaders.NONCE);
    
    if (nonceHeaders.isEmpty()) {
      throw new IllegalArgumentException(
          "Invalid PSK message - mandatory public Nonce header missing.");
    }
    
    if (nonceHeaders.size() != 1) {
      throw new IllegalArgumentException(
          "Invalid PSK message - more than one mandatory public Nonce header present.");
    }
    
    byte[] nonce = Base64.getUrlDecoder().decode(nonceHeaders.get(0).getValue());
    
    if (nonce == null || nonce.length != 16) {
      throw new IllegalArgumentException("Invalid PSK message - Nonce value must be 16 bytes");
    }
    
    return nonce;
  }
  
  /**
   * Convenience method to extract the encryption header from the PSK message.
   * 
   * @param message The message containing the encryption header.
   * @return The encryption header, provided that exactly one is present in the public header
   *         portion of the message.
   */
  public static PskEncryptionHeader getEncryptionHeader(PskMessage message) {
    Objects.requireNonNull(message, "PSK message must not be null");

    List<PskMessageHeader> encryptionHeaders =
        message.getPublicHeaders(PskMessageHeader.PublicHeaders.ENCRYPTION);

    if (encryptionHeaders.isEmpty()) {
      throw new IllegalArgumentException(
          "Invalid PSK message - mandatory public Encryption header missing.");
    }

    if (encryptionHeaders.size() != 1) {
      throw new IllegalArgumentException(
          "Invalid PSK message - more than one mandatory public Encryption header present.");
    }

    return new PskEncryptionHeader(encryptionHeaders.get(0).getName(),
        encryptionHeaders.get(0).getValue());
  }
  
  /**
   * Encrypts a block of data using the encryption scheme specific in the PSK RFC.
   * 
   * @param key The pre-shared key used to encrypt and decrypt the data.
   * @param nonce The nonce used in the PSK message.
   * @param data The data to encrypt.
   * @return The encrypted data and its accompanying GCM authentication tag.
   */
  public static GcmEncryptedData encryptPskData(SecretKey key, byte[] nonce, byte[] data)
      throws Exception {

    Cipher cipher = Cipher.getInstance(CIPHER_SPEC);

    /* convert the auth tag length to bits */
    GCMParameterSpec spec = new GCMParameterSpec(AUTH_TAG_LEN_BYTES * 8, nonce);

    cipher.init(Cipher.ENCRYPT_MODE, key, spec);

    byte[] encrypted = cipher.doFinal(data);

    /*
     * extract the auth tag, see
     * http://stackoverflow.com/questions/23864440/aes-gcm-implementation-with-authentication-tag-in
     * -java#26370130
     */

    byte[] authBytes =
        Arrays.copyOfRange(encrypted, encrypted.length - AUTH_TAG_LEN_BYTES, encrypted.length);

    /*
     * remove the auth tag from the encrypted data, since other implementations probably wont
     * understand proprietary java weirdness..
     */
    encrypted = Arrays.copyOf(encrypted, encrypted.length - AUTH_TAG_LEN_BYTES);

    return new GcmEncryptedData(encrypted, authBytes);
  }
  
  /**
   * Decrypts an encrypted data block from a PSK message, according to the rules in the PSK RFC.
   * 
   * @param key The pre-shared key used to encrypt and decrypt the data.
   * @param nonce   The nonce used in the message.
   * @param encryptedData   The encrypted data and authentication tag from the encrypted message.
   * @return    The decrypted data.
   */
  public static byte[] decryptPskData(SecretKey key, byte[] nonce, GcmEncryptedData encryptedData)
      throws Exception {
    
    Objects.requireNonNull(key, "cannot decrypt data without the key");
    Objects.requireNonNull(encryptedData, "cannot decrypt null data");
    
    if (nonce == null || nonce.length != NONCE_LEN_BYTES) {
      throw new RuntimeException("Invalid PSK message - nonce must be " + NONCE_LEN_BYTES);
    }

    Cipher cipher = Cipher.getInstance(CIPHER_SPEC);
    GCMParameterSpec spec = new GCMParameterSpec(AUTH_TAG_LEN_BYTES * 8, nonce);
    cipher.init(Cipher.DECRYPT_MODE, key, spec);

    /* the GCM java implementation has some weirdness to make it fit with the broad crypto api.
     * To validate the data, we must append the authentication tag to the end of the encrypted
     * data */
  
    cipher.update(encryptedData.getEncryptedData());
  
    /* this should decrypt the data and verify the authentication tag at the same time */
    return cipher.doFinal(encryptedData.getAuthenticationTag());
  }
  
  /**
   * Simple POJO to hold the GCM encrypted content of the private message portion along with the 
   * GCM authorization tag.
   */
  public static class GcmEncryptedData {
    private byte[] encryptedData;
    private byte[] authTag;
    
    public GcmEncryptedData(byte[] encryptedData, byte[] authTag) {
      this.encryptedData = encryptedData;
      this.authTag = authTag;
    }
    
    public byte[] getEncryptedData() {
      return encryptedData;
    }
    
    public byte[] getAuthenticationTag() {
      return authTag;
    }
  }

}
