package org.interledger.psk;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.interledger.Fulfillment;
import org.interledger.InterledgerAddress;
import org.interledger.ilp.InterledgerPayment;
import org.interledger.mocks.DeterministicSecureRandomProvider;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public class PskContextTest {

  // Known good values (mostly) taken from JS tests

  private static final byte[] TEST_SECRET =
      Arrays.copyOf("secret".getBytes(StandardCharsets.UTF_8), 32);
  private static final byte[] TEST_TOKEN =
      Arrays.copyOf(Base64.getUrlDecoder().decode("PE7rnGiULIrfu655nwSYew"), 16);
  private static final byte[] TEST_RECEIVER_ID =
      Arrays.copyOf(Base64.getUrlDecoder().decode("ebKWcAEB9_A"), 8);
  private static final byte[] TEST_SHARED_KEY = Arrays
      .copyOf(Base64.getUrlDecoder().decode("66iH2jKo-lMSs55jU8fH3Tm-G_rf9aDi-Q3bu6gddGM"), 32);
  private static final byte[] TEST_ENCRYPTION_KEY_DATA = Arrays
      .copyOf(Base64.getUrlDecoder().decode("Tb91YcJtGAbGPoABs8kLYMgPbFFnFjl0beZmeWkQ0eU"), 32);
  private static final byte[] TEST_FULFILLMENT_MAC_KEY = Arrays
      .copyOf(Base64.getUrlDecoder().decode("MzO6CHRGJLiId--y5j5s2iqgTFs0hVSfGAPVkciHOwQ"), 32);
  private static final byte[] TEST_PREIMAGE = Arrays
      .copyOf(Base64.getUrlDecoder().decode("RylP3qjMOvVpj1rEOwHNLgI2clR9svdc6YRhtRX_afs"), 32);
  private static final byte[] TEST_AUTHENTICATION_TAG =
      Arrays.copyOf(Base64.getUrlDecoder().decode("W515ugM9MdC6s-Ds12R9Ig"), 16);
  private static final byte[] TEST_NONCE =
      Arrays.copyOf(Base64.getUrlDecoder().decode("ZGfSouOpCk-5WBfGAZya4w"), 16);
  private static final byte[] TEST_DATA = "test data".getBytes(StandardCharsets.UTF_8);
  private static final byte[] ENCRYPTED_TEST_DATA =
      Arrays.copyOf(Base64.getUrlDecoder().decode("h0GsnMI28dhGxPAiD7YNfnHbL8M58WvyQQ"), 25);   
  private static InterledgerAddress TEST_ADDRESS_PREFIX =
      InterledgerAddress.from("test1.example.alice");
  private static InterledgerAddress TEST_ADDRESS =
      InterledgerAddress.from("test1.example.alice.ebKWcAEB9_APE7rnGiULIrfu655nwSYew");


  private void assertContextIsValid(PskContext context) {

    assertArrayEquals("Invalid token", TEST_TOKEN, context.getToken());
    assertArrayEquals("Invalid Receiver ID.", TEST_RECEIVER_ID, context.getReceiverId());
    assertArrayEquals("Invalid Shared key", TEST_SHARED_KEY, context.getSharedKey());
    assertArrayEquals("Invalid Encryption key content", TEST_ENCRYPTION_KEY_DATA,
        context.getEncryptionKey().getEncoded());
    assertEquals("Invalid Encryption key algorithm", "AES",
        context.getEncryptionKey().getAlgorithm());
    assertArrayEquals("Invalid Fulfillment HMAC key", TEST_FULFILLMENT_MAC_KEY,
        context.getFulfillmentHmacKey());

  }


  @Test
  public final void testFromSeed() {

    DeterministicSecureRandomProvider.setAsDefault(TEST_TOKEN);

    assertContextIsValid(PskContext.seed(TEST_SECRET));

    DeterministicSecureRandomProvider.remove();

  }

  @Test
  public final void testFromReceiverAddress() {

    assertContextIsValid(PskContext.fromReceiverAddress(TEST_SECRET, TEST_ADDRESS));

  }

  @Test
  public final void testDecryptMessage() {

    PskMessage encryptedMessage = PskMessage.builder()
        .addPublicHeader(PskEncryptionHeader.aesGcm(TEST_AUTHENTICATION_TAG))
        .addPublicHeader(PskNonceHeader.fromNonce(TEST_NONCE)).data(ENCRYPTED_TEST_DATA).build();

    PskMessage decryptedMessage =
        PskContext.fromToken(TEST_SECRET, TEST_TOKEN).decryptMessage(encryptedMessage);

    assertEquals("Invalid encryption type.", PskEncryptionType.NONE,
        decryptedMessage.getEncryptionHeader().getEncryptionType());
    assertEquals("Incorrect number of private headers.", 1,
        decryptedMessage.getPrivateHeaders().size());
    assertArrayEquals("Incorrect data.", decryptedMessage.getData(), TEST_DATA);

  }

  @Test
  public final void testEncryptMessage() {
    PskMessage decryptedMessage = PskMessage.builder().addPublicHeader(PskEncryptionHeader.none())
        .addPublicHeader(PskNonceHeader.fromNonce(TEST_NONCE)).addPrivateHeader("Private", "SECRET")
        .data(TEST_DATA).build();

    PskMessage encryptedMessage =
        PskContext.fromToken(TEST_SECRET, TEST_TOKEN).encryptMessage(decryptedMessage);

    assertEquals("Invalid encryption type.", PskEncryptionType.AES_256_GCM,
        encryptedMessage.getEncryptionHeader().getEncryptionType());
    assertEquals("Incorrect number of private headers.", 0,
        encryptedMessage.getPrivateHeaders().size());
    assertArrayEquals("Incorrect data.", encryptedMessage.getData(), ENCRYPTED_TEST_DATA);
  }

  @Test
  public final void testGenerateReceiverAddress() {

    PskContext context = PskContext.fromReceiverAddress(TEST_SECRET, TEST_ADDRESS);
    assertEquals("Incorrect address generated.",
        context.generateReceiverAddress(TEST_ADDRESS_PREFIX), TEST_ADDRESS);
  }

  @Test
  public final void testGenerateFulfillment() {

    PskContext context = PskContext.fromReceiverAddress(TEST_SECRET, TEST_ADDRESS);

    InterledgerPayment payment = InterledgerPayment.builder().destinationAccount(TEST_ADDRESS)
        .destinationAmount(100L).data(new byte[] {}).build();

    Fulfillment fulfillment = context.generateFulfillment(payment);

    assertArrayEquals("Incorrect fulfillment.", fulfillment.getPreimage(), TEST_PREIMAGE);

  }


}
