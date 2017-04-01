package org.interledger.ilp.ledger.events;

import org.interledger.ilp.InterledgerAddress;

public interface LedgerEventSource {
  
  InterledgerAddress getLedgerId();

}
