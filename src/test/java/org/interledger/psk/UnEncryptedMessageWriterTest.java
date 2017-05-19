package org.interledger.psk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.interledger.psk.io.UnencryptedPskMessageReader;
import org.interledger.psk.model.PskMessage;
import org.interledger.psk.model.PskMessageHeader;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * JUnit to exercise the {@link UnencryptedPskMessageReader} implementation.
 */
public class UnEncryptedMessageWriterTest {

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
    
    PskMessageWriter writer = PskWriterFactory.getUnencryptedWriter();
    
    byte[] data = writer.writeMessage(message);
    
    assertNotNull(data);
    
    /* we happen to know that the PSK message is just a UTF-8 encoded string */
    String messageString = new String(data, StandardCharsets.UTF_8);
    
    /* hand-craft what we expect to see. a bit awkward, since the header order isn't terribly
     * predictable from the outside. */
    String expected = "PSK/1.0\n" 
        + "Nonce: " + nonce + "\n"
        + "Encryption: none\n"
        + "\n"
        + "private header: private\theader\tvalue\n"
        + "\n"
        + "{some_application_data: 123}";
    
    assertEquals(expected, messageString);
  }

}
