package org.interledger.setup.model;

import javax.money.CurrencyUnit;

import org.interledger.ilp.InterledgerAddress;

public interface Receiver {
  
  InterledgerAddress getAccount();
  
  CurrencyUnit getCurrencyUnit();

  int getPrecision();
  
  int getScale();
  
}

