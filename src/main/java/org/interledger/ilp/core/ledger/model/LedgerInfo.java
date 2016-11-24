package org.interledger.ilp.core.ledger.model;

import java.util.List;

public interface LedgerInfo {

  int getPrecision();

  int getScale();

  String getCurrencyCode();

  String getCurrencySymbol();
  
  String getLedgerPrefix();
  
  //TODO: Decode public key
  String getConditionSignPublicKey();

  //TODO: Decode public key
  String getNotificationSignPublicKey();

  List<ConnectorInfo> getConnectors();

}
