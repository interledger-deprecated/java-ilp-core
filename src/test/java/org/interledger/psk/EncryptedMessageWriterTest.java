package org.interledger.psk;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.interledger.psk.io.UnencryptedPskMessageReader;
import org.interledger.psk.model.PskMessage;
import org.interledger.psk.model.PskMessageHeader;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * JUnit to exercise the {@link UnencryptedPskMessageReader} implementation.
 */
public class EncryptedMessageWriterTest {

  /* to make testing easier, we set the sharedSecretKey as all 0's */
  public static final byte[] key = new byte[256 / 8]; /* 256 bits long */

  
  @Test
  public void test() {
    
    /* we manually add a known nonce into the message for predictability in tests. this should be
     * frowned on anywhere else */
    String nonce = Base64.getUrlEncoder().encodeToString(new byte[16]);
    
    PskMessage message = new PskMessageBuilder()
        .addPrivateHeader("private header", "\tprivate\theader\tvalue\t")
        .addPublicHeader(PskMessageHeader.PublicHeaders.NONCE, nonce)
        .setApplicationData("{some_application_data: 123}".getBytes(StandardCharsets.UTF_8))
        .toMessage();
    
    PskMessageWriter writer = PskWriterFactory.getEncryptedWriter(key);
    
    byte[] data = writer.writeMessage(message);
    
    assertNotNull(data);
    
    /* we happen to know that the encrypted PSK message is mostly a UTF-8 encoded string, followed
     * by encrypted stuff. */
    String messageString = new String(data, StandardCharsets.UTF_8);
    
    /* to try break the circular testing of the reader and writer, lets try validate some of the 
     * public portion of the message we can expect */
    
    assertTrue(messageString.startsWith("PSK/1.0\n"));
    assertTrue(messageString.contains("Nonce: " + nonce + "\n"));
    assertTrue(messageString.contains("Encryption: aes-256-gcm Zty9wEWIRu-saavYXiCtbQ=="));
    
    /* the private header should be encrypted, so we shouldnt be able to see it */
    assertFalse(messageString.contains("private header"));
    /* we definitely shouldnt see the application data either */
    assertFalse(messageString.contains("{some_application_data: 123}"));
    
    PskMessage decrypted = PskReaderFactory.getEncryptedReader(key).readMessage(data);
    
    assertEquals(2, decrypted.getPublicHeaders().size());
    assertEquals(1, decrypted.getPrivateHeaders().size());
    assertEquals("private header", decrypted.getPrivateHeaders().get(0).getName());
    assertEquals("private\theader\tvalue", decrypted.getPrivateHeaders().get(0).getValue());
    assertArrayEquals("{some_application_data: 123}".getBytes(StandardCharsets.UTF_8),
        decrypted.getApplicationData());
  }

}
