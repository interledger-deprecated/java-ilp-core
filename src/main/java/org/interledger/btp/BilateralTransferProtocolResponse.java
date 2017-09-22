package org.interledger.btp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public interface BilateralTransferProtocolResponse extends BilateralTransferProtocolPacket {

  /**
   * Get the default builder.
   *
   * @return a {@link Builder} instance.
   */
  static Builder builder() {
    return new Builder();
  }


  /**
   * A builder for instances of {@link BilateralTransferProtocolResponse}.
   */
  class Builder {

    private long requestId;
    private List<SubProtocolData> subProtocolData;

    /**
     * Set the request id.
     *
     * @param requestId An UInt32 request id.
     */
    public Builder requestId(final long requestId) {
      this.requestId = requestId;
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
     * @return An instance of {@link BilateralTransferProtocolResponse}.
     */
    public BilateralTransferProtocolResponse build() {
      return new Impl(this);
    }

    /**
     * A private, immutable implementation of {@link BilateralTransferProtocolResponse}.
     */
    private static final class Impl implements BilateralTransferProtocolResponse {

      private final long requestId;
      private final List<SubProtocolData> subProtocolData;

      /**
       * No-args Constructor.
       */
      private Impl(final Builder builder) {
        Objects.requireNonNull(builder);
        Objects.requireNonNull(builder.subProtocolData, "subProtocolData must not be null!");

        //TODO: Safe copy
        this.requestId = builder.requestId;
        this.subProtocolData = new ArrayList<>(builder.subProtocolData);

      }

      @Override
      public long getRequestId() {
        return requestId;
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

        return requestId == impl.requestId &&
            subProtocolData.equals(impl.subProtocolData);
      }

      @Override
      public int hashCode() {
        return 13 * Objects.hashCode(this.subProtocolData);
      }

      @Override
      public String toString() {
        return "BilateralTransferProtocolResponse.Impl{"
            + "requestId=" + requestId
            + ",subProtocolData=" + Arrays.deepToString(subProtocolData.toArray())
            + '}';
      }
    }
  }

}
