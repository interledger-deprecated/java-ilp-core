package org.interledger.ilp;

import org.interledger.InterledgerAddress;
import org.interledger.wire.InterledgerPacket;

import java.util.Arrays;
import java.util.Objects;

import javax.money.MonetaryAmount;

/**
 * <p>Interledger Payments moves assets from one party to another that consists of one or more
 * ledger transfers, potentially across multiple ledgers.</p>
 * Interledger Payments have three major consumers:
 * <ul>
 * <li>Connectors utilize the Interledger Address contained in the payment to route the
 * payment.</li>
 * <li>The receiver of a payment uses it to identify the recipient and which condition to
 * fulfill.</li>
 * <li>Interledger sub-protocols utilize custom data encoded in a payment to facilitate
 * sub-protocol operations.</li>
 * </ul>
 * <p> When a sender prepares a transfer to start a payment, the sender attaches an ILP Payment to
 * the transfer, in the memo field if possible. If a ledger does not support attaching the entire
 * ILP Payment to a transfer as a memo, users of that ledger can transmit the ILP Payment using
 * another authenticated messaging channel, but MUST be able to correlate transfers and ILP
 * Payments.</p>
 * <p> When a connector sees an incoming prepared transfer with an ILP Payment, the receiver reads
 * the ILP Payment to confirm the details of the packet.  For example, the connector reads the
 * InterledgerAddress of the payment's receiver, and if the connector has a route to the receiver's
 * account, the connector prepares a transfer to continue the payment, and attaches the same ILP
 * Payment to the new transfer.  Likewise, the receiver confirms that the amount from the ILP
 * Payment Packet matches the amount actually delivered by the transfer. And finally, the receiver
 * decodes the data portion of the Payment and matches the condition to the payment. The receiver
 * MUST confirm the integrity of the ILP Payment, for example with a hash-based message
 * authentication code (HMAC). If the receiver finds the transfer acceptable, the receiver releases
 * the fulfillment for the transfer, which can be used to execute all prepared transfers that were
 * established prior to the receiver accepting the payment.</p>
 */
public interface InterledgerPayment extends InterledgerPacket {

  /**
   * The Interledger address of the account where the receiver should ultimately receive the
   * payment.
   *
   * @return An instance of {@link InterledgerAddress}.
   */
  InterledgerAddress getDestinationAccount();

  /**
   * The amount to deliver, in discrete units of the destination ledger's asset type. The scale of
   * the units is determined by the destination ledger's smallest indivisible unit.
   *
   * @return An instance of {@link MonetaryAmount}.
   */
  Long getDestinationAmount();

  /**
   * Arbitrary data for the receiver that is set by the transport layer of a payment (for example,
   * this may contain PSK data).
   *
   * @return A byte array.
   */
  byte[] getData();

  /**
   * A builder for instances of {@link InterledgerPayment}.
   */
  class Builder {

    private InterledgerAddress destinationAccount;
    private Long destinationAmount;
    private byte[] data;

    /**
     * Set the destination account address into this builder.
     *
     * @param destinationAccount An instance of {@link InterledgerAddress}.
     */
    public Builder destinationAccount(
        final InterledgerAddress destinationAccount) {
      this.destinationAccount = Objects.requireNonNull(destinationAccount);
      return this;
    }

    /**
     * Set the destination amount into this builder.
     *
     * @param destinationAmount An instance of {@link MonetaryAmount}.
     */
    public Builder destinationAmount(
        final Long destinationAmount) {
      this.destinationAmount = Objects.requireNonNull(destinationAmount);
      return this;
    }

    /**
     * Set the data payload for this payment.
     *
     * @param data An instance of {@link byte[]}.
     */
    public Builder data(final byte[] data) {
      this.data = Objects.requireNonNull(data);
      return this;
    }

    /**
     * The method that actually constructs a payment.
     *
     * @return An instance of {@link InterledgerPayment}.
     */
    public InterledgerPayment build() {
      return new Builder.Impl(this);
    }

    public static Builder builder() {
      return new Builder();
    }

    /**
     * A private, immutable implementation of {@link InterledgerPayment}.
     */
    private static class Impl implements InterledgerPayment {

      private final InterledgerAddress destinationAccount;
      private final Long destinationAmount;
      private final byte[] data;

      /**
       * No-args Constructor.
       */
      private Impl(final Builder builder) {
        Objects.requireNonNull(builder);
        this.destinationAccount = Objects
            .requireNonNull(builder.destinationAccount, "destinationAccount must not be null!");
        this.destinationAmount = Objects
            .requireNonNull(builder.destinationAmount, "destinationAmount must not be null!");
        this.data = Objects.requireNonNull(builder.data, "data must not be null!");
      }

      @Override
      public InterledgerAddress getDestinationAccount() {
        return this.destinationAccount;
      }

      @Override
      public Long getDestinationAmount() {
        return this.destinationAmount;
      }

      @Override
      public byte[] getData() {
        return this.data;
      }

      @Override
      public boolean equals(Object obj) {
        if (this == obj) {
          return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
          return false;
        }

        Impl impl = (Impl) obj;

        if (!destinationAccount.equals(impl.destinationAccount)) {
          return false;
        }
        if (!destinationAmount.equals(impl.destinationAmount)) {
          return false;
        }
        return Arrays.equals(data, impl.data);
      }

      @Override
      public int hashCode() {
        int result = destinationAccount.hashCode();
        result = 31 * result + destinationAmount.hashCode();
        result = 31 * result + Arrays.hashCode(data);
        return result;
      }

      @Override
      public String toString() {
        final StringBuilder sb = new StringBuilder("Impl{");
        sb.append("destinationAccount=").append(destinationAccount);
        sb.append(", destinationAmount=").append(destinationAmount);
        sb.append(", data=").append(Arrays.toString(data));
        sb.append('}');
        return sb.toString();
      }
    }
  }
}
