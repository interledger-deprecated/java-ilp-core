package org.interledger.ilp;

import org.interledger.core.InterledgerAddress;
import org.interledger.cryptoconditions.Condition;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * Immutable Interledger Packet Header.
 */
//TODO: is this still used?
public class InterledgerPacketHeader {

  private InterledgerAddress destinationAddress;
  private BigDecimal amount;
  private Condition condition;
  private ZonedDateTime expiry;

  /**
   * Constructs an Interledger packet header based on the destination address, amount, crypto 
   * condition and expiry.
   * 
   * @param destinationAddress
   *    The destination Interledger address.
   * @param amount
   *    The amount being transferred.
   * @param condition
   *    The condition that must be fulfilled to release prepared transfers.
   * @param expiry
   *    The time at which the unfulfilled transfer should expire.
   */
  public InterledgerPacketHeader(InterledgerAddress destinationAddress, BigDecimal amount,
      Condition condition, ZonedDateTime expiry) {
    this.destinationAddress = destinationAddress;
    this.amount = amount;
    this.condition = condition;
    this.expiry = expiry;

    if (condition == null && expiry != null) {
      throw new IllegalArgumentException("Must provide a condition if providing an expiry.");
    }

    if (condition != null && expiry == null) {
      throw new IllegalArgumentException("Must provide an expiry if providing a condition.");
    }

    // TODO Validate address
    // TODO Validate amount
  }


  /**
   * Returns the ILP Address of the destination account.
   */
  public InterledgerAddress getDestinationAddress() {
    return destinationAddress;
  }

  /**
   * Returns the amount that must be transferred into the destination account.
   */
  public BigDecimal getAmount() {
    return amount;
  }

  /**
   * Returns the condition that must be fulfilled to release prepared transfers.
   */
  public Condition getCondition() {
    return condition;
  }

  /**
   * Returns the expiry date of the payment after which any prepared transfers must be rolled back.
   */
  public ZonedDateTime getExpiry() {
    return expiry;
  }

  /**
   * Checks if this header is for an optimistic mode payment.
   *
   * @return
   *    True if the header contains no timeout and condition
   */
  public boolean isOptimisticModeHeader() {
    return (this.condition == null && this.expiry == null);
  }

}
