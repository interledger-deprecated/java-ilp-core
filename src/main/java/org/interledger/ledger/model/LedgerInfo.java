package org.interledger.ledger.model;

import org.interledger.InterledgerAddress;

import javax.money.CurrencyUnit;
import javax.money.format.MonetaryAmountFormat;
import java.security.PublicKey;


public interface LedgerInfo {

  // TODO Is this required?
  String getId();

  /**
   * Returns the ILP address prefix of the Ledger.
   */
  InterledgerAddress getAddressPrefix();

  /**
   * Returns the total number of decimal digits of precision the ledger uses to represent currency
   * amounts.
   */
  int getPrecision();

  /**
   * Returns the number of digits after the decimal place the ledger supports in currency amounts.
   */
  int getScale();

  /**
   * Returns the unit of currency used on the ledger.
   */
  CurrencyUnit getCurrencyUnit();

  /**
   * Returns a {@link MonetaryAmountFormat} that can be used to convert monetary amount
   * representations for the ledger to strings.
   */
  MonetaryAmountFormat getMonetaryAmountFormat();

  /**
   * TODO:??.
   */
  PublicKey getConditionSignPublicKey();

  /**
   * TODO:??.
   */
  PublicKey getNotificationSignPublicKey();

}
