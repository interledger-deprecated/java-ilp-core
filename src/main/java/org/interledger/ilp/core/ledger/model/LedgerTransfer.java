package org.interledger.ilp.core.ledger.model;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;

public interface LedgerTransfer {

  String getId();

  URI getLedger();

  List<LedgerTransferAccountEntry> getCredits();

  List<LedgerTransferAccountEntry> getDebits();

  String getExecutionCondition();

  String getCancellationCondition();

  ZonedDateTime getExpiresAt();

}
