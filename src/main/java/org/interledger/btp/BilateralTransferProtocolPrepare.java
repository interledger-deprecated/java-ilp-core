package org.interledger.btp;

import org.interledger.Condition;
import org.interledger.Fulfillment;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public interface BilateralTransferProtocolPrepare extends BilateralTransferProtocolTransferPacket {

  /**
   * The amount to deliver, in discrete units of the destination ledger's asset type. The scale of
   * the units is determined by the destination ledger's smallest indivisible unit.
   *
   * @return An instance of {@link BigInteger}.
   */
  BigInteger getAmount();

  /**
   * Execution condition for the transfer.
   *
   * @return An instance of {@link Condition}.
   */
  Condition getExecutionCondition();

  /**
   * Expiry of the transfer.
   *
   * @return An instance of {@link ZonedDateTime}.
   */
  ZonedDateTime getExpiresAt();

  /**
   * Get the default builder.
   *
   * @return a {@link Builder} instance.
   */
  static Builder builder() {
    return new Builder();
  }


  /**
   * A builder for instances of {@link BilateralTransferProtocolPrepare}.
   */
  class Builder {

    private long requestId;
    private UUID transferId;
    private BigInteger amount;
    private Condition executionCondition;
    private ZonedDateTime expiresAt;
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
     * Set the amount
     *
     * @param amount An instance of {@link BigInteger}.
     */
    public Builder amount(final BigInteger amount) {
      this.amount = amount;
      return this;
    }

    /**
     * Set the condition
     *
     * @param condition An instance of {@link Condition}.
     */
    public Builder condition(final Condition condition) {
      this.executionCondition = condition;
      return this;
    }

    /**
     * Set the expiry
     *
     * @param expiresAt An instance of {@link ZonedDateTime}.
     */
    public Builder expiresAt(final ZonedDateTime expiresAt) {
      this.expiresAt = expiresAt;
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
     * @return An instance of {@link BilateralTransferProtocolPrepare}.
     */
    public BilateralTransferProtocolPrepare build() {
      return new Impl(this);
    }

    /**
     * A private, immutable implementation of {@link BilateralTransferProtocolPrepare}.
     */
    private static final class Impl implements BilateralTransferProtocolPrepare {

      private final long requestId;
      private final UUID transferId;
      private final BigInteger amount;
      private final Condition condition;
      private final ZonedDateTime expiresAt;
      private final List<SubProtocolData> subProtocolData;

      /**
       * No-args Constructor.
       */
      private Impl(final Builder builder) {
        Objects.requireNonNull(builder);
        Objects.requireNonNull(builder.transferId, "transferId must not be null!");
        Objects.requireNonNull(builder.amount, "amount must not be null!");
        Objects.requireNonNull(builder.executionCondition, "executionCondition must not be null!");
        Objects.requireNonNull(builder.expiresAt, "expiresAt must not be null!");
        Objects.requireNonNull(builder.subProtocolData, "subProtocolData must not be null!");

        this.requestId = builder.requestId;
        this.transferId = builder.transferId;
        this.amount = builder.amount;
        this.condition = builder.executionCondition;
        this.expiresAt = builder.expiresAt;

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
      public BigInteger getAmount() {
        return amount;
      }

      @Override
      public Condition getExecutionCondition() {
        return condition;
      }

      @Override
      public ZonedDateTime getExpiresAt() {
        return expiresAt;
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
            + ",amount=" + amount.toString(10)
            + ",condition=" + condition.toString()
            + ",expiry=" + expiresAt.toString()
            + ",subProtocolData=" + Arrays.deepToString(subProtocolData.toArray())
            + '}';
      }

    }
  }

}
