package org.interledger.ilqp.core;

import org.interledger.ilp.core.InterledgerAddress;
import org.interledger.ilp.core.exceptions.InterledgerQuotingProtocolException;
import org.interledger.ilqp.core.model.QuoteRequest;
import org.interledger.ilqp.core.model.QuoteResponse;

/**
 * A quote service delivers quote requests to connectors and returns their response
 * 
 * @author adrianhopebailie
 *
 */
public interface QuoteService {
    
  QuoteResponse requestQuote(QuoteRequest query) throws InterledgerQuotingProtocolException;
  
  QuoteResponse requestQuote(QuoteRequest query, InterledgerAddress connector) throws InterledgerQuotingProtocolException;
}
