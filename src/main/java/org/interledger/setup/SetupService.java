package org.interledger.setup;

import org.interledger.ipr.InterledgerPaymentRequest;
import org.interledger.setup.model.Receiver;
import org.interledger.setup.model.ReceiverQuery;

import javax.money.MonetaryAmount;

/**
 * Defines a service for setting up a payment.
 */
public interface SetupService {

  /**
   * The sender (this service) queries the receiver endpoint to get information about the type of
   * payment that can be made to this receiver.
   *
   * @param query An appropriate ReceiverQuery for this service.
   * @return Information about the receiver
   */
  Receiver query(ReceiverQuery query);

  /**
   * The service produces an InterldgerPaymentRequest for this receiver, amount and with the given
   * sender data.
   *
   * @param receiver The receiver
   * @param amount The amount. Only relevant to payee receivers, not invoices
   * @param senderIdentifier Identifies the sender
   * @param memo A message for the recipient linked to the payment.
   * @return an {@link InterledgerPaymentRequest} representing the payment to be made.
   */
  InterledgerPaymentRequest setupPayment(Receiver receiver, MonetaryAmount amount,
      String senderIdentifier, String memo);

}
