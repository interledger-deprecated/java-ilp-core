package org.interledger.ilp.core.ledger.model;

import java.security.PublicKey;

import javax.money.MonetaryAmount;

import org.interledger.ilp.core.InterledgerAddress;

public interface AccountInfo {
  
  /**
   * The ledger on which the account is held
   * 
   * @return the ILP address of the ledger
   */
  InterledgerAddress getLedger();

  /**
   * A unique identifier for the account with respect to the ledger
   * 
   * @return a unique account identifier
   */
  String getId();

  /**
   * A human-readable account name
   * 
   * @return the account name
   */
  String getName();
  
  /**
   * The Interledger address of the account
   */
  InterledgerAddress getAddress();
  
  
  /**
   * The current balance of the account
   *
   * @return
   */
  MonetaryAmount getBalance();

  /**
   * Flag indicating if the account is currently able to transact
   * 
   * @return true if the account is disabled
   */
  boolean isDisabled();

  /**
   * The fingerprint of the account certificate
   * 
   * @return the fingerprint of the account certificate
   */
  byte[] getCertificateFingerprint();

  /**
   * The minimum allowed balance for the account.
   * 
   * MUST return null if there is no limit.
   * 
   * @return the minimum allowed balance for the account or null if there is no limit
   */
  MonetaryAmount getMinimumAllowedBalance();

  /**
   * The public key of this account
   * 
   * @return the account public key
   */
  PublicKey getPublicKey();
  
}
