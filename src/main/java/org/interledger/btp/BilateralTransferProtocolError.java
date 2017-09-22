package org.interledger.btp;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public interface BilateralTransferProtocolError extends BilateralTransferProtocolPacket {

  BilateralTransferProtocolErrorTypes getError();

  ZonedDateTime getTriggeredAt();

  byte[] getData();

  /**
   * Get the default builder.
   *
   * @return a {@link Builder} instance.
   */
  static Builder builder() {
    return new Builder();
  }


  /**
   * A builder for instances of {@link BilateralTransferProtocolError}.
   */
  class Builder {

    private long requestId;
    private BilateralTransferProtocolErrorTypes errorType;
    private ZonedDateTime triggeredAt;
    private byte[] data;
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
     * Set the type.
     *
     * @param errorType A {@link BilateralTransferProtocolErrorTypes}
     */
    public Builder errorType(final BilateralTransferProtocolErrorTypes errorType) {
      this.errorType = errorType;
      return this;
    }


    /**
     * Set the time that the error was triggered.
     *
     * @param triggeredAt A {@link ZonedDateTime}
     */
    public Builder triggeredAt(final ZonedDateTime triggeredAt) {
      this.triggeredAt = triggeredAt;
      return this;
    }


    /**
     * Set the additional data.
     *
     * @param data A byte array
     */
    public Builder data(final byte[] data) {
      this.data = data;
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
     * @return An instance of {@link BilateralTransferProtocolError}.
     */
    public BilateralTransferProtocolError build() {
      return new Impl(this);
    }

    /**
     * A private, immutable implementation of {@link BilateralTransferProtocolError}.
     */
    private static final class Impl implements BilateralTransferProtocolError {

      private final long requestId;
      private final BilateralTransferProtocolErrorTypes errorType;
      private final ZonedDateTime triggeredAt;
      private final byte[] data;
      private final List<SubProtocolData> subProtocolData;

      /**
       * No-args Constructor.
       */
      private Impl(final Builder builder) {
        Objects.requireNonNull(builder);
        Objects.requireNonNull(builder.errorType, "errorType must not be null!");
        Objects.requireNonNull(builder.triggeredAt, "triggeredAt must not be null!");
        Objects.requireNonNull(builder.data, "data must not be null!");
        Objects.requireNonNull(builder.subProtocolData, "subProtocolData must not be null!");

        this.requestId = builder.requestId;
        this.errorType = builder.errorType;
        this.triggeredAt = builder.triggeredAt;

        //TODO: Safe copy
        this.data = builder.data;
        this.subProtocolData = new ArrayList<>(builder.subProtocolData);

      }

      @Override
      public long getRequestId() {
        return requestId;
      }


      @Override
      public BilateralTransferProtocolErrorTypes getError() {
        return errorType;
      }

      @Override
      public ZonedDateTime getTriggeredAt() {
        return triggeredAt;
      }

      @Override
      public byte[] getData() {
        return data;
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

        return requestId == impl.requestId
            && subProtocolData.equals(impl.subProtocolData);
      }

      @Override
      public int hashCode() {
        return 31 * Objects.hashCode(this.subProtocolData);
      }

      @Override
      public String toString() {
        return "BilateralTransferProtocolMessage.Impl{"
            + "requestId=" + requestId
            + ",subProtocolData=" + Arrays.deepToString(subProtocolData.toArray())
            + '}';
      }

    }
  }

}
