package org.interledger.ilp.core.ledger.service;

import org.interledger.ilp.core.ledger.model.Account;

public interface LedgerAccountService {

  public Account getAccount(String name) throws Exception;

}
