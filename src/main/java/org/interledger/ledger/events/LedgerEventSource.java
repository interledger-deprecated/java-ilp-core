package org.interledger.ledger.events;

import org.interledger.InterledgerAddress;

public interface LedgerEventSource {
  
  InterledgerAddress getLedgerId();

}
