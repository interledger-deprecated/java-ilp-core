package org.interledger.setup.model;

import org.interledger.InterledgerAddress;

import javax.money.CurrencyUnit;


/**
 * Defines the receiver of a payment.
 */
public interface Receiver {

  /**
   * Returns the receivers ILP address.
   */
  InterledgerAddress getAccount();

  /**
   * Returns the unit of currency of the receiver.
   */
  CurrencyUnit getCurrencyUnit();

  /**
   * Returns the number of digits used to represent an amount in the currency of the receiver.
   */
  int getPrecision();

  /**
   * Returns the number of digits after the decimal place for amounts in the currency of the
   * receiver.
   */
  int getScale();

}

