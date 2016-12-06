package org.interledger.ilp.core.ledger.service;

import org.interledger.ilp.core.ledger.model.Account;

public interface LedgerAccountService {

  Account getAccount(String accountName) throws Exception;

  void subscribeToAccountNotifications(String accountName) throws Exception;

}
