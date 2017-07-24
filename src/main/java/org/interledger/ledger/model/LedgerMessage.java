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
  UUID getId();

  /**
   * Defines the type of message, for example 'quote_request'.
   */
  String getType();

  /**
   * Defines the account from which the message is sent.
   */
  InterledgerAddress getFrom();

  /**
   * Defines the account to which the message is sent.
   */
  InterledgerAddress getTo();

  /**
   * The message content.
   */
  Object getData();

}
