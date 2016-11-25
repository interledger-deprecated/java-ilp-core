package org.interledger.ilp.core.ledger.service;

public interface LedgerServiceFactory {

  public LedgerMetaService getMetaService() throws Exception;

  public LedgerAccountService getAccountService() throws Exception;

  public LedgerTransferService getTransferService() throws Exception;
}
