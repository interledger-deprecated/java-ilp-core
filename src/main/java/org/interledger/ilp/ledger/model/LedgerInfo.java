package org.interledger.ilp.ledger.model;

import java.security.PublicKey;

import javax.money.CurrencyUnit;
import javax.money.format.MonetaryAmountFormat;

import org.interledger.ilp.InterledgerAddress;

public interface LedgerInfo {
  
  //TODO Is this required?
  String getId();
  
  InterledgerAddress getAddressPrefix();

  int getPrecision();

  int getScale();

  CurrencyUnit getCurrencyUnit();

  MonetaryAmountFormat getMonetaryAmountFormat();
  
  PublicKey getConditionSignPublicKey();

  PublicKey getNotificationSignPublicKey();

}
