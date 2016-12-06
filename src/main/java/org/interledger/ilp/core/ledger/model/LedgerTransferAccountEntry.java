package org.interledger.ilp.core.ledger.model;

import java.net.URI;


public interface LedgerTransferAccountEntry {

  URI getAccount();

  String getAmount();

  Boolean isAuthorized();

  URI getInvoice();

  Object getMemo();

  Boolean isRejected();

  String getRejectionMessage();

}
