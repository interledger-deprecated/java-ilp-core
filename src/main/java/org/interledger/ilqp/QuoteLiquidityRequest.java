package org.interledger.ilqp;

import org.interledger.InterledgerAddress;

import java.time.Duration;
import java.util.Objects;

/**
 * A request to receive liquidity information between the current ledger and the destination
 * account. This information is sufficient to locally quote any amount until the curve expires.
 */
public interface QuoteLiquidityRequest extends QuoteRequest {

  @Override
  InterledgerAddress getDestinationAccount();

  @Override
  Duration getDestinationHoldDuration();

  /**
   * A builder for instances of {@link QuoteLiquidityRequest}.
   */
  class Builder {

    private InterledgerAddress destinationAccount;
    private Duration destinationHoldDuration;

    public static Builder builder() {
      return new Builder();
    }

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
     * Set the destination hold duration into this builder.
     *
     * @param destinationHoldDuration An instance of {@link Duration}.
     */
    public Builder destinationHoldDuration(final Duration destinationHoldDuration) {
      this.destinationHoldDuration = Objects.requireNonNull(destinationHoldDuration);
      return this;
    }

    /**
     * The method that actually constructs a QuoteByLiquidityRequest.
     *
     * @return An instance of {@link QuoteLiquidityRequest}
     */
    public QuoteLiquidityRequest build() {
      return new Builder.Impl(this);
    }

    private static class Impl implements QuoteLiquidityRequest {

      private final InterledgerAddress destinationAccount;
      private final Duration destinationHoldDuration;

      /**
       * Constructs an instance from the values held in the builder.
       *
       * @param builder A Builder used to construct {@link QuoteLiquidityRequest} instances.
       */
      private Impl(final Builder builder) {
        Objects.requireNonNull(builder);

        this.destinationAccount = Objects.requireNonNull(builder.destinationAccount,
            "destinationAccount must not be null!");

        this.destinationHoldDuration = Objects.requireNonNull(builder.destinationHoldDuration,
            "destinationHoldDuration must not be null!");
      }

      @Override
      public InterledgerAddress getDestinationAccount() {
        return this.destinationAccount;
      }

      @Override
      public Duration getDestinationHoldDuration() {
        return this.destinationHoldDuration;
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

        if (!destinationHoldDuration.equals(impl.destinationHoldDuration)) {
          return false;
        }

        return true;
      }

      @Override
      public int hashCode() {
        int result = destinationAccount.hashCode();
        result = 31 * result + destinationHoldDuration.hashCode();
        return result;
      }

      @Override
      public String toString() {
        final StringBuilder sb = new StringBuilder("Impl{");
        sb.append("destinationAccount=")
            .append(destinationAccount);
        sb.append(", destinationHoldDuration=")
            .append(destinationHoldDuration);
        sb.append('}');
        return sb.toString();
      }
    }
  }
}
