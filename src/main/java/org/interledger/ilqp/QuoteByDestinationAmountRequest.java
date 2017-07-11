package org.interledger.ilqp;

import org.interledger.InterledgerAddress;

import java.time.Duration;
import java.util.Objects;

/**
 * A request for a quote that specifies the amount to deliver at the destination address.
 */
public interface QuoteByDestinationAmountRequest extends QuoteRequest {

  @Override
  InterledgerAddress getDestinationAccount();

  /**
   * Returns fixed the amount that will arrive at the receiver.
   */
  long getDestinationAmount();
  
  @Override
  Duration getDestinationHoldDuration();
  
  /**
   * A builder for instances of {@link QuoteByDestinationAmountRequest}.
   */
  class Builder {
    
    private InterledgerAddress destinationAccount;
    private long destinationAmount;
    private Duration destinationHoldDuration;
    
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
     * @param destinationAmount The source amount value.
     */
    public Builder destinationAmount(long destinationAmount) {
      if (destinationAmount < 0) {
        throw new IllegalArgumentException("Destination amount must be at least 0");
      }
      this.destinationAmount = destinationAmount;
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
     * The method that actually constructs a QuoteByDestinationAmountRequest.
     * 
     * @return An instance of {@link QuoteByDestinationAmountRequest}
     */
    public QuoteByDestinationAmountRequest build() {
      return new Builder.Impl(this);
    }
    
    public static Builder builder() {
      return new Builder();
    }
    
    /**
     * A private, immutable implementation of {@link QuoteByDestinationAmountRequest}.
     */
    private static class Impl implements QuoteByDestinationAmountRequest {

      private final InterledgerAddress destinationAccount;
      private long destinationAmount;
      private final Duration destinationHoldDuration;
      
      /**
       * Constructs an instance from the values held in the builder.
       * 
       * @param builder A Builder used to construct {@link QuoteByDestinationAmountRequest}
       *        instances.
       */
      private Impl(final Builder builder) {
        Objects.requireNonNull(builder);
        
        this.destinationAccount = Objects.requireNonNull(builder.destinationAccount,
            "destinationAccount must not be null!");
        
        if (builder.destinationAmount < 0) {
          throw new IllegalArgumentException("Destination amount must be at least 0");
        }

        this.destinationAmount = builder.destinationAmount;
        
        this.destinationHoldDuration = Objects.requireNonNull(builder.destinationHoldDuration,
            "destinationHoldDuration must not be null!");
      }
      
      @Override
      public InterledgerAddress getDestinationAccount() {
        return this.destinationAccount;
      }

      @Override
      public long getDestinationAmount() {
        return this.destinationAmount;
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
        
        if (destinationAmount != impl.destinationAmount) {
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
        result = 31 * result + Long.hashCode(destinationAmount);
        result = 31 * result + destinationHoldDuration.hashCode();
        return result;
      }

      @Override
      public String toString() {
        final StringBuilder sb = new StringBuilder("Impl{");
        sb.append("destinationAccount=").append(destinationAccount);
        sb.append(", destinationAmount=").append(destinationAmount);
        sb.append(", destinationHoldDuration=").append(destinationHoldDuration);
        sb.append('}');
        return sb.toString();
      }
    }
  }
}