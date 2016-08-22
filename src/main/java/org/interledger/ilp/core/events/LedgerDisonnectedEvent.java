package org.interledger.ilp.core.events;

import org.interledger.ilp.core.Ledger;

public class LedgerDisonnectedEvent extends LedgerEvent {

    private static final long serialVersionUID = -2688034526014826323L;

    public LedgerDisonnectedEvent(Ledger source) {
        super(source);
    }

}
