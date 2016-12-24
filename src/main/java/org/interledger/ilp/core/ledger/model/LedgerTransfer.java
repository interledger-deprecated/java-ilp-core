package org.interledger.ilp.core.ledger.model;

import java.time.ZonedDateTime;

public interface LedgerTransfer {

  String getId();

  String getLedger();

  String getFromAccount();
  
  String getToAccount();

  String getAmount();

  boolean isAuthorized();

  String getInvoice();

  byte[] getData();

  String getExecutionCondition();

  String getCancellationCondition();

  ZonedDateTime getExpiresAt();
  
  boolean isRejected();

  String getRejectionMessage();

}
