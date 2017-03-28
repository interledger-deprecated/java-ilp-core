package org.interledger.ilp.ledger.model;

import org.interledger.cryptoconditions.Condition;
import org.interledger.ilp.InterledgerAddress;

import java.time.ZonedDateTime;
import java.util.UUID;

import javax.money.MonetaryAmount;


/**
 * Defines a transfer on the ledger.
 */
public interface LedgerTransfer {

  /**
   * Returns the unique id of the transfer.
   */
  UUID getId();

  /**
   * Returns the account from which funds will be transferred.
   */
  InterledgerAddress getFromAccount();
  
  /**
   * Returns the account to which funds will be transferred.
   */
  InterledgerAddress getToAccount();

  /**
   * Returns the amount being transferred.
   */
  MonetaryAmount getAmount();

  /**
   * TODO:??.
   */
  boolean isAuthorized();

  /**
   * TODO:??.
   */
  String getInvoice();

  /**
   * TODO:??.
   */
  byte[] getData();

  /**
   * TODO:??.
   */
  byte[] getNoteToSelf();

  /**
   * Returns the condition under which the transfer will be executed.
   */
  Condition getExecutionCondition();

  /**
   * TODO:??.
   */
  Condition getCancellationCondition();

  /**
   * The date when the transfer expires and will be rejected by the ledger.
   */
  ZonedDateTime getExpiresAt();
  
  /**
   * Indicates if the transfer has been rejected.
   */
  boolean isRejected();

  /**
   * Returns the reason for rejecting the transfer.
   */
  String getRejectionMessage();

}
