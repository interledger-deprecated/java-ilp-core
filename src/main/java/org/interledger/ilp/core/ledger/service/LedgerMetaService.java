package org.interledger.ilp.core.ledger.service;

import org.interledger.ilp.core.ledger.model.LedgerInfo;

public interface LedgerMetaService {

  public LedgerInfo getLedgerInfo() throws Exception;

}
