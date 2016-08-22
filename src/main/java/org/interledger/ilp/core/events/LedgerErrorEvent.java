package org.interledger.ilp.core.events;

import org.interledger.ilp.core.Ledger;

public class LedgerErrorEvent extends LedgerEvent {

    private static final long serialVersionUID = -6494295568908151670L;
    protected Exception error;

    public LedgerErrorEvent(Ledger source, Exception error) {
        super(source);

        this.error = error;

    }

    public Exception getError() {
        return error;
    }

}
