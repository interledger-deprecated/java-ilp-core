package org.interledger.ilp.core.ledger.model;

import java.security.PublicKey;

import javax.money.CurrencyUnit;
import javax.money.format.MonetaryAmountFormat;

import org.interledger.ilp.core.InterledgerAddress;

public interface LedgerInfo {
  
  String getId();
  
  InterledgerAddress getAddressPrefix();

  int getPrecision();

  int getScale();

  CurrencyUnit getCurrencyUnit();

  MonetaryAmountFormat getMonetaryAmountFormat();
  
  PublicKey getConditionSignPublicKey();

  PublicKey getNotificationSignPublicKey();

}
