package org.interledger.setup;

import javax.money.MonetaryAmount;

import org.interledger.ilp.InterledgerPaymentRequest;
import org.interledger.setup.model.Receiver;
import org.interledger.setup.model.ReceiverQuery;


public interface SetupService {

  /**
   * The sender (this service) queries the receiver endpoint to get information about the type of 
   * payment that can be made to this receiver:
   *
   * @param receiverEndpoint
   *  The URL of the receiver endpoint
   * @return
   *  Information about the receiver
   */
  Receiver query(ReceiverQuery query);

  /**
   * The sender submits payment information to the receiver to set up a payment. 
   *
   * @param receiver
   *  The receiver
   * @param amount
   *  The amount. Only relevant to payee receivers, not invoices
   * @param senderIdentifier
   *  Identifies the sender
   * @param memo
   *  A message for the recipient linked to the payment. Only relevant to payee receivers, not
   *  invoices.
   * @return
   */
  InterledgerPaymentRequest setupPayment(Receiver receiver, MonetaryAmount amount,
      String senderIdentifier, String memo);

}
