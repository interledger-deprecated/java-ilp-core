package org.interledger.ilqp;

import java.math.BigInteger;
import java.time.Duration;
import java.util.Objects;

/**
 * A quote sent in response to a request of type {@link QuoteBySourceAmountRequest}.
 */
public interface QuoteBySourceAmountResponse extends QuoteResponse {

  @Override
  Duration getSourceHoldDuration();

  /**
   * Returns the amount that will arrive at the receiver.
   */
  BigInteger getDestinationAmount();

  /**
   * A builder for constructing instances of {@link QuoteBySourceAmountResponse}.
   */
  class Builder {

    private BigInteger destinationAmount;
    private Duration sourceHoldDuration;

    /**
     * Constructs a new builder.
     */
    public static Builder builder() {
      return new Builder();
    }

    /**
     * Set the destination amount into this builder.
     *
     * @param destinationAmount The destination amount value.
     */
    public Builder destinationAmount(final BigInteger destinationAmount) {
      this.destinationAmount = Objects.requireNonNull(destinationAmount);
      return this;
    }

    /**
     * Set the source hold duration into this builder.
     *
     * @param sourceHoldDuration An instance of {@link Duration}.
     */
    public Builder sourceHoldDuration(final Duration sourceHoldDuration) {
      this.sourceHoldDuration = Objects.requireNonNull(sourceHoldDuration);
      return this;
    }

    /**
     * The method that actually constructs a QuoteBySourceAmountResponse instance.
     *
     * @return An instance of {@link QuoteBySourceAmountResponse}.
     */
    public QuoteBySourceAmountResponse build() {
      return new Builder.Impl(this);
    }

    /**
     * A private, immutable implementation of {@link QuoteBySourceAmountResponse}.
     */
    private static class Impl implements QuoteBySourceAmountResponse {

      private final BigInteger destinationAmount;
      private final Duration sourceHoldDuration;

      private Impl(final Builder builder) {
        Objects.requireNonNull(builder);

        this.destinationAmount = Objects
            .requireNonNull(builder.destinationAmount, "destinationAmount must not be null!");
        this.sourceHoldDuration = Objects.requireNonNull(builder.sourceHoldDuration,
            "sourceHoldDuration must not be null!");

      }

      @Override
      public Duration getSourceHoldDuration() {
        return this.sourceHoldDuration;
      }

      @Override
      public BigInteger getDestinationAmount() {
        return this.destinationAmount;
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

        if (!destinationAmount.equals(impl.destinationAmount)) {
          return false;
        }
        return sourceHoldDuration.equals(impl.sourceHoldDuration);
      }

      @Override
      public int hashCode() {
        int result = destinationAmount.hashCode();
        result = 31 * result + sourceHoldDuration.hashCode();
        return result;
      }

      @Override
      public String toString() {
        return "QuoteBySourceAmountResponse.Impl{"
            + "destinationAmount=" + destinationAmount
            + ", sourceHoldDuration=" + sourceHoldDuration
            + '}';
      }
    }
  }
}
