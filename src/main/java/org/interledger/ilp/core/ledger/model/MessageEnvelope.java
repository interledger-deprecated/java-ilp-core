package org.interledger.ilp.core.ledger.model;

public interface MessageEnvelope {

  String getId();

  MessageData getData();
}
