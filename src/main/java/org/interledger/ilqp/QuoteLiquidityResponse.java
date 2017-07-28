package org.interledger.ilqp;

import org.interledger.InterledgerAddress;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import javax.money.convert.ExchangeRate;

/**
 * A response to a quote request with liquidity information regarding transfers between the current
 * ledger and the destination account. This information is sufficient to locally quote any amount
 * until the curve expires.
 */
public interface QuoteLiquidityResponse extends QuoteResponse {

  @Override
  Duration getSourceHoldDuration();

  /**
   * <p>A series of exchange rates that can be plotted to assemble a "curve" of liquidity
   * representing the amount that one currency can be exchanged for another.</p> <p>For example, if
   * a liquidity curve contains the rate [0,0] and [10,20], then there is a linear path of rates for
   * which one currency can be exchange for another. To illustrate, it can be assumed that [5,10]
   * exists on this curve.</p>
   * 
   * @return A {@link List} of type {link ExchangeRate}.
   */
  LiquidityCurve getLiquidityCurve();

  /**
   * <p>A common address prefix of all addresses for which the above liquidity curve applies. If the
   * curve only applies to the destination account (see
   * {@link QuoteLiquidityRequest#getDestinationAccount()}) of the corresponding quote request, then
   * this value will be equal to that address. If the curve applies to other accounts with a certain
   * prefix, then this value will be set to that prefix.</p> <p>For more on ILP Address Prefixes,
   * see {@link InterledgerAddress}.</p>
   *
   * @return An instance of {@link InterledgerAddress}.
   */
  InterledgerAddress getAppliesToPrefix();

  /**
   * Maximum time where the connector (and any connectors after it) expects to be able to honor this
   * liquidity curve. Note that a quote in ILP is non-committal, meaning that the liquidity is only
   * likely to be available -- but not reserved -- and therefore not guaranteed.
   *
   * @return An instance of {@link ZonedDateTime}.
   */
  ZonedDateTime getExpiresAt();
  
  /**
   * A builder for instances of {@link QuoteLiquidityResponse}.
   */
  class Builder {
    private LiquidityCurve liquidityCurve;
    private InterledgerAddress appliesTo;
    private Duration sourceHoldDuration;
    private ZonedDateTime expiresAt;
    
    /**
     * Set the liquidity curve into this builder.
     *
     * @param liquidityCurve To An instance of {@link LiquidityCurve}.
     */
    public Builder liquidityCurve(final LiquidityCurve liquidityCurve) {
      this.liquidityCurve = Objects.requireNonNull(liquidityCurve);
      return this;
    }
    
    /**
     * Set the applies-to address into this builder.
     *
     * @param appliesTo An instance of {@link InterledgerAddress}.
     */
    public Builder appliesTo(final InterledgerAddress appliesTo) {
      this.appliesTo = Objects.requireNonNull(appliesTo);
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
     * Set the expires-at into this builder.
     * 
     * @param expiresAt An instance of {@link ZonedDateTime}
     */
    public Builder expiresAt(final ZonedDateTime expiresAt) {
      this.expiresAt = Objects.requireNonNull(expiresAt);
      return this;
    }

    /**
     * The method that actually constructs a QuoteByLiquidityResponse instance.
     *
     * @return An instance of {@link QuoteLiquidityResponse}.
     */
    public QuoteLiquidityResponse build() {
      return new Builder.Impl(this);
    }

    /**
     * Constructs a new builder.
     */
    public static Builder builder() {
      return new Builder();
    }
    
    
    private static class Impl implements QuoteLiquidityResponse {
      private final LiquidityCurve liquidityCurve;
      private final InterledgerAddress appliesTo;
      private final Duration sourceHoldDuration;
      private final ZonedDateTime expiresAt;
      
      private Impl(Builder builder) {
        Objects.requireNonNull(builder);
        
        this.liquidityCurve =
            Objects.requireNonNull(builder.liquidityCurve, "Liquidity curve must not be null!.");
        
        this.appliesTo =
            Objects.requireNonNull(builder.appliesTo, "Applies-to address must not be null.!");
        
        this.sourceHoldDuration = Objects.requireNonNull(builder.sourceHoldDuration,
            "sourceHoldDuration must not be null!");
        
        this.expiresAt = Objects.requireNonNull(builder.expiresAt, "Expires-at must not be null!.");
      }
      
      @Override
      public LiquidityCurve getLiquidityCurve() {
        return this.liquidityCurve;
      }
      
      @Override
      public InterledgerAddress getAppliesToPrefix() {
        return this.appliesTo;
      }
      
      @Override
      public Duration getSourceHoldDuration() {
        return this.sourceHoldDuration;
      }

      @Override
      public ZonedDateTime getExpiresAt() {
        return this.expiresAt;
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

        if (!liquidityCurve.equals(impl.liquidityCurve)) {
          return false;
        }

        if (!appliesTo.equals(impl.appliesTo)) {
          return false;
        }
        
        if (!sourceHoldDuration.equals(impl.sourceHoldDuration)) {
          return false;
        }
        
        if (!expiresAt.equals(impl.expiresAt)) {
          return false;
        }

        return true;
      }

      @Override
      public int hashCode() {
        int result = liquidityCurve.hashCode();
        result = 31 * result + appliesTo.hashCode();
        result = 31 * result + sourceHoldDuration.hashCode();
        result = 31 * result + expiresAt.hashCode();
        return result;
      }

      @Override
      public String toString() {
        final StringBuilder sb = new StringBuilder("Impl{");
        sb.append("liquidityCurve=").append(liquidityCurve);
        sb.append(", appliesTo=").append(appliesTo);
        sb.append(", sourceHoldDuration=").append(sourceHoldDuration);
        sb.append(", expiresAt=").append(expiresAt);
        sb.append('}');
        return sb.toString();
      }
    }    
  }
}
