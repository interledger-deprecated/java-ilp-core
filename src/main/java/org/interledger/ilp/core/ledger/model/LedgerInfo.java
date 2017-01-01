package org.interledger.ilp.core.ledger.model;

import java.security.PublicKey;
import java.util.Currency;
import java.util.List;

import org.interledger.ilp.core.InterledgerAddress;

public interface LedgerInfo {
  
  String getId();

  int getPrecision();

  int getScale();

  Currency getCurrency();
  
  InterledgerAddress getLedgerPrefix();
  
  PublicKey getConditionSignPublicKey();

  PublicKey getNotificationSignPublicKey();

  List<Account> getConnectors();

}
