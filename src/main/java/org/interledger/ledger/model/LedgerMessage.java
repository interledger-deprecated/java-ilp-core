package org.interledger.ledger.model;

import org.interledger.InterledgerAddress;

import java.util.UUID;

/**
 * Defines a message that is exchanged with a ledger.
 */
public interface LedgerMessage {

  /**
   * Returns the unique id of the message.
   */
  public UUID getId();

  /**
   * Defines the type of message, for example 'quote_request'.
   */
  public String getType();

  /**
   * Defines the account from which the message is sent.
   */
  public InterledgerAddress getFrom();

  /**
   * Defines the account to which the message is sent.
   */
  public InterledgerAddress getTo();

  /**
   * The message content.
   */
  public Object getData();

}
