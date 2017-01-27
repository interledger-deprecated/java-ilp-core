package org.interledger.quoting;

import org.interledger.ilp.InterledgerAddress;
import org.interledger.ilp.exceptions.InterledgerQuotingProtocolException;
import org.interledger.quoting.model.QuoteRequest;
import org.interledger.quoting.model.QuoteResponse;

/**
 * A quote service delivers quote requests to connectors and returns their response
 * 
 * @author adrianhopebailie
 *
 */
public interface QuoteService {
    
  QuoteResponse requestQuote(QuoteRequest query, QuoteSelectionStrategy selectionStrategy) throws InterledgerQuotingProtocolException;
  
  QuoteResponse requestQuote(QuoteRequest query, InterledgerAddress connector) throws InterledgerQuotingProtocolException;
}
