package org.interledger.ilqp;

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
  long getDestinationAmount();

  /**
   * A builder for constructing instances of {@link QuoteBySourceAmountResponse}.
   */
  class Builder {

    private long destinationAmount;
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
    public Builder destinationAmount(long destinationAmount) {
      if (destinationAmount < 0) {
        throw new IllegalArgumentException("Destination amount must be at least 0");
      }
      this.destinationAmount = destinationAmount;
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

      private final Duration sourceHoldDuration;
      private long destinationAmount;

      private Impl(final Builder builder) {
        Objects.requireNonNull(builder);

        this.sourceHoldDuration = Objects.requireNonNull(builder.sourceHoldDuration,
            "sourceHoldDuration must not be null!");

        if (builder.destinationAmount < 0) {
          throw new IllegalArgumentException("destination amount must be at least 0");
        }

        this.destinationAmount = builder.destinationAmount;
      }

      @Override
      public Duration getSourceHoldDuration() {
        return this.sourceHoldDuration;
      }

      @Override
      public long getDestinationAmount() {
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

        return sourceHoldDuration.equals(impl.sourceHoldDuration)
            && destinationAmount == impl.destinationAmount;
      }

      @Override
      public int hashCode() {
        int result = Long.hashCode(destinationAmount);
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
