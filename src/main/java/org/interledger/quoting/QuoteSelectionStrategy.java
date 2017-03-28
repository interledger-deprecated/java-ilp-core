package org.interledger.quoting;

import org.interledger.quoting.model.QuoteResponse;

import java.util.Set;
import java.util.function.Function;


@FunctionalInterface
public interface QuoteSelectionStrategy extends Function<Set<QuoteResponse>, QuoteResponse> {

}
