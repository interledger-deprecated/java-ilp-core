package org.interledger.ilp.core.events;

import org.interledger.ilp.core.Ledger;

public class LedgerConnectedEvent extends LedgerEvent {

    private static final long serialVersionUID = 6501842605798174441L;

    public LedgerConnectedEvent(Ledger source) {
        super(source);
    }

}
