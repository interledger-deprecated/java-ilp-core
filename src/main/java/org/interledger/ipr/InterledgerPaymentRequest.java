package org.interledger.ipr;

import org.interledger.InterledgerAddress;
import org.interledger.cryptoconditions.Condition;

import java.time.ZonedDateTime;

import javax.money.MonetaryAmount;

/**
 * An Interledger Payment Request as defined in ILP RFC 11.
 *
 * @see "https://github.com/interledger/rfcs/blob/master/0011-interledger-payment-request/0011
 * -interledger-payment-request.md"
 */
//FIXME: The types for getData() and getAdditionalHeaders() are probably wrong for now pending a
//decision on the ILP packet format
public class InterledgerPaymentRequest {

  private InterledgerAddress address;
  private MonetaryAmount amount;
  private Condition condition;
  private ZonedDateTime expiresAt;
  private Object data;
  private String additionalHeaders;

  public InterledgerAddress getAddress() {
    return address;
  }

  public MonetaryAmount getAmount() {
    return amount;
  }

  public Condition getCondition() {
    return condition;
  }

  public ZonedDateTime getExpiresAt() {
    return expiresAt;
  }

  public Object getData() {
    return data;
  }

  public String getAdditionalHeaders() {
    return additionalHeaders;
  }

  public void setAddress(InterledgerAddress address) {
    this.address = address;
  }

  public void setAmount(MonetaryAmount amount) {
    this.amount = amount;
  }

  public void setCondition(Condition condition) {
    this.condition = condition;
  }

  public void setExpiresAt(ZonedDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public void setAdditionalHeaders(String additionalHeaders) {
    this.additionalHeaders = additionalHeaders;
  }

}

