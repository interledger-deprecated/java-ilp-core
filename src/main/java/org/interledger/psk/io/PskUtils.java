package org.interledger.psk.io;

import org.interledger.Fulfillment;
import org.interledger.codecs.CodecContext;
import org.interledger.codecs.CodecContextFactory;
import org.interledger.ilp.InterledgerPayment;
import org.interledger.psk.PskParams;
import org.interledger.psk.model.PskEncryptionHeader;
import org.interledger.psk.model.PskMessage;
import org.interledger.psk.model.PskMessageHeader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

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
  /* the expected length of the receiver id, in bytes */
  public static final int RECEIVER_ID_LENGTH = 8;
  /* the expected length of the shared secret, in bytes */
  public static final int SHARED_KEY_LENGTH = 16;

  /* constant used to generate receiver id from secret */
  public static final String IPR_RECEIVER_ID_STRING = "ilp_psk_receiver_id";
  /* constant used to generate receiver id from secret */
  public static final String PSK_GENERATION_STRING = "ilp_psk_generation";
  /* constant used to generate receiver id from secret */
  public static final String PSK_CONDITION_STRING = "ilp_psk_condition";
  /* constant used to generate receiver id from secret */
  public static final String PSK_ENCRYPTION_STRING = "ilp_key_encryption";

  /*
   * the cipher spec used for encryption and decryption. Note that the RFC calls for PKCS-7, which
   * wikipedia claims is equivalent to PKCS-5
   */
  public static final String CIPHER_SPEC = "AES/GCM/PKCS5Padding";
  /* HMAC SHA-256 spec */
  public static final String HMAC_ALGORITHM = "HmacSHA256";

  /**
   * Generate PSK parameters from receiver secret.
   * 
   * @param receiverSecret The local secret used by the receiver
   * @return PSK parameters derived from secret
   */
  public static PskParams getPskParams(byte[] receiverSecret) {

    final byte[] token = getPskToken();
    final byte[] receiverId = getReceiverId(receiverSecret);
    final byte[] sharedKey = getPreSharedKey(receiverSecret, token);

    return new PskParams() {

      final String pskToken = Base64.getUrlEncoder().encodeToString(token);
      final String pskReceiverId = Base64.getUrlEncoder().encodeToString(receiverId);
      final byte[] pskSharedKey = sharedKey;

      @Override
      public String getToken() {
        return pskToken;
      }

      @Override
      public byte[] getSharedKey() {
        return pskSharedKey;
      }

      @Override
      public String getReceiverId() {
        return pskReceiverId;
      }
    };
  }

  private static byte[] getPskToken() {
    try {
      SecureRandom sr = SecureRandom.getInstanceStrong();
      byte[] nonce = new byte[16];
      sr.nextBytes(nonce);
      return nonce;
    } catch (NoSuchAlgorithmException nsa) {
      throw new RuntimeException("Could not generate secure nonce", nsa);
    }
  }

  private static byte[] getReceiverId(byte[] receiverSecret) {
    return Arrays.copyOf(hmac(receiverSecret, IPR_RECEIVER_ID_STRING), RECEIVER_ID_LENGTH);
  }

  private static byte[] getPreSharedKey(byte[] receiverSecret, byte[] token) {
    byte[] generator = hmac(receiverSecret, PSK_GENERATION_STRING);
    return Arrays.copyOf(hmac(generator, token), SHARED_KEY_LENGTH);
  }

  private static byte[] hmac(byte[] key, byte[] message) {
    try {
      Mac mac = Mac.getInstance(HMAC_ALGORITHM);
      mac.init(new SecretKeySpec(key, HMAC_ALGORITHM));
      return mac.doFinal(message);
    } catch (InvalidKeyException | NoSuchAlgorithmException | IllegalStateException e) {
      throw new RuntimeException("Error getting HMAC", e);
    }
  }

  private static byte[] hmac(byte[] key, String message) {
    try {
      return hmac(key, message.getBytes("UTF8"));
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("Error getting HMAC (couldn't decode message)", e);
    }
  }

  public static SecretKey getEncryptionKey(byte[] sharedKey) {
    return new SecretKeySpec(hmac(sharedKey, PSK_ENCRYPTION_STRING), "AES");
  }

  /**
   * Generate a condition preimage (fulfillment) from the given ILP Packet and shared key.
   * 
   * @param packet An ILP Packet
   * @param sharedKey The PSK
   * 
   * @return The 32 byte preimage to use for an Interledger condition
   */
  public static Fulfillment generateFulfillment(InterledgerPayment packet, byte[] sharedKey) {
    byte[] pskConditionKey = hmac(sharedKey, PSK_CONDITION_STRING);

    // Encode packet
    final CodecContext context = CodecContextFactory.interledger();
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    try {
      context.write(InterledgerPayment.class, packet, outputStream);
    } catch (IOException e) {
      throw new RuntimeException("Error encoding Interledger Packet.", e);
    }

    return new Fulfillment(hmac(pskConditionKey, outputStream.toByteArray()));
  }

  /**
   * Convenience method to extract and validate the Nonce value from the public message headers of a
   * PSK message. The nonce value is validated to be 16 bytes in length.
   * 
   * @param message The message containing a Nonce value.
   * @return The Nonce value from the public header.
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
   * <p>NOTE: May throw an InvalidKeyExcpetion if the Java Cryptography Extension (JCE) Unlimited
   * Strength Jurisdiction Policy Files are not installed.
   * 
   * <p>{@link http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html}
   * 
   * @param key The pre-shared key used to encrypt and decrypt the data.
   * @param nonce The nonce used in the PSK message.
   * @param data The data to encrypt.
   * @return The encrypted data and its accompanying GCM authentication tag.
   * @throws Exception if there is an error encrypting the data
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
   * @param nonce The nonce used in the message.
   * @param encryptedData The encrypted data and authentication tag from the encrypted message.
   * @return The decrypted data.
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

    /*
     * the GCM java implementation has some weirdness to make it fit with the broad crypto api. To
     * validate the data, we must append the authentication tag to the end of the encrypted data
     */

    cipher.update(encryptedData.getEncryptedData());

    /* this should decrypt the data and verify the authentication tag at the same time */
    return cipher.doFinal(encryptedData.getAuthenticationTag());
  }

  /**
   * Simple POJO to hold the GCM encrypted content of the private message portion along with the GCM
   * authorization tag.
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
