package org.interledger.ilp.core.ledger.model;

import java.math.BigDecimal;
import java.security.PublicKey;

/**
 * Represents an account on a ledger
 * 
 * @author adrianhopebailie
 *
 */
public interface Account {

  /**
   * A unique identifier for the account with respect to the ledger
   * 
   * @return a unique account identifier
   */
  String getId();

  /**
   * The account name
   * 
   * @return the account name
   */
  String getName();

  /**
   * The current balance of the account
   *
   * @return
   */
  BigDecimal getBalance();

  /**
   * Flag indicating if the account is currently able to transact
   * 
   * @return true if the account is disabled
   */
  boolean isDisabled();

  /**
   * The ledger on which the account is held
   * 
   * @return the ledger
   */
  LedgerInfo getLedger();

  /**
   * The fingerprint of the account certificate
   * 
   * @return the fingerprint of the account certificate
   */
  byte[] getCertificateFingerprint();

  /**
   * The minimum allowed balance for the account.
   * 
   * MUST return null is there is no limit.
   * 
   * @return the minimum allowed balance for the account or null if there is no limit
   */
  BigDecimal getMinimumAllowedBalance();

  /**
   * The account password
   * 
   * @return the account password
   */
  String getPassword();

  /**
   * The public key of this account
   * 
   * @return the account public key
   */
  PublicKey getPublicKey();

}
