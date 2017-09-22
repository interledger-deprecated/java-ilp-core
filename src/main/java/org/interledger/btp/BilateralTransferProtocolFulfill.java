package org.interledger.btp;

import org.interledger.Fulfillment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public interface BilateralTransferProtocolFulfill extends BilateralTransferProtocolTransferPacket {

  Fulfillment getFulfillment();

  /**
   * Get the default builder.
   *
   * @return a {@link Builder} instance.
   */
  static Builder builder() {
    return new Builder();
  }


  /**
   * A builder for instances of {@link BilateralTransferProtocolFulfill}.
   */
  class Builder {

    private long requestId;
    private UUID transferId;
    private Fulfillment fulfillment;
    private List<SubProtocolData> subProtocolData;

    /**
     * Set the request id
     *
     * @param requestId An UInt32 request id.
     */
    public Builder requestId(final long requestId) {
      this.requestId = requestId;
      return this;
    }

    /**
     * Set the transfer id
     *
     * @param transferId A UUID transfer id.
     */
    public Builder transferId(final UUID transferId) {
      this.transferId = transferId;
      return this;
    }

    /**
     * Set the fulfillment
     *
     * @param fulfillment An instance of {@link Fulfillment}.
     */
    public Builder fulfillment(final Fulfillment fulfillment) {
      this.fulfillment = fulfillment;
      return this;
    }

    /**
     * Push an instance of {@link SubProtocolData} onto the sequence.
     *
     * @param subProtocolData An instance of {@link SubProtocolData}.
     */
    public Builder pushSubProtocolData(final SubProtocolData subProtocolData) {
      this.subProtocolData.add(subProtocolData);
      return this;
    }

    /**
     * Set the sequence of {@link SubProtocolData}.
     *
     * @param subProtocolData A sequence of {@link SubProtocolData}.
     */
    public Builder setSubProtocolData(final List<SubProtocolData> subProtocolData) {
      this.subProtocolData = subProtocolData;
      return this;
    }

    /**
     * The method that actually constructs message.
     *
     * @return An instance of {@link BilateralTransferProtocolFulfill}.
     */
    public BilateralTransferProtocolFulfill build() {
      return new Impl(this);
    }

    /**
     * A private, immutable implementation of {@link BilateralTransferProtocolFulfill}.
     */
    private static final class Impl implements BilateralTransferProtocolFulfill {

      private final long requestId;
      private final UUID transferId;
      private final Fulfillment fulfillment;
      private final List<SubProtocolData> subProtocolData;

      /**
       * No-args Constructor.
       */
      private Impl(final Builder builder) {
        Objects.requireNonNull(builder);
        Objects.requireNonNull(builder.transferId, "transferId must not be null!");
        Objects.requireNonNull(builder.fulfillment, "fulfillment must not be null!");
        Objects.requireNonNull(builder.subProtocolData, "subProtocolData must not be null!");

        this.requestId = builder.requestId;
        this.transferId = builder.transferId;
        this.fulfillment = builder.fulfillment;

        //TODO: Safe copy
        this.subProtocolData = new ArrayList<>(builder.subProtocolData);

      }

      @Override
      public long getRequestId() {
        return requestId;
      }

      @Override
      public UUID getTransferId() {
        return transferId;
      }

      @Override
      public Fulfillment getFulfillment() {
        return fulfillment;
      }

      @Override
      public List<SubProtocolData> getSubProtocolData() {
        //TODO: Safe copy
        return new ArrayList<>(subProtocolData);
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

        return subProtocolData.equals(impl.subProtocolData);
      }

      @Override
      public int hashCode() {
        return 31 * Objects.hashCode(this.subProtocolData);
      }

      @Override
      public String toString() {
        return "BilateralTransferProtocolReject.Impl{"
            + "requestId=" + requestId
            + ",transferId=" + transferId.toString()
            + ",fulfillment=" + fulfillment.toString()
            + ",subProtocolData=" + Arrays.deepToString(subProtocolData.toArray())
            + '}';
      }

    }
  }

}
