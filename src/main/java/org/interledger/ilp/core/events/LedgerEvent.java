package org.interledger.ilp.core.events;

import java.util.EventObject;

import org.interledger.ilp.core.Ledger;

/**
 * Base for all events emitted by a ledger
 */
public abstract class LedgerEvent extends EventObject {

    private static final long serialVersionUID = 3292998781708775780L;

    public LedgerEvent(Ledger source) {
        super(source);
    }

    public Ledger getLedger() {
        return (Ledger) getSource();
    }

}
