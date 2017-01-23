package org.interledger.spsp.core.model;

import java.time.ZonedDateTime;

import org.interledger.cryptoconditions.Condition;
import org.interledger.ilp.core.InterledgerAddress;

//TODO: according to RFC9, this is a payment request, but is a *response* to setting up a payment
//at a receiver. The naming is tad confusing, perhaps this should be PaymentRequestResponse for
//clarity, since we also have to name the model that we send to the receiver.
public interface PaymentRequest {
  InterledgerAddress getAddress();
  
  String getAmount();
  
  Condition getCondition();
  
  ZonedDateTime getExpiresAt();
  
  Object getData();
  
  String getAdditionalHeaders();
}

