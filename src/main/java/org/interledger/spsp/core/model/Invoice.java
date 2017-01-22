package org.interledger.spsp.core.model;

import java.net.URI;

public interface Invoice extends Receiver {
  public String getAmount();
  
  public InvoiceStatus getStatus();
  
  public URI getInvoiceInfo();
}

