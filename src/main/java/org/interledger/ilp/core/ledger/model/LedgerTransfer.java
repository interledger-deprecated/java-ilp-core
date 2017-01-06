package org.interledger.ilp.core.ledger.model;

import java.time.ZonedDateTime;
import java.util.UUID;

import javax.money.MonetaryAmount;

import org.interledger.cryptoconditions.Condition;
import org.interledger.ilp.core.InterledgerAddress;

public interface LedgerTransfer {

  UUID getId();

  InterledgerAddress getFromAccount();
  
  InterledgerAddress getToAccount();

  MonetaryAmount getAmount();

  boolean isAuthorized();

  String getInvoice();

  byte[] getData();

  byte[] getNoteToSelf();

  Condition getExecutionCondition();

  Condition getCancellationCondition();

  ZonedDateTime getExpiresAt();
  
  boolean isRejected();

  String getRejectionMessage();

}
