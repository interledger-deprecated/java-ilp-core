package org.interledger.psk;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.interledger.psk.io.EncryptedPskMessageReader;
import org.interledger.psk.model.PskMessage;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * JUnit to exercise the {@link EncryptedPskMessageReader} implementation.
 */
public class EncryptedMessageReaderTest {

  /* to make testing easier, we set the sharedSecretKey as all 0's */
  public static final byte[] key = new byte[256 / 8]; /* 256 bits long */
  
  @Test()
  public void test_NoEncryption() {
    String testMessage = "PSK/1.0\n"
        + "Encryption: none\n"
        + "Testheader: test value\n"
        + "\n"
        + "PrivateHeader1:      some value\n"
        + "\n"
        + "binary data goes here";
    
    /* encrypted readers should be perfectly capable of reading unencrypted psk messages */
    PskMessageReader reader = PskReaderFactory.getEncryptedReader(new byte[256 / 8]);
    
    PskMessage message = reader.readMessage(testMessage.getBytes(StandardCharsets.UTF_8));
    
    assertNotNull(message);
    assertEquals(2, message.getPublicHeaders().size());
    assertEquals("Testheader", message.getPublicHeaders().get(1).getName());
    assertEquals("test value", message.getPublicHeaders().get(1).getValue());
    
    assertEquals(1, message.getPrivateHeaders().size());
    assertEquals("PrivateHeader1", message.getPrivateHeaders().get(0).getName());
    assertEquals("some value", message.getPrivateHeaders().get(0).getValue());
    
    assertArrayEquals("binary data goes here".getBytes(StandardCharsets.UTF_8),
        message.getApplicationData());
  }

  @Test()
  public void test_EncryptedData() {
    
    //TODO: this isnt great, since we have circular tests. try get an encrypted message from one
    //of the other implementations (like the js implementation).
    
    PskMessage clearMessage = new PskMessageBuilder().addPublicHeader("TestHeader", "test_value")
        .addPrivateHeader("private_header", "private_value")
        .setApplicationData("binary data".getBytes(StandardCharsets.UTF_8))
        .toMessage();
    
    byte[] encrypted = PskWriterFactory.getEncryptedWriter(key).writeMessage(clearMessage);
    
    PskMessageReader reader = PskReaderFactory.getEncryptedReader(key);
    
    PskMessage decryptedMessage = reader.readMessage(encrypted);
    
    assertNotNull(decryptedMessage);
    /* the encryption header, the nonce header, and our test header */
    assertEquals(3, decryptedMessage.getPublicHeaders().size());
    
    assertEquals(1, decryptedMessage.getPublicHeaders("Nonce").size());
    assertEquals(1, decryptedMessage.getPublicHeaders("Encryption").size());
    assertEquals(1, decryptedMessage.getPublicHeaders("TestHeader").size());
    
    assertEquals(1, decryptedMessage.getPrivateHeaders().size());
    assertEquals("private_header", decryptedMessage.getPrivateHeaders().get(0).getName());
    assertEquals("private_value", decryptedMessage.getPrivateHeaders().get(0).getValue());
    
    assertArrayEquals("binary data".getBytes(StandardCharsets.UTF_8),
        decryptedMessage.getApplicationData());
  }
}
