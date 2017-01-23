package org.interledger.spsp.core;

import java.net.URI;

import org.interledger.spsp.core.model.PaymentRequest;
import org.interledger.spsp.core.model.Receiver;

/**
 * A Simple Payment Setup Protocol service delivers queries and payment setup messages
 * to receivers.
 */
public interface SpspService {

  /**
   * The sender (this service) queries the receiver endpoint to get information about the type of 
   * payment that can be made to this receiver:
   *
   * @param endpoint
   *  The address of the receiver endpoint
   * @return
   *  Information about the receiver
   */
  Receiver query(URI endpoint);
  
  /**
   * The sender submits payment information to the receiver to set up a payment. 
   *
   * @param endpoint
   *  The address of the receiver endpoint
   * @param amount
   *  The amount. Only relevant to payee receivers, not invoices
   * @param senderIdentifier
   *  Identifies the sender
   * @param memo
   *  A message for the recipient linked to the payment. Only relevant to payee receivers, not
   *  invoices.
   * @return
   */
  PaymentRequest setupPayment(URI endpoint, String amount, String senderIdentifier, String memo);
}

