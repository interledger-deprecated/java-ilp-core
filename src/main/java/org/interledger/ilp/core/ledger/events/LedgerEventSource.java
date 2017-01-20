package org.interledger.ilp.core.ledger.events;

import org.interledger.ilp.core.InterledgerAddress;

public interface LedgerEventSource {
  
  InterledgerAddress getLedgerId();

}
