package org.interledger.quoting;

import org.interledger.ilp.InterledgerAddress;
import org.interledger.ilp.exceptions.InterledgerQuotingProtocolException;
import org.interledger.quoting.model.QuoteRequest;
import org.interledger.quoting.model.QuoteResponse;

/**
 * A quote service delivers quote requests to connectors and returns their response.
 * 
 * @author adrianhopebailie
 */
public interface QuoteService {

  /**
   * Requests a quote and applies the given selection strategy to return the best quote received.
   * 
   * @param query The quote requested.
   * @param selectionStrategy A strategy for selecting the best of a given set of quote responses.
   * @return The best quote response according to the selection strategy, or null if no quote
   *         responses where received.
   */
  QuoteResponse requestQuote(QuoteRequest query, QuoteSelectionStrategy selectionStrategy)
      throws InterledgerQuotingProtocolException;

  /**
   * Requests a quote from the connector at the given address.
   * 
   * @param query The quote requested.
   * @param connector The ILP address of the connector to get the quote from.
   * @return The quote response from the connector, or null if no response is received.
   */
  QuoteResponse requestQuote(QuoteRequest query, InterledgerAddress connector)
      throws InterledgerQuotingProtocolException;
}
