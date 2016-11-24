package org.interledger.ilp.core.ledger.model;

public interface Account {

  String getId();

  String getName();

  String getBalance();

  boolean isDisabled();

  String getLedger();

  String getCertificateFingerprint();

  boolean isAdmin();

  String getMinimumAllowedBalance();

  String getPassword();

  String getPublicKey();

}
