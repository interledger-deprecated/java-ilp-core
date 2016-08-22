package org.interledger.ilp.core.events;

public interface LedgerEventHandler<T extends LedgerEvent> {

    public void onLedgerEvent(T event);

}
