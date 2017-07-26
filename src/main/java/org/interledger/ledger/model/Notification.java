package org.interledger.ledger.model;

import java.util.Map;
import java.util.UUID;

public interface Notification<T> {

  UUID getId();

  String getEvent();

  T getResource();

  Map<String, String> getRelatedResources();


}
