package org.interledger.ilp.core.ledger.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import org.interledger.cryptoconditions.Condition;

public interface LedgerTransfer {

  String getId();

  LedgerInfo getLedger();

  Account getFromAccount();
  
  Account getToAccount();

  BigDecimal getAmount();

  boolean isAuthorized();

  String getInvoice();

  byte[] getData();

  Condition getExecutionCondition();

  Condition getCancellationCondition();

  ZonedDateTime getExpiresAt();
  
  boolean isRejected();

  String getRejectionMessage();

}
