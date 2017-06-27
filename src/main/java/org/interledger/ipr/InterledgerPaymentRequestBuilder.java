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

  /**
   * Build a new IPR from a receiver address, amount and expiry (default 60 seconds).
   * 
   * <p>The IPR uses the PSK transport format and derives the required keys from the given
   * receiverSecret.
   * 
   * @param receiverAddressPrefix ILP Address of the receiver (will have a unique id and token
   *        appended to allow the fulfillment to be generated when the packet is received later).
   * @param receiveAmount The amount to receive.
   * @param expiry The expiry of the payment request (used to populate the Expires-At header in the
   *        transport layer envelope).
   * @param receiverSecret The receiver secret as defined in the PSK specification.
   */
  public InterledgerPaymentRequestBuilder(InterledgerAddress receiverAddressPrefix,
      long receiveAmount, ZonedDateTime expiry, byte[] receiverSecret) {

    // Generate keys (uses same algo as PSK although the keys are not shared)
    this.pskParams = PskUtils.getPskParams(receiverSecret);

    // Append receiver id and token to address
    InterledgerAddress destinationAddress =
        receiverAddressPrefix.with("." + pskParams.getReceiverId() + pskParams.getToken());

    this.packetBuilder = InterledgerPayment.Builder.builder().destinationAccount(destinationAddress)
        .destinationAmount(receiveAmount);

    // Default expiry is 60 seconds from now
    expiry = Objects.isNull(expiry) ? ZonedDateTime.now().plusSeconds(60) : expiry;

    this.messageBuilder = new PskMessageBuilder().addPrivateHeader("Expires-At",
        expiry.toOffsetDateTime().toString());

  }

  /**
   * Get the message builder for the internal PSK message.
   * 
   * @return a reference to the internal PSK message builder.
   */
  public PskMessageBuilder getPskMessageBuilder() {
    return messageBuilder;
  }

  /**
   * Set false to prevent private data being encrypted at the transport layer.
   * 
   * @param encrypted false to disable ILP packet encryption.
   */
  public void setEncrypted(boolean encrypted) {
    this.encrypted = encrypted;
  }

  /**
   * Get the IPR.
   * 
   * <p>Calling this will result in the internal PSK message being built (and encrypted unless
   * encryption is disabled).
   * 
   * <p>After the PSK message is built the ILP Packet is built and OER encoded before the Condition
   * is generated.
   * 
   * @return an Interledger Payment Request.
   */
  public InterledgerPaymentRequest getIpr() {

    PskMessageWriter writer =
        (this.encrypted) ? PskWriterFactory.getEncryptedWriter(this.pskParams.getSharedKey())
            : PskWriterFactory.getUnencryptedWriter();

    packetBuilder.data(writer.writeMessage(messageBuilder.toMessage()));
    InterledgerPayment packet = packetBuilder.build();

    // TODO: It's a pity we have to encode the packet to get the condition here
    // would be better to do it during encoding of the IPR
    return new InterledgerPaymentRequest(packet,
        PskUtils.generateFulfillment(packet, pskParams.getSharedKey()).getCondition());
  }

}
