package org.interledger.ilqp;

import java.time.Duration;
import java.util.Objects;

/**
 * A quote sent in response to a request of type {@link QuoteByDestinationAmountresRequest}.
 */
public interface QuoteByDestinationAmountResponse extends QuoteResponse {

  @Override
  Duration getSourceHoldDuration();

  /**
   * The amount the sender needs to send based on the requested destination amount.
   *
   * @return The amount the sender needs to send.
   */
  long getSourceAmount();

  /**
   * A builder for instances of {@link QuoteByDestinationAmountRequest}.
   */
  class Builder {

    private long sourceAmount;
    private Duration sourceHoldDuration;

    /**
     * Set the source amount into this builder.
     *
     * @param sourceAmount The source amount value.
     */
    public Builder sourceAmount(long sourceAmount) {
      if (sourceAmount < 0) {
        throw new IllegalArgumentException("Source amount must be at least 0");
      }
      this.sourceAmount = sourceAmount;
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
     * The method that actually constructs a QuoteByDestinationAmountResponse instance.
     *
     * @return An instance of {@link QuoteByDestinationAmountResponse}.
     */
    public QuoteByDestinationAmountResponse build() {
      return new Builder.Impl(this);
    }

    /**
     * Constructs a new builder.
     */
    public static Builder builder() {
      return new Builder();
    }

    /**
     * A private, immutable implementation of {@link QuoteByDestinationAmountResponse}.
     */
    public static class Impl implements QuoteByDestinationAmountResponse {
      private final Duration sourceHoldDuration;
      private long sourceAmount;

      private Impl(final Builder builder) {
        Objects.requireNonNull(builder);

        this.sourceHoldDuration = Objects.requireNonNull(builder.sourceHoldDuration,
            "sourceHoldDuration must not be null!");

        if (builder.sourceAmount < 0) {
          throw new IllegalArgumentException("source amount must be at least 0");
        }

        this.sourceAmount = builder.sourceAmount;
      }


      @Override
      public Duration getSourceHoldDuration() {
        return this.sourceHoldDuration;
      }

      @Override
      public long getSourceAmount() {
        return this.sourceAmount;
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

        if (!sourceHoldDuration.equals(impl.sourceHoldDuration)) {
          return false;
        }

        if (sourceAmount != impl.sourceAmount) {
          return false;
        }

        return true;
      }

      @Override
      public int hashCode() {
        int result = Long.hashCode(sourceAmount);
        result = 31 * result + sourceHoldDuration.hashCode();
        return result;
      }

      @Override
      public String toString() {
        final StringBuilder sb = new StringBuilder("Impl{");
        sb.append("sourceAmount=").append(sourceAmount);
        sb.append(", sourceHoldDuration=").append(sourceHoldDuration);
        sb.append('}');
        return sb.toString();
      }
    }
  }
}
