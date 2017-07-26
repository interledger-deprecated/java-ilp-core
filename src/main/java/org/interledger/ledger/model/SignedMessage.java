package org.interledger.ledger.model;

public interface SignedMessage<T> {

  T getMessage();

  MessageSignature getSignature();

}
