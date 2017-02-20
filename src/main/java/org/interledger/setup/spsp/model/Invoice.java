package org.interledger.setup.spsp.model;

import java.net.URI;

import javax.money.MonetaryAmount;

/**
 * Defines an Invoice type receiver in the Simple Payment Setup Protocol. An invoice can be paid
 * only once and only with a specific amount.
 */
public interface Invoice extends SpspReceiver {

  public MonetaryAmount getAmount();

  public InvoiceStatus getStatus();

  public URI getInvoiceInfo();
}

