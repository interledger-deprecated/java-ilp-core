package org.interledger.quoting;

import java.util.Set;
import java.util.function.Function;

import org.interledger.quoting.model.QuoteResponse;

@FunctionalInterface
public interface QuoteSelectionStrategy extends Function<Set<QuoteResponse>, QuoteResponse>{
  
}
