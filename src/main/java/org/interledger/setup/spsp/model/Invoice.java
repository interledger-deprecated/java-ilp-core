package org.interledger.setup.spsp.model;

import java.net.URI;

import javax.money.MonetaryAmount;

public interface Invoice extends SpspReceiver {
  
  public MonetaryAmount getAmount();
  
  public InvoiceStatus getStatus();
  
  public URI getInvoiceInfo();
}

