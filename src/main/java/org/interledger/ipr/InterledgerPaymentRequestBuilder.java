package org.interledger.ipr;

import org.interledger.InterledgerAddress;
import org.interledger.ilp.InterledgerPayment;
import org.interledger.psk.PskMessageBuilder;
import org.interledger.psk.PskMessageWriter;
import org.interledger.psk.PskParams;
import org.interledger.psk.PskWriterFactory;
import org.interledger.psk.io.PskUtils;

import java.time.ZonedDateTime;
import java.util.Objects;

public class InterledgerPaymentRequestBuilder {

  private final PskParams pskParams;
  private final PskMessageBuilder messageBuilder;

  // ILP Packet headers
  private final InterledgerPayment.Builder packetBuilder;

  private boolean encrypted = true;

  public InterledgerPaymentRequestBuilder(InterledgerAddress receiverAddressPrefix,
      long receiveAmount, ZonedDateTime expiry, byte[] receiverSecret) {
    
    //Generate keys (uses same algo as PSK although the keys are not shared)
    this.pskParams = PskUtils.getPskParams(receiverSecret);
    
    //Append receiver id and token to address
    InterledgerAddress destinationAddress = receiverAddressPrefix
        .with("." + pskParams.getReceiverId() + pskParams.getToken());
    
    this.packetBuilder = InterledgerPayment.Builder.builder()
    .destinationAccount(destinationAddress)
    .destinationAmount(receiveAmount);    
    
    //Default expiry is 60 seconds from now
    expiry = Objects.isNull(expiry) ? ZonedDateTime.now().plusSeconds(60) : expiry;
    
    this.messageBuilder = new PskMessageBuilder()
    .addPrivateHeader("Expires-At", expiry.toOffsetDateTime().toString());
    
  }

  public PskMessageBuilder getPskMessageBuilder() {
    return messageBuilder;
  }
  
  public void setEncrypted(boolean encrypted) {
    this.encrypted = encrypted;
  }
  
  public InterledgerPaymentRequest getIpr() {

    PskMessageWriter writer = (this.encrypted) ? 
        PskWriterFactory.getEncryptedWriter(this.pskParams.getSharedKey()) :
        PskWriterFactory.getUnencryptedWriter();
        
    packetBuilder.data(writer.writeMessage(messageBuilder.toMessage()));
    InterledgerPayment packet = packetBuilder.build();
    
    //TODO: It's a pity we have to encode the packet to get the condition here
    // would be better to do it during encoding of the IPR
    return new InterledgerPaymentRequest(
        packet, 
        PskUtils.packetToCondition(packet, pskParams.getSharedKey()));     
  }

}
