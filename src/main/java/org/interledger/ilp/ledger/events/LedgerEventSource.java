package org.interledger.ilp.ledger.events;

import org.interledger.core.InterledgerAddress;

public interface LedgerEventSource {
  
  InterledgerAddress getLedgerId();

}
